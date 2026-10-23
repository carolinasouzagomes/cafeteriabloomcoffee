
package senai499.com.br.cafeteriabloomcoffee.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import senai499.com.br.cafeteriabloomcoffee.model.Produto;
import senai499.com.br.cafeteriabloomcoffee.service.ProdutoService;

@Controller
public class ProdutosController {

    private final ProdutoService produtoService;

    private static final List<String> CATEGORIAS = List.of(
            "Bebidas quentes",
            "Bebidas geladas",
            "Doces",
            "Salgados",
            "Saudáveis",
            "Produtos"
    );

    public ProdutosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // CARDÁPIO PÚBLICO
    @GetMapping({"/cardapio", "/produtos"})
    public String cardapio(Model model) {
        prepararCardapio(model);
        return "produtos/produtos";
    }

    // PÁGINA PARA MONTAR PEDIDOS
//    @GetMapping("/pedidos")
//    public String listarPedidos(Model model) {
//        model.addAttribute(
//                "produtos",
//                produtoService.listarDisponiveis()
//        );
//
//        return "pedidos";
//    }

    // ORGANIZA OS PRODUTOS POR CATEGORIA
    private void prepararCardapio(Model model) {
        List<Produto> produtos = produtoService.listarDisponiveis();

        Map<String, List<Produto>> produtosPorCategoria =
                new LinkedHashMap<>();

        for (String categoria : CATEGORIAS) {
            List<Produto> produtosDaCategoria = produtos.stream()
                    .filter(p -> p.getCategoria() != null
                            && categoria.equalsIgnoreCase(
                            p.getCategoria().trim()
                    ))
                    .collect(Collectors.toList());

            produtosPorCategoria.put(categoria, produtosDaCategoria);
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("categorias", CATEGORIAS);
        model.addAttribute(
                "produtosPorCategoria",
                produtosPorCategoria
        );
    }

    // ABRIR CADASTRO DE PRODUTOS
    @GetMapping("/admin/produtos/cadastrar")
    public String abrirCadastro(Model model) {
        model.addAttribute("produto", new Produto());

        return "admin/produtos/cadastrar_produto";
    }

    // SALVAR PRODUTO
    @PostMapping("/admin/produtos/salvar")
    public String salvar(
            @ModelAttribute("produto") Produto produto,
            RedirectAttributes redirectAttributes
    ) {
        produtoService.salvar(produto);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Produto cadastrado com sucesso!"
        );

        return "redirect:/admin/produtos/cadastrar";
    }
}
