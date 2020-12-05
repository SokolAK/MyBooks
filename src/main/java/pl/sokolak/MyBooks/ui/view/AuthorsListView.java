package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak.MyBooks.model.author.AuthorDto;
import pl.sokolak.MyBooks.model.author.AuthorService;
import pl.sokolak.MyBooks.security.Secured;
import pl.sokolak.MyBooks.ui.ExpandingTextField;
import pl.sokolak.MyBooks.ui.MainLayout;
import pl.sokolak.MyBooks.ui.form.AuthorForm;
import pl.sokolak.MyBooks.ui.form.DialogWindow;
import pl.sokolak.MyBooks.ui.form.Form;

import java.util.Set;
import java.util.stream.Stream;

import static pl.sokolak.MyBooks.security.OperationType.ADD;
import static pl.sokolak.MyBooks.security.OperationType.EDIT;
import static pl.sokolak.MyBooks.utils.TextFormatter.header;


@Route(value = "authors", layout = MainLayout.class)
@PageTitle("Authors | MyBooks")
public class AuthorsListView extends VerticalLayout {

    private final AuthorService authorService;
    private final Grid<AuthorDto> grid = new Grid<>(AuthorDto.class);
    private final ExpandingTextField txtFilter = new ExpandingTextField(header("filterByPhrase"));
    private HorizontalLayout toolbar;

    public AuthorsListView(AuthorService authorService) {
        this.authorService = authorService;
        addClassName("author-view");
        setSizeFull();

        configureGrid();
        configureToolbar();
        add(toolbar, grid);

        updateList();
    }

    private void configureToolbar() {
        Button btnAddAuthor = new Button(new Icon(VaadinIcon.PLUS), click -> addAuthor());

        txtFilter.setComponentsToHide(Set.of(btnAddAuthor));
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.setMinWidth("0%");

        toolbar = new HorizontalLayout();
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.setWidthFull();
        toolbar.add(txtFilter, btnAddAuthor);
    }

    private void configureGrid() {
        grid.addClassName("author-grid");
        grid.setSizeFull();
        grid.addItemDoubleClickListener(e -> {
            grid.select(e.getItem());
            editAuthor(e.getItem());
        });
        updateGrid();
    }

    private void addEntityColumns() {
        Stream.of("lastName", "firstName", "middleName", "prefix")
                .map(grid::addColumn)
                .map(c -> c.setHeader(header(c.getKey())))
                .forEach(c -> c.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(authorService.findAll(txtFilter.getValue()));
        updateGrid();
    }

    private void updateGrid() {
        grid.setColumns();
        addEntityColumns();
    }

    private void deleteAuthor(AuthorForm.DeleteEvent e) {
        authorService.delete(e.getDto(AuthorDto.class));
        updateList();
    }

    private void saveAuthor(AuthorForm.SaveEvent e) {
        authorService.save(e.getDto(AuthorDto.class));
        updateList();
    }

    @Secured(EDIT)
    private void editAuthor(AuthorDto authorDto) {
        AuthorForm authorForm = new AuthorForm(authorDto, Form.FormMode.EDIT);
        authorForm.setAuthor(authorDto);
        authorForm.addListener(AuthorForm.DeleteEvent.class, this::deleteAuthor);
        authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
        new DialogWindow(authorForm, header("editAuthor")).open();
    }

    @Secured(ADD)
    private void addAuthor() {
        grid.asSingleSelect().clear();
        AuthorForm authorForm = new AuthorForm(Form.FormMode.ADD);
        authorForm.setAuthor(new AuthorDto());
        authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
        new DialogWindow(authorForm, header("addAuthor")).open();
    }
}