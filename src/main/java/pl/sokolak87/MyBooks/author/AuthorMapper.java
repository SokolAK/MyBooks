package pl.sokolak87.MyBooks.author;


public class AuthorMapper {

    static public AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();

        authorDto.setId(author.getId());
        authorDto.setPrefix(author.getPrefix());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setMiddleName(author.getMiddleName());
        authorDto.setLastName(author.getLastName());
        authorDto.setSuffix(author.getSuffix());
        return authorDto;
    }
}
