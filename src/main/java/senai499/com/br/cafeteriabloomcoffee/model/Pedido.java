
package senai499.com.br.cafeteriabloomcoffee.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "produto_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "total_preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPreco;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "hora_pedido", nullable = false)
    private LocalTime horaPedido;

    @Column(name = "forma_pagamento", nullable = false, length = 50)
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private String status;

    // Construtor vazio
    public Pedido() {
    }

    // Construtor completo
    public Pedido(Long idCliente, Integer quantidade,
                         BigDecimal totalPreco, LocalDate dataPedido,
                         LocalTime horaPedido, String formaPagamento,
                         String status) {
        this.idCliente = idCliente;
        this.quantidade = quantidade;
        this.totalPreco = totalPreco;
        this.dataPedido = dataPedido;
        this.horaPedido = horaPedido;
        this.formaPagamento = formaPagamento;
        this.status = status;
    }

    // Getters e Setters

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
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

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalTime getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(LocalTime horaPedido) {
        this.horaPedido = horaPedido;
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
}
