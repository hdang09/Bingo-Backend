package hdang09.services;

import hdang09.entities.Player;
import hdang09.enums.ResponseStatus;
import hdang09.dtos.requests.LoginDTO;
import hdang09.dtos.responses.LoginResponseDTO;
import hdang09.mappers.PlayerMapper;
import hdang09.models.Response;
import hdang09.repositories.PlayerRepository;
import hdang09.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Map;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    private final PlayerRepository playerRepository;

    @Value("${url.client}")
    private String URL_CLIENT;

    @Autowired
    public AuthService(JwtUtil jwtUtil, PlayerRepository playerRepository) {
        this.jwtUtil = jwtUtil;
        this.playerRepository = playerRepository;
    }

    public ResponseEntity<Response<LoginResponseDTO>> login(LoginDTO account) {
        // Map to entity
         Player player = PlayerMapper.INSTANCE.toPlayer(account);
         player.setAvatarUrl(account.getAvatar());

        // Store account to database
        playerRepository.save(player);

        // Generate token
        String accessToken = jwtUtil.generateAccessToken(player.getPlayerId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(player.getPlayerId().toString());
        LoginResponseDTO tokenDTO = new LoginResponseDTO(accessToken, refreshToken);

        // Return response
        Response<LoginResponseDTO> response = new Response<>(ResponseStatus.SUCCESS, "Login successfully", tokenDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> loginWithGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String, Object> userOAuth = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        String email = (String) userOAuth.get("email");
        String fullName = (String) userOAuth.get("name");
        String avatar = (String) userOAuth.get("picture");

        Player player = playerRepository.findByEmail(email);

        // If player not exist -> create new user
        if (player == null) {
            Player newPlayer = new Player(avatar, fullName, email);
            player = playerRepository.save(newPlayer);
        }

        // Generate token
        String token = jwtUtil.generateAccessToken(player.getPlayerId().toString());

        // Create URI with token for redirect
        String url = URL_CLIENT + "/" + "?success=true&token=" + token;
        URI uri = URI.create(url);

        // Remove JSESSIONID
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
    }
}
