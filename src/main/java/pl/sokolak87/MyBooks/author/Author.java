package pl.sokolak87.MyBooks.author;

import lombok.Data;
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

    @Column(columnDefinition = "varchar(20) default ''")
    private String prefix;
    @Column(columnDefinition = "varchar(20) default ''")
    private String firstName;
    @Column(columnDefinition = "varchar(20) default ''")
    private String middleName;
    @Column(columnDefinition = "varchar(50) default ''")
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
}
