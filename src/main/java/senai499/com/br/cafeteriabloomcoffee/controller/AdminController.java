
package senai499.com.br.cafeteriabloomcoffee.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import senai499.com.br.cafeteriabloomcoffee.model.Produto;
import senai499.com.br.cafeteriabloomcoffee.service.ProdutoService;

@Controller
public class AdminController {

    private final ProdutoService produtoService;

    public AdminController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping({"/admin", "/admin/"})
    public String dashboard(Model model) {

        List<Produto> produtos = produtoService.listarTodos();

        long totalProdutos = produtos.size();

        long produtosDisponiveis = produtos.stream()
                .filter(p -> Boolean.TRUE.equals(p.getDisponivel()))
                .count();

        long produtosIndisponiveis =
                totalProdutos - produtosDisponiveis;

        long totalCategorias = produtos.stream()
                .map(Produto::getCategoria)
                .filter(categoria -> categoria != null
                        && !categoria.isBlank())
                .distinct()
                .count();

        List<Produto> produtosRecentes = produtos.stream()
                .sorted(Comparator.comparing(
                        Produto::getIdProduto,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .toList();

        model.addAttribute("totalProdutos", totalProdutos);
        model.addAttribute("produtosDisponiveis", produtosDisponiveis);
        model.addAttribute("produtosIndisponiveis", produtosIndisponiveis);
        model.addAttribute("totalCategorias", totalCategorias);
        model.addAttribute("produtosRecentes", produtosRecentes);

        return "admin/index";
    }
}
