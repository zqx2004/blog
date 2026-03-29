package com.example.blog.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类
 * 生成、解析、校验 token
 */
@Component
public class JwtUtil {

    /**
     * 密钥：自己随便写，越长越安全（至少32位）
     */
    private static final String SECRET_KEY = "myBlogSecretKey12345678901234567890";

    /**
     * 过期时间：24小时
     */
    private static final long EXPIRATION = 24 * 60 * 60 * 1000;

    /**
     * 获取密钥对象
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ==================== 1. 生成 Token ====================
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 把用户ID存在主题里
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 过期时间
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // 签名
                .compact();
    }

    // ==================== 2. 从 Token 解析出用户ID ====================
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // ==================== 3. 校验 Token 是否有效 ====================
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token 过期");
        } catch (Exception e) {
            System.out.println("Token 无效");
        }
        return false;
    }
}