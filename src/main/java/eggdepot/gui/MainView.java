package eggdepot.gui;

import eggdepot.common.messaging.EventBus;
import eggdepot.common.resources.Images;
import eggdepot.feature.cart.gui.ShoppingCartView;
import eggdepot.feature.cart.model.ShoppingCart;
import eggdepot.feature.cart.model.ShoppingCartItem;
import eggdepot.feature.cart.state.CartViewModel;
import eggdepot.feature.chatbot.ChatView;
import eggdepot.feature.chatbot.GroqChatClient;
import eggdepot.feature.inventory.gui.InventoryView;
import eggdepot.feature.inventory.model.Inventory;
import eggdepot.feature.inventory.model.InventoryItem;
import eggdepot.feature.inventory.state.InventoryViewModel;
import eggdepot.feature.inventory.state.StoreState;
import eggdepot.feature.transaction.gui.SalesReportView;
import eggdepot.feature.transaction.gui.TransactionView;
import eggdepot.gui.components.H2;
import eggdepot.gui.components.Section;
import eggdepot.gui.components.panel.SidePanel;
import eggdepot.gui.components.panel.SidePanelRow;
import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.border.framed.FramedBorder;

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
        var salesView = createSalesView();

        addPage("Sales", salesView);

        var transactionsView = new TransactionView();

        addPage("Transactions", transactionsView);

        var reportsView = new SalesReportView();

        addPage("Reports", reportsView);

        var assistantView = createChatView();

        addPage("Chatbot", assistantView);
    }

    private InventoryView createSalesView() {
        Inventory inventory = new Inventory();
        List<InventoryItem> items = TestData.getCoffeeShopMenu(null);

        for (InventoryItem item : items) {
            inventory.add(item);
        }

        return new InventoryView(new InventoryViewModel(inventory));
    }

    private JComponent createChatView() {
        var chatPanel = new Section();
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

        chatPanel.setLayout(new BorderLayout());

        chatPanel.add(new H2("Jarvis"), BorderLayout.NORTH);
        chatPanel.add(chatView);

        return chatPanel;
    }

    private void addPage(String name, JComponent view) {
        this.mainContent.add(view, name);

        SidePanelRow row = new SidePanelRow(name);

        row.setActionCommand(name);

        row.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                this.mainCardLayout.show(this.mainContent, name);
            }
        });

        if (this.mainContent.getComponentCount() == 1) {
            row.setSelected(true);
        }

        this.leftSidePanel.addRow(row);
    }
}
