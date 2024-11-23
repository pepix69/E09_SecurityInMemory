package com.upiiz.security.config;

import jakarta.servlet.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // Security Filter Chain - Cadena de filtros de seguridad
    // Been - Singleton - Tener solo una instancia
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        // Configurar los filtros personalizados

        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.GET,"api/v2/listar").hasAnyAuthority("READ");
                    http.requestMatchers(HttpMethod.GET,"api/v2/actualizar").hasAnyAuthority("UPDATE");
                    http.requestMatchers(HttpMethod.GET,"api/v2/eliminar").hasAnyAuthority("DELETE");
                    http.requestMatchers(HttpMethod.GET,"api/v2/crear").hasAnyAuthority("CREATE");
                    http.anyRequest().denyAll();
                })
                .build();
    }

    // Authentication Manager - Lo vamos a obtener de una instancia que ya existe
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Athentication Provider - DAO - Proporcionar los usuarios
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());;
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        // return new BCryptPasswordEncoder();

        // Si no queremos la contraseña ecriptada
        return NoOpPasswordEncoder.getInstance();
    }

    // UserDetailService -> Base de datos o suarios en memoria
    public UserDetailsService userDetailsService(){
        // Definir usuarios en memoria
        UserDetails usuarioMiguel = User
                .withUsername("miguel").password("miguel")
                .roles("ADMIN").authorities("READ", "CREATE", "UPDATE", "DELETE").build();

        UserDetails usuarioUpiiz = User
                .withUsername("upiiz").password("1234")
                .roles("ADMIN").authorities("READ", "CREATE", "UPDATE", "DELETE").build();


        UserDetails usuarioRodrigo = User
                .withUsername("rodrigo").password("rodrigo")
                .roles("USER").authorities("READ", "UPDATE").build();

        UserDetails usuarioInvitado = User.withUsername("guest").password("guest")
                .roles("GUEST").authorities("READ").build();

        List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
        userDetailsList.add(usuarioMiguel);
        userDetailsList.add(usuarioUpiiz);
        userDetailsList.add(usuarioRodrigo);
        userDetailsList.add(usuarioInvitado);

        return new InMemoryUserDetailsManager(userDetailsList);
    }

}

