package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.UI;
import lombok.Getter;

public class ListViewUI {
    @Getter
    private int screenWidth;

    public void configure(Runnable initFunc, Runnable reloadFunc) {
        initUI(initFunc);
        setReloadUIListener(reloadFunc);
    }

    private void initUI(Runnable initFunc) {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(receiver -> {
            screenWidth = receiver.getScreenWidth();
            initFunc.run();
        });
    }

    private void setReloadUIListener(Runnable reloadFunc) {
        com.vaadin.flow.component.page.Page page = UI.getCurrent().getPage();
        page.addBrowserWindowResizeListener(event -> {
            if (event.getWidth() != screenWidth) {
                screenWidth = event.getWidth();
                reloadFunc.run();
            }
        });
    }
}
