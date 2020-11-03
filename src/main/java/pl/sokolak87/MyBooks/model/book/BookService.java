package pl.sokolak87.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak87.MyBooks.model.author.AuthorMapper;
import pl.sokolak87.MyBooks.model.author.AuthorRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepo bookRepo;
    private final BookMapper bookMapper;
    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private AuthorMapper authorMapper;


    public BookService(BookRepo bookRepo, BookMapper bookMapper) {
        this.bookRepo = bookRepo;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> findAll() {
        return bookRepo.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findAll(String phrase, Map<String, Boolean> columnList) {
        if (phrase == null || phrase.isEmpty()) {
            return findAll();
        } else {
            return bookRepo.findAllContainingPhrase(phrase, columnList).stream()
                    .map(bookMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public BookDto save(BookDto bookDto) {
        if (bookDto.getId() != null) {
            Optional<Book> book = bookRepo.findById(bookDto.getId());
            book.ifPresent(b -> delete(bookDto));
        }
        Book book = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepo.save(book));
    }

    @Transactional
    public void delete(BookDto bookDto) {
        Optional<Book> book = bookRepo.findById(bookDto.getId());
        book.ifPresent(b -> {
            b.getAuthors().stream()
                    .peek(a -> a.getBooks().remove(b))
                    .filter(a -> a.getBooks().size() == 0)
                    .forEach(a -> authorRepo.delete(a));
            b.getAuthors().clear();
            bookRepo.delete(b);
        });
    }

/*    @Transactional
    public BookDto save(BookDto bookDto) {
        updateAuthors(bookDto);
        return bookMapper.toDto(bookRepo.save(bookMapper.toEntity(bookDto)));
    }

    @Transactional
    public void delete(BookDto bookDto) {
        deleteAuthors(bookDto);
        bookRepo.delete(bookMapper.toEntity(bookDto));
    }

    private void deleteAuthors(BookDto bookDto) {
        bookDto.getAuthors().clear();
        updateAuthors(bookDto);
    }

    private void updateAuthors(BookDto bookDto) {
        if (bookDto.getId() != null) {
            Optional<Book> book = bookRepo.findById(bookDto.getId());
            book.ifPresent(b -> b.getAuthors().stream()
                    .filter(a -> !bookDto.getAuthors().contains(authorMapper.toDto(a)))
                    .peek(a -> a.getBooks().remove(b))
                    .map(authorMapper::toDto)
                    .forEach(authorService::deleteOrphan));
        }
    }*/

/*
        Author authorEntity = authorMapper.toEntity(authorDto);
        Optional<Author> existingAuthor = authorRepo.findOne(Example.of(authorEntity));
        //Optional<Author> existingAuthor = authorRepo.findById(authorDto.getId());
        return authorMapper.toDto(existingAuthor.orElseGet(() -> authorRepo.save(authorEntity)));*/
}
