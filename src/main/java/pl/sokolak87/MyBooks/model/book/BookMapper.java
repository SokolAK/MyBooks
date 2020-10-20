package pl.sokolak87.MyBooks.model.book;

import pl.sokolak87.MyBooks.model.author.AuthorDto;
import pl.sokolak87.MyBooks.model.author.AuthorMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper {

    static public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setSubtitle(book.getSubtitle());
        bookDto.setYear(book.getYear());
        bookDto.setCity(book.getCity());
        bookDto.setVolume(book.getVolume());
        bookDto.setEdition(book.getEdition());

        Set<AuthorDto> authors = book.getAuthors().stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toSet());
        bookDto.setAuthors(authors);

        return bookDto;
    }
}
