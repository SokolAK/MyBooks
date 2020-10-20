package pl.sokolak87.MyBooks.model.book;

import lombok.Data;
import pl.sokolak87.MyBooks.model.author.AuthorDto;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String subtitle;
    private String city;
    private String year;
    private String volume;
    private String edition;
    private Set<AuthorDto> authors = new LinkedHashSet<>();
}
