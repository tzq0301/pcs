package cn.tzq0301.gateway.security;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import static cn.tzq0301.user.Role.*;
import static org.springframework.http.HttpMethod.*;

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

                .pathMatchers(POST, "/visit/apply").hasRole(STUDENT.getRole())
                .pathMatchers("/visit/id/{id}/applys").hasRole(STUDENT.getRole())
                .pathMatchers("/visit/id/{id}/apply_id/{apply_id}").hasRole(STUDENT.getRole())
                .pathMatchers("/visit/unfinished_applies").hasRole(ADMIN.getRole())
                .pathMatchers(DELETE, "/visit/user_id/{user_id}/global_id/{global_id}").hasRole(STUDENT.getRole())
                .pathMatchers(POST, "/visit/pass_apply").hasRole(ADMIN.getRole())
                .pathMatchers(GET, "/visit/visitor_id/{visitor_id}/first_visit_records").hasRole(VISITOR.getRole())
                .pathMatchers(GET, "/visit/user_id/{user_id}/global_id/{global_id}").hasRole(VISITOR.getRole())
                .pathMatchers(POST, "/visit/apply/global_id/{global_id}").hasRole(VISITOR.getRole())
                .pathMatchers(GET, "/visit/first_records").hasRole(ADMIN.getRole())
                .pathMatchers(DELETE, "/visit/reject/apply_id/{apply_id}").hasRole(ADMIN.getRole())
                .pathMatchers(DELETE, "/visit/records/global_id/{global_id}").hasRole(ADMIN.getRole())
                .pathMatchers("/visit/**").permitAll()

                .pathMatchers(GET, "/consult/applys").hasRole(ASSISTANT.getRole())
                .pathMatchers(POST, "/consult/global_id/{global_id}/weekday/{weekday}/from/{from}/address/{address}").hasRole(ASSISTANT.getRole())

                .pathMatchers(GET, "/duty/user_id/{user_id}/duties").hasAnyRole(VISITOR.getRole(), CONSULTANT.getRole(), ASSISTANT.getRole(), ADMIN.getRole())
                .pathMatchers(GET, "/duty/user_id/{user_id}/works").hasAnyRole(VISITOR.getRole(), CONSULTANT.getRole(), ASSISTANT.getRole(), ADMIN.getRole())
                .pathMatchers(GET, "/duty/duties").hasAnyRole(VISITOR.getRole(), CONSULTANT.getRole(), ASSISTANT.getRole(), ADMIN.getRole())
                .pathMatchers(POST, "/duty/user_id/{user_id}/duty/weekday/{weekday}/from/{from}/address/{address}").hasRole(ADMIN.getRole())
                .pathMatchers(DELETE, "/duty/user_id/{user_id}/duty/weekday/{weekday}/from/{from}/address/{address}").hasRole(ADMIN.getRole())
                .pathMatchers(POST, "/duty/user_id/{user_id}/duty/day/{day}/from/{from}/address/{address}/type/{type}").hasRole(ADMIN.getRole())
                .pathMatchers(DELETE, "/duty/user_id/{user_id}/duty/day/{day}/from/{from}/type/{type}").hasRole(ADMIN.getRole())

                .pathMatchers("/addresses").permitAll()
                .pathMatchers("/general/**").permitAll()

//                .pathMatchers("/student").hasRole(Role.STUDENT.getRole())
//                .pathMatchers("/admin").hasRole(Role.ADMIN.getRole())
                .anyExchange().authenticated()
                .and().build();
    }
}
