package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak.MyBooks.model.series.SeriesDto;
import pl.sokolak.MyBooks.model.series.SeriesService;
import pl.sokolak.MyBooks.ui.ExpandingTextField;
import pl.sokolak.MyBooks.ui.MainLayout;
import pl.sokolak.MyBooks.ui.TextFormatter;
import pl.sokolak.MyBooks.ui.form.AuthorForm;
import pl.sokolak.MyBooks.ui.form.DialogWindow;
import pl.sokolak.MyBooks.ui.form.Form;
import pl.sokolak.MyBooks.ui.form.SeriesForm;

import java.util.Set;
import java.util.stream.Stream;

@Route(value = "series", layout = MainLayout.class)
@PageTitle("Series | MyBooks")
public class SeriesListView extends VerticalLayout {

    private final SeriesService seriesService;
    private final Grid<SeriesDto> grid = new Grid<>(SeriesDto.class);
    private final ExpandingTextField txtFilter = new ExpandingTextField(TextFormatter.header("filterByPhrase"));
    private HorizontalLayout toolbar;

    public SeriesListView(SeriesService seriesService) {
        this.seriesService = seriesService;
        addClassName("author-view");
        setSizeFull();

        configureGrid();
        configureToolbar();
        add(toolbar, grid);

        updateList();
    }

    private void configureToolbar() {
        Button btnAdd = new Button(new Icon(VaadinIcon.PLUS), click -> add());

        txtFilter.setComponentsToHide(Set.of(btnAdd));
        txtFilter.addValueChangeListener(e -> updateList());
        txtFilter.setMinWidth("0%");

        toolbar = new HorizontalLayout();
        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        toolbar.setWidthFull();
        toolbar.add(txtFilter, btnAdd);
    }

    private void configureGrid() {
        grid.addClassName("author-grid");
        grid.setSizeFull();
        grid.addItemDoubleClickListener(e -> {
            grid.select(e.getItem());
            SeriesForm seriesForm = new SeriesForm(e.getItem(), Form.FormMode.EDIT);
            seriesForm.setSeries(e.getItem());
            seriesForm.addListener(SeriesForm.DeleteEvent.class, this::delete);
            seriesForm.addListener(SeriesForm.SaveEvent.class, this::save);
            new DialogWindow(seriesForm, TextFormatter.header("editSeries")).open();
        });
        updateGrid();
    }

    private void addEntityColumns() {
        Stream.of("name")
                .map(grid::addColumn)
                .map(c -> c.setHeader(TextFormatter.header(c.getKey())))
                .forEach(c -> c.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(seriesService.findAll(txtFilter.getValue()));
        updateGrid();
    }

    private void updateGrid() {
        grid.setColumns();
        addEntityColumns();
    }

    private void delete(AuthorForm.DeleteEvent e) {
        seriesService.delete(e.getDto(SeriesDto.class));
        updateList();
    }

    private void save(AuthorForm.SaveEvent e) {
        seriesService.save(e.getDto(SeriesDto.class));
        updateList();
    }

    private void add() {
        grid.asSingleSelect().clear();
        SeriesForm seriesForm = new SeriesForm(Form.FormMode.ADD);
        seriesForm.setSeries(new SeriesDto());
        seriesForm.addListener(AuthorForm.SaveEvent.class, this::save);
        new DialogWindow(seriesForm, TextFormatter.header("addSeries")).open();
    }
}