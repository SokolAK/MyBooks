package pl.sokolak87.MyBooks.model.publisher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepo extends JpaRepository<Publisher, Long> {
    List<Publisher> findAllByOrderByNameAsc();
    List<Publisher> findAllByNameContaining(String phrase);
}
