package pl.sokolak.MyBooks.utils;

import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.book.BookDto;
import pl.sokolak.MyBooks.model.book.BookService;

import java.io.*;
import java.util.List;

@Service
public class DataExporter {
    private static final String PATH = "src/main/webapp/vaadin/books_data.exp";

    private BookService bookService;

    public DataExporter(BookService bookService) {
        this.bookService = bookService;
    }

    public void exportData() {
        List<BookDto> books = bookService.findAll();
       // String str = books.stream().map(BookDto::toString).collect(Collectors.joining("\n"));

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(PATH);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            books.forEach(b -> printWriter.println(b.toString()));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Path path = Paths.get(PATH);
//        byte[] strToBytes = str.getBytes();
//        try {
//            Files.write(path, strToBytes);
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//        }

    }

    public InputStream exportFile() {
        exportData();
        File initialFile = new File(PATH);
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(initialFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return targetStream;
    }
}
