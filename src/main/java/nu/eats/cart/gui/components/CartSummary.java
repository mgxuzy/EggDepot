package nu.eats.cart.gui.components;

import nu.eats.common.messaging.EventBus;
import nu.eats.cart.model.CartItem;
import nu.eats.gui.plaf.Theme;
import nu.eats.cart.state.CartState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.function.Supplier;

public class CartSummary extends JPanel {
    public CartSummary() {
        super(new BorderLayout());

        setOpaque(false);
        getAccessibleContext().setAccessibleName("Order Summary");

        var totalSummaryRow = new JPanel(new BorderLayout());

        totalSummaryRow.setOpaque(false);
        totalSummaryRow.setBorder(new EmptyBorder(15, 0, 15, 0));

        var totalLabel = new JLabel("Total");

        totalLabel.setFont(Theme.FONT_BOLD_BASE);
        totalLabel.setForeground(Theme.COLOR_FG_MUTED);

        var subtotalPriceLabel = new JLabel("₱0.00");

        subtotalPriceLabel.setFont(Theme.FONT_BOLD_24);
        subtotalPriceLabel.setForeground(Theme.COLOR_GREEN);
        subtotalPriceLabel.getAccessibleContext().setAccessibleName("Subtotal Price");

        totalSummaryRow.add(totalLabel, BorderLayout.WEST);
        totalSummaryRow.add(subtotalPriceLabel, BorderLayout.EAST);

        add(totalSummaryRow, BorderLayout.NORTH);

        var mainBus = EventBus.mainBus();

        mainBus.subscribe(CartState.SUBTOTAL_CHANGED, total -> {
            subtotalPriceLabel.setText(String.format("₱%.2f", total));
        });

        mainBus.subscribe(CartState.CHECKED_OUT, this::showReceipt);
    }

    private void showReceipt(CartItem[] items) {
        if (items.length == 0) return;

        final int WIDTH = 36;

        var sb = new StringBuilder();

        Function<String, String> center = text -> {
            int padding = (WIDTH - text.length()) / 2;

            return " ".repeat(Math.max(0, padding)) + text;
        };

        Supplier<String> divider = () -> "=".repeat(WIDTH) + "\n";
        Supplier<String> thinDivider = () -> "-".repeat(WIDTH) + "\n";

        // Header
        sb.append(divider.get());
        sb.append(center.apply("NU Cafeteria")).append("\n");
        sb.append(divider.get());

        // Timestamp (centered)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss"));
        sb.append(center.apply(timestamp)).append("\n");
        sb.append(divider.get()).append("\n");

        // Column headers
        sb.append(String.format("%-20s %4s %8s\n", "Item", "Qty", "Price"));
        sb.append(thinDivider.get());

        // Items
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalQty = 0;

        for (var item : items) {
            String name = item.catalogItem().product().name();
            if (name.length() > 20) name = name.substring(0, 17) + "...";

            int qty = item.quantity();
            BigDecimal lineTotal = item.catalogItem().product().price()
                    .multiply(BigDecimal.valueOf(qty));

            subtotal = subtotal.add(lineTotal);
            totalQty += qty;

            sb.append(String.format("%-20s x%-3d %7.2f\n", name, qty, lineTotal));
        }

        // Totals section
        sb.append(thinDivider.get());
        sb.append(String.format("%-20s %-4s %7s\n",
                totalQty + " item(s)", "", ""));
        sb.append(divider.get());
        sb.append(String.format("%-24s %7.2f\n", "  TOTAL (PHP)", subtotal));
        sb.append(divider.get()).append("\n");

        // Footer
        sb.append(center.apply("Thank you for ordering!")).append("\n");
        sb.append(center.apply("Enjoy your meal!")).append("\n\n");
        sb.append(center.apply("-- NU Eats --")).append("\n");
        sb.append(divider.get());

        // Display
        var receiptArea = new JTextArea(sb.toString());

        receiptArea.setFont(Theme.FONT_MONO_14);
        receiptArea.setEditable(false);
        receiptArea.setOpaque(false);
        receiptArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        receiptArea.getAccessibleContext().setAccessibleName("Order Receipt Details");
        receiptArea.setSize(receiptArea.getPreferredSize());

        var scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(receiptArea.getPreferredSize());

        JOptionPane.showMessageDialog(this, scrollPane, "Order Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
}