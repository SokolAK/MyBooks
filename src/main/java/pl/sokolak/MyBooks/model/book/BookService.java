package pl.sokolak.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.author.AuthorMapper;
import pl.sokolak.MyBooks.model.author.AuthorRepo;

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
}
