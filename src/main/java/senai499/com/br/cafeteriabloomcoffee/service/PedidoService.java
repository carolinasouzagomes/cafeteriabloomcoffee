
package senai499.com.br.cafeteriabloomcoffee.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import senai499.com.br.cafeteriabloomcoffee.dto.PedidoRequest;
import senai499.com.br.cafeteriabloomcoffee.model.Cliente;
import senai499.com.br.cafeteriabloomcoffee.model.ItemPedido;
import senai499.com.br.cafeteriabloomcoffee.model.Pagamento;
import senai499.com.br.cafeteriabloomcoffee.model.Pedido;
import senai499.com.br.cafeteriabloomcoffee.model.Produto;
import senai499.com.br.cafeteriabloomcoffee.repository.ClienteRepository;
import senai499.com.br.cafeteriabloomcoffee.repository.PagamentoRepository;
import senai499.com.br.cafeteriabloomcoffee.repository.PedidoRepository;
import senai499.com.br.cafeteriabloomcoffee.repository.ProdutosRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutosRepository produtosRepository;
    private final ClienteRepository clienteRepository;
    private final PagamentoRepository pagamentoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ProdutosRepository produtosRepository,
            ClienteRepository clienteRepository,
            PagamentoRepository pagamentoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.produtosRepository = produtosRepository;
        this.clienteRepository = clienteRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional(rollbackFor = Exception.class)
    public Pedido registrarPedido(PedidoRequest dados, Cliente clienteAutenticado) {

        validarDados(dados);

        if (clienteAutenticado == null || clienteAutenticado.getId() == null) {
            throw new IllegalArgumentException(
                    "Entre na sua conta para finalizar o pedido.");
        }

        // Busca o cliente persistido pelo e-mail do login.
        String emailLogin = clienteAutenticado.getUsername();

        Cliente cliente = clienteRepository.findByEmail(emailLogin)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Não foi possível localizar o cliente autenticado."));

        Pedido pedido = new Pedido();

        // Relaciona o pedido ao cliente autenticado.
        pedido.setCliente(cliente);

        // Dados informados no formulário.
        pedido.setNome(dados.getNome().trim());
        pedido.setTelefone(dados.getTelefone().trim());
        pedido.setEmail(dados.getEmail().trim());
        pedido.setRua(dados.getRua().trim());
        pedido.setNumero(dados.getNumero().trim());
        pedido.setComplemento(textoOuNull(dados.getComplemento()));
        pedido.setBairro(dados.getBairro().trim());
        pedido.setCep(dados.getCep().trim());
        pedido.setCidade(dados.getCidade().trim());

        pedido.setFormaPagamento(dados.getPagamento());
        pedido.setObservacoes(textoOuNull(dados.getObservacoes()));
        pedido.setPrimeiraVisita(dados.getPrimeiraVisita());
        pedido.setOrigem(dados.getOrigem());
        pedido.setStatus("PENDENTE");

        BigDecimal subtotalGeral = BigDecimal.ZERO;

        // Cria os itens usando os preços cadastrados no banco.
        for (PedidoRequest.ItemRequest itemRequest : dados.getItens()) {

            if (itemRequest == null
                    || itemRequest.getProdutoId() == null
                    || itemRequest.getQuantidade() == null
                    || itemRequest.getQuantidade() < 1
                    || itemRequest.getQuantidade() > 100) {

                throw new IllegalArgumentException(
                        "Produto ou quantidade inválida.");
            }

            Produto produto = produtosRepository
                    .findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Um dos produtos não foi encontrado."));

            if (!Boolean.TRUE.equals(produto.getDisponivel())) {
                throw new IllegalArgumentException(
                        "O produto " + produto.getNome()
                                + " não está disponível.");
            }

            BigDecimal precoUnitario = produto.getPreco();

            if (precoUnitario == null || precoUnitario.signum() < 0) {
                throw new IllegalArgumentException(
                        "O produto " + produto.getNome()
                                + " possui um preço inválido.");
            }

            BigDecimal quantidade = BigDecimal.valueOf(
                    itemRequest.getQuantidade());

            BigDecimal subtotalItem = precoUnitario.multiply(quantidade);

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setSubtotal(subtotalItem);

            // Define também a referência do item para o pedido.
            pedido.adicionarItem(item);

            subtotalGeral = subtotalGeral.add(subtotalItem);
        }

        // Taxa de entrega configurada no projeto.
        BigDecimal taxaEntrega = new BigDecimal("5.00");

        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setTotalPreco(subtotalGeral.add(taxaEntrega));

        /*
         * O relacionamento Pedido.itens deve ter cascade = CascadeType.ALL
         * para que os ItemPedido sejam salvos junto com o Pedido.
         */
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Cria o pagamento vinculado ao pedido.
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedidoSalvo);
        pagamento.setMetodoPagamento(dados.getPagamento());
        pagamento.setStatusPagamento("PENDENTE");
        pagamento.setValorPago(pedidoSalvo.getTotalPreco());

        // A data será preenchida quando o pagamento for confirmado.
        pagamento.setDataPagamento(null);

        pagamentoRepository.save(pagamento);

        return pedidoSalvo;
    }

    private void validarDados(PedidoRequest dados) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "Os dados do pedido não foram informados.");
        }

        if (vazio(dados.getNome())
                || vazio(dados.getTelefone())
                || vazio(dados.getEmail())
                || vazio(dados.getRua())
                || vazio(dados.getNumero())
                || vazio(dados.getBairro())
                || vazio(dados.getCep())
                || vazio(dados.getCidade())) {

            throw new IllegalArgumentException(
                    "Preencha todos os campos obrigatórios do pedido.");
        }

        if (dados.getPagamento() == null
                || !List.of("pix", "cartao", "dinheiro")
                .contains(dados.getPagamento())) {

            throw new IllegalArgumentException(
                    "Selecione uma forma de pagamento válida.");
        }

        if (dados.getPrimeiraVisita() == null
                || !List.of("sim", "nao")
                .contains(dados.getPrimeiraVisita())) {

            throw new IllegalArgumentException(
                    "Informe se esta é sua primeira visita.");
        }

        if (!origemValida(dados.getOrigem())) {
            throw new IllegalArgumentException(
                    "Informe como conheceu o Bloom Coffee.");
        }

        if (dados.getItens() == null
                || dados.getItens().isEmpty()
                || dados.getItens().size() > 50) {

            throw new IllegalArgumentException(
                    "O pedido deve conter entre 1 e 50 itens.");
        }
    }
    private boolean origemValida(String origem) {
        return origem != null && List.of(
                "redes_sociais",
                "indicacao",
                "internet",
                "local"
        ).contains(origem);
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }

    private String textoOuNull(String valor) {
        return vazio(valor) ? null : valor.trim();
    }
}
