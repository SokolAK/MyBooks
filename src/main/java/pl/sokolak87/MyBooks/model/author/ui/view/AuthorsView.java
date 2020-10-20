package pl.sokolak87.MyBooks.model.author.ui.view;

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
import pl.sokolak87.MyBooks.model.author.ui.MainLayout;
import pl.sokolak87.MyBooks.model.author.ui.form.AuthorForm;
import pl.sokolak87.MyBooks.model.author.ui.form.FormDialog;

import java.util.stream.Stream;

import static pl.sokolak87.MyBooks.model.author.ui.TextFormatter.header;


@Route(value = "authors", layout = MainLayout.class)
@PageTitle("Authors | MyBooks")
public class AuthorsView extends VerticalLayout {

    private final AuthorService authorService;
    private final AuthorForm authorForm = new AuthorForm();
    private final Grid<AuthorDto> grid = new Grid<>(AuthorDto.class);
    private final TextField txtFilter = new TextField();
    //private Button btnAddAuthor;
    //private FormDialog formDialog;

    public AuthorsView(AuthorService authorService) {
        this.authorService = authorService;
        addClassName("author-view");
        setSizeFull();

        authorForm.addListener(AuthorForm.DeleteEvent.class, this::deleteAuthor);
        authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);

        configureGrid();

        add(getToolbar(), grid);
        updateList();
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        configureTxtFilter();
        Button btnAddAuthor = new Button(header("addAuthor"), click -> addAuthor());

        horizontalLayout.add(txtFilter, btnAddAuthor);
        return  horizontalLayout;
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
        grid.setColumns();

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

        addEntityColumns();

        grid.addItemDoubleClickListener(e -> {
            authorForm.setAuthor(e.getItem());
            new FormDialog(authorForm).open();
        });
    }

    private void addEditColumn() {
        grid.addComponentColumn(author -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                authorForm.setAuthor(author);
                new FormDialog(authorForm).open();
            });
            return edit;
        });
    }



    private void addEntityColumns() {
        Stream.of("lastName", "firstName", "middleName", "prefix")
                .map(grid::addColumn)
                .map(c -> c.setHeader(header(c.getKey())))
                .forEach(c -> c.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(authorService.findAll(txtFilter.getValue()));
    }

    private void deleteAuthor(AuthorForm.DeleteEvent e) {
        authorService.delete(e.getAuthor());
        updateList();
    }

    private void saveAuthor(AuthorForm.SaveEvent e) {
        authorService.save(e.getAuthor());
        updateList();
    }

    void addAuthor() {
        grid.asSingleSelect().clear();
        authorForm.setAuthor(new AuthorDto());
        new FormDialog(authorForm).open();
    }
}