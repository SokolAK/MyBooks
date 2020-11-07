package pl.sokolak87.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak87.MyBooks.model.publisher.PublisherDto;
import pl.sokolak87.MyBooks.model.publisher.PublisherService;
import pl.sokolak87.MyBooks.ui.ExpandingTextField;
import pl.sokolak87.MyBooks.ui.MainLayout;
import pl.sokolak87.MyBooks.ui.form.AuthorForm;
import pl.sokolak87.MyBooks.ui.form.DialogWindow;
import pl.sokolak87.MyBooks.ui.form.Form;
import pl.sokolak87.MyBooks.ui.form.PublisherForm;

import java.util.Set;
import java.util.stream.Stream;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;

@Route(value = "publishers", layout = MainLayout.class)
@PageTitle("Publishers | MyBooks")
public class PublishersListView extends VerticalLayout {

    private final PublisherService publisherService;
    private final Grid<PublisherDto> grid = new Grid<>(PublisherDto.class);
    private final ExpandingTextField txtFilter = new ExpandingTextField(header("filterByPhrase"));
    private HorizontalLayout toolbar;

    public PublishersListView(PublisherService publisherService) {
        this.publisherService = publisherService;
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
            PublisherForm publisherForm = new PublisherForm(e.getItem(), Form.FormMode.EDIT);
            publisherForm.setPublisher(e.getItem());
            publisherForm.addListener(PublisherForm.DeleteEvent.class, this::delete);
            publisherForm.addListener(PublisherForm.SaveEvent.class, this::save);
            new DialogWindow(publisherForm, header("editPublisher")).open();
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
        grid.setItems(publisherService.findAll(txtFilter.getValue()));
        updateGrid();
    }

    private void updateGrid() {
        grid.setColumns();
        addEntityColumns();
    }

    private void delete(AuthorForm.DeleteEvent e) {
        publisherService.delete(e.getDto(PublisherDto.class));
        updateList();
    }

    private void save(AuthorForm.SaveEvent e) {
        publisherService.save(e.getDto(PublisherDto.class));
        updateList();
    }

    private void add() {
        grid.asSingleSelect().clear();
        PublisherForm publisherForm = new PublisherForm(Form.FormMode.ADD);
        publisherForm.setPublisher(new PublisherDto());
        publisherForm.addListener(AuthorForm.SaveEvent.class, this::save);
        new DialogWindow(publisherForm, header("addPublisher")).open();
    }
}