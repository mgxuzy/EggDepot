package nu.eats.feature.transaction.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.feature.transaction.state.TransactionManager;
import nu.eats.feature.transaction.state.TransactionState;
import nu.eats.gui.components.H2;
import nu.eats.gui.components.Section;
import nu.eats.gui.components.picker.DatePickerDialog;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesReportView extends Section {
    private final JLabel revenueLabel = new JLabel();
    private final JLabel transactionsLabel = new JLabel();
    private final JLabel itemsSoldLabel = new JLabel();
    private final ProductSalesTableModel tableModel;

    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private String currentType = "All";

    private final JButton startDateButton;
    private final JButton endDateButton;
    private final JButton resetButton;
    private final JComboBox<String> typeFilter;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SalesReportView() {
        setLayout(new BorderLayout(Theme.SPACING_MD, Theme.SPACING_XL));
        setBorder(new FramedBorder.Builder()
                .corners(corner -> corner.radius(Theme.RADIUS_MD))
                .sides(side -> side.padding(Theme.SPACING_4XL))
                .build()
        );

        // --- 1. HEADER PANEL (Filters & Controls) ---
        var headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        var title = new H2("Sales Report");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, Theme.SPACING_XS, 0));
        headerPanel.add(title, BorderLayout.WEST);

        var filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, Theme.SPACING_SM, 0));
        filterPanel.setOpaque(false);

        var fromLabel = new JLabel("From:");
        fromLabel.setFont(Theme.FONT_MEDIUM_MD);
        fromLabel.setForeground(Theme.COLOR_FG_SECONDARY);

        startDateButton = new JButton("All Time");
        startDateButton.setFont(Theme.FONT_MEDIUM_MD);

        var toLabel = new JLabel("To:");
        toLabel.setFont(Theme.FONT_MEDIUM_MD);
        toLabel.setForeground(Theme.COLOR_FG_SECONDARY);

        endDateButton = new JButton("All Time");
        endDateButton.setFont(Theme.FONT_MEDIUM_MD);

        var typeLabel = new JLabel("Type:");
        typeLabel.setFont(Theme.FONT_MEDIUM_MD);
        typeLabel.setForeground(Theme.COLOR_FG_SECONDARY);

        typeFilter = new JComboBox<>(new String[]{"All", "Piece", "Tray"});
        typeFilter.setFont(Theme.FONT_MEDIUM_MD);
        typeFilter.setBackground(Theme.COLOR_SURFACE_ELEVATION_HIGHEST);
        typeFilter.setForeground(Theme.COLOR_FG_PRIMARY);

        resetButton = new JButton("Reset");
        resetButton.setFont(Theme.FONT_MEDIUM_MD);

        // Action Listeners
        startDateButton.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            DatePickerDialog picker = new DatePickerDialog(owner, startDate != null ? startDate : LocalDate.now());
            picker.setVisible(true);
            picker.getSelectedDate().ifPresent(date -> {
                startDate = date;
                updateFilterButtons();
                updateMetrics();
            });
        });

        endDateButton.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            DatePickerDialog picker = new DatePickerDialog(owner, endDate != null ? endDate : LocalDate.now());
            picker.setVisible(true);
            picker.getSelectedDate().ifPresent(date -> {
                endDate = date;
                updateFilterButtons();
                updateMetrics();
            });
        });

        typeFilter.addActionListener(e -> {
            currentType = (String) typeFilter.getSelectedItem();
            updateMetrics();
        });

        resetButton.addActionListener(e -> {
            startDate = null;
            endDate = null;
            typeFilter.setSelectedItem("All");
            currentType = "All";
            updateFilterButtons();
            updateMetrics();
        });

        filterPanel.add(fromLabel);
        filterPanel.add(startDateButton);
        filterPanel.add(toLabel);
        filterPanel.add(endDateButton);
        filterPanel.add(typeLabel);
        filterPanel.add(typeFilter);
        filterPanel.add(resetButton);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. BODY PANEL (Metrics & Table Layout) ---
        var bodyPanel = new JPanel(new BorderLayout(0, Theme.SPACING_3XL));
        bodyPanel.setOpaque(false);

        // Metrics Grid
        var metricsPanel = new JPanel(new GridLayout(1, 3, Theme.SPACING_2XL, 0));
        metricsPanel.setOpaque(false);
        metricsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 110));
        metricsPanel.add(createMetricCard("TOTAL REVENUE", revenueLabel));
        metricsPanel.add(createMetricCard("TOTAL TRANSACTIONS", transactionsLabel));
        metricsPanel.add(createMetricCard("TOTAL ITEMS SOLD", itemsSoldLabel));
        bodyPanel.add(metricsPanel, BorderLayout.NORTH);

        // --- 3. TABLE SETUP (Plain & Standard) ---
        var tableContainer = new JPanel(new BorderLayout(0, Theme.SPACING_MD));
        tableContainer.setOpaque(false);

        var breakdownTitle = new JLabel("Product Performance");
        breakdownTitle.setFont(Theme.FONT_BOLD_LG);
        breakdownTitle.setForeground(Theme.COLOR_FG_PRIMARY);
        tableContainer.add(breakdownTitle, BorderLayout.NORTH);

        tableModel = new ProductSalesTableModel();
        JTable table = new JTable(tableModel);

        // Standard Scroll Pane Wrapping
        JScrollPane scrollPane = new JScrollPane(table);

        tableContainer.add(scrollPane, BorderLayout.CENTER);
        bodyPanel.add(tableContainer, BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);

        // Initial data pull and event sub
        updateMetrics();
        EventBus.mainBus().subscribe(TransactionState.TRANSACTIONS_UPDATED, _ -> updateMetrics());
    }

    private void updateFilterButtons() {
        startDateButton.setText(startDate != null ? startDate.format(dateFormatter) : "All Time");
        endDateButton.setText(endDate != null ? endDate.format(dateFormatter) : "All Time");
    }

    private JPanel createMetricCard(String title, JLabel valueLabel) {
        var card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.COLOR_SURFACE_ELEVATION_HIGHEST);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(Theme.SPACING_3XL, Theme.SPACING_4XL, Theme.SPACING_3XL, Theme.SPACING_4XL)
        ));

        var titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_BOLD_SM);
        titleLabel.setForeground(Theme.COLOR_FG_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(Theme.FONT_BOLD_2XL);
        valueLabel.setForeground(Theme.COLOR_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(Theme.SPACING_XS));
        card.add(valueLabel);
        return card;
    }

    private void updateMetrics() {
        revenueLabel.setText(String.format("₱%,.2f", TransactionManager.getInstance().getTotalRevenue(startDate, endDate, currentType)));
        transactionsLabel.setText(String.valueOf(TransactionManager.getInstance().getTotalTransactions(startDate, endDate, currentType)));
        itemsSoldLabel.setText(String.valueOf(TransactionManager.getInstance().getTotalItemsSold(startDate, endDate, currentType)));
        tableModel.setSummaries(TransactionManager.getInstance().getProductSalesSummaries(startDate, endDate, currentType));
    }

    private static class ProductSalesTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Product", "Quantity Sold", "Unit Price", "Total Revenue"};
        private List<TransactionManager.ProductSalesSummary> summaries = List.of();

        public void setSummaries(List<TransactionManager.ProductSalesSummary> summaries) {
            this.summaries = summaries;
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return summaries.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int col) { return columnNames[col]; }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 1 ? Integer.class : String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex >= summaries.size()) return null;
            TransactionManager.ProductSalesSummary summary = summaries.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> summary.productName();
                case 1 -> summary.quantitySold();
                case 2 -> String.format("₱%,.2f", summary.unitPrice());
                case 3 -> String.format("₱%,.2f", summary.totalRevenue());
                default -> null;
            };
        }
    }
}