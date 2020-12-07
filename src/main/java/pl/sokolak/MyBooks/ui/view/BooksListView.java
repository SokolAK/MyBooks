package pl.sokolak.MyBooks.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.sokolak.MyBooks.model.author.AuthorService;
import pl.sokolak.MyBooks.model.book.BookDto;
import pl.sokolak.MyBooks.model.book.BookService;
import pl.sokolak.MyBooks.model.publisher.PublisherService;
import pl.sokolak.MyBooks.model.series.SeriesService;
import pl.sokolak.MyBooks.security.Secured;
import pl.sokolak.MyBooks.ui.MainLayout;
import pl.sokolak.MyBooks.ui.form.BookDetails;
import pl.sokolak.MyBooks.ui.form.DialogWindow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static pl.sokolak.MyBooks.model.author.AuthorDto.authorsSetToString;
import static pl.sokolak.MyBooks.model.publisher.PublisherDto.publishersSetToString;
import static pl.sokolak.MyBooks.security.OperationType.ADD;
import static pl.sokolak.MyBooks.utils.TextFormatter.header;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Books | MyBooks")
public class BooksListView extends VerticalLayout {
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final SeriesService seriesService;
    private final Grid<BookDto> grid = new Grid<>(BookDto.class);
    private final TextField txtFilter = new TextField();
    private final MenuBar mnuColumns = new MenuBar();
    private final TextField txtPage = new TextField();
    private final ComboBox<String> comboBox = new ComboBox<>();
    private final Map<String, Boolean> columnList = new LinkedHashMap<>();
    private Button btnLeft;
    private Button btnRight;
    private Button btnShortNotation;
    private Button btnAddBook;
    private boolean shortNotation = false;
    private VerticalLayout toolbar;
    private int pageNo;
    private int pageSize;
    private int pageMax = 0;

    public BooksListView(BookService bookService, AuthorService authorService, PublisherService publisherService, SeriesService seriesService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.seriesService = seriesService;
        addClassName("list-view");
        setSizeFull();

        initFullColumnList();
        //configureChkShortNotation();
        configureGrid();
        configureToolbar();

        add(toolbar, grid);

        updateList();
        configureTxtPage();
    }

    private Icon setBtnShortNotationIcon(boolean shortNotation) {
        if (shortNotation)
            return new Icon(VaadinIcon.EXPAND);
        else
            return new Icon(VaadinIcon.COMPRESS);
    }

    private void configureGrid() {
        grid.addClassName("book-grid");
        grid.setSizeFull();
        grid.setColumns();

        updateGrid();

        grid.addItemDoubleClickListener(e -> {
            grid.select(e.getItem());
            openBookDetails(e.getItem());
        });
    }

    //@Secured(EDIT)
    private void openBookDetails(BookDto bookDto) {
        BookDetails bookDetails = new BookDetails(BookDto.copy(bookDto), authorService, publisherService, seriesService);
        //authorForm.setAuthor(e);
        //authorForm.addListener(AuthorForm.DeleteEvent.class, this::deleteAuthor);
        //authorForm.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
        bookDetails.addListener(BookDetails.SaveEvent.class, this::saveBook);
        bookDetails.addListener(BookDetails.DeleteEvent.class, this::deleteBook);
        new DialogWindow(bookDetails, header("bookDetails")).open();
    }

    private void saveBook(BookDetails.SaveEvent e) {
        bookService.save(e.getDto(BookDto.class));
        updateList();
    }

    private void deleteBook(BookDetails.DeleteEvent e) {
        bookService.delete(e.getDto(BookDto.class));
        updateList();
    }

    private void configureTxtFilter() {
        txtFilter.setPlaceholder(header("filterByPhrase"));
        txtFilter.setClearButtonVisible(true);
        txtFilter.setValueChangeMode(ValueChangeMode.LAZY);
        txtFilter.addFocusListener(e -> expandTxtFilter());
        txtFilter.addBlurListener(e -> collapseTxtFilter());
        //txtFilter.setWidthFull();
        txtFilter.addValueChangeListener(e -> {
            resetPage();
            updateList();
        });
    }

    private void configureToolbar() {
        toolbar = new VerticalLayout();
        toolbar.setPadding(false);
        //toolbar.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        btnAddBook = new Button(new Icon(VaadinIcon.PLUS), click -> addBook());
        configureTxtFilter();
        configureBtnShortNotation();
        configureMnuColumns();

        toolbar.setWidthFull();
        //toolbar.expand(txtFilter);
        txtFilter.setMinWidth("0%");

        configureBtnPages();
        configureCmbPage();

        HorizontalLayout hL1 = new HorizontalLayout(btnAddBook, txtFilter);
        HorizontalLayout hL2 = new HorizontalLayout(mnuColumns, btnShortNotation);
        HorizontalLayout hL3 = new HorizontalLayout(btnLeft, txtPage, btnRight);
        HorizontalLayout hL4 = new HorizontalLayout(hL2, hL3, comboBox);
        hL1.setPadding(false);
        hL2.setPadding(false);
        hL3.setPadding(false);
        hL4.setPadding(false);
        hL1.setWidthFull();
        hL4.setWidthFull();
        hL1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hL2.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hL3.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hL4.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hL1.setSpacing(false);
        hL2.setSpacing(false);
        hL3.setSpacing(false);
        toolbar.add(hL1, hL4);
    }

    private void configureBtnExport() {
        //Anchor export = new Anchor(new StreamResource("filename.txt", () -> new DataExporter(bookService).exportFile()), "");
        //export.getElement().setAttribute("download", true);
        //export.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
    }

    private void configureBtnPages() {
        btnLeft = new Button(new Icon(VaadinIcon.CHEVRON_LEFT), e -> {
            if (pageNo > 0) pageNo--;
            updateList();
        });
        btnRight = new Button(new Icon(VaadinIcon.CHEVRON_RIGHT), e -> {
            if (pageNo < pageMax) pageNo++;
            updateList();
        });
    }

    private void configureBtnShortNotation() {
        btnShortNotation = new Button(setBtnShortNotationIcon(shortNotation), e -> {
            shortNotation = !shortNotation;
            if (shortNotation)
                initShortColumnList();
            else
                initFullColumnList();
            updateMnuColumns();
            updateGrid();
            e.getSource().setIcon(setBtnShortNotationIcon(shortNotation));
        });
    }

    private void configureCmbPage() {
        comboBox.setItems("10", "20", "50");
        comboBox.setValue("10");
        pageSize = Integer.parseInt(comboBox.getValue());
        comboBox.setWidth("8ch");
        comboBox.addValueChangeListener(e -> {
            pageSize = Integer.parseInt(e.getValue());
            resetPage();
            updateList();
        });
    }

    private void configureTxtPage() {
        txtPage.setValue(String.valueOf(pageNo));
        //txtPage.setReadOnly(true);
        txtPage.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        txtPage.setMaxWidth("6ch");
        txtPage.setValueChangeMode(ValueChangeMode.LAZY);
        txtPage.addValueChangeListener(e -> {
            try {
                pageNo = Integer.parseInt(txtPage.getValue());
            } catch (Exception ex) {
                pageNo = 0;
            }
            updateList();
        });
    }

    private void expandTxtFilter() {
        txtFilter.setMinWidth("100%");
        btnAddBook.setVisible(false);
        //btnShortNotation.setVisible(false);
        //mnuColumns.setVisible(false);
        //btnAddBook.setVisible(false);
    }

    private void collapseTxtFilter() {
        txtFilter.setMinWidth("0%");
        btnAddBook.setVisible(true);
        //btnShortNotation.setVisible(true);
        //mnuColumns.setVisible(true);
        //btnAddBook.setVisible(true);
    }

    @Secured(ADD)
    private void addBook() {
        grid.asSingleSelect().clear();
        openBookDetails(new BookDto());
    }

    private Grid.Column addGridColumn(String key) {
        Grid.Column<BookDto> column;
        if (key.equals("authors")) {
            ValueProvider<BookDto, String> valueProvider = b -> authorsSetToString(b.getAuthors(), shortNotation);
            column = grid.addColumn(valueProvider).setComparator(valueProvider);
        } else if (key.equals("publishers")) {
            ValueProvider<BookDto, String> valueProvider = b -> publishersSetToString(b.getPublishers());
            column = grid.addColumn(valueProvider).setComparator(valueProvider);
        } else {
            column = grid.addColumn(key);
        }
        column.setHeader(header(key));
        return column;
    }

    private void updateList() {
        pageNo = Math.max(pageNo, 0);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<BookDto> books = bookService.findAll(txtFilter.getValue(), columnList, pageable);
        if (books.size() == 0 && pageNo > 0) {
            pageNo--;
        } else {
            grid.setItems(books);
            updateGrid();
        }
        pageMax = pageNo + 1;
        txtPage.setValue(String.valueOf(pageNo));
    }

    private void resetPage() {
        pageNo = 0;
    }

    private void addEntityColumns() {
        columnList.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(e -> addGridColumn(e.getKey()))
                .forEach(c -> c.setAutoWidth(true));
    }

    private void updateGrid() {
        grid.setColumns();
        addEntityColumns();
    }

    private void initShortColumnList() {
        columnList.put("id", false);
        columnList.put("title", true);
        columnList.put("subtitle", false);
        columnList.put("authors", true);
        columnList.put("publishers", true);
        columnList.put("year", true);
        columnList.put("city", false);
        columnList.put("edition", true);
        columnList.put("volume", true);
        columnList.put("series", false);
        columnList.put("seriesVolume", false);
        columnList.put("comment", false);
    }

    private void initFullColumnList() {
        columnList.put("id", false);
        columnList.put("title", true);
        columnList.put("subtitle", true);
        columnList.put("authors", true);
        columnList.put("publishers", true);
        columnList.put("year", true);
        columnList.put("city", true);
        columnList.put("edition", true);
        columnList.put("volume", true);
        columnList.put("series", true);
        columnList.put("seriesVolume", true);
        columnList.put("comment", true);
    }

    private void configureMnuColumns() {
        MenuItem columns = mnuColumns.addItem(new Icon(VaadinIcon.TABLE));
        columnList.forEach((columnName, isChecked) -> {
                    MenuItem item = columns
                            .getSubMenu()
                            .addItem(header(columnName));
                    item.setId(columnName);
                    item.setCheckable(true);
                    item.setChecked(isChecked);
                    item.addClickListener(e -> {
                                boolean currentValue = columnList.get(columnName);
                                columnList.put(columnName, !currentValue);
                                updateGrid();
                                updateList();
                            }
                    );
                }
        );
        mnuColumns.addThemeVariants(MenuBarVariant.LUMO_ICON);
    }

    private void updateMnuColumns() {
        MenuItem columns = mnuColumns.getItems().get(0);
        columns.getSubMenu().getItems().forEach(it -> it.setChecked(columnList.get(it.getId().get())));
    }

/*    @Override
    public String getPageTitle() {
        return "Books | MyBooks";
    }*/
}