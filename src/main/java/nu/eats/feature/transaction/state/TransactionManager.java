package nu.eats.feature.transaction.state;

import nu.eats.common.messaging.EventBus;
import nu.eats.feature.cart.model.ShoppingCartItem;
import nu.eats.feature.cart.state.CartState;
import nu.eats.feature.transaction.data.DatabaseManager;
import nu.eats.feature.transaction.data.TransactionRepository;
import nu.eats.feature.transaction.model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class TransactionManager {
    private static final TransactionManager INSTANCE = new TransactionManager();

    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final TransactionRepository repository;

    private TransactionManager() {
        // Initialize Database
        var dbManager = DatabaseManager.getInstance();
        dbManager.initialize();
        this.repository = new TransactionRepository(dbManager);

        if (dbManager.isAvailable()) {
            List<Transaction> dbTransactions = repository.findAll();
            if (dbTransactions.isEmpty()) {
                // Pre-populate database with mock transactions
                addMockTransaction("Chicken Egg (Small)", 3, 89.00, LocalDateTime.now().minusHours(4));
                addMockTransaction("Chicken Egg (Medium) Tray", 1, 69.00, LocalDateTime.now().minusHours(2));
                addMockTransaction("Chicken Egg (Large)", 5, 89.00, LocalDateTime.now().minusMinutes(30));
            } else {
                transactions.addAll(dbTransactions);
            }
        } else {
            // Fallback: Populate memory list with mock transactions
            addMockTransaction("Chicken Egg (Small)", 3, 89.00, LocalDateTime.now().minusHours(4));
            addMockTransaction("Chicken Egg (Medium) Tray", 1, 69.00, LocalDateTime.now().minusHours(2));
            addMockTransaction("Chicken Egg (Large)", 5, 89.00, LocalDateTime.now().minusMinutes(30));
        }

        // Sync ID counter
        int maxId = 0;
        for (Transaction tx : transactions) {
            if (tx.id() > maxId) {
                maxId = tx.id();
            }
        }
        idCounter.set(maxId);

        // Subscribe to checkout events
        EventBus.mainBus().subscribe(CartState.CHECKED_OUT, this::handleCheckout);
    }

    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    private void addMockTransaction(String product, int quantity, double price, LocalDateTime date) {
        int id = idCounter.incrementAndGet();
        double total = price * quantity;
        Transaction tx = new Transaction(id, product, quantity, price, total, date);
        transactions.add(tx);
        repository.save(tx);
    }

    private void handleCheckout(ShoppingCartItem[] cartItems) {
        if (cartItems == null || cartItems.length == 0) {
            return;
        }

        // Each checkout can contain multiple items. We create a transaction entry for each item.
        for (ShoppingCartItem item : cartItems) {
            int id = idCounter.incrementAndGet();
            String product = item.catalogItem().product().name();
            int quantity = item.quantity();
            double price = item.catalogItem().product().price().doubleValue();
            double total = price * quantity;
            LocalDateTime date = LocalDateTime.now();

            Transaction tx = new Transaction(id, product, quantity, price, total, date);
            transactions.add(tx);
            repository.save(tx);
        }

        // Notify subscribers of transaction updates
        EventBus.mainBus().publish(TransactionState.TRANSACTIONS_UPDATED);
    }

    // --- Date and Type Filtered Queries ---

    public List<Transaction> getTransactions(LocalDate from, LocalDate to, String type) {
        if (DatabaseManager.getInstance().isAvailable()) {
            return repository.findByDateRangeAndType(from, to, type);
        }

        // Memory fallback filtering
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction tx : transactions) {
            LocalDate txDate = tx.date().toLocalDate();
            if (from != null && txDate.isBefore(from)) {
                continue;
            }
            if (to != null && txDate.isAfter(to)) {
                continue;
            }
            if ("Piece".equalsIgnoreCase(type) && tx.product().toLowerCase().contains("tray")) {
                continue;
            }
            if ("Tray".equalsIgnoreCase(type) && !tx.product().toLowerCase().contains("tray")) {
                continue;
            }
            filtered.add(tx);
        }
        // Keep descending chronological order like in DB
        filtered.sort((a, b) -> b.date().compareTo(a.date()));
        return filtered;
    }

    public double getTotalRevenue(LocalDate from, LocalDate to, String type) {
        double total = 0.0;
        for (Transaction tx : getTransactions(from, to, type)) {
            total += tx.total();
        }
        return total;
    }

    public int getTotalTransactions(LocalDate from, LocalDate to, String type) {
        return getTransactions(from, to, type).size();
    }

    public int getTotalItemsSold(LocalDate from, LocalDate to, String type) {
        int total = 0;
        for (Transaction tx : getTransactions(from, to, type)) {
            total += tx.quantity();
        }
        return total;
    }

    public List<ProductSalesSummary> getProductSalesSummaries(LocalDate from, LocalDate to, String type) {
        Map<String, ProductSalesSummaryBuilder> map = new LinkedHashMap<>();

        for (Transaction tx : getTransactions(from, to, type)) {
            map.computeIfAbsent(tx.product(), name -> new ProductSalesSummaryBuilder(name, tx.price()))
               .add(tx.quantity(), tx.total());
        }

        List<ProductSalesSummary> summaries = new ArrayList<>();
        for (ProductSalesSummaryBuilder builder : map.values()) {
            summaries.add(builder.build());
        }
        return summaries;
    }

    // --- Legacy / Date-Only Overloads ---

    public List<Transaction> getTransactions(LocalDate from, LocalDate to) {
        return getTransactions(from, to, "All");
    }

    public double getTotalRevenue(LocalDate from, LocalDate to) {
        return getTotalRevenue(from, to, "All");
    }

    public int getTotalTransactions(LocalDate from, LocalDate to) {
        return getTotalTransactions(from, to, "All");
    }

    public int getTotalItemsSold(LocalDate from, LocalDate to) {
        return getTotalItemsSold(from, to, "All");
    }

    public List<ProductSalesSummary> getProductSalesSummaries(LocalDate from, LocalDate to) {
        return getProductSalesSummaries(from, to, "All");
    }

    // --- Legacy / Unfiltered Delegate Queries ---

    public List<Transaction> getTransactions() {
        return getTransactions(null, null, "All");
    }

    public double getTotalRevenue() {
        return getTotalRevenue(null, null, "All");
    }

    public int getTotalTransactions() {
        return getTotalTransactions(null, null, "All");
    }

    public int getTotalItemsSold() {
        return getTotalItemsSold(null, null, "All");
    }

    public List<ProductSalesSummary> getProductSalesSummaries() {
        return getProductSalesSummaries(null, null, "All");
    }

    private static class ProductSalesSummaryBuilder {
        private final String productName;
        private final double unitPrice;
        private int quantitySold = 0;
        private double totalRevenue = 0.0;

        public ProductSalesSummaryBuilder(String productName, double unitPrice) {
            this.productName = productName;
            this.unitPrice = unitPrice;
        }

        public void add(int quantity, double total) {
            this.quantitySold += quantity;
            this.totalRevenue += total;
        }

        public ProductSalesSummary build() {
            return new ProductSalesSummary(productName, quantitySold, unitPrice, totalRevenue);
        }
    }

    public record ProductSalesSummary(
        String productName,
        int quantitySold,
        double unitPrice,
        double totalRevenue
    ) {}
}
