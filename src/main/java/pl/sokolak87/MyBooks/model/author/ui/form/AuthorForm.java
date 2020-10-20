package pl.sokolak87.MyBooks.model.author.ui.form;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import pl.sokolak87.MyBooks.model.author.AuthorDto;

import static pl.sokolak87.MyBooks.model.author.ui.TextFormatter.header;


public class AuthorForm extends Form {

    private AuthorDto author;
    Binder<AuthorDto> binder = new BeanValidationBinder<>(AuthorDto.class);

    //TextField id = new TextField(header("id"));
    TextField prefix = new TextField(header("prefix"));
    TextField firstName = new TextField(header("firstName"));
    TextField middleName = new TextField(header("middleName"));
    TextField lastName = new TextField(header("lastName"));

    Button save = new Button(header("save"));
    Button delete = new Button(header("delete"));

    public AuthorForm() {
        addClassName("author-form");
/*
        binder.forField(id)
                .withConverter(new StringToLongConverter("Must enter a Long"))
                .bind(AuthorDto::getId, AuthorDto::setId);
        id.setEnabled(false);*/

        binder.bindInstanceFields(this);
        add(prefix, firstName, middleName, lastName,
                createButtonsLayout());
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
        System.out.println("lastName: " + author.getLastName());
        binder.readBean(author);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, author)));

        return new HorizontalLayout(save, delete);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(author);
            fireEvent(new SaveEvent(this, author));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
