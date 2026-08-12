package br.com.talen_hub.jwt;

import br.com.talen_hub.jwt.service.JwtService;
import br.com.talen_hub.usuario.entity.Usuario;
import br.com.talen_hub.usuario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository
    ) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        System.out.println("======================================");
        System.out.println("JWT FILTER");
        System.out.println("Endpoint: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("Authorization: " + authorization);

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            System.out.println("JWT: nenhum token encontrado");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        System.out.println("JWT: token encontrado");

        if (!jwtService.isTokenValido(token)) {

            System.out.println("JWT: token INVALIDO");

            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JWT: token VALIDO");

        String email = jwtService.extrairEmail(token);

        System.out.println("JWT: email = " + email);

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        if (usuario == null) {

            System.out.println("JWT: usuario nao encontrado");

            filterChain.doFilter(request, response);
            return;
        }

        if (!usuario.getAtivo()) {

            System.out.println("JWT: usuario inativo");

            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JWT: usuario encontrado");
        System.out.println("JWT: role = " + usuario.getRole());

        var authorities = List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getRole().name()
                )
        );

        System.out.println(
                "JWT: authority = ROLE_" +
                        usuario.getRole().name()
        );

        var authentication =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        authorities
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        System.out.println("JWT: autenticacao configurada");

        filterChain.doFilter(request, response);
    }
}