
package senai499.com.br.cafeteriabloomcoffee.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final SavedRequestAwareAuthenticationSuccessHandler
            clienteSuccessHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();

    public CustomAuthenticationSuccessHandler() {
        // Destino padrão para clientes que fizeram login normalmente.
        clienteSuccessHandler.setDefaultTargetUrl("/cardapio");

        // Se existir uma página protegida solicitada antes do login,
        // o Spring Security prioriza esse endereço.
        clienteSuccessHandler.setAlwaysUseDefaultTargetUrl(false);
    }

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
            // Respeita a página que o cliente tentou acessar antes do login.
            clienteSuccessHandler.onAuthenticationSuccess(
                    request,
                    response,
                    authentication
            );
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/login?error");
        }
    }
}
