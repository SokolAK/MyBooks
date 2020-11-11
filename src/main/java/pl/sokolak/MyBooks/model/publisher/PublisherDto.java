package pl.sokolak.MyBooks.model.publisher;

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
public class PublisherDto implements Dto {
    private Long id;
    @NotNull
    @NotEmpty
    private String name = "";
    private Set<Long> booksIds = new HashSet<>();

    @Override
    public String toString() {
        return name;
    }

    public static String publishersSetToString(Collection<PublisherDto> set) {
        return set.stream()
                .map(PublisherDto::toString)
                .collect(Collectors.joining("; "));
    }

    public static PublisherDto copy(PublisherDto original) {
        PublisherDto copy = new PublisherDto();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setBooksIds(new HashSet<>(original.getBooksIds()));
        return copy;
    }
}
