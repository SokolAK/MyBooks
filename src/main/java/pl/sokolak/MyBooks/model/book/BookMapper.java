package pl.sokolak.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.AbstractEntity;
import pl.sokolak.MyBooks.model.author.AuthorDto;
import pl.sokolak.MyBooks.model.author.AuthorMapper;
import pl.sokolak.MyBooks.model.author.AuthorRepo;
import pl.sokolak.MyBooks.model.publisher.PublisherDto;
import pl.sokolak.MyBooks.model.publisher.PublisherMapper;
import pl.sokolak.MyBooks.model.publisher.PublisherRepo;
import pl.sokolak.MyBooks.model.series.Series;
import pl.sokolak.MyBooks.model.series.SeriesDto;
import pl.sokolak.MyBooks.model.series.SeriesMapper;
import pl.sokolak.MyBooks.model.series.SeriesRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookMapper {

    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private PublisherRepo publisherRepo;
    @Autowired
    private PublisherMapper publisherMapper;
    @Autowired
    private SeriesRepo seriesRepo;
    @Autowired
    private SeriesMapper seriesMapper;

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setSubtitle(book.getSubtitle());
        bookDto.setYear(book.getYear());
        bookDto.setCity(book.getCity());
        bookDto.setVolume(book.getVolume());
        bookDto.setEdition(book.getEdition());
        bookDto.setSeriesVolume(book.getSeriesVolume());
        bookDto.setComment(book.getComment());

        book.getAuthors().stream()
                .map(AbstractEntity::getId)
                .map(id -> authorRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(a -> bookDto.getAuthors().add(authorMapper.toDto(a)));

        book.getPublishers().stream()
                .map(AbstractEntity::getId)
                .map(id -> publisherRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(p -> bookDto.getPublishers().add(publisherMapper.toDto(p)));

        Optional<Series> series = Optional.ofNullable(book.getSeries());
        Optional<Long> idSeries = series.map(Series::getId);
        idSeries.ifPresent(id -> {
            Optional<Series> existingSeries = seriesRepo.findById(id);
            existingSeries.ifPresent(s -> bookDto.setSeries(seriesMapper.toDto(s)));
        });

        return bookDto;
    }

    public Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setSubtitle(bookDto.getSubtitle());
        book.setYear(bookDto.getYear());
        book.setCity(bookDto.getCity());
        book.setVolume(bookDto.getVolume());
        book.setEdition(bookDto.getEdition());
        book.setSeriesVolume(bookDto.getSeriesVolume());
        book.setComment(bookDto.getComment());

        //saved authors
        bookDto.getAuthors().stream()
                .map(AuthorDto::getId)
                .filter(Objects::nonNull)
                .map(id -> authorRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(a -> book.getAuthors().add(a));
        //new authors
        bookDto.getAuthors().stream()
                .filter(a -> a.getId() == null)
                .map(a -> authorMapper.toEntity(a))
                .map(a -> authorRepo.save(a))
                .forEach(a -> book.getAuthors().add(a));

        //saved publishers
        bookDto.getPublishers().stream()
                .map(PublisherDto::getId)
                .filter(Objects::nonNull)
                .map(id -> publisherRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(p -> book.getPublishers().add(p));
        //new publishers
        bookDto.getPublishers().stream()
                .filter(p -> p.getId() == null)
                .map(p -> publisherMapper.toEntity(p))
                .map(p -> publisherRepo.save(p))
                .forEach(p -> book.getPublishers().add(p));

        Optional<SeriesDto> seriesDto = Optional.ofNullable(bookDto.getSeries());
        seriesDto.ifPresent(sDto -> {
                    Optional<Long> idSeries = Optional.ofNullable(sDto.getId());
                    idSeries.flatMap(id -> seriesRepo.findById(id)).ifPresentOrElse(
                            book::setSeries,
                            () -> {
                                Series series = seriesMapper.toEntity(sDto);
                                Series savedSeries = seriesRepo.save(series);
                                book.setSeries(savedSeries);
                            });
                }
        );
        return book;
    }
}
