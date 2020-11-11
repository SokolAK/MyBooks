package pl.sokolak.MyBooks.model.series;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak.MyBooks.model.Dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class SeriesDto implements Dto {
    private Long id;
    @NotNull
    @NotEmpty
    private String name = "";
    private Set<Long> booksIds = new HashSet<>();

    @Override
    public String toString() {
        return name;
    }

    public static String seriesSetToString(Collection<SeriesDto> set) {
        return set.stream()
                .map(SeriesDto::toString)
                .collect(Collectors.joining("; "));
    }

    public static SeriesDto copy(SeriesDto original) {
        SeriesDto copy = new SeriesDto();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setBooksIds(new HashSet<>(original.getBooksIds()));
        return copy;
    }
}
