package pl.sokolak87.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorService;
import pl.sokolak87.MyBooks.ui.MainLayout;
import pl.sokolak87.MyBooks.ui.form.AuthorForm;
import pl.sokolak87.MyBooks.ui.form.Form;
import pl.sokolak87.MyBooks.ui.form.DialogWindow;

import java.util.stream.Stream;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;


@Route(value = "authors", layout = MainLayout.class)
@PageTitle("Authors | MyBooks")
public class AuthorsListView extends VerticalLayout {

    private final AuthorService authorService;
    //private final AuthorForm authorForm = new AuthorForm();
    private final Grid<AuthorDto> grid = new Grid<>(AuthorDto.class);
    private final TextField txtFilter = new TextField();
    //private Button btnAddAuthor;
    //private FormDialog formDialog;

    public AuthorsListView(AuthorService authorService) {
        this.authorService = authorService;
        addClassName("author-view");
        setSizeFull();

        add(getToolbar(), grid);
        configureGrid();
        updateList();
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        configureTxtFilter();
        Button btnAddAuthor = new Button(new Icon(VaadinIcon.PLUS), click -> addAuthor());

        horizontalLayout.setWidthFull();
        horizontalLayout.expand(txtFilter);
        horizontalLayout.add(txtFilter, btnAddAuthor);

        return horizontalLayout;
    }

    private void configureTxtFilter() {
        txtFilter.setPlaceholder(header("filterByPhrase"));
        txtFilter.setClearButtonVisible(true);
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        //txtFilter.setWidth("50em");
    }

    private void configureGrid() {
        grid.addClassName("author-grid");
        grid.setSizeFull();

/*        GridContextMenu<AuthorDto> contextMenu = new GridContextMenu<>(grid);
        contextMenu.addGridContextMenuOpenedListener(e -> e.getItem().ifPresentOrElse(i -> {
            if(formDialog == null) {
                authorForm.setAuthor(i);
                grid.select(i);
                formDialog = new FormDialog(authorForm);
                formDialog.open();
            }
            else {
                formDialog.close();
                formDialog = null;
            }
        }, contextMenu::close));*/

        grid.addItemDoubleClickListener(e -> {
            grid.select(e.getItem());
            AuthorForm authorForm = new AuthorForm(e.getItem(), Form.FormMode.EDIT);
            authorForm.setAuthor(e.getItem());
            authorForm.addListener(AuthorForm.DeleteEvent.class, this::deleteAuthor);
            authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
            new DialogWindow(authorForm, header("editAuthor")).open();
        });

        updateGrid();
    }


/*    private void addEditColumn() {
        grid.addComponentColumn(author -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                AuthorForm authorForm = new AuthorForm(Form.FormMode.EDIT);
                authorForm.setAuthor(author);
                authorForm.addListener(AuthorForm.DeleteEvent.class, this::deleteAuthor);
                authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
                new FormDialog(authorForm, "").open();
            });
            return edit;
        });
    }*/



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

    private void addAuthor() {
        grid.asSingleSelect().clear();
        AuthorForm authorForm = new AuthorForm(Form.FormMode.ADD);
        authorForm.setAuthor(new AuthorDto());
        authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
        new DialogWindow(authorForm, header("addAuthor")).open();
    }
}