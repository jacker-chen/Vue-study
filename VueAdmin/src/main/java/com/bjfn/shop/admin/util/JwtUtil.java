package com.bjfn.shop.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtUtil {
    /**EXPIRE设置token的过期时间*/
    private   long expire; //= 1000 * 60 * 60 * 24;
    /**密钥*/
    private   String secret; //= "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";
    //头
    private String header;

    /**
     * 根据用户信息生成Token
     *
     * @param username
     * @return
     */
    public String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("created", new Date());
        return generateToken(claims);
    }
    public String generateToken(Authentication authentication) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username",authentication.getName());
        claims.put("created",new Date());
        return generateToken(claims);
    }

    /**
     * 从Token中获取username
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            String subject = claims.getSubject();
            username = (String) claims.get("username");
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从Token中获取荷载
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
           return  e.getClaims();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 验证Token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    public boolean validateToken(String token, String userName) {
        String username = getUsernameFromToken(token);
        return username.equals(userName);
    }

    /**
     * 判断Token是否失效
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 从Token中获取过期时间
     * @param token
     * @return
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据荷载生成JWT Token
     *
     * @return
     */
    public String generateToken(Map claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成Token失效时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expire * 1000);
    }
}
