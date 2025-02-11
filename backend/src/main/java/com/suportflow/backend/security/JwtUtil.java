package com.suportflow.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") // Busca a chave secreta do arquivo de propriedades (application.properties ou application.yml)
    private String secret;

    @Value("${jwt.expiration}") // Busca o tempo de expiração do token do arquivo de propriedades
    private Long expiration;

    /**
     * Extrai o email (subject) do token JWT.
     *
     * @param token O token JWT.
     * @return O email extraído do token.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai a data de expiração do token JWT.
     *
     * @param token O token JWT.
     * @return A data de expiração do token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai um claim específico do token JWT usando uma função.
     *
     * @param token          O token JWT.
     * @param claimsResolver A função para resolver o claim desejado.
     * @param <T>            O tipo do claim.
     * @return O claim extraído.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai todos os claims do token JWT.
     *
     * @param token O token JWT.
     * @return Todos os claims do token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Verifica se o token JWT expirou.
     *
     * @param token O token JWT.
     * @return True se o token expirou, false caso contrário.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Gera um token JWT para o usuário especificado.
     *
     * @param userDetails Os detalhes do usuário.
     * @return O token JWT gerado.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Você pode adicionar mais claims personalizados aqui, se necessário.
        // Por exemplo: claims.put("cargo", userDetails.getCargo());
        return createToken(claims, userDetails.getUsername()); // userDetails.getUsername() retorna o email
    }

    /**
     * Cria um token JWT com os claims e subject especificados.
     *
     * @param claims  Os claims a serem incluídos no token.
     * @param subject O subject do token (neste caso, o email do usuário).
     * @return O token JWT criado.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims) // Adiciona os claims
                .subject(subject) // Define o subject (email do usuário)
                .issuedAt(now) // Define a data de emissão
                .expiration(expiryDate) // Define a data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina o token com a chave secreta
                .compact(); // Compacta o token
    }

    /**
     * Valida um token JWT.
     *
     * @param token       O token JWT a ser validado.
     * @param userDetails Os detalhes do usuário.
     * @return True se o token for válido, false caso contrário.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Obtém a chave secreta para assinar e verificar tokens JWT.
     *
     * @return A chave secreta.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}