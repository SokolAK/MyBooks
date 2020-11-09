package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import pl.sokolak.MyBooks.model.Dto;

public abstract class Form extends FormLayout {

    @Getter
    protected final FormMode formMode;

    public Form() {
        this.formMode = FormMode.EDIT;
    }

    public Form(FormMode formMode) {
        this.formMode = formMode;
    }

    public static abstract class FormEvent extends ComponentEvent<Form> {
        private final Dto dto;

        protected FormEvent(Form source, Dto dto) {
            super(source, false);
            this.dto = dto;
        }

        public <T extends Dto> T getDto(Class<T> clazz) {
            return clazz.cast(dto);
        }
    }

    public static class DeleteEvent extends FormEvent {
        DeleteEvent(Form source, Dto dto) {
            super(source, dto);
        }
    }

    public static class SaveEvent extends FormEvent {
        SaveEvent(Form source, Dto dto) {
            super(source, dto);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public enum FormMode {ADD, EDIT}
}
