package GiostriConstrucoes.dev.config;

import GiostriConstrucoes.dev.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Configuração CORS para respostas
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin != null ? origin : "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "Authorization, Content-Disposition");

        // Tratamento de pré-voo (preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Rotas públicas
        String uri = request.getRequestURI();
        if (uri.startsWith("/giostri/usuario/registrar") ||
                uri.startsWith("/giostri/usuario/login") ||
                uri.startsWith("/giostri/usuario/recuperar-senha") ||
                uri.startsWith("/giostri/usuario/redefinir-senha") ||
                (request.getMethod().equals("GET") && uri.startsWith("/giostri/produto/all"))) {
            filterChain.doFilter(request, response);
            return;
        }


        // Resto da lógica de autenticação
        var token = this.recoverToken(request);
        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
            return;
        }

        try {
            var login = tokenService.verificarToken(token);
            UserDetails user = userRepository.findByEmail(login)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token inválido ou expirado");
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}