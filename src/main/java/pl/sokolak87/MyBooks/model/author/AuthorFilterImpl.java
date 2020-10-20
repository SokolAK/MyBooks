package pl.sokolak87.MyBooks.model.author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static pl.sokolak87.MyBooks.utils.StringUtil.convertPhraseToSubPhrases;

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
    public List<Author> findAllContainingPhrase(String phrase) {
        init();
        Set<String> subPhrases = convertPhraseToSubPhrases(phrase);

        List<Predicate> predicates = new ArrayList<>();
        for (String subPhrase : subPhrases) {
            List<Predicate> subPredicates = new ArrayList<>();
            Stream.of("prefix", "firstName", "middleName", "lastName")
                    .forEach(columnName -> subPredicates.add(columnTextContainsPhrase(columnName, subPhrase, author)));
            Predicate predicate = cb.or(subPredicates.toArray(new Predicate[0]));
            predicates.add(predicate);
        }
        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
        cq.select(author)
                .where(finalPredicate)
                .distinct(true)
                .from(Author.class);
        TypedQuery<Author> query = em.createQuery(cq);
        return query.getResultList();
    }

    private Predicate columnTextContainsPhrase(String columnName, String phrase, Path<?> path) {
        Expression<String> columnText = cb.lower(path.get(columnName).as(String.class));
        return cb.like(columnText, phrase);
    }
}
