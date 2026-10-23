
package senai499.com.br.cafeteriabloomcoffee.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isFuncionario = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_FUNCIONARIO"));

        boolean isCliente = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_CLIENTE"));

        if (isAdmin) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/");
        } else if (isFuncionario) {
            response.sendRedirect(
                    request.getContextPath() + "/funcionario/");
        } else if (isCliente) {
            response.sendRedirect(
                    request.getContextPath() + "/cardapio");
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/login?error");
        }
    }
}
