
package senai499.com.br.cafeteriabloomcoffee;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import senai499.com.br.cafeteriabloomcoffee.model.Usuario;

@ControllerAdvice
public class UsuarioModelAdvice {

    @ModelAttribute("nomeUsuario")
    public String nomeUsuario(
            @AuthenticationPrincipal Usuario usuario) {

        if (usuario == null) {
            return "";
        }

        return usuario.getNome();
    }
}
