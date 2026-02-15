package rpgshop.presentation.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rpgshop.infraestructure.integration.jwt.JwtService;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTH_COOKIE_NAME = "rpgshop_token";
    public static final String USER_ID_ATTR = "authenticatedUserId";
    public static final String USER_EMAIL_ATTR = "authenticatedUserEmail";
    public static final String USER_TYPE_ATTR = "authenticatedUserType";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(final JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        extractTokenFromCookies(request).ifPresent(token -> {
            final Optional<Claims> claims = jwtService.validateToken(token);
            claims.ifPresent(c -> {
                request.setAttribute(USER_ID_ATTR, c.getSubject());
                request.setAttribute(USER_EMAIL_ATTR, c.get("email", String.class));
                request.setAttribute(USER_TYPE_ATTR, c.get("userType", String.class));
            });
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromCookies(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (final Cookie cookie : cookies) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
