package pl.sokolak87.MyBooks.author;

import java.util.List;

public interface AuthorFilter {
    List<Author> findAllContainingPhrase(String phrase);
}
