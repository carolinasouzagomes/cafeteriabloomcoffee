package senai499.com.br.cafeteriabloomcoffee.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PedidosController {

    @GetMapping("/pedidos")
    public String pedidos() {
        return "pedidos/pedidos";
    }

    @GetMapping("/coisado")
    public String coisadopedidos() {
        return "pedidos/coisa";
    }

}