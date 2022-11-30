package com.teamzero.domain;

import com.teamzero.domain.domain.MemberVo;
import com.teamzero.domain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {

    private final String SECRET_KEY = "TEAMZERO_ZEROMALL";

    private final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 6;  // 6시간

    public String createToken(String memberId, String email, String role, String grade){

        Claims claims = Jwts.claims()
                .setId(memberId)
                .setSubject(Aes256Util.encrypt(email));

        claims.put("role", role);
        claims.put("grade", grade);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public boolean validToken(String token){

        try {
            Jws<Claims> claimsJws
                    = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }

    }

    public MemberVo getMemberVo(String token){

        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

        return MemberVo.builder()
                .memberId(Long.valueOf(Objects.requireNonNull(claims.getId())))
                .email(Aes256Util.decrypt(claims.getSubject()))
                .role(String.valueOf(claims.get("role")))
                .grade(String.valueOf(claims.get("grade")))
                .build();

    }

}
