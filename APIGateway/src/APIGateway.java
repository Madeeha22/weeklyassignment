import java.util.*;

class TokenBucket {
    int tokens;
    int maxTokens;
    long lastRefillTime;
    double refillRate; // tokens per second

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on time
    public void refill() {
        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + (int) tokensToAdd);
            lastRefillTime = now;
        }
    }

    // Try to consume token
    public boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }
}

class RateLimiter {

    private HashMap<String, TokenBucket> clientMap;

    public RateLimiter() {
        clientMap = new HashMap<>();
    }

    public String checkRateLimit(String clientId) {

        clientMap.putIfAbsent(clientId,
                new TokenBucket(1000, 1000.0 / 3600)); // 1000 per hour

        TokenBucket bucket = clientMap.get(clientId);

        synchronized (bucket) {
            if (bucket.allowRequest()) {
                return "Allowed (" + bucket.tokens + " requests remaining)";
            } else {
                return "Denied (0 requests remaining)";
            }
        }
    }

    // Get status
    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientMap.get(clientId);

        if (bucket == null) return "No data";

        int used = bucket.maxTokens - bucket.tokens;

        return "Used: " + used +
                ", Limit: " + bucket.maxTokens;
    }

    // MAIN METHOD
    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        // simulate requests
        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(client));
        }

        System.out.println(limiter.getRateLimitStatus(client));
    }
}