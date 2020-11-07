package pl.sokolak87.MyBooks.model.publisher;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak87.MyBooks.model.Dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class PublisherDto implements Dto {
    private Long id;
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
}
