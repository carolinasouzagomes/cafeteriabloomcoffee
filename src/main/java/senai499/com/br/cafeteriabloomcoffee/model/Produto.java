
package senai499.com.br.cafeteriabloomcoffee.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 255)
    private String arquivoImagem;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itensPedido = new ArrayList<>();

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    // Construtor vazio
    public Produto() {
    }

    // Construtor completo
    public Produto(String nome, String descricao,
                   String categoria, BigDecimal preco,
                   String arquivoImagem, Boolean disponivel) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.arquivoImagem = arquivoImagem;
        this.disponivel = disponivel;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getArquivoImagem() {
        return arquivoImagem;
    }

    public void setArquivoImagem(String arquivoImagem) {
        this.arquivoImagem = arquivoImagem;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}
