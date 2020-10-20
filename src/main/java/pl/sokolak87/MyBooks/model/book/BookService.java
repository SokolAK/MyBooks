package pl.sokolak87.MyBooks.model.book;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public List<BookDto> findAll(String phrase, Map<String, Boolean> columnList) {
        if (phrase == null || phrase.isEmpty()) {
            return findAll();
        } else {
            return bookRepo.findAllContainingPhrase(phrase, columnList).stream()
                    .map(BookMapper::toDto)
                    .collect(Collectors.toList());
        }
    }


    public String test(String s) {
        System.out.println(s);
        return s;
    }
}
