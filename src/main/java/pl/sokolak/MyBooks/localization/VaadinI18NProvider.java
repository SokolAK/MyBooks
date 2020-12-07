package pl.sokolak.MyBooks.localization;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class VaadinI18NProvider implements I18NProvider {

    public static final String BUNDLE_PREFIX = "locale/messages";

    public final Locale LOCALE_EN = new Locale("en", "GB");
    public final Locale LOCALE_PL = new Locale("pl", "PL");

    private final List<Locale> locales = List.of(LOCALE_EN, LOCALE_PL);

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(VaadinI18NProvider.class.getName())
                    .warn("Got lang request for key with null value!");
            return "";
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(VaadinI18NProvider.class.getName())
                    .warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }

}
