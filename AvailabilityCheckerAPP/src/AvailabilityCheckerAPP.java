import java.util.*;

class AvailabilityCheckerAPP {

    private HashMap<String, Integer> userMap;
    private HashMap<String, Integer> attemptMap;

    public AvailabilityCheckerAPP() {
        userMap = new HashMap<>();
        attemptMap = new HashMap<>();
    }

    public void addUser(String username, int userId) {
        userMap.put(username, userId);
    }

    public boolean checkAvailability(String username) {
        attemptMap.put(username, attemptMap.getOrDefault(username, 0) + 1);
        return !userMap.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String newName = username + i;
            if (!userMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        if (username.contains("_")) {
            String alt = username.replace("_", ".");
            if (!userMap.containsKey(alt)) {
                suggestions.add(alt);
            }
        }

        return suggestions;
    }

    public String getMostAttempted() {
        String maxUser = null;
        int maxCount = 0;

        for (String user : attemptMap.keySet()) {
            int count = attemptMap.get(user);
            if (count > maxCount) {
                maxCount = count;
                maxUser = user;
            }
        }

        return maxUser;
    }

    // ✅ MAIN METHOD HERE
    public static void main(String[] args) {
        AvailabilityCheckerAPP app = new AvailabilityCheckerAPP();

        app.addUser("john_doe", 1);

        System.out.println(app.checkAvailability("john_doe")); // false
        System.out.println(app.checkAvailability("jane_smith")); // true

        System.out.println(app.suggestAlternatives("john_doe"));
        System.out.println(app.getMostAttempted());
    }
}