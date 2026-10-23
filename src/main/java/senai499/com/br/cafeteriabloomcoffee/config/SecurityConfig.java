
package senai499.com.br.cafeteriabloomcoffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import senai499.com.br.cafeteriabloomcoffee.service.UsuarioDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UsuarioDetailsService usuarioDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(usuarioDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            CustomAuthenticationSuccessHandler successHandler)
            throws Exception {

        http
                .authenticationManager(authenticationManager)

                .authorizeHttpRequests(auth -> auth

                        // Páginas públicas e recursos estáticos
                        .requestMatchers(
                                "/",
                                "/login",
                                "/cadastro",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/images/**",
                                "/webjars/**",
                                "/favicon.ico"
                        ).permitAll()

                        // Área administrativa
                        .requestMatchers("/admin", "/admin/**")
                        .hasRole("ADMIN")

                        // Área do funcionário
                        .requestMatchers(
                                "/funcionario",
                                "/funcionario/**"
                        ).hasAnyRole("FUNCIONARIO", "ADMIN")

                        // Finalização de pedidos somente para clientes
                        .requestMatchers("/pedidos/finalizar")
                        .hasRole("CLIENTE")

                        // Demais páginas públicas por enquanto
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .successHandler(successHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        // A proteção CSRF permanece habilitada.
        return http.build();
    }
}
