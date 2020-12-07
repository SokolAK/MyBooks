package pl.sokolak.MyBooks.model.book;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak.MyBooks.model.Dto;
import pl.sokolak.MyBooks.model.author.AuthorDto;
import pl.sokolak.MyBooks.model.publisher.PublisherDto;
import pl.sokolak.MyBooks.model.series.SeriesDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class BookDto implements Dto {
    private Long id;
    @NotNull
    @NotEmpty
    private String title = "";
    private String subtitle = "";
    private String city = "";
    private String year = "";
    private String volume = "";
    private String edition = "";
    private String seriesVolume = "";
    private String comment = "";
    //private Set<AuthorDto> authors = new LinkedHashSet<>();
    private List<AuthorDto> authors = new ArrayList<>();
    private List<PublisherDto> publishers = new ArrayList<>();
    private SeriesDto series;

    public static BookDto copy(BookDto original) {
        BookDto copy = new BookDto();
        copy.setId(original.getId());
        copy.setTitle(original.getTitle());
        copy.setSubtitle(original.getSubtitle());
        copy.setYear(original.getYear());
        copy.setCity(original.getCity());
        copy.setVolume(original.getVolume());
        copy.setEdition(original.getEdition());
        copy.setSeriesVolume(original.getSeriesVolume());
        copy.setComment(original.getComment());
        copy.setAuthors(new ArrayList<>());
        original.getAuthors().forEach(oa -> copy.getAuthors().add(AuthorDto.copy(oa)));
        original.getPublishers().forEach(op -> copy.getPublishers().add(PublisherDto.copy(op)));
        Optional<SeriesDto> originalSeries = Optional.ofNullable(original.getSeries());
        originalSeries.ifPresent(s -> copy.setSeries(SeriesDto.copy(s)));
        return copy;
    }

    @Override
    public String toString() {
        List<String> items = new ArrayList<>();
        items.add(id.toString());
        items.add(title);
        items.add(subtitle);
        return String.join(" # ", items);
    }
}
