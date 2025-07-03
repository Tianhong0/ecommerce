package com.example.admin.util;

import com.example.admin.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT(JSON Web Token)工具类
 * <p>
 * 提供JWT令牌的生成、验证、解析以及管理功能。
 * 该工具类使用HMAC-SHA算法对令牌进行签名，
 * 并结合Redis进行令牌的缓存和黑名单管理，
 * 提供了更加安全和可控的JWT实现。
 * </p>
 * <p>
 * 主要功能：
 * - 生成JWT令牌
 * - 从令牌中提取用户名
 * - 验证令牌有效性
 * - 将无效令牌加入黑名单
 * </p>
 * 
 * @author admin team
 * @version 1.0
 * @since 2023-06-01
 */
@Component
public class JwtUtil {

    /**
     * JWT签名密钥，从配置文件中注入
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT令牌过期时间(毫秒)，从配置文件中注入
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Redis模板，用于缓存令牌和管理黑名单
     */
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 用于JWT签名的密钥
     */
    private SecretKey key;

    /**
     * 构造函数，注入Redis模板
     * 
     * @param redisTemplate Redis操作模板
     */
    public JwtUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 初始化方法，在Bean创建后自动执行
     * 根据配置的密钥字符串创建签名密钥对象
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据认证信息生成JWT令牌
     * 
     * @param authentication Spring Security认证对象
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername());
    }

    /**
     * 根据用户名生成JWT令牌
     * <p>
     * 生成的令牌包含用户名、签发时间和过期时间信息，
     * 同时将令牌存入Redis缓存，便于后续验证和管理。
     * </p>
     * 
     * @param username 用户名
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();

        // 将token存入Redis
        String redisKey = "token:" + username;
        redisTemplate.opsForValue().set(redisKey, token, expiration, TimeUnit.MILLISECONDS);

        return token;
    }

    /**
     * 从JWT令牌中提取用户名
     * <p>
     * 解析JWT令牌中的负载(claims)，获取subject字段作为用户名
     * </p>
     * 
     * @param token JWT令牌字符串
     * @return 令牌中包含的用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 验证JWT令牌有效性
     * <p>
     * 验证步骤：
     * 1. 解析令牌获取用户名
     * 2. 检查Redis中是否存在对应令牌
     * 3. 验证Redis中的令牌和当前令牌是否匹配
     * 4. 验证令牌签名和过期时间
     * </p>
     * 
     * @param token JWT令牌字符串
     * @return 如果令牌有效返回true，否则返回false
     */
    public boolean validateToken(String token) {
        try {
            // 从token中获取用户名
            String username = getUsernameFromToken(token);
            
            // 从Redis中获取token
            String redisKey = "token:" + username;
            String redisToken = (String) redisTemplate.opsForValue().get(redisKey);
            
            // 验证token是否存在且匹配
            if (redisToken == null || !redisToken.equals(token)) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从系统中移除JWT令牌
     * <p>
     * 用于用户退出登录或管理员强制下线用户时，
     * 将令牌从Redis中删除，使其立即失效。
     * </p>
     * 
     * @param token 要移除的JWT令牌字符串
     */
    public void removeToken(String token) {
        try {
            String username = getUsernameFromToken(token);
            String redisKey = "token:" + username;
            redisTemplate.delete(redisKey);
        } catch (Exception e) {
            // 忽略异常
        }
    }
}
