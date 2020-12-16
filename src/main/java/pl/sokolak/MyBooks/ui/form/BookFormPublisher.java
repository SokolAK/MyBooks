package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import pl.sokolak.MyBooks.model.book.BookDto;
import pl.sokolak.MyBooks.model.publisher.PublisherDto;
import pl.sokolak.MyBooks.model.publisher.PublisherService;

import java.util.ArrayList;
import java.util.List;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;

public class BookFormPublisher extends Form {

    private final BookDto bookDto;
    private final PublisherService publisherService;
    private final Binder<PublisherDto> binder = new BeanValidationBinder<>(PublisherDto.class);
    private final TextField id = new TextField(header("id"));
    private final TextField name = new TextField(header("name"));
    private final Grid<PublisherDto> gridAvailablePublisher = new Grid<>();
    private final Grid<PublisherDto> gridAddedPublisher = new Grid<>();
    private final List<PublisherDto> addedPublishers = new ArrayList<>();
    private Button btnAdd;
    private Button btnSave;
    private Button btnCancel;

    public BookFormPublisher(BookDto bookDto, PublisherService publisherService) {
        this.bookDto = bookDto;
        this.publisherService = publisherService;

        addClassName("publisher-form");

        gridAvailablePublisher.setHeight("10vh");
        gridAddedPublisher.setHeight("10vh");
        addedPublishers.addAll(bookDto.getPublishers());

        configureButtons();
        configureAvailablePublishersGrid();
        updateAvailablePublishersList();
        configureAddedPublishersGrid();
        updateAddedPublishersList();

        name.setRequired(true);
        setUpdateListenerForTxtField(name);

        binder.forField(id)
                .withConverter(new StringToLongConverter("Must enter a Long"))
                .bind(PublisherDto::getId, PublisherDto::setId);
        binder.bindInstanceFields(this);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(name);
        verticalLayout.getChildren().map(c -> (TextField) c).forEach(HasSize::setWidthFull);
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        VerticalLayout verticalLayout2 = new VerticalLayout(gridAvailablePublisher, btnAdd, gridAddedPublisher, btnSave, btnCancel);
        verticalLayout2.setPadding(false);
        //verticalLayout.setSpacing(false);

        add(verticalLayout, verticalLayout2);
    }

    private void setUpdateListenerForTxtField(TextField txtField) {
        txtField.addValueChangeListener(e -> updateAvailablePublishersList());
        txtField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
    }

    private void configureButtons() {
        btnAdd = new Button(header("add"), event -> addPublisher());
        btnAdd.setWidthFull();

        btnSave = new Button(header("save"), event -> save());
        btnSave.setWidthFull();
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnCancel= new Button(header("cancel"), event -> close());
        btnCancel.setWidthFull();
        //btnCancel.addThemeVariants(ButtonVariant.);
    }


    private void configureAvailablePublishersGrid() {
        gridAvailablePublisher.addClassName("publisher-grid");
        gridAvailablePublisher.setWidthFull();
        addEntityColumns(gridAvailablePublisher);
        gridAvailablePublisher.addItemClickListener(e -> {
            binder.readBean(e.getItem());
        });
    }

    private void updateAvailablePublishersList() {
        gridAvailablePublisher.setItems(publisherService.findAll(name.getValue()));
        gridAvailablePublisher.getColumns().forEach(c -> c.setAutoWidth(true));
    }

    private void configureAddedPublishersGrid() {
        gridAddedPublisher.addClassName("publisher-grid");
        gridAddedPublisher.setWidthFull();

        gridAddedPublisher.addComponentColumn(publisher -> {
            Button edit = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            //edit.addClassName("edit");
            edit.addClickListener(e -> {
                addedPublishers.remove(publisher);
                updateAddedPublishersList();
            });
            return edit;
        });

        addEntityColumns(gridAddedPublisher);
    }

    private void updateAddedPublishersList() {
        gridAddedPublisher.setItems(addedPublishers);
        gridAddedPublisher.getColumns().forEach(c -> c.setAutoWidth(true));
    }

    private void addEntityColumns(Grid<PublisherDto> grid) {
        grid.addColumn(PublisherDto::getName);
    }

    private void addPublisher() {
        PublisherDto publisher = new PublisherDto();
        try {
            binder.writeBean(publisher);
            if (!addedPublishers.contains(publisher)) {
                addedPublishers.add(publisher);
                updateAddedPublishersList();
                binder.readBean(null);
            }
        } catch (ValidationException e) {
            //e.printStackTrace();
        }

    }

    private void save() {
        bookDto.getPublishers().clear();
        bookDto.setPublishers(addedPublishers);
        fireEvent(new SaveEvent(this, bookDto));
        close();
    }

    private void close() {
        getParent().ifPresent(d -> ((Dialog) d).close());
    }
}
