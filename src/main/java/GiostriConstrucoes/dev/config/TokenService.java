package GiostriConstrucoes.dev.config;

import GiostriConstrucoes.dev.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenService {


    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                //diz quem criou o token com base no email
                .withSubject(usuario.getEmail())
                //adiciona elemento UsuarioId no token para dizer o token é do usuário com id x
                .withClaim("UsuarioId", usuario.getId())
                //adiciona elemento name no token para dizer o nome do usuário do token
                .withClaim("name", usuario.getNome())
                //indica se o usuário é admin ou não
                .withClaim("isAdmin", usuario.isAdmin())
                //token expira em 24h
                .withExpiresAt(Instant.now().plusSeconds(86400))
                //quando token foi gerado
                .withIssuedAt(Instant.now())
                //gerado pela API
                .withIssuer("API GiostriConstrucoes")
                //criptografa com alogitimo anterior
                .sign(algorithm);
    }

    public String verificarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API GiostriConstrucoes")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }
}
