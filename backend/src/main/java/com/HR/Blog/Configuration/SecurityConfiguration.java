package com.HR.Blog.Configuration;

import com.HR.Blog.Security.JwtAuthenticationEntryPoint;
import com.HR.Blog.Security.JwtAuthenticationFilter;
import com.HR.Blog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    public static final String[] PUBLIC_URLS = {"/api/auth/**","/api/users/add","/api/categories/create", "/api/users/{followerId}/follow/{userId}" , "/api/users/{followerId}/unfollow/{userId}" , "/api/users/{userId}/following", "/api/users/{userId}/followers" , "api/users/name/{email}" , "api/users/user/imageUpload/**" ,
           "/chat.sendMessage", "/api/reset-password/**", "/api/background.jpg", "/api/contact-us" , "api/posts/{postId}/like" , "api/posts/{postId}/dislike" , "api/posts/{postId}/unLike" , "api/posts/{postId}/unDislike" , "/v3/api-docs", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**"

    };

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> {
                    try {
                        authorizeHttpRequests
                                .requestMatchers(PUBLIC_URLS)
                                .permitAll()
                                .requestMatchers("/error")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET)
                                .permitAll()
                                .requestMatchers("/api/hrx").hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
//                                .and()// add this line to close the authorizeHttpRequests block
//                                .oauth2Login()
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/api/auth/current-user")
                                .and()// add this line to close the authorizeHttpRequests block
                                .exceptionHandling()
                                .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                                .and()
                                .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(daoAuthenticationProvider())
            ;
        return http.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;

    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public FilterRegistrationBean coresFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("Authorization");
        corsConfiguration.addAllowedHeader("Content-Type");
        corsConfiguration.addAllowedHeader("Accept");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        bean.setOrder(-110);

        return bean;
    }
//
//    	/*
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.
//				csrf()
//				.disable()
//				.authorizeHttpRequests()
//				.requestMatchers(PUBLIC_URLS)
//				.permitAll()
//				.requestMatchers(HttpMethod.GET)
//				.permitAll()
//				.anyRequest()
//				.authenticated()
//				.and().exceptionHandling()
//				.authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
//				.and()
//				.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//	}
//
//	 */
//
//    /*
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
//    }
//
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	 */
//
}
