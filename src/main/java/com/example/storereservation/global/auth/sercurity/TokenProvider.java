package com.example.storereservation.global.auth.sercurity;

import com.example.storereservation.global.auth.service.AuthService;
import com.example.storereservation.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; //1시간
    private final AuthService authService;

    /**
     * 토큰 생성 (발급)
     */
    public String generateToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) //사용할 암호화 알고리즘, 시크릿 키
                .compact();
    }

    /**
     * token으로 username(사용자 ID) 찾기
     *
     * @param token
     * @return username
     */
    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * 토큰 유효성 검사
     * true : 유효  false : 유효하지 않음
     */
    public boolean validateToken(String token) {
        try{
            if (!StringUtils.hasText(token)) return false;
            Claims claims = this.parseClaims(token);
            return !claims.getExpiration().before(new Date());
        }catch (JwtException e){
            throw new JwtException(e.getMessage());
        }

    }

    /**
     * 토큰이 유효한지 확인
     */
    private Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode.TOKEN_TIME_OUT.getDescription());
        }catch (SignatureException e){
            throw new JwtException(ErrorCode.JWT_TOKEN_WRONG_TYPE.getDescription());
        }
    }

    public Authentication getAuthentication(String token) {
        String username = this.getUsername(token);
        UserDetails userDetails = authService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
