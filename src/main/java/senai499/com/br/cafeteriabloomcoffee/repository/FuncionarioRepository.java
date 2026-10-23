
package senai499.com.br.cafeteriabloomcoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import senai499.com.br.cafeteriabloomcoffee.model.Funcionario;

@Repository
public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Long> {

    boolean existsByCpf(String cpf);

}
