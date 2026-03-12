import java.util.concurrent.*;
import java.util.*;

class Transaction {
    String id; double amount; String userId;
    Transaction(String id, String userId, double amount) { 
        this.id = id; this.userId = userId; this.amount = amount; 
    }
}

public class FraudEngine {
    private static final ConcurrentHashMap<String, Long> userLastTransaction = new ConcurrentHashMap<>();

    public static String processTransaction(Transaction tx) {
        long currentTime = System.currentTimeMillis();
        long lastTime = userLastTransaction.getOrDefault(tx.userId, 0L);

      
        if (currentTime - lastTime < 2000) {
            return "ALARM: Potential Fraud for User " + tx.userId;
        }
        
        userLastTransaction.put(tx.userId, currentTime);
        return "SUCCESS: Transaction processed for " + tx.userId;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Transaction[] txs = { new Transaction("1", "Mohit", 100), new Transaction("2", "Mohit", 200) };

        for (Transaction t : txs) {
            executor.execute(() -> System.out.println(processTransaction(t)));
        }
        executor.shutdown();
    }
}
