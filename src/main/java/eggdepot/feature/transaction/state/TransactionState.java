package eggdepot.feature.transaction.state;

import eggdepot.common.messaging.Topic;

public final class TransactionState {
    public static final Topic<Void> TRANSACTIONS_UPDATED = new Topic<>();

    private TransactionState() {
    }
}
