package pl.sokolak87.MyBooks.ui.form;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import pl.sokolak87.MyBooks.model.book.BookDto;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;


public class BookForm extends Form {

    private BookDto book;
    private final Binder<BookDto> binder = new BeanValidationBinder<>(BookDto.class);

    //TextField id = new TextField(header("id"));
    private final TextField prefix = new TextField(header("prefix"));
    private final TextField firstName = new TextField(header("firstName"));
    private final TextField middleName = new TextField(header("middleName"));
    private final TextField lastName = new TextField(header("lastName"));

    private final Button save = new Button(header("save"));
    private final Button delete = new Button(header("delete"));

    public BookForm() {
        addClassName("book-form");
/*
        binder.forField(id)
                .withConverter(new StringToLongConverter("Must enter a Long"))
                .bind(BookDto::getId, BookDto::setId);
        id.setEnabled(false);*/

        lastName.setRequired(true);

        binder.bindInstanceFields(this);
        add(prefix, firstName, middleName, lastName,
                createButtonsLayout());
    }

    public void setBook(BookDto book) {
        this.book = book;
        binder.readBean(book);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, book)));

        return new HorizontalLayout(save, delete);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(book);
            fireEvent(new SaveEvent(this, book));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
