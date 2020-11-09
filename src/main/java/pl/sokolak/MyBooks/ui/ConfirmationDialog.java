package pl.sokolak.MyBooks.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmationDialog {

    public ConfirmationDialog() {

        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Label header = new Label(TextFormatter.header("sureDelete"));

        Button confirmButton = new Button(TextFormatter.header("yes"), event -> {
            dialog.close();
        });
        Button cancelButton = new Button(TextFormatter.header("no"), event -> {
            dialog.close();
        });

        dialog.add(new VerticalLayout(header,
                new HorizontalLayout(confirmButton, cancelButton)));
        dialog.open();
    }
}
