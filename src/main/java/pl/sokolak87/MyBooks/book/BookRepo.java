package pl.sokolak87.MyBooks.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepo extends JpaRepository<Book,Long> {

    @Query(nativeQuery = true, value =
        "SELECT * FROM book b WHERE b.id IN" +
            "(SELECT DISTINCT b.id FROM book b " +
            "LEFT JOIN book_author ba ON b.id = ba.book_id " +
            "LEFT JOIN author a ON a.id = ba.author_id " +
            "WHERE concat(b.id, lower(b.title), lower(b.subtitle), lower(b.city), b.year," +
                "lower(a.first_name), lower(a.last_name)) " +
            "LIKE lower(concat('%', :searchTerm, '%')))")
    List<Book> search(@Param("searchTerm") String searchTerm);
}
