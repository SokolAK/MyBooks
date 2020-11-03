package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;

public class ConfirmationDialog {

    public ConfirmationDialog() {

        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Label header = new Label(header("sureDelete"));

        Button confirmButton = new Button(header("yes"), event -> {
            dialog.close();
        });
        Button cancelButton = new Button(header("no"), event -> {
            dialog.close();
        });

        dialog.add(new VerticalLayout(header,
                new HorizontalLayout(confirmButton, cancelButton)));
        dialog.open();
    }
}
