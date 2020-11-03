package pl.sokolak87.MyBooks.ui.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorService;
import pl.sokolak87.MyBooks.model.book.BookDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;
import static pl.sokolak87.MyBooks.ui.TextFormatter.uppercase;

public class BookDetails extends Form {

    private BookDto bookDto;
    private final AuthorService authorService;
    private final Binder<BookDto> binder = new BeanValidationBinder<>(BookDto.class);
    private final TextField title = new TextField();
    private final TextField subtitle = new TextField();
    private final TextField year = new TextField();
    private final TextField city = new TextField();
    private final TextField edition = new TextField();
    private final TextField volume = new TextField();
    private final DeleteButton btnDelete = new DeleteButton();

    public BookDetails(BookDto bookDto, AuthorService authorService) {
        this.bookDto = BookDto.copy(bookDto);
        this.authorService = authorService;
        setClassName("book-details");

        binder.bindInstanceFields(this);
        addListener(BookDetails.SaveEvent.class, e -> getParent().ifPresent(d -> ((Dialog)d).close()));
        addListener(BookDetails.DeleteEvent.class, e -> getParent().ifPresent(d -> ((Dialog)d).close()));

        updateDetails();
    }

    private void updateDetails() {
        removeAll();
        VerticalLayout verticalLayout = new VerticalLayout();

        configureTxtField(title, bookDto.getTitle());
        verticalLayout.add(createSection("title", getEditButton(title), title));

        configureTxtField(subtitle, bookDto.getSubtitle());
        verticalLayout.add(createSection("subtitle", getEditButton(subtitle), subtitle));

        Component[] authorsSection = bookDto.getAuthors().stream()
                .map(AuthorDto::toString)
                .map(s -> new TextField(null, s, ""))
                .peek(t -> t.setReadOnly(true))
                .peek(HasSize::setWidthFull)
                .toArray(Component[]::new);

        verticalLayout.add(createSection("authors", new Button(new Icon(VaadinIcon.EDIT), click -> editAuthors()), authorsSection));

        configureTxtField(year, bookDto.getYear());
        verticalLayout.add(createSection("year", getEditButton(year), year));

        configureTxtField(city, bookDto.getCity());
        verticalLayout.add(createSection("city", getEditButton(city), city));

        configureTxtField(volume, bookDto.getVolume());
        verticalLayout.add(createSection("volume", getEditButton(volume), volume));

        configureTxtField(edition, bookDto.getEdition());
        verticalLayout.add(createSection("edition", getEditButton(edition), edition));

        add(verticalLayout, createButtonsLayout());
    }

    private void configureTxtField(TextField textField, String value) {
        textField.setValue(value);
        textField.setReadOnly(true);
        textField.setWidthFull();
        textField.addBlurListener(e -> e.getSource().setReadOnly(true));
        textField.addValueChangeListener(e -> writeBook());
    }

    private Button getEditButton(TextField textField) {
        return new Button(new Icon(VaadinIcon.EDIT), click -> {
            textField.setReadOnly(false);
            textField.focus();
        });
    }

    private VerticalLayout createSection(String title, Button btnEdit, Component... components) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);

        Label lblTitle = new Label(uppercase(title));
        lblTitle.setClassName("section-header");
        btnEdit.setClassName("edit-button");
        btnEdit.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY_INLINE);
        HorizontalLayout horizontalLayout = new HorizontalLayout(lblTitle, btnEdit);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        horizontalLayout.setPadding(false);
        horizontalLayout.setSpacing(false);
        verticalLayout.add(horizontalLayout);

        Arrays.stream(components).forEach(verticalLayout::add);
        return verticalLayout;
    }

    private void editAuthors() {
        BookFormAuthor bookFormAuthor = new BookFormAuthor(BookDto.copy(bookDto), authorService);
        bookFormAuthor.addListener(BookFormAuthor.SaveEvent.class, this::saveAuthors);
        new DialogWindow(bookFormAuthor, header("editAuthor")).open();
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button btnSave = new Button(header("save"));
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickShortcut(Key.ENTER);
        btnSave.addClickListener(e -> saveBook());
        buttonsLayout.addAndExpand(btnSave);

/*        Button btnDelete = new Button(header("delete"));
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> new ConfirmationDialog());*/

        btnDelete.addClickListener(e -> {
            if (((DeleteButton) e.getSource()).isReady())
                deleteBook();
        });
        buttonsLayout.addAndExpand(btnDelete);

        return buttonsLayout;
    }

    private boolean writeBook() {
        try {
            binder.writeBean(bookDto);
            return true;
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveBook() {
        if(writeBook())
            fireEvent(new SaveEvent(this, bookDto));
    }

    private void deleteBook() {
        fireEvent(new DeleteEvent(this, bookDto));
    }

    private void saveAuthors(BookFormAuthor.SaveEvent e) {
        List<AuthorDto> authors = e.getDto(BookDto.class).getAuthors();
        bookDto.setAuthors(new ArrayList<>(authors));
        updateDetails();
    }
}
