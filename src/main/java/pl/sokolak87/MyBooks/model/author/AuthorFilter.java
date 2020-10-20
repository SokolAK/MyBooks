package pl.sokolak87.MyBooks.model.author;

import java.util.List;

public interface AuthorFilter {
    List<Author> findAllContainingPhrase(String phrase);
}
