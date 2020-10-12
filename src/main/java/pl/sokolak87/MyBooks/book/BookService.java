package pl.sokolak87.MyBooks.book;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<BookDto> findAll() {
        return bookRepo.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return findAll();
        } else {
            return bookRepo.search(stringFilter).stream()
                    .map(BookMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
