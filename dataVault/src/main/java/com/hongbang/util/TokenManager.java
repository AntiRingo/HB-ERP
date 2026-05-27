package com.hongbang.util;


import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenManager {
    private static final Map<String, Map<String, Long>> tokenStore = new ConcurrentHashMap<>();
    private static final int MAX_TOKENS_PER_SESSION = 20;
    private static final long TOKEN_EXPIRATION = 5 * 60 * 1000; // 5分钟
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        // 启动定时清理任务
        scheduler.scheduleAtFixedRate(TokenManager::cleanupExpiredTokens, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 生成并存储新令牌
     * @param session HTTP会话对象
     * @return 生成的令牌字符串
     */
    public static String generateToken(HttpSession session) {
        String sessionId = session.getId();
        String token = UUID.randomUUID().toString();
        long currentTime = System.currentTimeMillis();

        tokenStore.compute(sessionId, (key, tokenMap) -> {
            if (tokenMap == null) {
                tokenMap = new ConcurrentHashMap<>();
            }

            // 如果令牌数量超过上限，移除最旧的令牌
            if (tokenMap.size() >= MAX_TOKENS_PER_SESSION) {
                Map<String, Long> finalTokenMap = tokenMap;
                tokenMap.entrySet().stream()
                        .min(Map.Entry.comparingByValue())
                        .ifPresent(entry -> finalTokenMap.remove(entry.getKey()));
            }

            tokenMap.put(token, currentTime);
            return tokenMap;
        });

        return token;
    }

    /**
     * 验证令牌有效性
     * @param session HTTP会话对象
     * @param token 待验证的令牌
     * @return 是否有效
     */
    public static boolean validateToken(HttpSession session, String token) {
        String sessionId = session.getId();
        Map<String, Long> tokenMap = tokenStore.get(sessionId);

        if (tokenMap == null || token == null) {

            return false;
        }

        Long createTime = tokenMap.get(token);
        if (createTime == null) {
            return false;
        }

        // 检查令牌是否过期
        if (System.currentTimeMillis() - createTime > TOKEN_EXPIRATION) {
            tokenMap.remove(token);
            return false;
        }

        // 验证成功后移除令牌
        tokenMap.remove(token);
        return true;
    }

    /**
     * 清理过期令牌和空会话
     */
    private static void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();

        tokenStore.forEach((sessionId, tokenMap) -> {
            tokenMap.entrySet().removeIf(entry ->
                    currentTime - entry.getValue() > TOKEN_EXPIRATION
            );

            // 移除空会话
            if (tokenMap.isEmpty()) {
                tokenStore.remove(sessionId);
            }
        });
    }
}
