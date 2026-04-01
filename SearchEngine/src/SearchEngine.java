import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children;
    List<String> words; // store words with this prefix

    public TrieNode() {
        children = new HashMap<>();
        words = new ArrayList<>();
    }
}

class AutocompleteSystem {

    private TrieNode root;
    private HashMap<String, Integer> freqMap;

    public AutocompleteSystem() {
        root = new TrieNode();
        freqMap = new HashMap<>();
    }

    // Insert query into Trie
    public void insert(String query) {
        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.words.add(query); // store word for prefix
        }
    }

    // Update frequency
    public void updateFrequency(String query) {
        freqMap.put(query, freqMap.getOrDefault(query, 0) + 1);
        insert(query);
    }

    // Search prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        // Traverse Trie
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        // Get words with prefix
        List<String> words = node.words;

        // Min heap for top 10
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> freqMap.get(a) - freqMap.get(b)
        );

        for (String word : words) {
            pq.add(word);
            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result); // highest first
        return result;
    }

    // MAIN METHOD
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");

        List<String> results = system.search("jav");

        System.out.println("Suggestions:");
        for (String s : results) {
            System.out.println(s + " (" + system.freqMap.get(s) + ")");
        }
    }
}