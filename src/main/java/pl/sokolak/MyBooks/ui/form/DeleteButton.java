package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import lombok.Getter;
import pl.sokolak.MyBooks.ui.TextFormatter;

public class DeleteButton extends Button {

    private int counter = 3;
    private final String label = TextFormatter.header("delete");
    @Getter
    private boolean ready = false;

    public DeleteButton() {
        setLabel();
        addThemeVariants(ButtonVariant.LUMO_ERROR);
        addClickListener(e -> decreaseCounter());
    }

    private void decreaseCounter() {
        if(counter > 0) {
            counter--;
            setLabel();
        }
        else {
            ready = true;
        }
    }

    private void setLabel() {
        if(counter > 0)
            setText(label + " (" + counter + ")");
        else
            setText(label);
    }
}
