package pl.sokolak.MyBooks.model.book;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BookFilter {
    List<Book> findAllContainingPhrase(String phrase, Map<String,Boolean> columnList, Pageable pageable);
}
