package pl.sokolak87.MyBooks.model.publisher;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak87.MyBooks.model.AbstractEntity;
import pl.sokolak87.MyBooks.model.book.Book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Publisher extends AbstractEntity {
    @Column(columnDefinition = "varchar(127) default ''")
    private String name = "";

    @ManyToMany(mappedBy = "publishers", fetch = FetchType.EAGER)
    private Set<Book> books = new HashSet<>();
}
