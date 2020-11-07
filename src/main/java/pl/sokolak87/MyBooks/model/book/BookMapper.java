package pl.sokolak87.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak87.MyBooks.model.AbstractEntity;
import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorMapper;
import pl.sokolak87.MyBooks.model.author.AuthorRepo;
import pl.sokolak87.MyBooks.model.publisher.PublisherDto;
import pl.sokolak87.MyBooks.model.publisher.PublisherMapper;
import pl.sokolak87.MyBooks.model.publisher.PublisherRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookMapper {

    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private PublisherRepo publisherRepo;
    @Autowired
    private PublisherMapper publisherMapper;

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setSubtitle(book.getSubtitle());
        bookDto.setYear(book.getYear());
        bookDto.setCity(book.getCity());
        bookDto.setVolume(book.getVolume());
        bookDto.setEdition(book.getEdition());
        bookDto.setComment(book.getComment());

        book.getAuthors().stream()
                .map(AbstractEntity::getId)
                .map(id -> authorRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(a -> bookDto.getAuthors().add(authorMapper.toDto(a)));

        book.getPublishers().stream()
                .map(AbstractEntity::getId)
                .map(id -> publisherRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(p -> bookDto.getPublishers().add(publisherMapper.toDto(p)));

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
        book.setComment(bookDto.getComment());

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

        //saved publishers
        bookDto.getPublishers().stream()
                .map(PublisherDto::getId)
                .filter(Objects::nonNull)
                .map(id -> publisherRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(p -> book.getPublishers().add(p));
        //new publishers
        bookDto.getPublishers().stream()
                .filter(p -> p.getId() == null)
                .map(p -> publisherMapper.toEntity(p))
                .map(p -> publisherRepo.save(p))
                .forEach(p -> book.getPublishers().add(p));

        return book;
    }
}
