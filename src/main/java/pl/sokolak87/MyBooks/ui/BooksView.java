package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.sokolak87.MyBooks.book.BookDto;
import pl.sokolak87.MyBooks.book.BookService;

@Component
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
@PageTitle("Books | MyBooks")
public class BooksView extends VerticalLayout {

    private final BookService bookService;
    private final Grid<BookDto> grid = new Grid<>(BookDto.class);
    private TextField filterTextField = new TextField();
    private Checkbox shortViewButton = new Checkbox();

    public BooksView(BookService bookService) {
        this.bookService = bookService;
        addClassName("list-view");
        setSizeFull();

        configureShortViewButton();
        configureFilterField();
        configureGrid();

        add(shortViewButton, filterTextField, grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("book-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        if(shortViewButton.getValue())
            grid.setColumns("id", "title", "authors", "year");
        else
            grid.setColumns("id", "title", "subtitle", "authors", "year");
        grid.addColumn("city");
    }

    private void configureShortViewButton() {
        shortViewButton.setLabel("Short View");
        shortViewButton.setValue(false);
        shortViewButton.addValueChangeListener(e -> configureGrid());
    }

    private void configureFilterField() {
        filterTextField.setPlaceholder("Filter by phrase...");
        filterTextField.setClearButtonVisible(true);
        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        filterTextField.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        grid.setItems(bookService.findAll(filterTextField.getValue()));
    }
}