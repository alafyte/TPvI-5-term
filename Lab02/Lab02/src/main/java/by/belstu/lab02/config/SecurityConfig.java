package by.belstu.lab02.config;

import by.belstu.lab02.jwt.JwtAuthEntryPoint;
import by.belstu.lab02.jwt.JwtAuthenticationFilter;
import by.belstu.lab02.models.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                .httpBasic(withDefaults())
                .cors(cors -> cors.disable())
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/ViewGuest/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/ViewOrders").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/ViewReservation").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/view-rooms").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/ViewServices").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/ViewTypeRooms").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/view-workers").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/view-users").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/ViewPayments").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/CreateGuest").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/CreateOrders/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/CreateReservation/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/CreateRoom").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/CreateService").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/CreateTypeRoom").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/register-worker").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/create-worker").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/CreatePayments/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/EditGuest/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/EditOrders/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/EditReservation/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/EditRoom/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/EditService/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/EditTypeRoom/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/EditUser/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/edit-worker/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/edit-worker").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/DeleteOrders/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/DeleteReservation/*").hasAnyAuthority(Roles.ADMIN.toString(), Roles.WORKER.toString())
                        .requestMatchers("/DeleteRooms/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/DeleteServices/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/DeleteTypeRooms/*").hasAuthority(Roles.ADMIN.toString())
                        .requestMatchers("/delete-worker/*").hasAuthority(Roles.ADMIN.toString())
                        )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "token")
                );

        http.addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration
                .getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}