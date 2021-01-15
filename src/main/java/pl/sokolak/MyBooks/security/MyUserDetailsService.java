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
    @Profile({"prod", "heroku"})
    public UserDetailsService userDetailsServiceProd() {
        UserDetails guest =
                User.withUsername("guest")
                        .password("{noop}guest")
                        .roles("GUEST")
                        .build();
        UserDetails admin =
                User.withUsername("admin")
                        .password("{bcrypt}$2b$10$o.HzHB6YPvP6walqhlL94.edSMF5Vo0hLlVqVsH5r01XGL4I1JuHa")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(List.of(admin, guest));
    }

    @Bean
    @Profile("heroku-demo")
    public UserDetailsService userDetailsServiceHerokuDemo() {
        UserDetails user =
                User.withUsername("user")
                        .password("{noop}pass")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(List.of(user));
    }
}
