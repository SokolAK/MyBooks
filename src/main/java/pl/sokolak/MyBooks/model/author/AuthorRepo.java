package pl.sokolak.MyBooks.model.author;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepo extends JpaRepository<Author, Long>, AuthorFilter {
    List<Author> findAllByOrderByLastNameAscFirstNameAscMiddleNameAscPrefixAsc(Pageable pageable);
}
