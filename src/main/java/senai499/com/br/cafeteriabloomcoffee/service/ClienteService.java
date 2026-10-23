
package senai499.com.br.cafeteriabloomcoffee.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import senai499.com.br.cafeteriabloomcoffee.model.Cliente;
import senai499.com.br.cafeteriabloomcoffee.model.PerfilUsuario;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.repository.ClienteRepository;
import senai499.com.br.cafeteriabloomcoffee.repository.UsuarioRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {

        if (cliente.getUsuario() == null) {
            throw new IllegalArgumentException(
                    "Os dados de acesso do cliente são obrigatórios.");
        }

        Usuario usuario = cliente.getUsuario();

        if (usuario.getEmail() == null
                || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "O e-mail é obrigatório.");
        }

        if (usuario.getSenha() == null
                || usuario.getSenha().isBlank()) {
            throw new IllegalArgumentException(
                    "A senha é obrigatória.");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado.");
        }

        if (cliente.getCpf() == null
                || cliente.getCpf().isBlank()) {
            throw new IllegalArgumentException(
                    "O CPF é obrigatório.");
        }

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException(
                    "Este CPF já está cadastrado.");
        }

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha()));

        usuario.setPerfil(PerfilUsuario.CLIENTE);
        usuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        cliente.setUsuario(usuarioSalvo);

        return clienteRepository.save(cliente);
    }
}
