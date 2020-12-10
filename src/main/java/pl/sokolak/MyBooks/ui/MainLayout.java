package pl.sokolak.MyBooks.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.sokolak.MyBooks.ui.view.AuthorsListView;
import pl.sokolak.MyBooks.ui.view.BooksListView;
import pl.sokolak.MyBooks.ui.view.PublishersListView;
import pl.sokolak.MyBooks.ui.view.SeriesListView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static pl.sokolak.MyBooks.utils.TextFormatter.header;
import static pl.sokolak.MyBooks.utils.TextFormatter.label;


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

    public MainLayout() throws FileNotFoundException {
        createHeader();
        createDrawer();
        ThemeUtil.setThemeVariant(Lumo.DARK);
    }


    private void createHeader() throws FileNotFoundException {

        H1 logo = new H1(label("appName"));
        logo.addClassName("logo");

        Button logoutButton = new Button(new Icon(VaadinIcon.SIGN_OUT));
        logoutButton.addClickListener(click -> getUI().ifPresent(u -> u.getPage().setLocation("/logout")));
        Button toggleThemeVariantButton = new Button(new Icon(VaadinIcon.MOON));
        toggleThemeVariantButton.addClickListener(click -> ThemeUtil.toggleThemeVariant());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Text username = new Text(authentication.getName());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, username, toggleThemeVariantButton, logoutButton);
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
        routerLinks.add(new RouterLink(header("MainLayout.series"), SeriesListView.class));

        routerLinks.forEach(l -> l.setHighlightCondition(HighlightConditions.sameLocation()));

        Image logo = new Image("icons/icon.png", "");
        logo.setMaxHeight("88px");

        VerticalLayout drawerLayout = new VerticalLayout(logo, new H2(label("appName")));
        drawerLayout.add(routerLinks.toArray(new RouterLink[0]));
        addToDrawer(drawerLayout);
        setDrawerOpened(false);
    }
}