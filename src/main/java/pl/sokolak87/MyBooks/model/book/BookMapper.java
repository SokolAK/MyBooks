package pl.sokolak87.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak87.MyBooks.model.AbstractEntity;
import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorMapper;
import pl.sokolak87.MyBooks.model.author.AuthorRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookMapper {

    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private AuthorMapper authorMapper;

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setSubtitle(book.getSubtitle());
        bookDto.setYear(book.getYear());
        bookDto.setCity(book.getCity());
        bookDto.setVolume(book.getVolume());
        bookDto.setEdition(book.getEdition());

        book.getAuthors().stream()
                .map(AbstractEntity::getId)
                .map(id -> authorRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(a -> bookDto.getAuthors().add(authorMapper.toDto(a)));

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

        //saved authors
        bookDto.getAuthors().stream()
                .map(AuthorDto::getId)
                .filter(Objects::nonNull)
                .map(id -> authorRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(a -> book.getAuthors().add(a));
        //new authors
        bookDto.getAuthors().stream()
                .filter(a -> a.getId() == null)
                .map(a -> authorMapper.toEntity(a))
                .map(a -> authorRepo.save(a))
                .forEach(a -> book.getAuthors().add(a));

        return book;
    }
}
