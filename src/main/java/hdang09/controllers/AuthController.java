package hdang09.controllers;

import hdang09.dtos.requests.LoginDTO;
import hdang09.dtos.responses.LoginResponseDTO;
import hdang09.models.Response;
import hdang09.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login with full name")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO account) {
        return authService.login(account);
    }

    @Operation(summary = "Login with Google")
    @GetMapping("/login/google")
    public ResponseEntity<String> loginWithGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return authService.loginWithGoogle(oAuth2AuthenticationToken);
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

    @Operation(summary = "Get new access token")
    @PostMapping("/refresh-token")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response<Void>> refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }
}