
package senai499.com.br.cafeteriabloomcoffee.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import senai499.com.br.cafeteriabloomcoffee.model.Cliente;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.service.ClienteService;

@Controller
public class AuthController {

    private final ClienteService clienteService;

    public AuthController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/login")
    public String exibirLogin() {
        return "auth/login";
    }

    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        if (!model.containsAttribute("cliente")) {
            Cliente cliente = new Cliente();
            cliente.setUsuario(new Usuario());

            model.addAttribute("cliente", cliente);
        }

        return "auth/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (resultado.hasErrors()) {
            if (cliente.getUsuario() == null) {
                cliente.setUsuario(new Usuario());
            }

            return "auth/cadastro";
        }

        try {
            clienteService.cadastrar(cliente);

        } catch (IllegalArgumentException e) {
            if (cliente.getUsuario() == null) {
                cliente.setUsuario(new Usuario());
            }

            model.addAttribute("erro", e.getMessage());
            return "auth/cadastro";
        }

        redirectAttributes.addFlashAttribute(
                "cadastroSucesso",
                "Conta criada com sucesso! Você já pode entrar."
        );

        return "redirect:/login";
    }
}
