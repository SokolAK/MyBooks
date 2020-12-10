package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.sokolak.MyBooks.ui.ExpandingTextField;
import pl.sokolak.MyBooks.utils.LayoutBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ToolbarUtils extends VerticalLayout {

    public static void reloadToolbar(VerticalLayout toolbar, ListViewUI listViewUI, Button btnAdd, Button btnLeft, Button btnRight,
                                     ExpandingTextField txtFilter, TextField txtPage, ComboBox<Integer> comboBox) {
        toolbar.removeAll();
        toolbar.setPadding(false);
        toolbar.setWidthFull();
        Stream.of(btnAdd, txtFilter, btnLeft, txtPage, btnRight, comboBox).forEach(c -> c.setVisible(true));
        //toolbar.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //toolbar.expand(txtFilter);
        if (listViewUI.getScreenWidth() > 700) {
            HorizontalLayout hl3 = LayoutBuilder.builder()
                    .components(List.of(btnLeft, txtPage, btnRight))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .build().getHorizontalLayout();
            HorizontalLayout hl4 = LayoutBuilder.builder()
                    .components(List.of(btnAdd, txtFilter, hl3, comboBox))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .widthFull(true)
                    .build().getHorizontalLayout();
            txtFilter.setComponentsToHide(Set.of(btnAdd, btnLeft, txtPage, btnRight, comboBox));
            toolbar.add(hl4);
        } else {
            HorizontalLayout hl1 = LayoutBuilder.builder()
                    .components(List.of(btnAdd, txtFilter))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .widthFull(true)
                    .build().getHorizontalLayout();

            HorizontalLayout hl3 = LayoutBuilder.builder()
                    .components(List.of(btnLeft, txtPage, btnRight))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .build().getHorizontalLayout();

            HorizontalLayout hl4 = LayoutBuilder.builder()
                    .components(List.of(hl3, comboBox))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .widthFull(true)
                    .build().getHorizontalLayout();
            txtFilter.setComponentsToHide(Set.of(btnAdd));
            toolbar.add(hl1, hl4);
        }
    }

    public static void reloadToolbar(VerticalLayout toolbar, ListViewUI listViewUI, Button btnAdd, Button btnLeft, Button btnRight,
                              ExpandingTextField txtFilter, TextField txtPage, ComboBox<Integer> comboBox, MenuBar mnuColumns, Button btnShortNotation) {
        toolbar.removeAll();
        toolbar.setPadding(false);
        toolbar.setWidthFull();
        Stream.of(btnAdd, txtFilter, mnuColumns, btnShortNotation, btnLeft, txtPage, btnRight, comboBox).forEach(c -> c.setVisible(true));
        //toolbar.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //toolbar.expand(txtFilter);
        if (listViewUI.getScreenWidth() > 700) {
            HorizontalLayout hl3 = LayoutBuilder.builder()
                    .components(List.of(btnLeft, txtPage, btnRight))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .build().getHorizontalLayout();
            HorizontalLayout hl4 = LayoutBuilder.builder()
                    .components(List.of(btnAdd, txtFilter, mnuColumns, btnShortNotation, hl3, comboBox))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .widthFull(true)
                    .build().getHorizontalLayout();
            txtFilter.setComponentsToHide(Set.of(btnAdd, mnuColumns, btnShortNotation, btnLeft, txtPage, btnRight, comboBox));
            toolbar.add(hl4);
        } else {
            HorizontalLayout hl1 = LayoutBuilder.builder()
                    .components(List.of(btnAdd, txtFilter))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .widthFull(true)
                    .build().getHorizontalLayout();

            HorizontalLayout hl2 = LayoutBuilder.builder()
                    .components(List.of(mnuColumns, btnShortNotation))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .build().getHorizontalLayout();

            HorizontalLayout hl3 = LayoutBuilder.builder()
                    .components(List.of(btnLeft, txtPage, btnRight))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .spacing(false)
                    .build().getHorizontalLayout();

            HorizontalLayout hl4 = LayoutBuilder.builder()
                    .components(List.of(hl2, hl3, comboBox))
                    .alignment(Alignment.CENTER)
                    .padding(false)
                    .widthFull(true)
                    .build().getHorizontalLayout();

            txtFilter.setComponentsToHide(Set.of(btnAdd));

            toolbar.add(hl1, hl4);
        }
    }
}
