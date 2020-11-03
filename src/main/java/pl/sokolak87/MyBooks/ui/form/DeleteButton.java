package pl.sokolak87.MyBooks.ui.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import lombok.Getter;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;

public class DeleteButton extends Button {

    private int counter = 3;
    private final String label = header("delete");
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
