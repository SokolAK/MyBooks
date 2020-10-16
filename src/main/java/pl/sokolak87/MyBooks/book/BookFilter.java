package pl.sokolak87.MyBooks.book;

import java.util.List;
import java.util.Map;

public interface BookFilter {
    List<Book> findAllContainingPhrase(String phrase, Map<String,Boolean> columnList);
}
