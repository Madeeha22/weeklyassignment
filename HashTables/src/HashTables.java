import java.util.*;

class MultiLevelCache {

    private int L1_CAPACITY = 3;   // small for demo
    private int L2_CAPACITY = 5;

    // L1 → LRU Cache
    private LinkedHashMap<String, String> L1;

    // L2 → Normal HashMap
    private HashMap<String, String> L2;

    // L3 → Database simulation
    private HashMap<String, String> L3;

    // Access count for promotion
    private HashMap<String, Integer> accessCount;

    // Stats
    private int L1_hits = 0, L2_hits = 0, L3_hits = 0;

    public MultiLevelCache() {

        L1 = new LinkedHashMap<String, String>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new HashMap<>();
        L3 = new HashMap<>();
        accessCount = new HashMap<>();

        // preload DB
        L3.put("video_123", "Video Data 123");
        L3.put("video_456", "Video Data 456");
        L3.put("video_999", "Video Data 999");
    }

    // Get video
    public String getVideo(String videoId) {

        // L1 check
        if (L1.containsKey(videoId)) {
            L1_hits++;
            return "L1 HIT → " + L1.get(videoId);
        }

        // L2 check
        if (L2.containsKey(videoId)) {
            L2_hits++;
            promoteToL1(videoId, L2.get(videoId));
            return "L2 HIT → promoted to L1";
        }

        // L3 (DB)
        if (L3.containsKey(videoId)) {
            L3_hits++;
            String data = L3.get(videoId);

            // add to L2
            if (L2.size() >= L2_CAPACITY) {
                // remove random (simple eviction)
                String key = L2.keySet().iterator().next();
                L2.remove(key);
            }

            L2.put(videoId, data);
            return "L3 HIT → added to L2";
        }

        return "Video not found";
    }

    // Promote to L1
    private void promoteToL1(String videoId, String data) {

        int count = accessCount.getOrDefault(videoId, 0) + 1;
        accessCount.put(videoId, count);

        if (count >= 2) { // threshold
            L1.put(videoId, data);
        }
    }

    // Stats
    public void getStatistics() {

        int total = L1_hits + L2_hits + L3_hits;

        System.out.println("L1 Hit Rate: " + percent(L1_hits, total));
        System.out.println("L2 Hit Rate: " + percent(L2_hits, total));
        System.out.println("L3 Hit Rate: " + percent(L3_hits, total));
    }

    private String percent(int x, int total) {
        if (total == 0) return "0%";
        return (x * 100 / total) + "%";
    }

    // MAIN METHOD
    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        System.out.println(cache.getVideo("video_123")); // L3
        System.out.println(cache.getVideo("video_123")); // L2 → promote
        System.out.println(cache.getVideo("video_123")); // L1

        System.out.println(cache.getVideo("video_999")); // L3

        cache.getStatistics();
    }
}