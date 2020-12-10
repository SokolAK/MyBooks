package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import pl.sokolak.MyBooks.ui.ThemeUtil;

import static pl.sokolak.MyBooks.security.SecurityConfiguration.getActiveProfiles;
import static pl.sokolak.MyBooks.utils.TextFormatter.header;
import static pl.sokolak.MyBooks.utils.TextFormatter.label;

@Route("login")
@PageTitle("Login | MyBooks")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();

    public LoginView(){
        addClassName("login-view");
        login.setI18n(createI18n());
        login.setForgotPasswordButtonVisible(false);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");

        Image logo = new Image("icons/icon.png", "");
        logo.setMaxHeight("88px");

        H1 title = new H1(label("appName"));

        add(logo, title, login);

        ThemeUtil.setThemeVariant(Lumo.DARK);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

    private LoginI18n createI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        //i18n.getHeader().setTitle(header("loging"));
        i18n.getHeader().setDescription(header("LoginView.login"));
        i18n.getForm().setUsername(header("LoginView.username"));
        i18n.getForm().setTitle(header("LoginView.loging"));
        i18n.getForm().setSubmit(header("LoginView.login"));
        i18n.getForm().setPassword(header("LoginView.password"));
        i18n.getForm().setForgotPassword("");
        i18n.getErrorMessage().setTitle(header("LoginView.error.title"));
        i18n.getErrorMessage().setMessage(header("LoginView.error.message"));

        if(getActiveProfiles().contains("heroku-demo"))
            i18n.setAdditionalInformation(header("LoginView.info.demo"));
        else
            i18n.setAdditionalInformation(header("LoginView.info"));
        return i18n;
    }
}

