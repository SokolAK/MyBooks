package pl.sokolak87.MyBooks.model.book;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.sokolak87.MyBooks.model.AbstractEntity;
import pl.sokolak87.MyBooks.model.author.Author;
import pl.sokolak87.MyBooks.model.publisher.Publisher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Book extends AbstractEntity {
    @Column(columnDefinition = "varchar(255) default ''")
    private String title = "";
    @Column(columnDefinition = "varchar(511) default ''")
    private String subtitle = "";
    @Column(columnDefinition = "varchar(31) default ''")
    private String city = "";
    @Column(columnDefinition = "varchar(15) default ''")
    private String year = "";
    @Column(columnDefinition = "varchar(7) default ''")
    private String volume = "";
    @Column(columnDefinition = "varchar(127) default ''")
    private String edition = "";
    @Column(columnDefinition = "varchar(511) default ''")
    private String comment = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_author")
    @OrderColumn
    private List<Author> authors = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_publisher")
    @OrderColumn
    private List<Publisher> publishers = new ArrayList<>();
}
