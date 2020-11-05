package pl.sokolak87.MyBooks.model.publisher;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PublisherDto {
    private Long id;
    private String name = "";
    private Set<Long> booksIds = new HashSet<>();
}
