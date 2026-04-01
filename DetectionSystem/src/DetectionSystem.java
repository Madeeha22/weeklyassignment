import java.util.*;

class PlagiarismDetector {

    // n-gram → set of document IDs
    private HashMap<String, Set<String>> ngramMap;

    private int N = 3; // you can use 5 or 7 for better accuracy

    public PlagiarismDetector() {
        ngramMap = new HashMap<>();
    }

    // Add document
    public void addDocument(String docId, String text) {

        String[] words = text.split(" ");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder ngram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                ngram.append(words[i + j]).append(" ");
            }

            String gram = ngram.toString().trim();

            ngramMap.putIfAbsent(gram, new HashSet<>());
            ngramMap.get(gram).add(docId);
        }
    }

    // Analyze new document
    public void analyzeDocument(String docId, String text) {

        String[] words = text.split(" ");
        int totalGrams = 0;

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (int i = 0; i <= words.length - N; i++) {

            totalGrams++;

            StringBuilder ngram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                ngram.append(words[i + j]).append(" ");
            }

            String gram = ngram.toString().trim();

            if (ngramMap.containsKey(gram)) {

                for (String existingDoc : ngramMap.get(gram)) {
                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        // Print results
        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / totalGrams;

            System.out.println("Compared with: " + doc);
            System.out.println("Matching n-grams: " + matches);
            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 50) {
                System.out.println("⚠ PLAGIARISM DETECTED");
            }
            System.out.println();
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String doc1 = "this is a simple essay about data structures and algorithms";
        String doc2 = "this is a simple essay about machine learning and algorithms";

        detector.addDocument("essay_001", doc1);
        detector.addDocument("essay_002", doc2);

        String newDoc = "this is a simple essay about data structures and algorithms";

        detector.analyzeDocument("essay_003", newDoc);
    }
}