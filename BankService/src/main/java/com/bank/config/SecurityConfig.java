package com.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité pour la banque.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // Définit quelles pages sont publiques et lesquelles demandent une connexion
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((requests) -> requests
                                                .requestMatchers("/login", "/css/**").permitAll() // La page login est
                                                                                                  // publique
                                                .anyRequest().authenticated()) // Le reste demande d'être connecté
                                .formLogin((form) -> form
                                                .loginPage("/login") // Utilise notre page HTML personnalisée
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout((logout) -> logout.permitAll())
                                .csrf(csrf -> csrf.disable()); // Désactivé pour faciliter les tests RabbitMQ

                return http.build();
        }

        // Crée l'utilisateur par défaut pour se connecter à l'interface banque
        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user = User.withDefaultPasswordEncoder()
                                .username("bankadmin") // IDENTIFIANT
                                .password("bankadmin") // MOT DE PASSE
                                .roles("ADMIN")
                                .build();

                return new InMemoryUserDetailsManager(user);
        }
}
