package pl.sokolak87.MyBooks.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

@PWA(
        name = "MyBooks",
        shortName = "MyBooks",
        offlineResources = {
                "./styles/offline.css",
                "./images/offline.png"
        },
        enableInstallPrompt = false
)
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
        ThemeUtil.setThemeVariant(Lumo.DARK);
    }

    private void createHeader() {
        H1 logo = new H1("MyBooks");
        logo.addClassName("logo");

        //Anchor logout = new Anchor("/logout", "Log out");
        Button logoutButton = new Button("Log out", new Icon(VaadinIcon.EXIT));
        logoutButton.addClickListener(click -> getUI().ifPresent(u -> u.getPage().setLocation("/logout")));
        Button toggleThemeVariantButton = new Button("Toggle theme", new Icon(VaadinIcon.MOON));
        toggleThemeVariantButton.addClickListener(click -> ThemeUtil.toggleThemeVariant());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, toggleThemeVariantButton, logoutButton);
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Books", BooksView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                new RouterLink("Authors", AuthorView.class)
        ));
        setDrawerOpened(true);
    }
}