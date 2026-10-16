
package senai499.com.br.cafeteriabloomcoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import senai499.com.br.cafeteriabloomcoffee.model.Pagamento;

public interface PagamentoRepository
        extends JpaRepository<Pagamento, Long> {
}
