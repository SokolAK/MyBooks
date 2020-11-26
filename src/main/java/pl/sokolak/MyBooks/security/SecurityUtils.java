package pl.sokolak.MyBooks.security;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.disjoint;
import static pl.sokolak.MyBooks.security.SecurityConfiguration.getActiveProfiles;

@Component
public final class SecurityUtils {

    private SecurityUtils() {
    }

    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter
                (ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(HandlerHelper.RequestType.values())
                .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static Set<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public static boolean hasAccess(OperationType opType) {
        if(getActiveProfiles().contains("dev"))
            return true;
        Set<String> userRoles = getUserRoles();
        Set<String> rolesForOperationType = getRolesForOperationType(opType);
        return !disjoint(userRoles, rolesForOperationType);
    }

    private static Set<String> getRolesForOperationType(OperationType opType) {
        return opType.getRoles();
    }

    public enum OperationType {
        ADD("ROLE_ADMIN"),
        DELETE("ROLE_ADMIN"),
        MODIFY("ROLE_ADMIN");

        @Getter
        private Set<String> roles;
        OperationType(String... roles) {
            this.roles = Set.of(roles);
        }
    }
}
