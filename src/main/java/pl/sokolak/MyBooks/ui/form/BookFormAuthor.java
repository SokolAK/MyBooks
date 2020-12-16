package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import pl.sokolak.MyBooks.model.author.AuthorDto;
import pl.sokolak.MyBooks.model.author.AuthorService;
import pl.sokolak.MyBooks.model.book.BookDto;

import java.util.ArrayList;
import java.util.List;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;

public class BookFormAuthor extends Form {

    private final BookDto bookDto;
    private final AuthorService authorService;
    private final Binder<AuthorDto> binder = new BeanValidationBinder<>(AuthorDto.class);
    private final TextField id = new TextField(header("id"));
    private final TextField prefix = new TextField(header("prefix"));
    private final TextField firstName = new TextField(header("firstName"));
    private final TextField middleName = new TextField(header("middleName"));
    private final TextField lastName = new TextField(header("lastName"));
    private final Grid<AuthorDto> gridAvailableAuthor = new Grid<>();
    private final Grid<AuthorDto> gridAddedAuthor = new Grid<>();
    private final List<AuthorDto> addedAuthors = new ArrayList<>();
    private Button btnAdd;
    private Button btnSave;
    private Button btnCancel;

    public BookFormAuthor(BookDto bookDto, AuthorService authorService) {
        this.bookDto = bookDto;
        this.authorService = authorService;

        addClassName("author-form");

        gridAvailableAuthor.setHeight("10vh");
        gridAddedAuthor.setHeight("10vh");
        addedAuthors.addAll(bookDto.getAuthors());

        configureButtons();
        configureAvailableAuthorsGrid();
        updateAvailableAuthorsList();
        configureAddedAuthorsGrid();
        updateAddedAuthorsList();

        lastName.setRequired(true);
        setUpdateListenerForTxtField(lastName);
        setUpdateListenerForTxtField(firstName);
        setUpdateListenerForTxtField(middleName);
        setUpdateListenerForTxtField(prefix);

        binder.forField(id)
                .withConverter(new StringToLongConverter("Must enter a Long"))
                .bind(AuthorDto::getId, AuthorDto::setId);
        binder.bindInstanceFields(this);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(lastName, firstName, middleName, prefix);
        verticalLayout.getChildren().map(c -> (TextField) c).forEach(HasSize::setWidthFull);
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        VerticalLayout verticalLayout2 = new VerticalLayout(gridAvailableAuthor, btnAdd, gridAddedAuthor, btnSave, btnCancel);
        verticalLayout2.setPadding(false);
        //verticalLayout.setSpacing(false);

        add(verticalLayout, verticalLayout2);
    }

    private void setUpdateListenerForTxtField(TextField txtField) {
        txtField.addValueChangeListener(e -> updateAvailableAuthorsList());
        txtField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
    }

    private void configureButtons() {
        btnAdd = new Button(header("add"), event -> addAuthor());
        btnAdd.setWidthFull();

        btnSave = new Button(header("save"), event -> save());
        btnSave.setWidthFull();
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnCancel= new Button(header("cancel"), event -> close());
        btnCancel.setWidthFull();
        //btnCancel.addThemeVariants(ButtonVariant.);
    }


    private void configureAvailableAuthorsGrid() {
        gridAvailableAuthor.addClassName("author-grid");
        gridAvailableAuthor.setWidthFull();
        addEntityColumns(gridAvailableAuthor);
        gridAvailableAuthor.addItemClickListener(e -> {
            binder.readBean(e.getItem());
        });
    }

    private void updateAvailableAuthorsList() {
        gridAvailableAuthor.setItems(authorService.findAll(
                prefix.getValue(),
                firstName.getValue(),
                middleName.getValue(),
                lastName.getValue()));
        gridAvailableAuthor.getColumns().forEach(c -> c.setAutoWidth(true));
    }

    private void configureAddedAuthorsGrid() {
        gridAddedAuthor.addClassName("author-grid");
        gridAddedAuthor.setWidthFull();

        gridAddedAuthor.addComponentColumn(author -> {
            Button edit = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            //edit.addClassName("edit");
            edit.addClickListener(e -> {
                addedAuthors.remove(author);
                updateAddedAuthorsList();
            });
            return edit;
        });

        addEntityColumns(gridAddedAuthor);
    }

    private void updateAddedAuthorsList() {
        gridAddedAuthor.setItems(addedAuthors);
        gridAddedAuthor.getColumns().forEach(c -> c.setAutoWidth(true));
    }

    private void addEntityColumns(Grid<AuthorDto> grid) {
        grid.addColumn(AuthorDto::getLastName);
        grid.addColumn(AuthorDto::getFirstName);
        grid.addColumn(AuthorDto::getMiddleName);
        grid.addColumn(AuthorDto::getPrefix);
    }

    private void addAuthor() {
        AuthorDto author = new AuthorDto();
        try {
            binder.writeBean(author);
            if (!addedAuthors.contains(author)) {
                addedAuthors.add(author);
                updateAddedAuthorsList();
                binder.readBean(null);
            }
        } catch (ValidationException e) {
            //e.printStackTrace();
        }
    }

    private void save() {
        bookDto.getAuthors().clear();
        bookDto.setAuthors(addedAuthors);
        fireEvent(new SaveEvent(this, bookDto));
        close();
    }

    private void close() {
        getParent().ifPresent(d -> ((Dialog) d).close());
    }
}
