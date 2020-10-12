package pl.sokolak87.MyBooks.author;

import lombok.Data;

@Data
public class AuthorDto {
    private Long id;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
}
