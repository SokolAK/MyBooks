package pl.sokolak.MyBooks.model.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.AbstractEntity;
import pl.sokolak.MyBooks.model.book.BookMapper;
import pl.sokolak.MyBooks.model.book.BookRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class SeriesMapper {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BookMapper bookMapper;

    public SeriesDto toDto(Series series) {
        SeriesDto seriesDto = new SeriesDto();
        seriesDto.setId(series.getId());
        seriesDto.setName(series.getName());

        series.getBooks().stream()
                .map(AbstractEntity::getId)
                .forEach(id -> seriesDto.getBooksIds().add(id));

        return seriesDto;
    }

    public Series toEntity(SeriesDto seriesDto) {
        Series series = new Series();
        series.setId(seriesDto.getId());
        series.setName(seriesDto.getName());

        seriesDto.getBooksIds().stream()
                .filter(Objects::nonNull)
                .map(id -> bookRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(b -> series.getBooks().add(b));
        return series;
    }
}
