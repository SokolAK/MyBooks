package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.sokolak87.MyBooks.author.AuthorDto;
import pl.sokolak87.MyBooks.author.AuthorService;

@Component
@Scope("prototype")
@Route(value = "authors", layout = MainLayout.class)
@PageTitle("Authors | MyBooks")
public class AuthorView extends VerticalLayout {

    private final AuthorService authorService;
    private final Grid<AuthorDto> grid = new Grid<>(AuthorDto.class);
    private TextField filterText = new TextField();

    public AuthorView(AuthorService authorService) {
        this.authorService = authorService;
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
        grid.setColumns("id", "lastName", "firstName", "middleName", "prefix", "suffix");
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by phrase...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        grid.setItems(authorService.findAll(filterText.getValue()));
    }
}