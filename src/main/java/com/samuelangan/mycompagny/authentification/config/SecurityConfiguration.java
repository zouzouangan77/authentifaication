package com.samuelangan.mycompagny.authentification.config;

import com.samuelangan.mycompagny.authentification.security.AuthoritiesConstants;
import com.samuelangan.mycompagny.authentification.security.DomainUserDetailsService;
import com.samuelangan.mycompagny.authentification.security.jwt.JWTConfigurer;
import com.samuelangan.mycompagny.authentification.security.jwt.JWTFilter;
import com.samuelangan.mycompagny.authentification.security.jwt.RequestTypeFilter;
import com.samuelangan.mycompagny.authentification.security.jwt.TokenProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(TokenProvider tokenProvider, CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public FilterRegistrationBean<RequestTypeFilter> requestTypeFilter() {
        FilterRegistrationBean<RequestTypeFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestTypeFilter());
        registrationBean.addUrlPatterns("/api/*", "/**");
        return registrationBean;
    }


    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**", "/management/**", "/websocket/**")
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(problemSupport)
                        .accessDeniedHandler(problemSupport)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/activate").permitAll()
                        .requestMatchers("/api/account/reset-password/init").permitAll()
                        .requestMatchers("/api/account/reset-password/finish").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/websocket/**").permitAll()
                        .requestMatchers("/management/health", "/management/health/**", "/management/info", "/management/prometheus").permitAll()
                        .requestMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public SecurityFilterChain mvcSecurityFilterChain(
            HttpSecurity http,
            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

        http
                .securityMatcher("/**") // Ne matchera QUE ce qui n’est pas déjà pris par `apiSecurityFilterChain`
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                        .requestMatchers("/user/**").hasAuthority(AuthoritiesConstants.USER)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout").permitAll()
                )
                .addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied"));

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider)
                .build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            DomainUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}

