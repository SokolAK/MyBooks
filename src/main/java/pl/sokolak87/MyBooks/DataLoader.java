package pl.sokolak87.MyBooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sokolak87.MyBooks.model.author.*;
import pl.sokolak87.MyBooks.model.book.Book;
import pl.sokolak87.MyBooks.model.book.BookMapper;
import pl.sokolak87.MyBooks.model.book.BookRepo;
import pl.sokolak87.MyBooks.model.book.BookService;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class DataLoader implements ApplicationRunner {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final AuthorService authorService;
    private final BookService bookService;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    @Autowired
    public DataLoader(BookRepo bookRepo,
                      AuthorRepo authorRepo,
                      AuthorService authorService,
                      BookService bookService,
                      AuthorMapper authorMapper, BookMapper bookMapper) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.authorService = authorService;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }

    public void run(ApplicationArguments args) {
        Stream.of("Ogniem i lalką;  powieść;    Henryk Sienkiewicz, Bolesław Prus;  1930;   1;  1",
                "Lalka;             powieść;    Bolesław Prus;                      1922;   ;   3",
                "Dziady;            ;           Adam Mickiewicz;                    1901;   ;   2",
                "Kordian;           ;           Juliusz Słowacki;                   1914;   ;   ;",
                "Chłopi;            Zima;       Władysław Reymont;                  1906;   1;  ;",
                "Chłopi;            Wiosna;     Władysław Reymont;                  1906;   2;  ;")
                .forEach(s -> {
                    Book book = new Book();
                    book.setTitle(s.split(";")[0].trim());
                    book.setSubtitle(s.split(";")[1].trim());
                    book.setYear(s.split(";")[3].trim());
                    book.setVolume(s.split(";")[4].trim());
                    book.setEdition(s.split(";")[5].trim());

                    Arrays.stream(s.split(";")[2].split(","))
                            .forEach(a -> {
                                AuthorDto author = new AuthorDto();
                                author.setFirstName(a.trim().split(" ")[0]);
                                author.setLastName(a.trim().split(" ")[1]);
                                AuthorDto savedAuthor = authorService.save(author);
                                book.addAuthor(authorMapper.toEntity(savedAuthor));

                            });

                    bookService.save(bookMapper.toDto(book));

                });


    }
}
