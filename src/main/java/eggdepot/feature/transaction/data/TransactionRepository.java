package eggdepot.feature.transaction.data;

import eggdepot.feature.transaction.model.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TransactionRepository {
    private final DatabaseManager dbManager;

    public TransactionRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void save(Transaction transaction) {
        if (!dbManager.isAvailable()) {
            return;
        }

        String sql = "INSERT INTO transactions (product, quantity, price, total, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, transaction.product());
            pstmt.setInt(2, transaction.quantity());
            pstmt.setDouble(3, transaction.price());
            pstmt.setDouble(4, transaction.total());
            pstmt.setTimestamp(5, Timestamp.valueOf(transaction.date()));

            pstmt.executeUpdate();

            // Obtain auto-generated ID if needed (though we keep the in-memory counter matched or let it auto-assign)
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Option to sync transaction record ID if needed, but not strictly required since it is a void method
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Failed to save transaction: " + e.getMessage());
        }
    }

    public List<Transaction> findAll() {
        return findByDateRangeAndType(null, null, "All");
    }

    public List<Transaction> findByDateRange(LocalDate from, LocalDate to) {
        return findByDateRangeAndType(from, to, "All");
    }

    public List<Transaction> findByDateRangeAndType(LocalDate from, LocalDate to, String type) {
        List<Transaction> list = new ArrayList<>();
        if (!dbManager.isAvailable()) {
            return list;
        }

        StringBuilder sql = new StringBuilder("SELECT id, product, quantity, price, total, date FROM transactions WHERE 1=1");
        boolean hasFrom = (from != null);
        boolean hasTo = (to != null);

        if (hasFrom) {
            sql.append(" AND date >= ?");
        }
        if (hasTo) {
            sql.append(" AND date <= ?");
        }

        if ("Piece".equalsIgnoreCase(type)) {
            sql.append(" AND product NOT LIKE '%Tray%'");
        } else if ("Tray".equalsIgnoreCase(type)) {
            sql.append(" AND product LIKE '%Tray%'");
        }

        sql.append(" ORDER BY date DESC");

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (hasFrom) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(from.atStartOfDay()));
            }
            if (hasTo) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(to.atTime(23, 59, 59, 999000000)));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String product = rs.getString("product");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    double total = rs.getDouble("total");
                    LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                    list.add(new Transaction(id, product, quantity, price, total, date));
                }
            }
        } catch (SQLException e) {
            System.err.println("[TransactionRepository] Failed to query transactions: " + e.getMessage());
        }

        return list;
    }
}
