package pl.sokolak87.MyBooks.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sokolak87.MyBooks.ui.MainLayout;

@Route(value = "publishers", layout = MainLayout.class)
@PageTitle("Publishers | MyBooks")
public class PublishersListView extends VerticalLayout {
}
