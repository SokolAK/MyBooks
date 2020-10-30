package pl.sokolak87.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorMapper;
import pl.sokolak87.MyBooks.model.author.AuthorService;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookMapper {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private AuthorService authorService;

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setSubtitle(book.getSubtitle());
        bookDto.setYear(book.getYear());
        bookDto.setCity(book.getCity());
        bookDto.setVolume(book.getVolume());
        bookDto.setEdition(book.getEdition());

        Set<AuthorDto> authors = book.getAuthors().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        bookDto.setAuthors(authors);

        return bookDto;
    }

    public Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setSubtitle(bookDto.getSubtitle());
        book.setYear(bookDto.getYear());
        book.setCity(bookDto.getCity());
        book.setVolume(bookDto.getVolume());
        book.setEdition(bookDto.getEdition());

/*       List<Author> authors = bookDto.getAuthors().stream()
                .map(authorMapper::toEntity)
                .collect(Collectors.toList());
       book.setAuthors(authors);*/

/*        bookDto.getAuthors().stream()
                .map(a -> {
                    Optional<AuthorDto> existingAuthor = authorService.find(a);
                    return existingAuthor.orElse(authorService.save(a));
                })
                .map(authorMapper::toEntity)
                .forEach(book::addAuthor);*/

        bookDto.getAuthors().stream()
                .map(a -> authorService.find(a).orElse(authorService.save(a)))
                .map(authorMapper::toEntity)
                .forEach(book::addAuthor);

        return book;
    }
}
