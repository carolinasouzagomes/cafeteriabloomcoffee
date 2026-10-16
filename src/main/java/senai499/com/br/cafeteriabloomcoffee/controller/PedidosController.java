
package senai499.com.br.cafeteriabloomcoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import senai499.com.br.cafeteriabloomcoffee.service.ProdutoService;

import java.util.Map;

@Controller
public class PedidosController {

    private final ProdutoService produtoService;

    public PedidosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping({"/cardapio", "/pedidos"})
    public String pedidos(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());

        return "pedidos/pedidos";
    }

    @PostMapping("/pedidos/finalizar")
    @ResponseBody
    public ResponseEntity<Map<String, String>> finalizarPedido(
            @RequestBody Map<String, Object> dadosPedido) {

        // Por enquanto, apenas confirma o recebimento dos dados.
        // Ainda não salva o pedido no banco de dados.

        if (dadosPedido == null || dadosPedido.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Os dados do pedido estão vazios.")
            );
        }

        return ResponseEntity.ok(
                Map.of("mensagem", "Dados recebidos com sucesso.")
        );
    }

    @GetMapping("/coisado")
    public String coisadopedidos() {
        return "pedidos/coisa";
    }
}
