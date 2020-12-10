package pl.sokolak.MyBooks.model.series;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    private final SeriesRepo seriesRepo;
    private final SeriesMapper seriesMapper;

    public SeriesService(SeriesRepo seriesRepo, SeriesMapper seriesMapper) {
        this.seriesRepo = seriesRepo;
        this.seriesMapper = seriesMapper;
    }

    public long count() {
        return seriesRepo.count();
    }

    public List<SeriesDto> findAll() {
        Pageable pageable = null;
        return findAll(pageable);
    }

    public List<SeriesDto> findAll(Pageable pageable) {
        return findAll(pageable);
    }

    public List<SeriesDto> findAll(String stringFilter) {
        return findAll(stringFilter, null);
    }

    public List<SeriesDto> findAll(String stringFilter, Pageable pageable) {
        List<Series> seriesList;
        if (stringFilter == null || stringFilter.isEmpty()) {
            seriesList = seriesRepo.findAllByOrderByNameAsc(pageable);
        } else {
            seriesList = seriesRepo.findAllByNameContainingIgnoreCase(stringFilter, pageable);
        }

        return seriesList.stream()
                .map(seriesMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<SeriesDto> findById(Long id) {
        Optional<Series> series = seriesRepo.findById(id);
        return series.map(seriesMapper::toDto);
    }

    public Optional<SeriesDto> find(SeriesDto seriesDto) {
        Series seriesEntity = seriesMapper.toEntity(seriesDto);
        Optional<Series> series = seriesRepo.findOne(Example.of(seriesEntity));
        return series.map(seriesMapper::toDto);
    }

    public SeriesDto save(SeriesDto seriesDto) {
        Series series = seriesMapper.toEntity(seriesDto);
        Optional<Series> existingSeries = seriesRepo.findOne(Example.of(series));
        return seriesMapper.toDto(existingSeries.orElseGet(() -> seriesRepo.save(series)));
    }

    @Transactional
    public void delete(SeriesDto seriesDto) {
        Optional<Series> series = seriesRepo.findById(seriesDto.getId());
        series.ifPresent(s -> {
            s.getBooks().forEach(b -> b.setSeries(null));
            s.getBooks().clear();
            seriesRepo.delete(s);
        });
    }

    @Transactional
    public void deleteOrphan(SeriesDto seriesDto) {
        Optional<Series> series = seriesRepo.findById(seriesDto.getId());
        series.ifPresent(s -> {
            if (s.getBooks().size() == 0) {
                seriesRepo.delete(s);
            }
        });
    }
}