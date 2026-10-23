package senai499.com.br.cafeteriabloomcoffee.controller;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import senai499.com.br.cafeteriabloomcoffee.model.Cliente;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.repository.ClienteRepository;
import senai499.com.br.cafeteriabloomcoffee.service.PedidoService;
import senai499.com.br.cafeteriabloomcoffee.service.ProdutoService;
import senai499.com.br.cafeteriabloomcoffee.dto.PedidoRequest;

@Controller
public class PedidosController {

    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;

    public PedidosController(
            ProdutoService produtoService,
            PedidoService pedidoService,
            ClienteRepository clienteRepository) {

        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/pedidos")
    public String pedidos(
            Model model,
            @AuthenticationPrincipal Usuario usuario) {

        model.addAttribute("produtos", produtoService.listarTodos());

        if (usuario != null && usuario.getIdUsuario() != null) {
            Cliente cliente = clienteRepository
                    .findByUsuarioIdUsuario(usuario.getIdUsuario())
                    .orElse(null);

            model.addAttribute("cliente", cliente);
        }

        return "pedidos/pedidos";
    }

    @PostMapping("/pedidos/finalizar")
    @ResponseBody
    public ResponseEntity<Map<String, String>> finalizarPedido(
            @RequestBody PedidoRequest dadosPedido,
            @AuthenticationPrincipal Usuario usuario) {

        if (usuario == null || usuario.getIdUsuario() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "mensagem",
                            "Você precisa estar logado para finalizar o pedido."
                    ));
        }

        try {
            var pedidoSalvo = pedidoService.registrarPedido(
                    dadosPedido,
                    usuario
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensagem", "Pedido realizado com sucesso!",
                            "idPedido", String.valueOf(pedidoSalvo.getIdPedido())
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensagem", e.getMessage()));
        }
    }

    @GetMapping("/coisado")
    public String coisadopedidos() {
        return "pedidos/coisa";
    }
}
