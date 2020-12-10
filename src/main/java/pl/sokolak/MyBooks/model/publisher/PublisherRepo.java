package pl.sokolak.MyBooks.model.publisher;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepo extends JpaRepository<Publisher, Long> {
    List<Publisher> findAllByOrderByNameAsc(Pageable pageable);
    List<Publisher> findAllByNameContainingIgnoreCase(String phrase, Pageable pageable);
}
