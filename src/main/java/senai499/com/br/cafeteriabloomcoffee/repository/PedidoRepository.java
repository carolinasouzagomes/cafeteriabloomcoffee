package senai499.com.br.cafeteriabloomcoffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import senai499.com.br.cafeteriabloomcoffee.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {
            "cliente",
            "itens",
            "itens.produto"
    })
    List<Pedido> findAllByOrderByDataPedidoDesc();
}
