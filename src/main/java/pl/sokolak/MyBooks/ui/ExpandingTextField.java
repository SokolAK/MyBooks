package pl.sokolak.MyBooks.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class ExpandingTextField extends TextField {
    @Getter @Setter
    private Set<Component> componentsToHide = new HashSet<>();

    public ExpandingTextField() {
        setClearButtonVisible(true);
        setValueChangeMode(ValueChangeMode.ON_CHANGE);
        addFocusListener(e -> expandTxtFilter());
        addBlurListener(e -> collapseTxtFilter());
        collapseTxtFilter();
    }

    public ExpandingTextField(String placeholder) {
        this();
        setPlaceholder(placeholder);
    }

    private void expandTxtFilter() {
        setMinWidth("100%");
        componentsToHide.forEach(c -> c.setVisible(false));
    }

    private void collapseTxtFilter() {
        setMinWidth("265px");
        setWidthFull();
        componentsToHide.forEach(c -> c.setVisible(true));
    }
}
