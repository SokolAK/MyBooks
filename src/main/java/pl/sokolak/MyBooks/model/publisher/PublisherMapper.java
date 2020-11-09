package pl.sokolak.MyBooks.model.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokolak.MyBooks.model.AbstractEntity;
import pl.sokolak.MyBooks.model.book.BookMapper;
import pl.sokolak.MyBooks.model.book.BookRepo;

import java.util.Objects;
import java.util.Optional;

@Service
public class PublisherMapper {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BookMapper bookMapper;

    public PublisherDto toDto(Publisher publisher) {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisher.getId());
        publisherDto.setName(publisher.getName());

        publisher.getBooks().stream()
                .map(AbstractEntity::getId)
                .forEach(id ->  publisherDto.getBooksIds().add(id));

        return publisherDto;
    }

    public Publisher toEntity(PublisherDto publisherDto) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherDto.getId());
        publisher.setName(publisherDto.getName());

        publisherDto.getBooksIds().stream()
                .filter(Objects::nonNull)
                .map(id -> bookRepo.findById(id))
                .flatMap(Optional::stream)
                .forEach(b -> publisher.getBooks().add(b));
        return publisher;
    }
}
