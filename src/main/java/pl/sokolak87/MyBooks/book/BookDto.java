package pl.sokolak87.MyBooks.book;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String subtitle;
    private String city;
    private Integer year;
    private String authors;
    //private List<String> authors = new ArrayList<>();
}
