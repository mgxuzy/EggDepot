package nu.eats.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.common.resources.Images;
import nu.eats.feature.cart.gui.ShoppingCartView;
import nu.eats.feature.cart.model.ShoppingCart;
import nu.eats.feature.cart.model.ShoppingCartItem;
import nu.eats.feature.cart.state.CartViewModel;
import nu.eats.feature.chatbot.ChatView;
import nu.eats.feature.chatbot.GroqChatClient;
import nu.eats.feature.inventory.gui.InventoryView;
import nu.eats.feature.inventory.model.Inventory;
import nu.eats.feature.inventory.model.InventoryItem;
import nu.eats.feature.inventory.state.InventoryViewModel;
import nu.eats.feature.inventory.state.StoreState;
import nu.eats.feature.transaction.gui.SalesReportView;
import nu.eats.feature.transaction.gui.TransactionView;
import nu.eats.gui.components.Section;
import nu.eats.gui.components.panel.SidePanel;
import nu.eats.gui.components.panel.SidePanelRow;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainView extends JPanel {
    private final JPanel mainContent;
    private final CartViewModel cartViewModel;
    private final SidePanel leftSidePanel;
    private final CardLayout mainCardLayout;

    public MainView() {
        setLayout(new BorderLayout(Theme.SPACING_SM, 0));
        setBackground(Theme.COLOR_BG_PRESSED);
        setMinimumSize(new Dimension(600, 400));

        this.mainCardLayout = new CardLayout();
        this.leftSidePanel = new SidePanel();
        this.mainContent = new JPanel(this.mainCardLayout);

        this.cartViewModel = new CartViewModel(new ShoppingCart());

        ShoppingCartView shoppingCartView = new ShoppingCartView(this.cartViewModel);

        var leftSidePane = new Section();

        leftSidePanel.setBorder(new FramedBorder.Builder()
                .sides.top(side -> side.padding(Theme.SPACING_4XL))
                .sides.left(side -> side.margin(Theme.SPACING_2XL))
                .build()
        );

        leftSidePane.setLayout(new BorderLayout());

        var logoDisplay = new JLabel(Images.loadIcon("icon.png", 128));

        logoDisplay.setBorder(new EmptyBorder(Theme.SPACING_4XL, Theme.SPACING_4XL, Theme.SPACING_4XL, Theme.SPACING_4XL));

        leftSidePane.add(logoDisplay, BorderLayout.NORTH);
        leftSidePane.add(this.leftSidePanel, BorderLayout.CENTER);

        this.add(leftSidePane, BorderLayout.WEST);
        this.add(this.mainContent, BorderLayout.CENTER);
        this.add(shoppingCartView, BorderLayout.EAST);
        this.initPages();

        var eventBus = EventBus.mainBus();

        eventBus.subscribe(StoreState.ITEM_SELECTED, item -> {
            if (item instanceof InventoryItem inventoryItem) {
                this.cartViewModel.addToCart(new ShoppingCartItem(inventoryItem));
            }
        });
    }

    private void initPages() {
        // 1. Point of Sale page (displays egg and tray inventory)
        var posView = createPointOfSaleView();
        addPage("sales", "Sales", posView);

        // 2. Transactions page (displays transaction history table)
        var transactionsView = new TransactionView();
        addPage("transactions", "Transactions", transactionsView);

        // 3. Sales Report page (displays revenue, volume metrics and product breakdown)
        var salesReportView = new SalesReportView();
        addPage("reports", "Reports", salesReportView);

        // 4. Assistant page (placeholder section)
        var assistantView = createChatView();
        addPage("chatbot", "Chatbot", assistantView);
    }

    private InventoryView createPointOfSaleView() {
        Inventory inventory = new Inventory();
        List<InventoryItem> items = TestData.getCoffeeShopMenu(null);

        for (InventoryItem item : items) {
            inventory.add(item);
        }

        return new InventoryView(new InventoryViewModel(inventory));
    }

    private JComponent createChatView() {
        var chatView = new ChatView();

        GroqChatClient client = new GroqChatClient();

        chatView.setMessageSentListener(message -> {
            CompletableFuture.supplyAsync(() -> client.chat(message))
                    .thenAccept(response -> {
                        SwingUtilities.invokeLater(() -> chatView.addMessage(response, false));
                    });
        });

        CompletableFuture.supplyAsync(() -> client.chat("Hi!"))
                .thenAccept(assistantResponse -> {
                    SwingUtilities.invokeLater(() -> chatView.addMessage(assistantResponse, false));
                });

        return chatView;
    }

    private void addPage(String id, String name, JComponent view) {
        this.mainContent.add(view, id);

        SidePanelRow row = new SidePanelRow(name);
        row.setActionCommand(name);

        row.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                this.mainCardLayout.show(this.mainContent, id);
            }
        });

        if (this.mainContent.getComponentCount() == 1) {
            row.setSelected(true);
        }

        this.leftSidePanel.addRow(row);
    }
}
