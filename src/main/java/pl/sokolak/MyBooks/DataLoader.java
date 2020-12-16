package pl.sokolak.MyBooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokolak.MyBooks.model.author.AuthorDto;
import pl.sokolak.MyBooks.model.author.AuthorMapper;
import pl.sokolak.MyBooks.model.author.AuthorService;
import pl.sokolak.MyBooks.model.book.Book;
import pl.sokolak.MyBooks.model.book.BookMapper;
import pl.sokolak.MyBooks.model.book.BookService;
import pl.sokolak.MyBooks.model.publisher.PublisherDto;
import pl.sokolak.MyBooks.model.publisher.PublisherMapper;
import pl.sokolak.MyBooks.model.publisher.PublisherService;
import pl.sokolak.MyBooks.model.series.SeriesDto;
import pl.sokolak.MyBooks.model.series.SeriesMapper;
import pl.sokolak.MyBooks.model.series.SeriesService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Component
public class DataLoader implements ApplicationRunner {
    private final Logger log = Logger.getLogger(getClass().getName());

    @Value("${dataLoader.populate:false}")
    private boolean populate;

    @Value("${dataLoader.fromLine:0}")
    private long fromLine;

    @Value("${dataLoader.toLine:10000}")
    private long toLine;

    private final AuthorService authorService;
    private final BookService bookService;
    private final PublisherService publisherService;
    private final SeriesService seriesService;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;
    private final PublisherMapper publisherMapper;
    private final SeriesMapper seriesMapper;

    @Autowired
    public DataLoader(AuthorService authorService,
                      BookService bookService,
                      PublisherService publisherService,
                      SeriesService seriesService,
                      AuthorMapper authorMapper,
                      BookMapper bookMapper,
                      PublisherMapper publisherMapper,
                      SeriesMapper seriesMapper) {
        this.publisherService = publisherService;
        this.seriesService = seriesService;
        this.publisherMapper = publisherMapper;
        this.authorService = authorService;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
        this.seriesMapper = seriesMapper;
    }

    public void run(ApplicationArguments args) {
        if (populate) {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("books_data.txt");
            Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines();

            lines.peek(s -> log.log(Level.INFO, "Reading line: {0}", s))
                    .map(s -> s.split("#"))
                    .filter(items -> items.length == 12)
                    .filter(items -> Long.parseLong(items[0].trim()) >= fromLine)
                    .filter(items -> Long.parseLong(items[0].trim()) <= toLine)
                    .peek(this::saveBook)
                    .forEach(s -> log.log(Level.INFO, "Saved"));
            lines.close();
        }
    }

    private void saveBook(String[] items) {
        Book book = new Book();

        setAuthors(book, items[1].trim());

        book.setTitle(items[2].trim());
        book.setSubtitle(items[3].trim());

        setPublishers(book, items[4].trim());

        book.setVolume(items[5].trim());
        book.setEdition(items[6].trim());
        book.setCity(items[7].trim());
        book.setYear(items[8].trim());

        setSeries(book, items[9].trim());

        book.setSeriesVolume(items[10].trim());
        book.setComment(items[11].trim());

        bookService.save(bookMapper.toDto(book));
    }

    private void setAuthors(Book book, String authors) {
        Arrays.stream(authors.split(";"))
                .filter(a -> !a.isBlank())
                .forEach(a -> {
                    AuthorDto author = new AuthorDto();
                    String[] aItems = a.trim().split(",", 2);
                    if (aItems.length > 1) {
                        author.setPrefix(aItems[1]);
                    }
                    String[] names = aItems[0].split(" ");
                    author.setLastName(names[0]);
                    if (names.length > 1) {
                        author.setFirstName(names[1]);
                    }
                    if (names.length > 2) {
                        List<String> middleNames = new ArrayList<>(Arrays.asList(names).subList(2, names.length));
                        String middleName = String.join(" ", middleNames);
                        author.setMiddleName(middleName);
                    }
                    AuthorDto savedAuthor = authorService.save(author);
                    savedAuthor.getBooksIds().add(book.getId());
                    book.getAuthors().add(authorMapper.toEntity(savedAuthor));
                });
    }

    private void setPublishers(Book book, String publishers) {
        Arrays.stream(publishers.split(";"))
                .filter(p -> !p.isBlank())
                .forEach(p -> {
                    PublisherDto publisher = new PublisherDto();
                    publisher.setName(p.trim());
                    PublisherDto savedPublisher = publisherService.save(publisher);
                    savedPublisher.getBooksIds().add(book.getId());
                    book.getPublishers().add(publisherMapper.toEntity(savedPublisher));
                });
    }

    private void setSeries(Book book, String seriesName) {
        if (!seriesName.isBlank()) {
            SeriesDto series = new SeriesDto();
            series.setName(seriesName);
            SeriesDto savedSeries = seriesService.save(series);
            savedSeries.getBooksIds().add(book.getId());
            book.setSeries(seriesMapper.toEntity(savedSeries));
        }
    }
}
