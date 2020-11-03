package pl.sokolak87.MyBooks.model.author;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepo authorRepo, AuthorMapper authorMapper) {
        this.authorRepo = authorRepo;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDto> findAll() {
        return findAll(null);
    }

    public List<AuthorDto> findAll(String stringFilter) {
        List<Author> authorList;
        if (stringFilter == null || stringFilter.isEmpty()) {
            authorList = authorRepo.findAllByOrderByLastNameAscFirstNameAscMiddleNameAscPrefixAsc();
        } else {
            authorList = authorRepo.findAllContainingPhrase(stringFilter);
        }

        return authorList.stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> findAll(String prefix, String firstName, String middleName, String lastName) {
        return authorRepo.findAllContainingPhrases(prefix, firstName, middleName, lastName).stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDto> findById(Long id) {
        Optional<Author> author = authorRepo.findById(id);
        return author.map(authorMapper::toDto);
    }

    public Optional<AuthorDto> find(AuthorDto authorDto) {
        Author authorEntity = authorMapper.toEntity(authorDto);
        Optional<Author> author = authorRepo.findOne(Example.of(authorEntity));
        return author.map(authorMapper::toDto);
    }

    public AuthorDto save(AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        Optional<Author> existingAuthor = authorRepo.findOne(Example.of(author));
        return authorMapper.toDto(existingAuthor.orElseGet(() -> authorRepo.save(author)));
    }

    @Transactional
    public void delete(AuthorDto authorDto) {
        Optional<Author> author = authorRepo.findById(authorDto.getId());
        author.ifPresent(a -> {
            a.getBooks().forEach(b -> b.getAuthors().remove(a));
            a.getBooks().clear();
            authorRepo.delete(a);
        });
    }

    @Transactional
    public void deleteOrphan(AuthorDto authorDto) {
        Optional<Author> author = authorRepo.findById(authorDto.getId());
        author.ifPresent(a -> {
            if (a.getBooks().size() == 0) {
                authorRepo.delete(a);
            }
        });
    }
}
