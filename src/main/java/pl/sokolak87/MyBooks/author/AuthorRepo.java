package pl.sokolak87.MyBooks.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepo extends JpaRepository<Author, Long> {

    @Query(nativeQuery = true, value =
            "SELECT * FROM author a " +
            "WHERE concat(a.id, lower(a.prefix), lower(a.first_name), lower(a.middle_name), lower(a.last_name), lower(a.suffix)) " +
            "LIKE lower(concat('%', :searchTerm, '%'))")
    List<Author> search(@Param("searchTerm") String searchTerm);
}
