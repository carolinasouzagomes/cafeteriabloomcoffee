
package senai499.com.br.cafeteriabloomcoffee.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import senai499.com.br.cafeteriabloomcoffee.dto.PedidoRequest;
import senai499.com.br.cafeteriabloomcoffee.model.Usuario;
import senai499.com.br.cafeteriabloomcoffee.service.PedidoService;
import senai499.com.br.cafeteriabloomcoffee.service.ProdutoService;

@Controller
public class PedidosController {

    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public PedidosController(
            ProdutoService produtoService,
            PedidoService pedidoService) {

        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping({"/cardapio", "/pedidos"})
    public String pedidos(Model model) {
        model.addAttribute(
                "produtos",
                produtoService.listarTodos()
        );

        return "pedidos/pedidos";
    }

    @PostMapping("/pedidos/finalizar")
    @ResponseBody
    public ResponseEntity<Map<String, String>> finalizarPedido(
            @RequestBody PedidoRequest dadosPedido,
            @AuthenticationPrincipal Usuario usuario) {

        // Verifica se existe um usuário autenticado.
        if (usuario == null || usuario.getIdUsuario() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "mensagem",
                            "Entre na sua conta para finalizar o pedido."
                    ));
        }

        try {
            // O PedidoService recebe Usuario, não Cliente.
            var pedidoSalvo = pedidoService.registrarPedido(
                    dadosPedido,
                    usuario
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensagem",
                            "Pedido registrado com sucesso!",
                            "idPedido",
                            pedidoSalvo.getIdPedido().toString()
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "mensagem",
                            e.getMessage()
                    ));
        }
    }

    @GetMapping("/coisado")
    public String coisadopedidos() {
        return "pedidos/coisa";
    }
}
