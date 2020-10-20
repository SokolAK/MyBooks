package pl.sokolak87.MyBooks.model.author.ui.form;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import static pl.sokolak87.MyBooks.model.author.ui.TextFormatter.header;


public class BookForm extends Form {

    TextField title = new TextField(header("title"));
    TextField subtitle = new TextField(header("subtitle"));
    TextField year = new TextField(header("year"));
    TextField city = new TextField(header("city"));
    TextField edition = new TextField(header("edition"));
    TextField volume = new TextField(header("volume"));



    Button save = new Button(header("save"));
    Button delete = new Button(header("delete"));
    Button close = new Button(header("close"));


    public BookForm() {
        addClassName("contact-form");
        add(title,
                subtitle,
                year,
                city,
                edition,
                volume,
                createButtonsLayout());
    }



    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(save, delete, close);
    }
}
