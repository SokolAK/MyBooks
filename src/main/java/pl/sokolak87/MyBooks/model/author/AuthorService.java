package pl.sokolak87.MyBooks.model.author;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;

    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<AuthorDto> findAll() {
        return authorRepo.findAll().stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return findAll();
        } else {
            return authorRepo.findAllContainingPhrase(stringFilter).stream()
                    .map(AuthorMapper::toDto)
                    .collect(Collectors.toList());
        }
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
    public AuthorDto save(AuthorDto authorDto) {
        //Optional<Author> author = authorRepo.findById(authorDto.getId());
        Author savedAuthor = authorRepo.save(AuthorMapper.toEntity(authorDto));
        return AuthorMapper.toDto(savedAuthor);
    }
}
