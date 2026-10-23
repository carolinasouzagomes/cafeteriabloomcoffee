
package senai499.com.br.cafeteriabloomcoffee.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Cópia dos dados usados no momento do pedido
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 30)
    private String telefone;

    @Column(nullable = false, length = 150)
    private String email;

    // Endereço de entrega
    @Column(nullable = false, length = 150)
    private String rua;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(nullable = false, length = 100)
    private String bairro;

    @Column(nullable = false, length = 9)
    private String cep;

    @Column(nullable = false, length = 100)
    private String cidade;

    // Informações do pedido
    @Column(nullable = false)
    private Integer quantidade = 0;

    @Column(
            name = "total_preco",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal totalPreco = BigDecimal.ZERO;

    @Column(
            name = "taxa_entrega",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal taxaEntrega = new BigDecimal("5.00");

    @Column(
            name = "forma_pagamento",
            nullable = false,
            length = 50
    )
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private String status = "PENDENTE";

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @Transient
    private LocalDate dataPedidoAntiga;

    @Transient
    private LocalTime horaPedido;

    @Column(length = 1000)
    private String observacoes;

    @Column(
            name = "primeira_visita",
            nullable = false,
            length = 3
    )
    private String primeiraVisita;

    @Column(nullable = false, length = 100)
    private String origem;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Pagamento pagamento;

    public Pedido() {
    }

    @PrePersist
    public void antesDeSalvar() {
        if (dataPedido == null) {
            dataPedido = LocalDateTime.now();
        }
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "O item do pedido não pode ser nulo."
            );
        }

        itens.add(item);
        item.setPedido(this);
        atualizarQuantidade();
    }

    public void removerItem(ItemPedido item) {
        if (itens.remove(item)) {
            item.setPedido(null);
            atualizarQuantidade();
        }
    }

    public void atualizarQuantidade() {
        quantidade = itens.stream()
                .filter(item -> item.getQuantidade() != null)
                .mapToInt(ItemPedido::getQuantidade)
                .sum();
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getIdCliente() {
        return cliente != null ? cliente.getId() : null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getTotalPreco() {
        return totalPreco;
    }

    public void setTotalPreco(BigDecimal totalPreco) {
        this.totalPreco = totalPreco;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataPedidoAntiga() {
        return dataPedidoAntiga;
    }

    public void setDataPedidoAntiga(
            LocalDate dataPedidoAntiga
    ) {
        this.dataPedidoAntiga = dataPedidoAntiga;
    }

    public LocalTime getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(LocalTime horaPedido) {
        this.horaPedido = horaPedido;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getPrimeiraVisita() {
        return primeiraVisita;
    }

    public void setPrimeiraVisita(String primeiraVisita) {
        this.primeiraVisita = primeiraVisita;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens != null
                ? itens
                : new ArrayList<>();

        for (ItemPedido item : this.itens) {
            item.setPedido(this);
        }

        atualizarQuantidade();
    }

    public Pagamento getPagamento() {
        return pagamento;
    }
}
