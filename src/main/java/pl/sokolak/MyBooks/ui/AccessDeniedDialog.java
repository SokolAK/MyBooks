package pl.sokolak.MyBooks.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;

public class AccessDeniedDialog {

    public AccessDeniedDialog() {

        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        Label header = new Label(header("accessDenied"));

        Button closeButton = new Button(header("close"), event -> {
            dialog.close();
        });

        VerticalLayout verticalLayout = new VerticalLayout(header, closeButton);
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dialog.add(verticalLayout);
        dialog.open();
    }
}
