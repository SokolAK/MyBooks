package pl.sokolak.MyBooks.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class LayoutBuilder {
    @Builder.Default
    private final boolean padding = true;
    @Builder.Default
    private final boolean spacing = true;
    @Builder.Default
    private final boolean widthFull = false;
    @Builder.Default
    private final Alignment alignment = Alignment.CENTER;
    @Builder.Default
    private final List<Component> components = new ArrayList<>();

    public HorizontalLayout getHorizontalLayout() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(alignment);
        hl.setPadding(padding);
        hl.setSpacing(spacing);
        if (widthFull)
            hl.setWidthFull();
        hl.add(components.toArray(Component[]::new));
        return hl;
    }

    public VerticalLayout getVerticalLayout() {
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(alignment);
        vl.setPadding(padding);
        vl.setSpacing(spacing);
        if (widthFull)
            vl.setWidthFull();
        vl.add(components.toArray(Component[]::new));
        return vl;
    }
//    public static HorizontalLayout getLayout(boolean padding, boolean spacing, boolean widthFull, Alignment alignment, Component... components) {
//        HorizontalLayout hl = new HorizontalLayout(components);
//        hl.setDefaultVerticalComponentAlignment(alignment);
//        hl.setPadding(padding);
//        hl.setSpacing(spacing);
//        hl.setWidthFull();
//
//        HorizontalLayout hL1 = new HorizontalLayout(btnAddBook, txtFilter);
//        HorizontalLayout hL2 = new HorizontalLayout(mnuColumns, btnShortNotation);
//        HorizontalLayout hL3 = new HorizontalLayout(btnLeft, txtPage, btnRight);
//        HorizontalLayout hL4 = new HorizontalLayout(hL2, hL3, comboBox);
//        hL1.setPadding(false);
//        hL2.setPadding(false);
//        hL3.setPadding(false);
//        hL4.setPadding(false);
//        hL1.setWidthFull();
//        hL4.setWidthFull();
//        hL1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
//        hL2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
//        hL3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
//        hL4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
//        hL1.setSpacing(false);
//        hL2.setSpacing(false);
//        hL3.setSpacing(false);
//    }
}
