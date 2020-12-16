package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.sokolak.MyBooks.model.Dto;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.StrictMath.ceil;

public class NavigationController {
    private TextField txtPage;
    @Getter
    private Page page = new Page();
    private Function<Pageable, List<? extends Dto>> getItems;
    private Supplier<Long> countAllItems;
    private Consumer<List<? extends Dto>> updateList;

    public NavigationController(Function<Pageable,
                                List<? extends Dto>> getItems,
                                Supplier<Long> countAllItems,
                                Consumer<List<? extends Dto>> updateList,
                                TextField txtPage) {
        this.txtPage = txtPage;
        this.getItems = getItems;
        this.countAllItems = countAllItems;
        this.updateList = updateList;
    }

    public void configureBtnPages(Button btnLeft, Button btnRight) {
        btnLeft.setIcon(new Icon(VaadinIcon.CHEVRON_LEFT));
        btnLeft.addClickListener(e -> {
            if (page.no > 0)
                page.no--;
            update();
        });

        btnRight.setIcon(new Icon(VaadinIcon.CHEVRON_RIGHT));
        btnRight.addClickListener(e -> {
            if (page.no < page.max)
                page.no++;
            update();
        });
    }

    public void configureCmbPage(ComboBox<Integer> comboBox) {
        comboBox.setItems(10, 20, 50);
        comboBox.setValue(10);
        page.size = comboBox.getValue();
        comboBox.setWidth("8ch");
        comboBox.addValueChangeListener(e -> {
            page.size = e.getValue();
            page.no = 0;
            update();
        });
    }

    public void configureTxtPage(TextField txtPage) {
        txtPage.setValue(String.valueOf(page.no));
        //txtPage.setReadOnly(true);
        txtPage.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        txtPage.setMaxWidth("50px");
        txtPage.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        txtPage.addValueChangeListener(e -> {
            try {
                page.no = Integer.parseInt(txtPage.getValue());
                page.no = Math.max(page.no, 0);
            } catch (Exception ex) {
                page.no = 0;
            }
            update();
        });
    }

    public void update() {
        long count = countAllItems.get();
        if ((page.no + 1) * page.size > count)
            page.no = (int) ceil((float) count / page.size) - 1;

        Pageable pageable = PageRequest.of(page.no, page.size);
        List<? extends Dto> itemsList = getItems.apply(pageable);

        if (itemsList.size() == 0 && page.no > 0) {
            page.no--;
        } else {
            updateList.accept(itemsList);
        }

        page.max = page.no + 1;
        txtPage.setValue(String.valueOf(page.no));
    }

    public static class Page {
        public int no;
        public int max;
        public int size;
    }
}
