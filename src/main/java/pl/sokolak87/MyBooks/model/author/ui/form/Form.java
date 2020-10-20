package pl.sokolak87.MyBooks.model.author.ui.form;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.shared.Registration;
import pl.sokolak87.MyBooks.model.author.AuthorDto;

public class Form extends FormLayout {

    public static abstract class FormEvent extends ComponentEvent<AuthorForm> {
        private final AuthorDto author;

        protected FormEvent(AuthorForm source, AuthorDto author) {
            super(source, false);
            this.author = author;
        }

        public AuthorDto getAuthor() {
            return author;
        }
    }

    public static class DeleteEvent extends FormEvent {
        DeleteEvent(AuthorForm source, AuthorDto author) {
            super(source, author);
        }
    }

    public static class SaveEvent extends FormEvent {
        SaveEvent(AuthorForm source, AuthorDto author) {
            super(source, author);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
