package com.hico.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.hico.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    //

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    //
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        System.out.println("bcryptpassword encoder");
        return new BCryptPasswordEncoder();
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // issue --
    /*
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        System.out.println("authication provider");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    */

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        System.out.println("authication Managerbeab");
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }
    //
    @Bean
    public UserDetailsService mongoUserDetails() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println(" ********* Configuring User details Security *****************");
        UserDetailsService userDetailsService = mongoUserDetails();
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println(" ********* Configuring HTTP Security *****************");
        /*
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/dashboard/**").hasAuthority("ADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling();
                */

        http.httpBasic().disable().csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
            .antMatchers("/api/school/**").hasAuthority("ADMIN")
            .antMatchers("/api/teacher/**").hasAnyAuthority("ADMIN", "SCHOOL-ADMIN", "TEACHER")
            .antMatchers("/api/student/**").hasAnyAuthority("ADMIN", "SCHOOL-ADMIN", "TEACHER", "STUDENT")
            .antMatchers("/api/club/**").hasAnyAuthority("ADMIN", "SCHOOL-ADMIN", "TEACHER", "STUDENT")
            .antMatchers("/api/user/**").hasAnyAuthority("ADMIN", "SCHOOL-ADMIN", "TEACHER", "STUDENT")
            .antMatchers("/api/auth/pending/**").hasAnyAuthority("ADMIN", "SCHOOL-ADMIN", "TEACHER", "STUDENT")
            .antMatchers("/api/auth/login").permitAll()
            .antMatchers("/api/auth/register").permitAll()
            .antMatchers("/api/auth/lookup/**").permitAll()
            .anyRequest().authenticated().and().csrf()
            .disable().exceptionHandling()
            .authenticationEntryPoint(unauthorizedEntryPoint()).and()
            .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        System.out.println(" ********* Configuring Web Security *****************");
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}
