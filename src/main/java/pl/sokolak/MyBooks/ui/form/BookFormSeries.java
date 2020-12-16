package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import pl.sokolak.MyBooks.model.book.BookDto;
import pl.sokolak.MyBooks.model.series.SeriesDto;
import pl.sokolak.MyBooks.model.series.SeriesService;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;

public class BookFormSeries extends Form {

    private final BookDto bookDto;
    private final SeriesService seriesService;
    private final Binder<SeriesDto> binder = new BeanValidationBinder<>(SeriesDto.class);
    private final TextField id = new TextField(header("id"));
    private final TextField name = new TextField(header("name"));
    private final Grid<SeriesDto> gridAvailableSeries = new Grid<>();
    private Button btnSave;
    private Button btnCancel;

    public BookFormSeries(BookDto bookDto, SeriesService seriesService) {
        this.bookDto = bookDto;
        this.seriesService = seriesService;

        addClassName("series-form");

        gridAvailableSeries.setHeight("10vh");
        binder.readBean(bookDto.getSeries());

        configureButtons();
        configureAvailableSeriesGrid();
        updateAvailableSeriesList();

        //name.setRequired(true);
        setUpdateListenerForTxtField(name);

        binder.forField(id)
                .withConverter(new StringToLongConverter("Must enter a Long"))
                .bind(SeriesDto::getId, SeriesDto::setId);
        binder.bindInstanceFields(this);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(name);
        verticalLayout.getChildren().map(c -> (TextField) c).forEach(HasSize::setWidthFull);
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        VerticalLayout verticalLayout2 = new VerticalLayout(gridAvailableSeries, btnSave, btnCancel);
        verticalLayout2.setPadding(false);
        //verticalLayout.setSpacing(false);

        add(verticalLayout, verticalLayout2);
    }

    private void setUpdateListenerForTxtField(TextField txtField) {
        txtField.addValueChangeListener(e -> updateAvailableSeriesList());
        txtField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
    }

    private void configureButtons() {
        btnSave = new Button(header("save"), event -> save());
        btnSave.setWidthFull();
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnCancel= new Button(header("cancel"), event -> close());
        btnCancel.setWidthFull();
        //btnCancel.addThemeVariants(ButtonVariant.);
    }


    private void configureAvailableSeriesGrid() {
        gridAvailableSeries.addClassName("series-grid");
        gridAvailableSeries.setWidthFull();
        addEntityColumns(gridAvailableSeries);
        gridAvailableSeries.addItemClickListener(e -> {
            binder.readBean(e.getItem());
        });
    }

    private void updateAvailableSeriesList() {
        gridAvailableSeries.setItems(seriesService.findAll(name.getValue()));
        gridAvailableSeries.getColumns().forEach(c -> c.setAutoWidth(true));
    }

    private void addEntityColumns(Grid<SeriesDto> grid) {
        grid.addColumn(SeriesDto::getName);
    }


    private void save() {
        if(name.getValue() == null || name.getValue().isBlank()) {
            bookDto.setSeries(null);
        }
        else {
            SeriesDto seriesDto = new SeriesDto();
            try {
                binder.writeBean(seriesDto);
                bookDto.setSeries(seriesDto);
            } catch (ValidationException e) {
                //e.printStackTrace();
            }
        }
        fireEvent(new SaveEvent(this, bookDto));
        close();
    }

    private void close() {
        getParent().ifPresent(d -> ((Dialog) d).close());
    }
}
