
package senai499.com.br.cafeteriabloomcoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import senai499.com.br.cafeteriabloomcoffee.model.Funcionario;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.service.FuncionarioService;

@Controller
@RequestMapping("/admin/funcionarios")
public class AdminFuncionarioController {

    private final FuncionarioService funcionarioService;

    public AdminFuncionarioController(
            FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute(
                "funcionarios",
                funcionarioService.listarTodos()
        );

        return "admin/funcionarios";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Funcionario funcionario = new Funcionario();
        funcionario.setUsuario(new Usuario());

        model.addAttribute("funcionario", funcionario);

        return "admin/funcionario-form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("funcionario") Funcionario funcionario,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // O cadastro sempre cria uma nova conta.
            funcionario.setIdFuncionario(null);

            funcionarioService.cadastrar(funcionario);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Funcionário cadastrado com sucesso!"
            );

            return "redirect:/admin/funcionarios";

        } catch (IllegalArgumentException e) {
            if (funcionario.getUsuario() == null) {
                funcionario.setUsuario(new Usuario());
            }

            model.addAttribute("erro", e.getMessage());
            return "admin/funcionario-form";
        }
    }

    @PostMapping("/{id}/inativar")
    public String inativar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        funcionarioService.alterarAtivacao(id, false);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Funcionário inativado com sucesso."
        );

        return "redirect:/admin/funcionarios";
    }

    @PostMapping("/{id}/reativar")
    public String reativar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        funcionarioService.alterarAtivacao(id, true);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Funcionário reativado com sucesso."
        );

        return "redirect:/admin/funcionarios";
    }
}
