package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak87.MyBooks.book.BookDto;
import pl.sokolak87.MyBooks.book.BookService;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.sokolak87.MyBooks.author.AuthorDto.authorsListToString;
import static pl.sokolak87.MyBooks.utils.StringUtil.lowerAndCapitalizeFirst;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Books | MyBooks")
public class BooksView extends VerticalLayout {

    private final BookService bookService;
    private final Grid<BookDto> grid = new Grid<>(BookDto.class);
    private final TextField txtFilter = new TextField();
    private final MenuBar mnuMenuBar = new MenuBar();
    private final Checkbox chkShortNotation = new Checkbox();
    private final Map<String, Boolean> columnList = new LinkedHashMap<>();

    public BooksView(BookService bookService) {
        this.bookService = bookService;
        addClassName("book-view");
        setSizeFull();

        initColumnList();
        configureTxtField();
        configureChkShortNotation();
        configureMnuMenuBar();
        configureGrid();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        horizontalLayout.add(txtFilter, mnuMenuBar, chkShortNotation);

        add(horizontalLayout, grid);
        updateList();
    }

    private void configureChkShortNotation() {
        chkShortNotation.setLabel(getTranslation("shortNotation"));
        chkShortNotation.setValue(false);
        chkShortNotation.addClickListener(e -> this.configureGrid());
    }

    private void configureGrid() {
        grid.addClassName("book-grid");
        grid.setSizeFull();
        grid.setColumns();

        columnList.entrySet().stream()
                .filter(Map.Entry::getValue)
                .forEach(e -> addGridColumn(e.getKey()));

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void addGridColumn(String key) {
        Grid.Column<BookDto> column;
        if (key.equals("authors")) {
            ValueProvider<BookDto, String> valueProvider = b -> authorsListToString(b.getAuthors(), chkShortNotation.getValue());
            column = grid.addColumn(valueProvider).setComparator(valueProvider);
        } else {
            column = grid.addColumn(key);
        }
        column.setHeader(lowerAndCapitalizeFirst(getTranslation(key)));
    }

    private void configureTxtField() {
        txtFilter.setPlaceholder(getTranslation("filterByPhrase"));
        txtFilter.setClearButtonVisible(true);
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.setWidth("50em");
    }

    private void updateList() {
        grid.setItems(bookService.findAll(txtFilter.getValue(), columnList));
    }

    private void initColumnList() {
        columnList.put("id", false);
        columnList.put("title", true);
        columnList.put("subtitle", false);
        columnList.put("authors", true);
        columnList.put("year", true);
        columnList.put("city", false);
        columnList.put("edition", true);
        columnList.put("volume", true);
    }

    private void configureMnuMenuBar() {
        MenuItem columns = mnuMenuBar.addItem(getTranslation("columns"));
        columnList.forEach((columnName, isChecked) -> {
                    MenuItem item = columns
                            .getSubMenu()
                            .addItem(lowerAndCapitalizeFirst(getTranslation(columnName)));
                    item.setCheckable(true);
                    item.setChecked(isChecked);
                    item.addClickListener(e -> {
                                boolean currentValue = columnList.get(columnName);
                                columnList.put(columnName, !currentValue);
                                configureGrid();
                                updateList();
                            }
                    );
                }
        );
    }
}