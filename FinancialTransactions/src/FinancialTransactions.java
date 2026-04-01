import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    int time; // minutes
    String account;

    public Transaction(int id, int amount, String merchant, int time, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
        this.account = account;
    }
}

class TransactionAnalyzer {

    // Classic Two-Sum
    public void findTwoSum(List<Transaction> list, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : list) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                System.out.println("Pair: " + map.get(complement).id + ", " + t.id);
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum with 1 hour window
    public void findTwoSumWithTime(List<Transaction> list, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : list) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= 60) {
                    System.out.println("Time Pair: " + prev.id + ", " + t.id);
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    public void detectDuplicates(List<Transaction> list) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {
            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {
            List<Transaction> group = map.get(key);

            if (group.size() > 1) {
                System.out.println("Duplicate: " + key);
                for (Transaction t : group) {
                    System.out.println("  ID: " + t.id + ", Account: " + t.account);
                }
            }
        }
    }

    // K-Sum (simple recursive)
    public void findKSum(List<Transaction> list, int k, int target) {
        backtrack(list, k, target, 0, new ArrayList<>());
    }

    private void backtrack(List<Transaction> list, int k, int target,
                           int start, List<Transaction> path) {

        if (k == 0 && target == 0) {
            System.out.print("K-Sum: ");
            for (Transaction t : path) {
                System.out.print(t.id + " ");
            }
            System.out.println();
            return;
        }

        if (k == 0 || target < 0) return;

        for (int i = start; i < list.size(); i++) {
            path.add(list.get(i));
            backtrack(list, k - 1, target - list.get(i).amount, i + 1, path);
            path.remove(path.size() - 1);
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        List<Transaction> list = new ArrayList<>();

        list.add(new Transaction(1, 500, "StoreA", 600, "acc1"));
        list.add(new Transaction(2, 300, "StoreB", 615, "acc2"));
        list.add(new Transaction(3, 200, "StoreC", 630, "acc3"));
        list.add(new Transaction(4, 500, "StoreA", 640, "acc4"));

        TransactionAnalyzer ta = new TransactionAnalyzer();

        System.out.println("Two-Sum:");
        ta.findTwoSum(list, 500);

        System.out.println("\nTwo-Sum with Time:");
        ta.findTwoSumWithTime(list, 500);

        System.out.println("\nDuplicates:");
        ta.detectDuplicates(list);

        System.out.println("\nK-Sum:");
        ta.findKSum(list, 3, 1000);
    }
}