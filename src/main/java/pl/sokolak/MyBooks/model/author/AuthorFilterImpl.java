package pl.sokolak.MyBooks.model.author;

import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.sokolak.MyBooks.utils.StringUtils.convertPhraseToSubPhrases;

public class AuthorFilterImpl implements AuthorFilter {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<Author> cq;
    private Root<Author> author;

    private void init() {
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(Author.class);
        author = cq.from(Author.class);
    }

    @Override
    public List<Author> findAllContainingPhrase(String phrase, Pageable pageable) {
        init();
        Set<String> subPhrases = convertPhraseToSubPhrases(phrase);

        List<Predicate> predicates = new ArrayList<>();
        for (String subPhrase : subPhrases) {
            List<Predicate> subPredicates = new ArrayList<>();
            Stream.of("prefix", "firstName", "middleName", "lastName")
                    .forEach(columnName -> subPredicates.add(columnTextContainsPhrase(columnName, subPhrase, author)));
            Predicate predicate = cb.or(subPredicates.toArray(Predicate[]::new));
            predicates.add(predicate);
        }
        Predicate finalPredicate = cb.and(predicates.toArray(Predicate[]::new));

        buildQuery(finalPredicate);
        TypedQuery<Author> query = em.createQuery(cq);
        if(pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();
    }

    @Override
    public List<Author> findAllContainingPhrases(String prefix, String firstName, String middleName, String lastName, Pageable pageable) {
        init();
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(columnTextContainsPhrase("prefix", prefix, author));
        predicates.add(columnTextContainsPhrase("firstName", firstName, author));
        predicates.add(columnTextContainsPhrase("middleName", middleName, author));
        predicates.add(columnTextContainsPhrase("lastName", lastName, author));
        Predicate finalPredicate = cb.and(predicates.toArray(Predicate[]::new));

        buildQuery(finalPredicate);
        TypedQuery<Author> query = em.createQuery(cq);
        if(pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();
    }

    private void buildQuery(Predicate predicate) {
        cq.select(author)
                .where(predicate)
                .distinct(true)
                .orderBy(getOrderList("lastName", "firstName", "middleName", "prefix"))
                .from(Author.class);
    }

    private Predicate columnTextContainsPhrase(String columnName, String phrase, Path<?> path) {
        Expression<String> columnText = cb.lower(path.get(columnName).as(String.class));
        return cb.like(columnText, "%" + phrase.toLowerCase() + "%");
    }

    private List<Order> getOrderList(String... fields) {
        return Arrays.stream(fields)
                .map(f -> cb.asc(author.get(f)))
                .collect(Collectors.toList());
    }
}
