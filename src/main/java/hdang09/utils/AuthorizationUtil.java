package hdang09.utils;

import hdang09.constants.AuthConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthorizationUtil {

    @Autowired
    private JwtUtil jwtUtil;

    public UUID getPlayerIdFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AuthConstant.PREFIX_HEADER).substring(AuthConstant.PREFIX_TOKEN.length());
        String playerId = jwtUtil.extractUsername(token);
        return UUID.fromString(playerId);
    }

}