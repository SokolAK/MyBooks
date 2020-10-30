package pl.sokolak87.MyBooks.model.author;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak87.MyBooks.model.AbstractEntity;
import pl.sokolak87.MyBooks.model.book.Book;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Author extends AbstractEntity {

    @Column(columnDefinition = "varchar(20) default ''")
    private String prefix = "";
    @Column(columnDefinition = "varchar(20) default ''")
    private String firstName = "";
    @Column(columnDefinition = "varchar(20) default ''")
    private String middleName = "";
    @NotNull
    @Column(columnDefinition = "varchar(50) default ''")
    private String lastName = "";

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

/*    public void addBook(Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }*/

    public void removeBook(Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
    }
}
