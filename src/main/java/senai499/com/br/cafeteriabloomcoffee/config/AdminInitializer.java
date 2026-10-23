
package senai499.com.br.cafeteriabloomcoffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import senai499.com.br.cafeteriabloomcoffee.model.PerfilUsuario;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.repository.UsuarioRepository;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner criarAdminInicial(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${bloomcoffee.admin.email:admin@bloomcoffee.com}")
            String emailAdmin,
            @Value("${bloomcoffee.admin.senha:Admin@123}")
            String senhaAdmin) {

        return args -> {
            if (usuarioRepository.existsByEmail(emailAdmin)) {
                return;
            }

            Usuario admin = new Usuario();
            admin.setEmail(emailAdmin);
            admin.setSenha(passwordEncoder.encode(senhaAdmin));
            admin.setPerfil(PerfilUsuario.ADMIN);
            admin.setAtivo(true);

            usuarioRepository.save(admin);

            System.out.println(
                    "Usuário administrador inicial criado com sucesso."
            );
        };
    }
}

/*
E-mail admin@bloomcoffee.com
    Senha Admin@123
 */