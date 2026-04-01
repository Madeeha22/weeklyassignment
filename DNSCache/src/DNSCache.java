import java.util.*;

class DNSEntry {
    String ipAddress;
    long expiryTime;

    public DNSEntry(String ipAddress, long ttlSeconds) {
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private int capacity;
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }

    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        }

        misses++;

        String newIP = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(newIP, 5));

        if (entry == null)
            return "Cache MISS → " + newIP;
        else
            return "Cache EXPIRED → " + newIP;
    }

    private String queryUpstreamDNS(String domain) {
        return "192.168.1." + (new Random().nextInt(100));
    }

    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }


    public static void main(String[] args) throws InterruptedException {

        DNSCache dns = new DNSCache(3);

        System.out.println(dns.resolve("google.com")); // MISS
        System.out.println(dns.resolve("google.com")); // HIT

        Thread.sleep(6000); // wait for TTL expiry

        System.out.println(dns.resolve("google.com")); // EXPIRED

        dns.getCacheStats();
    }
}