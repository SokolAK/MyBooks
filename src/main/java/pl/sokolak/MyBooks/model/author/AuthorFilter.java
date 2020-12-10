package pl.sokolak.MyBooks.model.author;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorFilter {
    List<Author> findAllContainingPhrase(String phrase, Pageable pageable);
    List<Author> findAllContainingPhrases(String prefix, String firstName, String middleName, String lastName, Pageable pageable);
}
