package nu.eats.gui.plaf.component;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_STATE;
import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

@FunctionalInterface
public interface StatefulComponentVariant<T extends JComponent, S extends ComponentState<T>> extends PropertyChangeListener {

    @SuppressWarnings("unchecked")
    static <T extends JComponent, S extends ComponentState<T>> StatefulComponentVariant<T, S> get(T component) {
        Object variant = component.getClientProperty(KEY_COMPONENT_VARIANT);

        return variant instanceof StatefulComponentVariant<?, ?> ? (StatefulComponentVariant<T, S>) variant : null;
    }

    static <T extends JComponent, S extends ComponentState<T>> void set(T component, StatefulComponentVariant<T, S> variant) {
        component.putClientProperty(KEY_COMPONENT_VARIANT, variant);
    }

    void apply(T component, S state);

    @Override
    @SuppressWarnings("unchecked")
    default void propertyChange(PropertyChangeEvent event) {
        if (!(event.getSource() instanceof JComponent eventComponent)) return;
        if (eventComponent.getClientProperty(KEY_COMPONENT_VARIANT) != this) return;

        // Safety: Since it passed the ownership check, we statically guarantee the component is T
        T component = (T) eventComponent;

        Object eventState;

        switch (event.getPropertyName()) {
            case KEY_COMPONENT_VARIANT -> eventState = component.getClientProperty(KEY_COMPONENT_STATE);
            case KEY_COMPONENT_STATE -> eventState = event.getNewValue();

            default -> {
                return;
            }
        }

        if (eventState instanceof ComponentState) {
            this.apply(component, (S) eventState);
        }
    }

    default void install(T component, S initialState) {
        StatefulComponentVariant.set(component, this);
        initialState.install(component);

        this.apply(component, initialState);

        component.removePropertyChangeListener(this);
        component.addPropertyChangeListener(KEY_COMPONENT_VARIANT, this);
        component.addPropertyChangeListener(KEY_COMPONENT_STATE, this);

        component.repaint();
    }
}