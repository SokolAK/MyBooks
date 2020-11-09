package pl.sokolak.MyBooks.ui.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

public class DialogWindow {

    private final String header;
    private final Component body;
    private Dialog dialog;

    public DialogWindow(Component body, String header) {
        this.body = body;
        this.header = header;
    }

    public void open() {
        dialog = new SelfClosingDialog();
        //addTopBar();

        dialog.add(body);

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.setMaxWidth("500px");
        dialog.setWidthFull();

        dialog.open();
    }

    private void addTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        topBar.setWidthFull();

        Div div = new Div();
        div.setText(header);
        topBar.add(div);
        topBar.expand(div);

        Button btnClose = new Button(new Icon(VaadinIcon.CLOSE), e -> dialog.close());
        topBar.add(btnClose);

        dialog.add(topBar);
    }

    public static class SelfClosingDialog extends Dialog implements AfterNavigationObserver {
        @Override
        public void afterNavigation(AfterNavigationEvent event) {
            this.close();
        }
    }
}
