package nu.eats.feature.chatbot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Standard client for interacting with the Groq Chat Completion API
 * while maintaining stateful conversation history.
 */
public class GroqChatClient {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private JSONArray conversationHistory = new JSONArray().put(
            new JSONObject().put("role", "system").put("content", "You are a helpful assistant.")
    );

    /**
     * Sends a message to the Groq API, maintaining full conversation context.
     *
     * @param userMessage The new message text from the user.
     * @return The text response from the AI model.
     */
    public String chat(String userMessage) {
        conversationHistory.put(new JSONObject().put("role", "user").put("content", userMessage));

        String jsonPayload = new JSONObject()
                .put("model", "llama-3.1-8b-instant")
                .put("messages", conversationHistory)
                .toString();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            String responseBody = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();

            String assistantResponse = new JSONObject(responseBody)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            conversationHistory.put(new JSONObject().put("role", "assistant").put("content", assistantResponse));

            return assistantResponse;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Clears the current conversation memory and resets the system persona.
     */
    public void clearConversationHistory() {
        conversationHistory = new JSONArray().put(
                new JSONObject().put("role", "system").put("content", "You are a helpful assistant.")
        );
    }
}