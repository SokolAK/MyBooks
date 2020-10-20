package pl.sokolak87.MyBooks.model.author.ui.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class FormDialog {

    private final Form form;
    private Dialog dialog;

    public FormDialog(Form form) {
        this.form = form;
    }

    public void open() {
        dialog = new Dialog();
        //FormLayout layout = new FormLayout();
        //layout.setSizeFull();
        addCloseButton();

        form.addListener(AuthorForm.DeleteEvent.class, e -> dialog.close());
        form.addListener(AuthorForm.SaveEvent.class, e -> dialog.close());
        dialog.add(form);

        //dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        dialog.open();
    }

    private void addCloseButton() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topBar.add(new Button(new Icon(VaadinIcon.CLOSE), e -> dialog.close()));
        dialog.add(topBar);
    }
}
