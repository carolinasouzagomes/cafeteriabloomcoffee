
package senai499.com.br.cafeteriabloomcoffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import senai499.com.br.cafeteriabloomcoffee.service.ClienteDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Administrador criado diretamente no sistema.
    @Bean
    public UserDetails adminUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .username("admin@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .roles("ADMIN")
                .build();
    }

    // Reconhece o administrador e os clientes cadastrados.
    @Bean
    public UserDetailsService userDetailsService(
            ClienteDetailsService clienteDetailsService,
            UserDetails adminUser) {

        InMemoryUserDetailsManager adminManager =
                new InMemoryUserDetailsManager(adminUser);

        return email -> {
            if ("admin@gmail.com".equalsIgnoreCase(email)) {
                return adminManager.loadUserByUsername("admin@gmail.com");
            }

            return clienteDetailsService.loadUserByUsername(email);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    // Configuração principal de segurança.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler)
            throws Exception {

        http
                .authenticationManager(authenticationManager)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/cadastro",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/img/**"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Exige login para finalizar um pedido.
                        .requestMatchers("/pedidos/finalizar").authenticated()

                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
