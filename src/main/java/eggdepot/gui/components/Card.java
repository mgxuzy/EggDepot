package eggdepot.gui.components;

import javax.swing.*;
import java.awt.*;

public abstract class Card extends JPanel {
    public Card(LayoutManager manager) {
        super(manager);
    }

    public void stack(Card card) {
        var parent = getParent();

        if (parent.getLayout() instanceof CardLayout cardLayout) {
            var cardName = card.name();

            parent.add(card, cardName);
            cardLayout.show(parent, cardName);
        }
    }

    public void unstack() {
        var parent = getParent();

        if (parent.getLayout() instanceof CardLayout cardLayout) {
            cardLayout.previous(parent);
            parent.remove(this);
        }
    }

    public String name() {
        return getClass().getName();
    }
}
