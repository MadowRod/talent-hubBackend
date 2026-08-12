package br.com.talen_hub.config;

import br.com.talen_hub.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/skills/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/skills/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/skills/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/skills/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuario/skills").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuario/skills").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuario/skills/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuario/skills/**").authenticated()
                        .requestMatchers("/api/v1/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}