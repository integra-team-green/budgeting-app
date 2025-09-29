package cloudflight.integra.backend.security.config;

import cloudflight.integra.backend.controller.problem.JwtAuthenticationEntryPoint;
import cloudflight.integra.backend.security.JwtRequestFilter;
import cloudflight.integra.backend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final UserService userService;
  private final JwtRequestFilter jwtRequestFilter;
  private final JwtAuthenticationEntryPoint authEntryPoint;

  public SecurityConfig(
      UserService userService,
      JwtRequestFilter jwtRequestFilter,
      JwtAuthenticationEntryPoint authEntryPoint) {
    this.userService = userService;
    this.jwtRequestFilter = jwtRequestFilter;
    this.authEntryPoint = authEntryPoint;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(corsConfig -> corsConfig.configure(http))
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated())
        .authenticationProvider(authenticationProvider()) // Add this line
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    // Create the provider through the constructor with required dependencies
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
    provider.setUserDetailsService(userService);
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
