package nu.eats.feature.chatbot;

import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A lightweight, unstyled chat UI component for Swing.
 * Uses JTextArea to simulate wrapped chat bubbles.
 */
public class ChatView extends JPanel {

    private final JPanel messageContainer;
    private final JScrollPane scrollPane;
    private final JTextField inputField;
    private final JButton sendButton;
    private Consumer<String> messageListener;

    public ChatView() {
        setLayout(new BorderLayout());

        // Container that holds all chat bubbles
        messageContainer = new JPanel(new GridBagLayout());

        setOpaque(false);

        // Wrap container in a BorderLayout panel so bubbles align to the top
        var viewportView = new JPanel(new BorderLayout());

        viewportView.setBackground(Color.WHITE);
        viewportView.add(messageContainer, BorderLayout.NORTH);

        scrollPane = new JScrollPane(viewportView);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Input section
        var inputPanel = new JPanel(new BorderLayout(5, 0));

        inputPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        inputField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Register action listeners
        inputField.addActionListener(e -> handleSendAction());
        sendButton.addActionListener(e -> handleSendAction());
    }

    /**
     * Registers a callback for when the user submits a message.
     */
    public void setMessageSentListener(Consumer<String> listener) {
        this.messageListener = listener;
    }

    /**
     * Appends a new message bubble to the conversation.
     *
     * @param text   The message content.
     * @param isUser True if the message is from the user, false for the assistant.
     */
    public void addMessage(String text, boolean isUser) {
        var bubble = new JTextArea(text);

        bubble.setEditable(false);
        bubble.setLineWrap(true);
        bubble.setWrapStyleWord(true);
        bubble.setOpaque(true);

        bubble.setFont(Theme.FONT_REGULAR_MD);
        bubble.setColumns(Math.min(text.length(), 30));

        bubble.setBackground(isUser ? new Color(220, 248, 198) : new Color(240, 240, 240));
        bubble.setForeground(Color.BLACK);

        bubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isUser ? new Color(190, 230, 170) : Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // GridBag layout constraints for positioning the bubble
        var gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = isUser ? GridBagConstraints.EAST : GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 8, 4, 8);

        messageContainer.add(bubble, gbc);
        messageContainer.revalidate();
        messageContainer.repaint();

        SwingUtilities.invokeLater(() -> {
            var vertical = scrollPane.getVerticalScrollBar();

            vertical.setValue(vertical.getMaximum());
        });
    }

    private void handleSendAction() {
        String text = inputField.getText().trim();

        if (!text.isEmpty()) {
            addMessage(text, true);

            inputField.setText("");

            if (messageListener != null) {
                messageListener.accept(text);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Groq Assistant");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            frame.setLocationRelativeTo(null);

            // Initialize both components
            GroqChatClient client = new GroqChatClient();
            ChatView chatUI = new ChatView();

            // Set up coordination between UI input and API client
            chatUI.setMessageSentListener(userMessage -> {
                // Perform network I/O asynchronously to keep UI responsive
                CompletableFuture.supplyAsync(() -> client.chat(userMessage))
                        .thenAccept(assistantResponse -> {
                            // Update the UI back on the Event Dispatch Thread
                            SwingUtilities.invokeLater(() -> chatUI.addMessage(assistantResponse, false));
                        });
            });

            frame.add(chatUI);
            frame.setVisible(true);

            CompletableFuture.supplyAsync(() -> client.chat("Hi!"))
                    .thenAccept(assistantResponse -> {
                        SwingUtilities.invokeLater(() -> chatUI.addMessage(assistantResponse, false));
                    });
        });
    }
}