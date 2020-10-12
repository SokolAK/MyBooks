package pl.sokolak87.MyBooks.author;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorDto {
    private Long id;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sufix;
    private List<String> books = new ArrayList<>();
}
