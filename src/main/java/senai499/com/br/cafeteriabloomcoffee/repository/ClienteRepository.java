
package senai499.com.br.cafeteriabloomcoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import senai499.com.br.cafeteriabloomcoffee.model.Cliente;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Cliente> findByUsuarioIdUsuario(Long idUsuario);

}


/*
* O que essa interface permite fazer?
- save(cliente): salvar um cliente ou atualizar um existente.
- findById(id): buscar pelo identificador.
- findAll(): listar todos os clientes.
- deleteById(id): excluir um cliente.
- findByEmail(email): buscar pelo e-mail, útil para o login.
- existsByCpf(cpf): verificar se o CPF já está cadastrado.
*
* */