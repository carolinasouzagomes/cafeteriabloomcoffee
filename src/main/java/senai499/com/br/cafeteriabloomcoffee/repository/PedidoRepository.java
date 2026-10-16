package senai499.com.br.cafeteriabloomcoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import senai499.com.br.cafeteriabloomcoffee.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}