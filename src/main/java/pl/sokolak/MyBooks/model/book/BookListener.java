package pl.sokolak.MyBooks.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.author.AuthorMapper;
import pl.sokolak.MyBooks.model.author.AuthorService;

import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Service
public class BookListener {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorMapper authorMapper;

    @PreUpdate
    @PreRemove
    public void process(Book book) {
        System.out.println(book.getAuthors());
        book.getAuthors().stream()
                .peek(a -> System.out.println(a.getBooks().size()))
                .filter(a -> a.getBooks().size() == 0)
                .forEach(a -> authorService.delete(authorMapper.toDto(a)));
    }
}