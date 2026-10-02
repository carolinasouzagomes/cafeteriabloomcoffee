package senai499.com.br.cafeteriabloomcoffee.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/login")   
    public String login() {
        return "login_cliente";
    }

    @GetMapping("/login-admin")
    public String logi_admin() {
        return "login_funcionarios_adm";
    }
}