package pl.sokolak.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.author.AuthorMapper;
import pl.sokolak.MyBooks.model.author.AuthorRepo;
import pl.sokolak.MyBooks.model.publisher.PublisherMapper;
import pl.sokolak.MyBooks.model.publisher.PublisherRepo;
import pl.sokolak.MyBooks.model.series.Series;
import pl.sokolak.MyBooks.model.series.SeriesMapper;
import pl.sokolak.MyBooks.model.series.SeriesRepo;

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
    @Autowired
    private PublisherRepo publisherRepo;
    @Autowired
    private PublisherMapper publisherMapper;
    @Autowired
    private SeriesRepo seriesRepo;
    @Autowired
    private SeriesMapper seriesMapper;

    public BookService(BookRepo bookRepo, BookMapper bookMapper) {
        this.bookRepo = bookRepo;
        this.bookMapper = bookMapper;
    }

    public long count() {
        return bookRepo.count();
    }

    public List<BookDto> findAll() {
        return bookRepo.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findAll(Pageable pageable) {
        return bookRepo.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findAll(String phrase, Map<String, Boolean> columnList, Pageable pageable) {
        if (phrase == null || phrase.isEmpty()) {
            return findAll(pageable);
        } else {
            return bookRepo.findAllContainingPhrase(phrase, columnList, pageable).stream()
                    .map(bookMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public BookDto save(BookDto bookDto) {
        return bookMapper.toDto(bookRepo.save(bookMapper.toEntity(bookDto)));
/*        if (bookDto.getId() != null) {
            Optional<Book> book = bookRepo.findById(bookDto.getId());
            book.ifPresent(b -> delete(bookDto));
        }
        Book book = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepo.save(book));*/
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

            b.getPublishers().stream()
                    .peek(p -> p.getBooks().remove(b))
                    .filter(p -> p.getBooks().size() == 0)
                    .forEach(p -> publisherRepo.delete(p));
            b.getPublishers().clear();

            Optional<Series> series = Optional.ofNullable(b.getSeries());
            series.ifPresent(s -> {
                        s.getBooks().remove(b);
                        if (s.getBooks().size() == 0)
                            seriesRepo.delete(b.getSeries());
                    }
            );
            bookRepo.delete(b);
        });
    }
}
