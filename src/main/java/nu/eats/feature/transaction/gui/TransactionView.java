package nu.eats.feature.transaction.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.feature.transaction.model.Transaction;
import nu.eats.feature.transaction.state.TransactionManager;
import nu.eats.feature.transaction.state.TransactionState;
import nu.eats.gui.components.H2;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionView extends Section {
    private final TransactionTableModel tableModel;

    public TransactionView() {
        setLayout(new BorderLayout(Theme.SPACING_MD, Theme.SPACING_MD));
        setBorder(new FramedBorder.Builder()
                .corners(corner -> corner.radius(Theme.RADIUS_MD))
                .sides(side -> side.padding(Theme.SPACING_4XL))
                .build()
        );

        var title = new H2("Transaction Log");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, Theme.SPACING_MD, 0));
        add(title, BorderLayout.NORTH);

        tableModel = new TransactionTableModel();
        JTable table = new JTable(tableModel);

        // Accessibility
        table.getAccessibleContext().setAccessibleName("Transactions Table");
        table.getAccessibleContext().setAccessibleDescription("List of sales transactions.");

        // Table Styling
        table.setRowHeight(36);
        table.setFont(Theme.FONT_REGULAR_MD);
        table.getTableHeader().setFont(Theme.FONT_BOLD_MD);
        table.getTableHeader().setBackground(Theme.ZINC_800);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(Theme.COLOR_BORDER);
        table.setBackground(Theme.COLOR_BG);
        table.setForeground(Theme.COLOR_FG_PRIMARY);
        table.setSelectionBackground(Theme.COLOR_BG_PRIMARY_HOVER);
        table.setSelectionForeground(Theme.COLOR_FG_PRIMARY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Align numbers to the right and format doubles
        var rightAlignRenderer = new DefaultTableCellRenderer();
        rightAlignRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Product Name
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Quantity
        table.getColumnModel().getColumn(2).setCellRenderer(rightAlignRenderer);
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Price
        table.getColumnModel().getColumn(3).setCellRenderer(rightAlignRenderer);
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Total
        table.getColumnModel().getColumn(4).setCellRenderer(rightAlignRenderer);
        table.getColumnModel().getColumn(5).setPreferredWidth(180); // Date

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(Theme.COLOR_BG);
        add(scrollPane, BorderLayout.CENTER);

        // Subscribe to transactions update
        EventBus.mainBus().subscribe(TransactionState.TRANSACTIONS_UPDATED, _ -> {
            tableModel.fireTableDataChanged();
        });
    }

    private static class TransactionTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Product", "Quantity", "Price", "Total", "Date"};
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
                case 0 -> Integer.class;
                case 1 -> String.class;
                case 2 -> Integer.class;
                case 3, 4 -> String.class; // formatted as currency-friendly strings
                case 5 -> String.class;
                default -> Object.class;
            };
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<Transaction> transactions = TransactionManager.getInstance().getTransactions();
            if (rowIndex < 0 || rowIndex >= transactions.size()) {
                return null;
            }
            // Show recent transactions at the top of the table (reverse order)
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