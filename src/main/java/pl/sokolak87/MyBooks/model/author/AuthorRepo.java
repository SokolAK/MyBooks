package pl.sokolak87.MyBooks.model.author;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long>, AuthorFilter {

}
