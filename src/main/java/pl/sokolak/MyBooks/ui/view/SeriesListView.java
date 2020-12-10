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
import pl.sokolak.MyBooks.model.series.SeriesDto;
import pl.sokolak.MyBooks.model.series.SeriesService;
import pl.sokolak.MyBooks.security.Secured;
import pl.sokolak.MyBooks.ui.ExpandingTextField;
import pl.sokolak.MyBooks.ui.MainLayout;
import pl.sokolak.MyBooks.ui.form.AuthorForm;
import pl.sokolak.MyBooks.ui.form.DialogWindow;
import pl.sokolak.MyBooks.ui.form.Form;
import pl.sokolak.MyBooks.ui.form.SeriesForm;

import java.util.List;
import java.util.stream.Stream;

import static pl.sokolak.MyBooks.security.OperationType.ADD;
import static pl.sokolak.MyBooks.security.OperationType.EDIT;
import static pl.sokolak.MyBooks.utils.TextFormatter.header;

@Route(value = "series", layout = MainLayout.class)
@PageTitle("Series | MyBooks")
public class SeriesListView extends VerticalLayout {

    private final SeriesService seriesService;
    private final Grid<SeriesDto> grid = new Grid<>(SeriesDto.class);
    private final ExpandingTextField txtFilter = new ExpandingTextField();
    private VerticalLayout toolbar;
    private Button btnAddPublisher;
    private final ComboBox<Integer> comboBox = new ComboBox<>();
    private Button btnLeft = new Button();
    private Button btnRight = new Button();
    private final TextField txtPage = new TextField();
    private final ListViewUI listViewUI = new ListViewUI();
    private NavigationController nc;

    public SeriesListView(SeriesService seriesService) {
        this.seriesService = seriesService;
        addClassName("author-view");
        setSizeFull();

        configureGrid();

        configureUI();
    }

    private void configureUI() {
        nc = new NavigationController(this::getSeries, seriesService::count, this::updateList, txtPage);
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

    private List<SeriesDto> getSeries(Pageable pageable) {
        return seriesService.findAll(txtFilter.getValue(), pageable);
    }

    private void updateList(List<? extends Dto> series) {
        grid.setItems((List<SeriesDto>) series);
        updateGrid();
    }

    private void configureToolbar() {
        toolbar = new VerticalLayout();
        btnAddPublisher = new Button(new Icon(VaadinIcon.PLUS), click -> addSeries());
        configureTxtFilter();
        //configureBtnPages();
        nc.configureBtnPages(btnLeft, btnRight);
        //configureCmbPage();
        nc.configureCmbPage(comboBox);
        reloadToolbar();
    }

    private void reloadToolbar() {
        ToolbarUtils.reloadToolbar(toolbar, listViewUI, btnAddPublisher, btnLeft, btnRight, txtFilter, txtPage, comboBox);
    }

    private void configureTxtFilter() {
        txtFilter.setPlaceholder(header("filterByPhrase"));
        txtFilter.addValueChangeListener(e -> {
            nc.getPage().no = 0;
            nc.update();
        });
    }

    private void configureGrid() {
        grid.addClassName("series-grid");
        grid.setSizeFull();
        grid.addItemDoubleClickListener(e -> {
            grid.select(e.getItem());
            editSeries(e.getItem());
        });
        updateGrid();
    }

    private void addEntityColumns() {
        Stream.of("name")
                .map(grid::addColumn)
                .map(c -> c.setHeader(header(c.getKey())))
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

    @Secured(EDIT)
    private void editSeries(SeriesDto seriesDto) {
        SeriesForm seriesForm = new SeriesForm(seriesDto, Form.FormMode.EDIT);
        seriesForm.setSeries(seriesDto);
        seriesForm.addListener(SeriesForm.DeleteEvent.class, this::delete);
        seriesForm.addListener(SeriesForm.SaveEvent.class, this::save);
        new DialogWindow(seriesForm, header("editSeries")).open();
    }

    @Secured(ADD)
    private void addSeries() {
        grid.asSingleSelect().clear();
        SeriesForm seriesForm = new SeriesForm(Form.FormMode.ADD);
        seriesForm.setSeries(new SeriesDto());
        seriesForm.addListener(AuthorForm.SaveEvent.class, this::save);
        new DialogWindow(seriesForm, header("addSeries")).open();
    }
}