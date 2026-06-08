package nu.eats.feature.transaction.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.feature.transaction.model.Transaction;
import nu.eats.feature.transaction.state.TransactionManager;
import nu.eats.feature.transaction.state.TransactionState;
import nu.eats.gui.components.H2;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionView extends Section {
    private final TransactionTableModel tableModel;

    public TransactionView() {
        setLayout(new BorderLayout(Theme.SPACING_MD, Theme.SPACING_MD));

        var title = new H2("History");

        add(title, BorderLayout.NORTH);

        tableModel = new TransactionTableModel();
        JTable table = new JTable(tableModel);

        // Accessibility metadata
        table.getAccessibleContext().setAccessibleName("Transactions Table");
        table.getAccessibleContext().setAccessibleDescription("List of sales transactions.");

        // Clean & standard ScrollPane wrapper layout handle
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Subscribe to transactions update events
        EventBus.mainBus().subscribe(TransactionState.TRANSACTIONS_UPDATED, _ -> {
            tableModel.fireTableDataChanged();
        });
    }

    private static class TransactionTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Product", "Quantity", "Unit Price", "Total", "Date"};
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public int getRowCount() {
            return TransactionManager.getInstance().getTransactions().size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0, 2 -> Integer.class;
                default -> String.class;
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<Transaction> transactions = TransactionManager.getInstance().getTransactions();
            if (rowIndex < 0 || rowIndex >= transactions.size()) {
                return null;
            }
            // Reverse chronological order: newest entries display at the top track position
            Transaction tx = transactions.get(transactions.size() - 1 - rowIndex);
            return switch (columnIndex) {
                case 0 -> tx.id();
                case 1 -> tx.product();
                case 2 -> tx.quantity();
                case 3 -> String.format("₱%,.2f", tx.price());
                case 4 -> String.format("₱%,.2f", tx.total());
                case 5 -> tx.date().format(formatter);
                default -> null;
            };
        }
    }
}