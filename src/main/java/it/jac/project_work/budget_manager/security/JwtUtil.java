package it.jac.project_work.budget_manager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import it.jac.project_work.budget_manager.entity.Role;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class JwtUtil {
    private static final SecretKey SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 864_000_000;




    public static String generateToken(String username, Set<Role> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles.stream().map(Enum::name).toList())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public static Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public static boolean isTokenMalformed(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Token non valido: " + e.getMessage());
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token scaduto: " + e.getMessage());
            return true;
        } catch (UnsupportedJwtException e) {
            System.out.println("Token non supportato: " + e.getMessage());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Claims JWT vuoti: " + e.getMessage());
            return true;
        }
    }

    public static List<String> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }
    public static boolean hasRole(String token, String role) {
        List<String> roles = extractRoles(token);
        return roles != null && roles.contains(role);
    }



}
