package eggdepot.gui.plaf.component;

import javax.swing.*;


import static eggdepot.gui.plaf.Constants.KEY_COMPONENT_STATE;

public interface ComponentState<T extends JComponent> {
    static <T extends JComponent> void set(JComponent component, ComponentState<T> state) {
        component.putClientProperty(KEY_COMPONENT_STATE, state);
    }

    @SuppressWarnings("unchecked")
    static <T extends JComponent> ComponentState<T> get(T component) {
        var clientState = component.getClientProperty(KEY_COMPONENT_STATE);

        return clientState instanceof ComponentState<?> ?
                (ComponentState<T>) clientState :
                null;
    }

    default void install(T component) {
        ComponentState.set(component, this);
    }
}

