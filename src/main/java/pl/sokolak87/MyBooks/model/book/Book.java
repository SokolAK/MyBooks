package pl.sokolak87.MyBooks.model.book;

import lombok.Data;
import pl.sokolak87.MyBooks.model.author.Author;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private Set<Author> authors = new LinkedHashSet<>();

/*    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_publisher",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id")
    )
    private List<Publisher> publishers = new ArrayList<>();*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
