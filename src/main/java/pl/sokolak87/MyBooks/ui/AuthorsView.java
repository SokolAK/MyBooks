package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak87.MyBooks.author.AuthorDto;
import pl.sokolak87.MyBooks.author.AuthorService;

import java.util.stream.Stream;

import static pl.sokolak87.MyBooks.utils.StringUtil.lowerAndCapitalizeFirst;

@Route(value = "authors", layout = MainLayout.class)
@PageTitle("Authors | MyBooks")
public class AuthorsView extends VerticalLayout {

    private final AuthorService authorService;
    private final Grid<AuthorDto> grid = new Grid<>(AuthorDto.class);
    private final TextField txtFilter = new TextField();

    public AuthorsView(AuthorService authorService) {
        this.authorService = authorService;
        addClassName("author-view");
        setSizeFull();

        configureTxtField();
        configureGrid();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        horizontalLayout.add(txtFilter);

        add(horizontalLayout, grid);
        updateList();
    }

    private void configureTxtField() {
        txtFilter.setPlaceholder(getTranslation("filterByPhrase"));
        txtFilter.setClearButtonVisible(true);
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.setWidth("50em");
    }


    private void configureGrid() {
        grid.addClassName("author-grid");
        grid.setSizeFull();
        grid.setColumns();

        Stream.of("lastName", "firstName", "middleName", "prefix")
                .map(grid::addColumn)
                .map(c -> c.setHeader(lowerAndCapitalizeFirst(getTranslation(c.getKey()))))
                .forEach(c -> c.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(authorService.findAll(txtFilter.getValue()));
    }

}