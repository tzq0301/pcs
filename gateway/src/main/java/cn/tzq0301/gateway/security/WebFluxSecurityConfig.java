package cn.tzq0301.gateway.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import static cn.tzq0301.user.Role.ADMIN;
import static cn.tzq0301.user.Role.STUDENT;

/**
 * @author tzq0301
 * @version 1.0
 */
@EnableWebFluxSecurity
@AllArgsConstructor
public class WebFluxSecurityConfig {
    private final ReactiveAuthenticationManager authenticationManager;

    private final ServerSecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) ->
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                ).accessDeniedHandler((swe, e) ->
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                ).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)

                .authorizeExchange()

                .pathMatchers(HttpMethod.OPTIONS).permitAll()

                .pathMatchers("/login/**").permitAll()
                .pathMatchers("/logout").permitAll()

                .pathMatchers("/test/**").permitAll()

                .pathMatchers("/auth/test").permitAll()
                .pathMatchers("/auth/student").hasRole(STUDENT.getRole())
                .pathMatchers("/auth/admin").hasRole(ADMIN.getRole())
                .pathMatchers("/auth/**").permitAll() // FIXME 需要细化

//                .pathMatchers("/student").hasRole(Role.STUDENT.getRole())
//                .pathMatchers("/admin").hasRole(Role.ADMIN.getRole())
                .anyExchange().authenticated()
                .and().build();
    }
}
