package pl.sokolak87.MyBooks.model.book;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sokolak87.MyBooks.model.AbstractEntity;
import pl.sokolak87.MyBooks.model.author.Author;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Book extends AbstractEntity {

    @Column(columnDefinition = "varchar(512) default ''")
    private String title = "";
    @Column(columnDefinition = "varchar(512) default ''")
    private String subtitle = "";
    @Column(columnDefinition = "varchar(30) default ''")
    private String city = "";
    @Column(columnDefinition = "varchar(15) default ''")
    private String year = "";
    @Column(columnDefinition = "varchar(5) default ''")
    private String volume = "";
    @Column(columnDefinition = "varchar(255) default ''")
    private String edition = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_author")
    @OrderColumn
    private List<Author> authors = new ArrayList<>();

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

/*    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
    }*/

/*    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_publisher",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id")
    )
    private List<Publisher> publishers = new ArrayList<>();*/
}
