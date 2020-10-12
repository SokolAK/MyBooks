package pl.sokolak87.MyBooks.book;

import pl.sokolak87.MyBooks.author.Author;

import java.util.stream.Collectors;

public class BookMapper {

    static public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setSubTitle(book.getSubTitle());
        bookDto.setYear(book.getYear());
/*        bookDto.setAuthors(
                book.getAuthors().stream()
                .map(Author::toString)
                .collect(Collectors.toList())
        );*/
        bookDto.setAuthors(book.getAuthors().stream()
                .map(Author::toString)
                .collect(Collectors.joining("; ")));

        return bookDto;
    }
}
