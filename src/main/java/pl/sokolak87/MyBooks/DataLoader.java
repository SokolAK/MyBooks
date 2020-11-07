package pl.sokolak87.MyBooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorMapper;
import pl.sokolak87.MyBooks.model.author.AuthorService;
import pl.sokolak87.MyBooks.model.book.Book;
import pl.sokolak87.MyBooks.model.book.BookMapper;
import pl.sokolak87.MyBooks.model.book.BookService;
import pl.sokolak87.MyBooks.model.publisher.PublisherDto;
import pl.sokolak87.MyBooks.model.publisher.PublisherMapper;
import pl.sokolak87.MyBooks.model.publisher.PublisherService;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class DataLoader implements ApplicationRunner {

    private final AuthorService authorService;
    private final BookService bookService;
    private final PublisherService publisherService;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;
    private final PublisherMapper publisherMapper;

    @Autowired
    public DataLoader(AuthorService authorService,
                      BookService bookService,
                      PublisherService publisherService,
                      AuthorMapper authorMapper,
                      BookMapper bookMapper,
                      PublisherMapper publisherMapper) {
        this.publisherService = publisherService;
        this.publisherMapper = publisherMapper;
        this.authorService = authorService;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }

    public void run(ApplicationArguments args) {
        Stream.of("Ogniem i lalką;  powieść;    Henryk Sienkiewicz, Bolesław Prus;  1930;   1;  1;  Gebethner;          Warszawa;",
                "Lalka;             powieść;    Bolesław Prus;                      1922;   ;   3;  Książnica Atlas;    Lwów;",
                "Dziady;            ;           Adam Mickiewicz;                    1901;   ;   2;  Gebethner, Fiszer;  Warszawa;",
                "Kordian;           ;           Juliusz Słowacki;                   1914;   ;   ;   Fiszer;             Łódź",
                "Chłopi;            Zima;       Władysław Reymont;                  1906;   1;  ;   ;                   ;",
                "Chłopi;            Wiosna;     Władysław Reymont;                  1906;   2;  ;   ;                   ;")
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

                    bookService.save(bookMapper.toDto(book));

                });


    }
}
