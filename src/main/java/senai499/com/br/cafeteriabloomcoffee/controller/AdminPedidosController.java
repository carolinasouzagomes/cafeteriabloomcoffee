package senai499.com.br.cafeteriabloomcoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import senai499.com.br.cafeteriabloomcoffee.service.PedidoService;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidosController {

    private final PedidoService pedidoService;

    public AdminPedidosController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listarPedidos(Model model) {

        model.addAttribute(
                "pedidos",
                pedidoService.listarPedidosAdministrativos()
        );

        return "admin/pedidos/index";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {

        try {
            pedidoService.atualizarStatus(id, status);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Status do pedido #" + id + " atualizado com sucesso!"
            );

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "redirect:/admin/pedidos";
    }
}
