package pl.sokolak.MyBooks.utils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

    public static String getInitial(String word) {
        return getFirstChar(word.toUpperCase())+".";
    }

    public static String getFirstChar(String word) {
        if(!word.isBlank())
            return word.substring(0, 1);
        return "";
    }

    public static String lowerAndCapitalizeFirst(String word) {
        if(!word.isBlank())
            return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        return "";
    }

    public static Set<String> convertPhraseToSubPhrases(String phrase) {
        Set<String> collect = Stream.of(phrase
                .toLowerCase()
                .split(" "))
                //.map(sp -> "%" + sp + "%")
                .collect(Collectors.toSet());
        return collect;
    }
}
