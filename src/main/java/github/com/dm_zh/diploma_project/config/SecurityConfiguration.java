package github.com.dm_zh.diploma_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .oauth2Client()
                .and()
                .oauth2Login()
                .tokenEndpoint()
                .and()
                .userInfoEndpoint();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http
                .csrf().disable();

        http
                .authorizeHttpRequests()
                .requestMatchers("/unauthenticated", "/oauth2/**", "/login/**").permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .logout()
                .logoutSuccessUrl("http://192.168.1.193:8080/realms/external/protocol/openid-connect/logout");

//                .logoutSuccessUrl("http://localhost:8080/realms/external/protocol/openid-connect/logout?redirect_uri=http://localhost:8081/");

        return http.build();
    }
}
