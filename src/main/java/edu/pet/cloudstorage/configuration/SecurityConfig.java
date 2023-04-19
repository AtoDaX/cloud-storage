package edu.pet.cloudstorage.configuration;

import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.repositories.UserRepository;
import edu.pet.cloudstorage.services.UserService;
import edu.pet.cloudstorage.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableRedisHttpSession
public class SecurityConfig {
    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public SecurityConfig(UserServiceImpl userService,
                          BCryptPasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/**","/fill").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/storage", "/storage/**").authenticated()
                                .requestMatchers("/login", "/register").anonymous()
                                )

                .formLogin((form) ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
                                )
                .logout(logout ->
                        logout
                                /*.deleteCookies("JSESSIONID")*/
                                .logoutSuccessUrl("/login")
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                ).anonymous().and()
                /*.rememberMe()
                .key("uniqueAndSecret")
                .and()*/
                .build();
    }

    /*@Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null) return user;

            throw new UsernameNotFoundException("User " + username + " not found!");
        };
    }*/

    /*@Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider auth =
                new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }*/
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    /*@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/teamplates/**");
    }*/


}
