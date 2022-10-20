package com.example.springbootecommerceapi.config;

import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private final JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    public SecurityConfiguration(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(new CookieCsrfTokenRepository())
                .ignoringAntMatchers("/h2-console/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/ecommerce/v1/authentication/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/ecommerce/v1/products/**").permitAll()
                .antMatchers("/api/ecommerce/v1/products/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/ecommerce/v1/products").hasAnyRole("EMPLOYEE", "ADMIN")
//                .antMatchers(HttpMethod.PUT, "/api/ecommerce/v1/products/{\\d+}").hasAnyRole("EMPLOYEE, ADMIN")
//                .antMatchers(HttpMethod.PATCH, "/api/ecommerce/v1/products/{\\d+}").hasAnyRole("EMPLOYEE, ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/api/ecommerce/v1/products/**").hasAnyRole("EMPLOYEE, ADMIN")
                .antMatchers("/api/ecommerce/v1/orders/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .userDetailsService(jpaUserDetailsService)
                .headers().frameOptions().sameOrigin()
                .and()
                .formLogin();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    CommandLineRunner is executed after the application context is created
    and before the mail application runs
     */
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        UserEntity employee = new UserBuilder()
                .firstName("employee")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email("employee@gmail.com")
                .password(passwordEncoder.encode("12345678"))
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        employee.setActive(true);
        employee.setRole(Role.EMPLOYEE);
        UserEntity customer = new UserBuilder()
                .firstName("customer")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email("customer@gmail.com")
                .password(passwordEncoder.encode("12345678"))
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        customer.setActive(true);
        customer.setRole(Role.CUSTOMER);
        UserEntity admin = new UserBuilder()
                .firstName("admin")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("12345678"))
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        admin.setActive(true);
        admin.setRole(Role.ADMIN);
        return  args -> {
            userRepository.saveAll(List.of(customer, admin, employee));
        };
    }

}
