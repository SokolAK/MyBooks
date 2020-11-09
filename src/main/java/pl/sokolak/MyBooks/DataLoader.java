package pl.sokolak.MyBooks;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class DataLoader implements ApplicationRunner {

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
        Stream.of("Ogniem i lalką;  powieść;    Henryk Sienkiewicz, Bolesław Prus;  1930;   1;  1;  Gebethner;          Warszawa;   Wielcy Pisarze; 1;",
                "Lalka;             powieść;    Bolesław Prus;                      1922;   ;   3;  Książnica Atlas;    Lwów;       Wielcy Pisarze; 2;",
                "Dziady;            ;           Adam Mickiewicz;                    1901;   ;   2;  Gebethner, Fiszer;  Warszawa;   Wielcy Pisarze; 3;",
                "Kordian;           ;           Juliusz Słowacki;                   1914;   ;   ;   Fiszer;             Łódź;       ;               ;",
                "Chłopi;            Zima;       Władysław Reymont;                  1906;   1;  ;   ;                   ;           Nobliści;       1;",
                "Chłopi;            Wiosna;     Władysław Reymont;                  1906;   2;  ;   ;                   ;           Nobliści;       2;")
                .forEach(s -> {
                    Book book = new Book();
                    book.setTitle(s.split(";")[0].trim());
                    book.setSubtitle(s.split(";")[1].trim());
                    book.setYear(s.split(";")[3].trim());
                    book.setVolume(s.split(";")[4].trim());
                    book.setEdition(s.split(";")[5].trim());
                    book.setCity(s.split(";")[7].trim());

                    Arrays.stream(s.split(";")[2].split(","))
                            .filter(a -> !a.isBlank())
                            .forEach(a -> {
                                AuthorDto author = new AuthorDto();
                                author.setFirstName(a.trim().split(" ")[0]);
                                author.setLastName(a.trim().split(" ")[1]);
                                AuthorDto savedAuthor = authorService.save(author);
                                savedAuthor.getBooksIds().add(book.getId());
                                book.getAuthors().add(authorMapper.toEntity(savedAuthor));
                            });

                    Arrays.stream(s.split(";")[6].split(","))
                            .filter(p -> !p.isBlank())
                            .forEach(p -> {
                                PublisherDto publisher = new PublisherDto();
                                publisher.setName(p.trim());
                                PublisherDto savedPublisher = publisherService.save(publisher);
                                savedPublisher.getBooksIds().add(book.getId());
                                book.getPublishers().add(publisherMapper.toEntity(savedPublisher));
                            });

                    String seriesName = s.split(";")[8].trim();
                    if(!seriesName.isBlank()) {
                        SeriesDto series = new SeriesDto();
                        series.setName(seriesName);
                        SeriesDto savedSeries = seriesService.save(series);
                        savedSeries.getBooksIds().add(book.getId());
                        book.setSeries(seriesMapper.toEntity(savedSeries));
                    }

                    book.setSeriesVolume(s.split(";")[9].trim());

                    bookService.save(bookMapper.toDto(book));
                });
    }
}
