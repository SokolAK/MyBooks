package pl.sokolak87.MyBooks.author;

import lombok.*;
import pl.sokolak87.MyBooks.book.Book;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sufix;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if(!prefix.isBlank()) {
            stringBuilder.append(getPrefix());
            stringBuilder.append(" ");
        }

        stringBuilder.append(lastName);

        if(!firstName.isBlank()) {
            stringBuilder.append(" ");
            stringBuilder.append(firstName);
        }

        if(!middleName.isBlank()) {
            stringBuilder.append(" ");
            stringBuilder.append(middleName);
        }

        if(!sufix.isBlank()) {
            stringBuilder.append(" ");
            stringBuilder.append(getPrefix());
        }
        return stringBuilder.toString();
    }
}
