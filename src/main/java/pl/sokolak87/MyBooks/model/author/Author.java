package pl.sokolak87.MyBooks.model.author;

import lombok.Data;
import pl.sokolak87.MyBooks.model.book.Book;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


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
    @NotNull
    @NotEmpty
    @Column(columnDefinition = "varchar(50) default ''")
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;

        Author author = (Author) o;

        return id.equals(author.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
