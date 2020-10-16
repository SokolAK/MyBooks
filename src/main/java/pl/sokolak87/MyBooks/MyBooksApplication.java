package pl.sokolak87.MyBooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import pl.sokolak87.MyBooks.localization.VaadinI18NProvider;

import static java.lang.System.setProperty;

@SpringBootApplication
@PropertySource("classpath:application.yml")
public class MyBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBooksApplication.class, args);
		setProperty("vaadin.i18n.provider", VaadinI18NProvider.class.getName());
	}
}
