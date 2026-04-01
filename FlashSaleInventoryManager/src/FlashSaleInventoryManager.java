import java.util.*;

class FlashSaleInventoryManager {

    private HashMap<String, Integer> stockMap;
    private HashMap<String, Queue<Integer>> waitListMap;

    public FlashSaleInventoryManager() {
        stockMap = new HashMap<>();
        waitListMap = new HashMap<>();
    }

    // Add product
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitListMap.put(productId, new LinkedList<>());
    }

    // Check stock (O(1))
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase (Thread Safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success! Remaining stock: " + (stock - 1);
        } else {
            Queue<Integer> queue = waitListMap.get(productId);
            queue.add(userId);
            return "Out of stock! Added to waiting list. Position: " + queue.size();
        }
    }

    // Restock and serve waiting list
    public synchronized void restock(String productId, int quantity) {

        int stock = stockMap.getOrDefault(productId, 0);
        stock += quantity;

        Queue<Integer> queue = waitListMap.get(productId);

        while (stock > 0 && !queue.isEmpty()) {
            int user = queue.poll();
            System.out.println("Notify user " + user);
            stock--;
        }

        stockMap.put(productId, stock);
    }

    // MAIN METHOD
    public static void main(String[] args) {

        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();

        manager.addProduct("IPHONE15_256GB", 2);

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 101));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 102));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 103)); // waiting

        manager.restock("IPHONE15_256GB", 1);
    }
}