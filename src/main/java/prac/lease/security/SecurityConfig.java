package prac.lease.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import prac.lease.repository.UserRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService myUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor to inject dependencies
    public SecurityConfig(UserDetailsService myUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/login/**", "/register/**")
                                .permitAll()
                                .requestMatchers("/users/**","/user/delete/**", "/users/active", "/user/{username}", "/user/update/{id}", "/roles/**").hasRole("ADMIN")

                                // Lease approval endpoints - ADMIN only
                                .requestMatchers("/api/leases/*/approve", "/api/leases/*/reject", "/api/leases/pending-approval")
                                .hasRole("ADMIN")

                                // Lease creation and management - Both ADMIN and USER
                                .requestMatchers("/api/leases", "POST", "/api/leases/*", "PUT", "/api/leases", "GET")
                                .hasAnyRole("ADMIN", "USER", "SITE_ACQUISITION")

                                // Document upload to leases - Both ADMIN and USER (for approved leases)
                                .requestMatchers("/api/leases/*/documents", "POST")
                                .hasAnyRole("ADMIN", "USER", "SITE_ACQUISITION")

                                // Document management - Both ADMIN and USER
                                .requestMatchers("/api/documents/**")
                                .hasAnyRole("ADMIN", "USER", "SITE_ACQUISITION")

                                // Landlord and Site management - Both ADMIN and USER
                                .requestMatchers("/api/landlords/**", "/api/sites/**")
                                .hasAnyRole("ADMIN", "USER", "SITE_ACQUISITION")

                                // Reports and statistics - Both ADMIN and USER
                                .requestMatchers("/api/reports/**")
                                .hasAnyRole("ADMIN", "USER")

                                // Lease deletion - ADMIN only
                                .requestMatchers("/api/leases/*", "DELETE")
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                ).userDetailsService(myUserDetailsService)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider(
            ActiveDirectoryProperties activeDirectoryProperties, UserRepository userRepository) {

        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
                activeDirectoryProperties.getDomain(),
                activeDirectoryProperties.getUrl(),
                activeDirectoryProperties.getRootDn());

        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setUserDetailsContextMapper(new CustomUserDetailsContextMapper(userRepository));
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}