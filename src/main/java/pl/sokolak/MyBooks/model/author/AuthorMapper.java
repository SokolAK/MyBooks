package pl.sokolak.MyBooks.model.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.AbstractEntity;
import pl.sokolak.MyBooks.model.book.BookMapper;
import pl.sokolak.MyBooks.model.book.BookRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorMapper {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BookMapper bookMapper;

    public AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setPrefix(author.getPrefix());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setMiddleName(author.getMiddleName());
        authorDto.setLastName(author.getLastName());

/*        author.getBooks().stream()
                .map(AbstractEntity::getId)
                .map(id -> bookRepo.findById(id))
                .flatMap(Optional::stream)
                .map(bookMapper::toDto)
                .forEach(authorDto.getBooksIds());*/
        author.getBooks().stream()
                .map(AbstractEntity::getId)
                .forEach(id -> authorDto.getBooksIds().add(id));

        return authorDto;
    }

    public Author toEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setPrefix(authorDto.getPrefix());
        author.setFirstName(authorDto.getFirstName());
        author.setMiddleName(authorDto.getMiddleName());
        author.setLastName(authorDto.getLastName());

        authorDto.getBooksIds().stream()
                .filter(Objects::nonNull)
                .map(id -> bookRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(b -> author.getBooks().add(b));

        return author;
    }
}
