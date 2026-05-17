package nu.eats.gui.plaf.component;

import javax.swing.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_STATE;
import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

@FunctionalInterface
public interface ComponentVariant extends PropertyChangeListener {
    static ComponentVariant get(JComponent component) {
        var clientVariant = component.getClientProperty(KEY_COMPONENT_VARIANT);

        return clientVariant instanceof ComponentVariant variant ? variant : null;
    }

    static void set(JComponent component, ComponentVariant variant) {
        component.putClientProperty(KEY_COMPONENT_VARIANT, variant);
    }

    void apply(JComponent component, ComponentState state);

    @Override
    default void propertyChange(PropertyChangeEvent event) {
        if (!(event.getSource() instanceof JComponent component)) {
            return;
        }

        switch (event.getPropertyName()) {
            case KEY_COMPONENT_VARIANT -> {
                if (event.getNewValue() != this) return;

                this.apply(component, ComponentState.get(component));
            }

            case KEY_COMPONENT_STATE -> {
                if (!(event.getNewValue() instanceof ComponentState state)) return;

                this.apply(component, state);
            }
        }
    }

    default void install(JComponent component) {
        var initialState = ComponentState.get(component);

        component.putClientProperty(KEY_COMPONENT_VARIANT, this);

        this.apply(component, initialState);

        component.removePropertyChangeListener(this);
        component.addPropertyChangeListener(KEY_COMPONENT_VARIANT, this);
        component.addPropertyChangeListener(KEY_COMPONENT_STATE, this);
    }
}
