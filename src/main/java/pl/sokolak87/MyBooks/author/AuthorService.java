package pl.sokolak87.MyBooks.author;

import org.springframework.stereotype.Service;

import java.util.List;
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
            return authorRepo.search(stringFilter).stream()
                    .map(AuthorMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
