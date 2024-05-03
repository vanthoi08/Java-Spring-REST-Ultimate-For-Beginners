package vn.hoidanit.jobhunter.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

    // định nghĩa thuật toán HS512
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    // Lấy tham số môi trường trong file application.properties
    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtKey;

    @Value("${hoidanit.jwt.token-validity-in-seconds}")
    private String jwtExpiration;

    public void createToken(Authentication authentication) {

    }
}
