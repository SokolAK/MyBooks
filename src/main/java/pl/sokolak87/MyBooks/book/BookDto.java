package pl.sokolak87.MyBooks.book;

import lombok.Data;
import pl.sokolak87.MyBooks.author.AuthorDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String subtitle;
    private String city;
    private String year;
    private String volume;
    private String edition;
    private List<AuthorDto> authors = new ArrayList<>();
}
