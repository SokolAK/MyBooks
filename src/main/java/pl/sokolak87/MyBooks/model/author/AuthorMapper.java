package pl.sokolak87.MyBooks.model.author;

import org.springframework.stereotype.Service;

@Service
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setPrefix(author.getPrefix());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setMiddleName(author.getMiddleName());
        authorDto.setLastName(author.getLastName());
        return authorDto;
    }

    public Author toEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setPrefix(authorDto.getPrefix());
        author.setFirstName(authorDto.getFirstName());
        author.setMiddleName(authorDto.getMiddleName());
        author.setLastName(authorDto.getLastName());
        return author;
    }
}
