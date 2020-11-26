package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import pl.sokolak.MyBooks.model.publisher.PublisherDto;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;

public class PublisherForm extends Form {

    private PublisherDto publisher;
    private final Binder<PublisherDto> binder = new BeanValidationBinder<>(PublisherDto.class);

    private final TextField name = new TextField(header("name"));
    private final Button btnSave = new Button(header("save"));
    private final DeleteButton btnDelete = new DeleteButton();

    public PublisherForm(PublisherDto publisher, FormMode formMode) {
        this(formMode);
        this.publisher = publisher;
    }

    public PublisherForm(FormMode formMode) {
        super(formMode);
        addClassName("publisher-form");
        binder.bindInstanceFields(this);
        name.setRequired(true);

        addListener(DeleteEvent.class, e -> getParent().ifPresent(d -> ((Dialog) d).close()));
        addListener(SaveEvent.class, e -> getParent().ifPresent(d -> ((Dialog) d).close()));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(name);
        verticalLayout.getChildren().map(c -> (TextField) c).forEach(HasSize::setWidthFull);
        verticalLayout.setSpacing(false);
        verticalLayout.setPadding(false);

        VerticalLayout verticalLayout2 = new VerticalLayout(new Label(), createButtonsLayout());
        verticalLayout2.setPadding(false);


        add(verticalLayout, verticalLayout2);
    }

    public void setPublisher(PublisherDto publisher) {
        this.publisher = publisher;
        binder.readBean(publisher);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickShortcut(Key.ENTER);
        btnSave.addClickListener(event -> validateAndSave());
        horizontalLayout.addAndExpand(btnSave);

        if (formMode.equals(FormMode.EDIT)) {
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            btnDelete.addClickListener(event -> {
                if (btnDelete.isReady())
                    fireEvent(new DeleteEvent(this, publisher));
            });
            horizontalLayout.addAndExpand(btnDelete);
        }
        return horizontalLayout;
    }

    private void validateAndSave() {
        try {
            binder.writeBean(publisher);
            fireEvent(new SaveEvent(this, publisher));
        } catch (ValidationException e) {
            //e.printStackTrace();
        }
    }
}
