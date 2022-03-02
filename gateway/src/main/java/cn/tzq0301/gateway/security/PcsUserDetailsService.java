package cn.tzq0301.gateway.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tzq0301
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class PcsUserDetailsService implements ReactiveUserDetailsService {
    private final PcsUserManager userManager;

    @Override
    public Mono<UserDetails> findByUsername(String account) {
        return userManager.getUserByUserId(account)
                .map(user -> new User(user.getUserId(), user.getPassword(), user.getEnabled(),
                        true, true, true,
                        Stream.of(user.getRole()).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }
}
