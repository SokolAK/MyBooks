package pl.sokolak.MyBooks.model.series;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepo extends JpaRepository<Series, Long> {
    List<Series> findAllByOrderByNameAsc(Pageable pageable);
    List<Series> findAllByNameContainingIgnoreCase(String phrase, Pageable pageable);
}
