
package senai499.com.br.cafeteriabloomcoffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import senai499.com.br.cafeteriabloomcoffee.model.Produto;

@Repository
public interface ProdutosRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByDisponivelTrue();
}
