package it.jac.project_work.budget_manager.security;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtService {    /**
     *
     * @param request
     * @return.
     */
public String getUsername(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String token = authorizationHeader.substring(7);

        try {
            String username = JwtUtil.extractUsername(token);
            return username;
        } catch (Exception e) {
            System.out.println("Errore nell'estrazione del username: " + e.getMessage());
            return null;
        }
    } else {
        System.out.println("Authorization header non presente o non valido");
    }

    return null;
}

}
