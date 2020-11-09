package pl.sokolak.MyBooks.model.author;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak.MyBooks.model.book.Book;
import pl.sokolak.MyBooks.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Author extends AbstractEntity {
    @Column(columnDefinition = "varchar(31) default ''")
    private String prefix = "";
    @Column(columnDefinition = "varchar(31) default ''")
    private String firstName = "";
    @Column(columnDefinition = "varchar(31) default ''")
    private String middleName = "";
    @NotNull
    @Column(columnDefinition = "varchar(63) default ''")
    private String lastName = "";

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private Set<Book> books = new HashSet<>();
}
