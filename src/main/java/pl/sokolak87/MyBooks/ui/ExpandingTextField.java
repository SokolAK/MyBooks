package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Setter;

import java.util.Set;

public class ExpandingTextField extends TextField {

    @Setter
    private Set<Component> componentsToHide;

    public ExpandingTextField(String placeholder) {
        setPlaceholder(placeholder);
        setClearButtonVisible(true);
        setValueChangeMode(ValueChangeMode.LAZY);
        addFocusListener(e -> expandTxtFilter());
        addBlurListener(e -> collapseTxtFilter());
    }

    private void expandTxtFilter() {
        setMinWidth("100%");
        componentsToHide.forEach(c -> c.setVisible(false));
    }

    private void collapseTxtFilter() {
        setMinWidth("0%");
        componentsToHide.forEach(c -> c.setVisible(true));
    }
}
