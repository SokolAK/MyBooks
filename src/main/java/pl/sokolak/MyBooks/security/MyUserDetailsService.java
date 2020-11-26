package pl.sokolak.MyBooks.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
public class MyUserDetailsService {
    @Bean
    @Profile("prod")
    public UserDetailsService userDetailsServiceProd() {
        UserDetails guest =
                User.withUsername("guest")
                        .password("{noop}guest")
                        .roles("GUEST")
                        .build();
        UserDetails admin =
                User.withUsername("admin")
                        .password("{bcrypt}$2b$10$nBiEgDUOjw.MVP6GNDoC6u0cKmQWVWFjU1Rv/oROhs4kqv6c2vwe.")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(List.of(admin, guest));
    }
}
