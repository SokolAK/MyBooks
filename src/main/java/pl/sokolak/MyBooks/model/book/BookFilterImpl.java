package pl.sokolak.MyBooks.model.book;

import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

import static pl.sokolak.MyBooks.utils.StringUtils.convertPhraseToSubPhrases;

public class BookFilterImpl implements BookFilter {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAllContainingPhrase(String phrase, Map<String, Boolean> columnList, Pageable pageable) {
        return findAllContainingPhraseSQL(phrase, columnList, pageable);
    }

    //Awful but works - NEW
    @SuppressWarnings("unchecked")
    private List<Book> findAllContainingPhraseSQL(String phrase, Map<String, Boolean> columnList, Pageable pageable) {
        Set<String> subPhrases = convertPhraseToSubPhrases(phrase);
        StringJoiner columns = new StringJoiner(", ';' ,");

        if (columnList.getOrDefault("title", false)) {
            columns.add("LOWER(b.title)");
        }
        if (columnList.getOrDefault("subtitle", false)) {
            columns.add("LOWER(b.subtitle)");
        }
        if (columnList.getOrDefault("seriesVolume", false)) {
            columns.add("LOWER(b.series_volume)");
        }
        if (columnList.getOrDefault("city", false)) {
            columns.add("LOWER(b.city)");
        }
        if (columnList.getOrDefault("year", false)) {
            columns.add("b.year");
        }
        if (columnList.getOrDefault("volume", false)) {
            columns.add("b.volume");
        }
        if (columnList.getOrDefault("edition", false)) {
            columns.add("LOWER(b.edition)");
        }
        if (columnList.getOrDefault("comment", false)) {
            columns.add("LOWER(b.comment)");
        }
        if (columnList.getOrDefault("authors", false)) {
            columns.add("LOWER(STRING_AGG(CONCAT(a.first_name, a.middle_name, a.last_name, a.prefix), ';'))");
        }
        if (columnList.getOrDefault("publishers", false)) {
            columns.add("LOWER(STRING_AGG(p.name, ';'))");
        }
        if (columnList.getOrDefault("series", false)) {
            columns.add("LOWER(STRING_AGG(s.name, ';'))");
        }

        String row = "CONCAT(" + columns + ")";
        String conditions = subPhrases.stream()
                .map(s -> row + " LIKE '%" + s + "%'")
                .collect(Collectors.joining(" AND "));

        String subSql = "SELECT b.id FROM Book b " +

                "LEFT OUTER JOIN Book_author ba ON ba.books_id = b.id " +
                "LEFT OUTER JOIN Author a ON ba.authors_id = a.id " +

                "LEFT OUTER JOIN Book_publisher bp ON bp.books_id = b.id " +
                "LEFT OUTER JOIN Publisher p ON bp.publishers_id = p.id " +

                "LEFT OUTER JOIN Series s ON s.id = b.series_id " +

                "GROUP BY b.id " +
                "HAVING " +
                conditions;
        String sql = "SELECT * FROM Book b " +
                "WHERE b.id IN (" + subSql + ") " +
                "ORDER BY b.id " +
                "LIMIT " + pageable.getPageSize() +
                " OFFSET " + pageable.getPageSize() * pageable.getPageNumber();

        Query query = em.createNativeQuery(sql, Book.class);
        return new ArrayList<Book>(query.getResultList());
    }

    //Awful but works - OLD
    private List<Book> findAllContainingPhraseJPQL(String phrase, Map<String, Boolean> columnList) {
        Set<String> subPhrases = convertPhraseToSubPhrases(phrase);
        StringJoiner columns = new StringJoiner(", ';' ,");

        if (columnList.getOrDefault("title", false)) {
            columns.add("LOWER(b.title)");
        }
        if (columnList.getOrDefault("subtitle", false)) {
            columns.add("LOWER(b.subtitle)");
        }
        if (columnList.getOrDefault("authors", false)) {
            columns.add("LOWER(STRING_AGG(CONCAT(a.firstName,a.middleName,a.lastName,a.prefix), ';'))");
        }
        if (columnList.getOrDefault("publishers", false)) {
            columns.add("LOWER(STRING_AGG(CONCAT(p.name), ';'))");
        }
        if (columnList.getOrDefault("series", false)) {
            columns.add("LOWER(s.name)");
        }
        if (columnList.getOrDefault("seriesVolume", false)) {
            columns.add("LOWER(b.seriesVolume)");
        }
        if (columnList.getOrDefault("city", false)) {
            columns.add("b.city");
        }
        if (columnList.getOrDefault("year", false)) {
            columns.add("b.year");
        }
        if (columnList.getOrDefault("volume", false)) {
            columns.add("b.volume");
        }
        if (columnList.getOrDefault("edition", false)) {
            columns.add("LOWER(b.edition)");
        }
        if (columnList.getOrDefault("comment", false)) {
            columns.add("LOWER(b.comment)");
        }

        String row = "CONCAT(" + columns + ")";
        String conditions = subPhrases.stream()
                .map(s -> row + " LIKE '%" + s + "%'")
                .collect(Collectors.joining(" AND "));

        String subSql = "SELECT b.id FROM Book b " +
                "LEFT OUTER JOIN b.authors a " +
                "LEFT OUTER JOIN b.publishers p " +
                "LEFT OUTER JOIN b.series s " +
                "GROUP BY b.id " +
                "HAVING " +
                conditions;
        String sql = "SELECT b FROM Book b " +
                "WHERE b.id IN (" + subSql + ")" +
                "ORDER BY b.id";

        TypedQuery<Book> query = em.createQuery(sql, Book.class);
        return query.getResultList();
    }
}
