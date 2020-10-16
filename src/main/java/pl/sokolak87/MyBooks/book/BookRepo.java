package pl.sokolak87.MyBooks.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book,Long>, BookFilter {

}
