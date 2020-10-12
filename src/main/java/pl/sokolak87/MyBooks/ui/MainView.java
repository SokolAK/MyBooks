package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.sokolak87.MyBooks.book.BookDto;
import pl.sokolak87.MyBooks.book.BookService;

@Route("")
public class MainView extends VerticalLayout {

    private final BookService bookService;
    private final Grid<BookDto> grid = new Grid<>(BookDto.class);
    private TextField filterText = new TextField();

    public MainView(BookService bookService) {
        this.bookService = bookService;
        addClassName("list-view");
        setSizeFull();
        configureFilter();
        configureGrid();

        add(filterText, grid);
        updateList();
    }
    private void configureGrid() {
        grid.addClassName("book-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        grid.setItems(bookService.findAll(filterText.getValue()));
    }
}