import java.util.*;

class AnalyticsDashboard {

    private HashMap<String, Integer> pageViews;
    private HashMap<String, Set<String>> uniqueVisitors;
    private HashMap<String, Integer> trafficSource;

    public AnalyticsDashboard() {
        pageViews = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSource = new HashMap<>();
    }

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique users
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Track traffic source
        trafficSource.put(source, trafficSource.getOrDefault(source, 0) + 1);
    }

    // Get Top 10 pages
    public List<String> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        List<String> result = new ArrayList<>();
        int count = 0;

        while (!pq.isEmpty() && count < 10) {
            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            result.add(url + " - " + views + " views (" + unique + " unique)");
            count++;
        }

        return result;
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("=== Top Pages ===");
        for (String s : getTopPages()) {
            System.out.println(s);
        }

        System.out.println("\n=== Traffic Sources ===");
        for (String src : trafficSource.keySet()) {
            System.out.println(src + " → " + trafficSource.get(src));
        }
    }

    // MAIN METHOD
    public static void main(String[] args) throws InterruptedException {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        // Simulate events
        dashboard.processEvent("/article/news", "user1", "google");
        dashboard.processEvent("/article/news", "user2", "facebook");
        dashboard.processEvent("/sports/match", "user3", "google");
        dashboard.processEvent("/article/news", "user1", "direct");
        dashboard.processEvent("/sports/match", "user4", "google");

        // Simulate real-time update (every 5 sec)
        Thread.sleep(2000); // just demo

        dashboard.getDashboard();
    }
}