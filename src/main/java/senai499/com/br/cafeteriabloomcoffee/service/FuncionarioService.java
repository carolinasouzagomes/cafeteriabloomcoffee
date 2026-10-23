
package senai499.com.br.cafeteriabloomcoffee.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import senai499.com.br.cafeteriabloomcoffee.model.Funcionario;
import senai499.com.br.cafeteriabloomcoffee.model.PerfilUsuario;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.repository.FuncionarioRepository;
import senai499.com.br.cafeteriabloomcoffee.repository.UsuarioRepository;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    @Transactional
    public void cadastrar(Funcionario funcionario) {

        if (funcionario.getUsuario() == null) {
            throw new IllegalArgumentException(
                    "Os dados de acesso do funcionário são obrigatórios."
            );
        }

        String nome = funcionario.getNome();
        String cpf = funcionario.getCpf();
        String email = funcionario.getUsuario().getEmail();
        String senha = funcionario.getUsuario().getSenha();

        if (nome == null || nome.isBlank()
                || cpf == null || cpf.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException(
                    "Preencha todos os campos obrigatórios."
            );
        }

        cpf = cpf.replaceAll("\\D", "");
        email = email.trim().toLowerCase();

        if (cpf.length() != 11) {
            throw new IllegalArgumentException(
                    "O CPF deve conter 11 dígitos."
            );
        }

        if (senha.length() < 8) {
            throw new IllegalArgumentException(
                    "A senha deve ter pelo menos 8 caracteres."
            );
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        if (funcionarioRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException(
                    "Este CPF já está cadastrado."
            );
        }

        Usuario usuario = funcionario.getUsuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfil(PerfilUsuario.FUNCIONARIO);
        usuario.setAtivo(true);

        // Salva primeiro a conta de acesso.
        usuarioRepository.save(usuario);

        funcionario.setCpf(cpf);
        funcionario.setUsuario(usuario);

        // Salva os dados profissionais vinculados à conta.
        funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void alterarAtivacao(Long id, boolean ativo) {

        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Funcionário não encontrado."
                ));

        Usuario usuario = funcionario.getUsuario();

        if (usuario == null
                || usuario.getPerfil() != PerfilUsuario.FUNCIONARIO) {
            throw new IllegalStateException(
                    "A conta vinculada não é de um funcionário."
            );
        }

        usuario.setAtivo(ativo);
        usuarioRepository.save(usuario);
    }
}
