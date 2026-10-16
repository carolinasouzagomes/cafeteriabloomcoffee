package senai499.com.br.cafeteriabloomcoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import senai499.com.br.cafeteriabloomcoffee.model.Produto;
import senai499.com.br.cafeteriabloomcoffee.service.ProdutoService;

@Controller
public class ProdutosController {

    private final ProdutoService produtoService;

    public ProdutosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Página de pedidos
    @GetMapping("/pedidos")
    public String listarProdutos(Model model) {
        model.addAttribute(
                "produtos",
                produtoService.listarDisponiveis()
        );

        return "pedidos";
    }

    // Página de produtos
    @GetMapping("/produtos")
    public String produtos() {
        return "produtos/produtos";
    }

    // Abre o formulário de cadastro
//    @GetMapping("/admin/")
//    public String homeAdmin(Model model) {
//        return "admin/index";
//    }

    @GetMapping("/admin/produtos/cadastrar")
    public String abrirCadastro(Model model) {
        model.addAttribute("produto", new Produto());
        return "admin/produtos/cadastrar_produto";
    }

    // Recebe os dados e salva no banco
    @PostMapping("/admin/produtos/salvar")
    public String salvar(
            @ModelAttribute("produto") Produto produto,
            RedirectAttributes redirectAttributes) {

        produtoService.salvar(produto);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Produto cadastrado com sucesso!"
        );

        return "redirect:/admin/produtos/cadastrar";
    }
}
