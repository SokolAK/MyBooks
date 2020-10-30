package pl.sokolak87.MyBooks.model.book;

import lombok.Data;
import pl.sokolak87.MyBooks.model.Dto;
import pl.sokolak87.MyBooks.model.author.AuthorDto;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class BookDto implements Dto {
    private Long id;
    private String title = "";
    private String subtitle = "";
    private String city = "";
    private String year = "";
    private String volume = "";
    private String edition = "";
    private Set<AuthorDto> authors = new LinkedHashSet<>();

    public static BookDto copy(BookDto original) {
        BookDto copy = new BookDto();
        copy.setId(original.getId());
        copy.setTitle(original.getTitle());
        copy.setSubtitle(original.getSubtitle());
        copy.setYear(original.getYear());
        copy.setCity(original.getCity());
        copy.setVolume(original.getVolume());
        copy.setEdition(original.getEdition());
        copy.setAuthors(new LinkedHashSet<>());
        original.getAuthors().forEach(oa -> copy.getAuthors().add(AuthorDto.copy(oa)));
        return copy;
    }
}
