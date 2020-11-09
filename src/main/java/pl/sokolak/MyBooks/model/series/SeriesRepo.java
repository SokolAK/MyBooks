package pl.sokolak.MyBooks.model.series;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepo extends JpaRepository<Series, Long> {
    List<Series> findAllByOrderByNameAsc();
    List<Series> findAllByNameContaining(String phrase);
}
