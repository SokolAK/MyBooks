package pl.sokolak.MyBooks.model.publisher;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepo publisherRepo;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepo publisherRepo, PublisherMapper publisherMapper) {
        this.publisherRepo = publisherRepo;
        this.publisherMapper = publisherMapper;
    }

    public long count() {
        return publisherRepo.count();
    }

    public List<PublisherDto> findAll() {
        Pageable pageable = null;
        return findAll(pageable);
    }

    public List<PublisherDto> findAll(Pageable pageable) {
        return findAll(pageable);
    }

    public List<PublisherDto> findAll(String stringFilter) {
        return findAll(stringFilter, null);
    }

    public List<PublisherDto> findAll(String stringFilter, Pageable pageable) {
        List<Publisher> publisherList;
        if (stringFilter == null || stringFilter.isEmpty()) {
            publisherList = publisherRepo.findAllByOrderByNameAsc(pageable);
        } else {
            publisherList = publisherRepo.findAllByNameContainingIgnoreCase(stringFilter, pageable);
        }

        return publisherList.stream()
                .map(publisherMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PublisherDto> findById(Long id) {
        Optional<Publisher> publisher = publisherRepo.findById(id);
        return publisher.map(publisherMapper::toDto);
    }

    public Optional<PublisherDto> find(PublisherDto publisherDto) {
        Publisher publisherEntity = publisherMapper.toEntity(publisherDto);
        Optional<Publisher> publisher = publisherRepo.findOne(Example.of(publisherEntity));
        return publisher.map(publisherMapper::toDto);
    }

    public PublisherDto save(PublisherDto publisherDto) {
        Publisher publisher = publisherMapper.toEntity(publisherDto);
        Optional<Publisher> existingPublisher = publisherRepo.findOne(Example.of(publisher));
        return publisherMapper.toDto(existingPublisher.orElseGet(() -> publisherRepo.save(publisher)));
    }

    @Transactional
    public void delete(PublisherDto publisherDto) {
        Optional<Publisher> publisher = publisherRepo.findById(publisherDto.getId());
        publisher.ifPresent(p -> {
            p.getBooks().forEach(b -> b.getPublishers().remove(p));
            p.getBooks().clear();
            publisherRepo.delete(p);
        });
    }

    @Transactional
    public void deleteOrphan(PublisherDto publisherDto) {
        Optional<Publisher> publisher = publisherRepo.findById(publisherDto.getId());
        publisher.ifPresent(p -> {
            if (p.getBooks().size() == 0) {
                publisherRepo.delete(p);
            }
        });
    }
}
