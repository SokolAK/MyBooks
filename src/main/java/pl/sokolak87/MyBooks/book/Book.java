package pl.sokolak87.MyBooks.book;

import lombok.Data;
import pl.sokolak87.MyBooks.author.Author;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(512) default ''")
    private String title;
    @Column(columnDefinition = "varchar(512) default ''")
    private String subtitle;
    @Column(columnDefinition = "varchar(30) default ''")
    private String city;
    @Column(columnDefinition = "varchar(15) default ''")
    private String year;
    @Column(columnDefinition = "varchar(5) default ''")
    private String volume;
    @Column(columnDefinition = "varchar(255) default ''")
    private String edition;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

/*    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_publisher",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id")
    )
    private List<Publisher> publishers = new ArrayList<>();*/
}
