
package senai499.com.br.cafeteriabloomcoffee.dto;

import java.util.List;

public class PedidoRequest {

    private String nome;
    private String telefone;
    private String email;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private String cidade;
    private String pagamento;
    private String observacoes;
    private String primeiraVisita;
    private String origem;
    private List<ItemRequest> itens;

    public static class ItemRequest {
        private Long produtoId;
        private Integer quantidade;

        public Long getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPagamento() { return pagamento; }
    public void setPagamento(String pagamento) { this.pagamento = pagamento; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getPrimeiraVisita() { return primeiraVisita; }
    public void setPrimeiraVisita(String primeiraVisita) { this.primeiraVisita = primeiraVisita; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public List<ItemRequest> getItens() { return itens; }
    public void setItens(List<ItemRequest> itens) { this.itens = itens; }
}
