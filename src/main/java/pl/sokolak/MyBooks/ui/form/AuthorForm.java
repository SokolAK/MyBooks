package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import pl.sokolak.MyBooks.model.author.AuthorDto;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;

public class AuthorForm extends Form {

    private AuthorDto author;
    private final Binder<AuthorDto> binder = new BeanValidationBinder<>(AuthorDto.class);

    //TextField id = new TextField(header("id"));
    private final TextField prefix = new TextField(header("prefix"));
    private final TextField firstName = new TextField(header("firstName"));
    private final TextField middleName = new TextField(header("middleName"));
    private final TextField lastName = new TextField(header("lastName"));

    private final Button btnSave = new Button(header("save"));
    private final DeleteButton btnDelete = new DeleteButton();

    public AuthorForm(AuthorDto author, FormMode formMode) {
        this(formMode);
        this.author = author;
    }

    public AuthorForm(FormMode formMode) {
        super(formMode);
        addClassName("author-form");
/*
        binder.forField(id)
                .withConverter(new StringToLongConverter("Must enter a Long"))
                .bind(AuthorDto::getId, AuthorDto::setId);
        id.setEnabled(false);*/
        binder.bindInstanceFields(this);

        lastName.setRequired(true);

        addListener(AuthorForm.DeleteEvent.class, e -> getParent().ifPresent(d -> ((Dialog) d).close()));
        addListener(AuthorForm.SaveEvent.class, e -> getParent().ifPresent(d -> ((Dialog) d).close()));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(prefix, firstName, middleName, lastName);
        verticalLayout.getChildren().map(c -> (TextField) c).forEach(HasSize::setWidthFull);
        verticalLayout.setSpacing(false);
        verticalLayout.setPadding(false);

        VerticalLayout verticalLayout2 = new VerticalLayout(new Label(), createButtonsLayout());
        verticalLayout2.setPadding(false);


        add(verticalLayout, verticalLayout2);
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
        binder.readBean(author);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickShortcut(Key.ENTER);
        btnSave.addClickListener(event -> validateAndSave());
        horizontalLayout.addAndExpand(btnSave);

        if (formMode.equals(FormMode.EDIT)) {
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            btnDelete.addClickListener(event -> {
                if (btnDelete.isReady())
                    fireEvent(new DeleteEvent(this, author));
            });
            horizontalLayout.addAndExpand(btnDelete);
        }
        return horizontalLayout;
    }

    private void validateAndSave() {
        try {
            binder.writeBean(author);
            fireEvent(new SaveEvent(this, author));
        } catch (ValidationException e) {
            //e.printStackTrace();
        }
    }
}
