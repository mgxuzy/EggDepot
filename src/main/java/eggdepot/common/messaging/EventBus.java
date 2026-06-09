package eggdepot.common.messaging;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {
    private static final Map<Topic<?>, List<Consumer<?>>> subscriberByTopic = new ConcurrentHashMap<>();

    private EventBus() {
    }

    public static EventBus mainBus() {
        return InstanceHolder.MAIN_BUS;
    }

    public <T> Subscription subscribe(Topic<T> topic, Consumer<T> subscriber) {
        subscriberByTopic.computeIfAbsent(topic, _ -> new CopyOnWriteArrayList<>())
                .add(subscriber);

        return () -> {
            var subscribers = subscriberByTopic.get(topic);

            if (subscribers != null) {
                subscribers.remove(subscriber);
            }
        };
    }

    public void publish(Topic<Void> topic) {
        publish(topic, null);
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(Topic<T> topic, T data) {
        var subscribers = subscriberByTopic.get(topic);

        if (subscribers == null) {
            return;
        }

        for (Consumer<?> subscriber : subscribers) {
            ((Consumer<T>) subscriber).accept(data);
        }
    }

    private static final class InstanceHolder {
        private static final EventBus MAIN_BUS = new EventBus();
    }
}
