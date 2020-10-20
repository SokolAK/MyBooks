package pl.sokolak87.MyBooks.model.author;

import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

import static pl.sokolak87.MyBooks.utils.StringUtil.getInitial;

@Data
public class AuthorDto {
    private Long id;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean shortNotation) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(lastName);

        if (!firstName.isBlank()) {
            stringBuilder.append(" ");
            if (!shortNotation)
                stringBuilder.append(firstName);
            else
                stringBuilder.append(getInitial(firstName));
        }

        if (!middleName.isBlank()) {
            stringBuilder.append(" ");
            if (!shortNotation)
                stringBuilder.append(middleName);
            else
                stringBuilder.append(getInitial(middleName));
        }

        if (!prefix.isBlank()) {
            if (!shortNotation)
                stringBuilder.append(", ").append(getPrefix());
        }

        return stringBuilder.toString();
    }

    public static String authorsSetToString(Set<AuthorDto> set, boolean shortForm) {
        return set.stream()
                .map(a -> a.toString(shortForm))
                .collect(Collectors.joining("; "));
    }
}