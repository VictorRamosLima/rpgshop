package rpgshop.presentation.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.auth.LoginCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.auth.AuthenticateUseCase;
import rpgshop.domain.entity.user.User;
import rpgshop.infraestructure.integration.jwt.JwtService;
import rpgshop.presentation.filter.JwtAuthenticationFilter;

@Controller
public final class LoginController {
    private final AuthenticateUseCase authenticateUseCase;
    private final JwtService jwtService;

    public LoginController(
        final AuthenticateUseCase authenticateUseCase,
        final JwtService jwtService
    ) {
        this.authenticateUseCase = authenticateUseCase;
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam final String email,
        @RequestParam final String password,
        final HttpServletResponse response,
        final Model model
    ) {
        try {
            final var command = new LoginCommand(email, password);
            final User user = authenticateUseCase.execute(command);

            final String token = jwtService.generateToken(
                user.id(),
                user.email(),
                user.userType().name()
            );

            final Cookie cookie = new Cookie(JwtAuthenticationFilter.AUTH_COOKIE_NAME, token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            return "redirect:/";
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "auth/login";
        }
    }

    @PostMapping("/logout")
    public String logout(final HttpServletResponse response) {
        final Cookie cookie = new Cookie(JwtAuthenticationFilter.AUTH_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
