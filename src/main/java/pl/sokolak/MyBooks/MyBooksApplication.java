package pl.sokolak.MyBooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import pl.sokolak.MyBooks.localization.VaadinI18NProvider;

import static java.lang.System.setProperty;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@PropertySource("classpath:application.yml")
public class MyBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBooksApplication.class, args);
        setProperty("vaadin.i18n.provider", VaadinI18NProvider.class.getName());
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}


