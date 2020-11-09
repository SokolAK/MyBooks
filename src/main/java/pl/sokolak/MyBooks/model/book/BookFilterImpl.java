package pl.sokolak.MyBooks.model.book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static pl.sokolak.MyBooks.utils.StringUtil.convertPhraseToSubPhrases;

public class BookFilterImpl implements BookFilter {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<Book> cq;
    private Root<Book> book;

/*    private void init() {
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(Book.class);
        book = cq.from(Book.class);
    }*/

    //Awful but works
    @Override
    public List<Book> findAllContainingPhrase(String phrase, Map<String, Boolean> columnList) {
        Set<String> subPhrases = convertPhraseToSubPhrases(phrase);

        StringJoiner columns = new StringJoiner(", ';' ,");
        if(columnList.getOrDefault("title",false)) {
            columns.add("LOWER(b.title)");
        }
        if(columnList.getOrDefault("subtitle",false)) {
            columns.add("LOWER(b.subtitle)");
        }
        if(columnList.getOrDefault("authors",false)) {
            columns.add("LOWER(STRING_AGG(CONCAT(a.firstName,a.middleName,a.lastName,a.prefix), ';'))");
        }
        if(columnList.getOrDefault("publishers",false)) {
            columns.add("LOWER(STRING_AGG(CONCAT(p.name), ';'))");
        }
        if(columnList.getOrDefault("year",false)) {
            columns.add("b.year");
        }
        if(columnList.getOrDefault("volume",false)) {
            columns.add("b.volume");
        }
        if(columnList.getOrDefault("edition",false)) {
            columns.add("LOWER(b.edition)");
        }

        String row = "CONCAT(" + columns + ")";
        String conditions = subPhrases.stream()
                .map(s -> row + " LIKE '%" + s + "%'")
                .collect(Collectors.joining(" AND "));

        String subSql = "SELECT b.id FROM Book b " +
                "LEFT JOIN b.authors a " +
                "LEFT JOIN b.publishers p " +
                "GROUP BY b.id " +
                "HAVING " +
                conditions;
        String sql = "SELECT b FROM Book b " +
                "WHERE b.id IN (" + subSql + ")" +
                "";

        TypedQuery<Book> query = em.createQuery(sql,Book.class);
        return query.getResultList();
    }

/*
    //Multi author problem
    public List<Book> findAllContainingPhraseOld(String phrase, Map<String, Boolean> columnList) {
        init();
        Subquery<Long> sq = getIdsOfBooksContainingPhrase(phrase, columnList);
        cq.where(cb.in(book.get("id")).value(sq)).distinct(true);
        TypedQuery<Book> query = em.createQuery(cq);
        return query.getResultList();
    }

    //Multi author problem
    private Subquery<Long> getIdsOfBooksContainingPhrase(String phrase, Map<String, Boolean> columnList) {
        Set<String> subPhrases = convertPhraseToSubPhrases(phrase);

        Join<Book, Author> bookAuthors = book.join(
                "authors",
                JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        for (String subPhrase : subPhrases) {
            List<Predicate> subPredicates = new ArrayList<>();
            Stream.of("title", "subtitle", "year", "volume", "edition")
                    .filter(columnList::containsKey)
                    .filter(columnList::get)
                    .forEach(columnName -> subPredicates.add(columnTextContainsPhrase(columnName, subPhrase, book))
                    );

            if (columnList.get("authors")) {
                Stream.of("prefix", "firstName", "middleName", "lastName")
                        .forEach(columnName -> subPredicates.add(columnTextContainsPhrase(columnName, subPhrase, bookAuthors)));
            }
            Predicate predicate = cb.or(subPredicates.toArray(Predicate[]::new));
            predicates.add(predicate);
        }
        Predicate finalPredicate = cb.and(predicates.toArray(Predicate[]::new));
        Subquery<Long> sq = cq.subquery(Long.class);
        sq.select(book.get("id"))
                .where(finalPredicate)
                .from(Book.class);
        return sq;
    }

    private Predicate columnTextContainsPhrase(String columnName, String phrase, Path<?> path) {
        Expression<String> columnText = cb.lower(path.get(columnName).as(String.class));
        return cb.like(columnText, "%" + phrase.toLowerCase() + "%");
    }*/
}