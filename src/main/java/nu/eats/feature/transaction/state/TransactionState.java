package nu.eats.feature.transaction.state;

import nu.eats.common.messaging.Topic;

public final class TransactionState {
    public static final Topic<Void> TRANSACTIONS_UPDATED = new Topic<>();

    private TransactionState() {
    }
}
