package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Pageable;
import pl.sokolak.MyBooks.model.Dto;
import pl.sokolak.MyBooks.model.author.AuthorDto;
import pl.sokolak.MyBooks.model.author.AuthorService;
import pl.sokolak.MyBooks.security.Secured;
import pl.sokolak.MyBooks.ui.ExpandingTextField;
import pl.sokolak.MyBooks.ui.MainLayout;
import pl.sokolak.MyBooks.ui.form.AuthorForm;
import pl.sokolak.MyBooks.ui.form.DialogWindow;
import pl.sokolak.MyBooks.ui.form.Form;

import java.util.List;
import java.util.stream.Stream;

import static pl.sokolak.MyBooks.security.OperationType.ADD;
import static pl.sokolak.MyBooks.security.OperationType.EDIT;
import static pl.sokolak.MyBooks.utils.TextFormatter.header;


@Route(value = "authors", layout = MainLayout.class)
@PageTitle("Authors | MyBooks")
public class AuthorsListView extends VerticalLayout {

    private final AuthorService authorService;
    private final Grid<AuthorDto> grid = new Grid<>(AuthorDto.class);
    private final ExpandingTextField txtFilter = new ExpandingTextField();
    private VerticalLayout toolbar;
    private Button btnAddAuthor;
    private final ComboBox<Integer> comboBox = new ComboBox<>();
    private Button btnLeft = new Button();
    private Button btnRight = new Button();
    private final TextField txtPage = new TextField();
    private final ListViewUI listViewUI = new ListViewUI();
    private NavigationController nc;

    public AuthorsListView(AuthorService authorService) {
        this.authorService = authorService;
        addClassName("author-view");
        setSizeFull();

        configureGrid();

        configureUI();
    }

    private void configureUI() {
        nc = new NavigationController(this::getAuthors, authorService::count, this::updateList, txtPage);
        nc.configureTxtPage(txtPage);
        listViewUI.configure(this::onInit, this::onReload);
    }

    private void onInit() {
        configureToolbar();
        nc.update();
        add(toolbar, grid);
    }

    private void onReload() {
        reloadToolbar();
    }

    private List<AuthorDto> getAuthors(Pageable pageable) {
        return authorService.findAll(txtFilter.getValue(), pageable);
    }

    private void updateList(List<? extends Dto> authors) {
        grid.setItems((List<AuthorDto>) authors);
        updateGrid();
    }


    private void configureToolbar() {
        toolbar = new VerticalLayout();
        btnAddAuthor = new Button(new Icon(VaadinIcon.PLUS), click -> addAuthor());
        configureTxtFilter();
        //configureBtnPages();
        nc.configureBtnPages(btnLeft, btnRight);
        //configureCmbPage();
        nc.configureCmbPage(comboBox);
        reloadToolbar();
    }

    private void reloadToolbar() {
        ToolbarUtils.reloadToolbar(toolbar, listViewUI, btnAddAuthor, btnLeft, btnRight, txtFilter, txtPage, comboBox);
    }

    private void configureTxtFilter() {
        txtFilter.setPlaceholder(header("filterByPhrase"));
        txtFilter.addValueChangeListener(e -> {
            nc.getPage().no = 0;
            nc.update();
        });
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

    private void updateGrid() {
        grid.setColumns();
        addEntityColumns();
    }

    private void deleteAuthor(AuthorForm.DeleteEvent e) {
        authorService.delete(e.getDto(AuthorDto.class));
        nc.update();
    }

    private void saveAuthor(AuthorForm.SaveEvent e) {
        authorService.save(e.getDto(AuthorDto.class));
        nc.update();
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