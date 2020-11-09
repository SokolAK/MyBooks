package pl.sokolak.MyBooks.model.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book,Long>, BookFilter {
}
