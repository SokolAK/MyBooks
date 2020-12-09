package pl.sokolak.MyBooks.model.author;

import lombok.Getter;
import lombok.Setter;
import pl.sokolak.MyBooks.model.Dto;
import pl.sokolak.MyBooks.utils.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class AuthorDto implements Dto {
    private Long id;
    private String prefix = "";
    private String firstName = "";
    private String middleName = "";
    @NotNull
    @NotEmpty
    private String lastName = "";
    private Set<Long> booksIds = new HashSet<>();

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
                stringBuilder.append(StringUtils.getInitial(firstName));
        }

        if (!middleName.isBlank()) {
            stringBuilder.append(" ");
            if (!shortNotation)
                stringBuilder.append(middleName);
            else
                stringBuilder.append(StringUtils.getInitial(middleName));
        }

        if (!prefix.isBlank()) {
            if (!shortNotation)
                stringBuilder.append(", ").append(getPrefix());
        }

        return stringBuilder.toString();
    }

    public static String authorsSetToString(Collection<AuthorDto> set, boolean shortForm) {
        return set.stream()
                .map(a -> a.toString(shortForm))
                .collect(Collectors.joining("; "));
    }

    public static AuthorDto copy(AuthorDto original) {
        AuthorDto copy = new AuthorDto();
        copy.setId(original.getId());
        copy.setPrefix(original.getPrefix());
        copy.setFirstName(original.getFirstName());
        copy.setMiddleName(original.getMiddleName());
        copy.setLastName(original.getLastName());
        copy.setBooksIds(new HashSet<>(original.getBooksIds()));
        return copy;
    }
}