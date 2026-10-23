
package senai499.com.br.cafeteriabloomcoffee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import senai499.com.br.cafeteriabloomcoffee.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

//    Optional<Usuario> findByEmail(String email);
    @Query("""
        SELECT u
        FROM Usuario u
        LEFT JOIN FETCH u.cliente
        WHERE u.email = :email
        """)
    Optional<Usuario> findByEmailComCliente(
            @Param("email") String email
    );

    boolean existsByEmail(String email);
}


