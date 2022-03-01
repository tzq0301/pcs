package cn.tzq0301.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class JWTUtils {
    private static final String SECRET = "ThisIsTheSecretOfJwtAuthorizationOfOutPerfectProjectPcsSystem";

    public static final long EXPIRATION = 6 * 60 * 60 * 1000;

    private static final SignatureAlgorithm ALG = SignatureAlgorithm.HS256;

    private static final SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), ALG.getJcaName());

    private static final DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(ALG, secretKeySpec);

    /**
     * 创建 JWT Token
     *
     * @param userId 用户 ID
     * @param role 用户角色
     * @return JWT Token
     */
    public static String generateToken(final String userId, final String role) {
        // Use username as subject
        // Add role into claims
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("role", role);
        return createToken(claims, userId);
    }

    /**
     * 验证 JWT Token 是否合法
     *
     * @param token JWT Token
     * @param userId 用户 ID
     * @return JWT Token 是否合法
     */
    public static boolean validateToken(final String token, final String userId) {
        final String userIdFromJWT = extractUserId(token);
        return (Objects.equals(userIdFromJWT, userId) && !isTokenExpired(token));
    }

    /**
     * 从 JWT Token 验证码中获取用户角色
     *
     * @param token JWT Token
     * @return 用户角色
     */
    public static String extractUserRole(final String token) {
        return (String) extractClaim(token, claims -> claims.get("role"));
    }

    /**
     * 从 JWT Token 中获取用户 ID
     *
     * @param token JWT Token
     * @return JWT Token 中的用户 ID
     */
    public static String extractUserId(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 使用 jjwt API 创建 JWT Token
     *
     * @param claims JWT Token 应该包含的 claims
     * @param userId 用户 ID
     * @return JWT Token
     */
    private static String createToken(Map<String, Object> claims, final String userId) {
        long currentTime = Instant.now().toEpochMilli();

        return Jwts.builder()
                .setClaims(claims)

                // Sets the JWT Claims sub (subject) value.
                .setSubject(userId)

                // the timestamp when the JWT was created.
                .setIssuedAt(new Date(currentTime))

                // A JWT obtained after this timestamp should not be used.
                .setExpiration(new Date(currentTime + EXPIRATION))

                // Signs the constructed JWT using the specified algorithm with the specified key.
                .signWith(ALG, SECRET)

                // Actually builds the JWT and serializes it to a compact
                .compact();
    }

    /**
     * 判断 JWT Token 是否过期
     *
     * @param token JWT Token
     * @return JWT Token 是否过期
     */
    public static boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 从 JWT Token 中抽取 Expiration
     *
     * @param token JWT Token
     * @return Expiration
     */
    private static Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从 JWT Token 中，通过传入的 {@code Function<Claims, T>} 获取指定的 claim
     *
     * @param token JWT Token
     * @param claimsResolver 获取指定 claim 的函数
     * @return 通过传入的 {@code Function<Claims, T>} 获取的指定的 claim
     */
    private static <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取给定 JWT Token 中的所有 claims
     * @param token JWT Token
     * @return JWT Token 中的所有 claims
     */
    public static Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    private JWTUtils() {}
}
