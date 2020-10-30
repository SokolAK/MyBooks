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
import pl.sokolak87.MyBooks.ui.view.AuthorsListView;
import pl.sokolak87.MyBooks.ui.view.BooksListView;
import pl.sokolak87.MyBooks.ui.view.PublishersListView;

import java.util.ArrayList;
import java.util.List;

import static pl.sokolak87.MyBooks.ui.TextFormatter.header;


@PWA(
        name = "MyBooks",
        shortName = "MyBooks",
        offlinePath = "offline-page.html",
        offlineResources = {
                "./styles/offline.css",
                "./images/offline.png"
        }
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

        Button logoutButton = new Button(new Icon(VaadinIcon.SIGN_OUT));
        logoutButton.addClickListener(click -> getUI().ifPresent(u -> u.getPage().setLocation("/logout")));
        Button toggleThemeVariantButton = new Button(new Icon(VaadinIcon.MOON));
        toggleThemeVariantButton.addClickListener(click -> ThemeUtil.toggleThemeVariant());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, toggleThemeVariantButton, logoutButton);
        header.addClassName("header");
        header.setWidthFull();
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        addToNavbar(header);
    }

    private void createDrawer() {
        List<RouterLink> routerLinks = new ArrayList<>();

        routerLinks.add(new RouterLink(header("MainLayout.books"), BooksListView.class));
        routerLinks.add(new RouterLink(header("MainLayout.authors"), AuthorsListView.class));
        routerLinks.add(new RouterLink(header("MainLayout.publishers"), PublishersListView.class));

        routerLinks.forEach(l -> l.setHighlightCondition(HighlightConditions.sameLocation()));
        addToDrawer(new VerticalLayout(routerLinks.toArray(new RouterLink[0])));
        setDrawerOpened(false);
    }
}