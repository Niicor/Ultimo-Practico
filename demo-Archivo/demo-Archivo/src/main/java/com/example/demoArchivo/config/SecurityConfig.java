
package com.example.demoArchivo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Configuracion 1
    //Configuracion inicial de Seguridad
  /*  @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
    return httpSecurity
            .authorizeHttpRequests()
                .requestMatchers("/v1/index2").permitAll()//permitir el acceso a este endpoint sin autenticar
                .anyRequest().authenticated()//para los demas endpoints autenticar
            .and()
            .formLogin().permitAll()
            .and()
            .build();
    }*/
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
    return httpSecurity
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/v1/index2").permitAll();
                auth.anyRequest().authenticated();
            })
            .formLogin()
                .successHandler(successHandler()) //para redirigir a donde queremos que se cargue una vez inicia sesion
                .permitAll()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)//Sirve para crear una sesion para el usuario que se esta autenticando
                .invalidSessionUrl("/login")//en caso que no se autentique lo manda al login
                .maximumSessions(1)//cantidad de sesiones maximas
                .expiredUrl("/login")//si se expira la sesion se lo manda al login
                .sessionRegistry(sessionRegistry())
            .and()
            .sessionFixation()//cuando generamos un id de sesion, si el atacante se apropia del id puede autenticarse
                .migrateSession()//esto registra un id diferente cuando es atacada la id
            .and()
            .httpBasic()
            .and()
            .build();
    }
    
    public SessionRegistry sessionRegistry(){
    return new SessionRegistryImpl();
    }
    
    public AuthenticationSuccessHandler successHandler(){
    return ((request, response, authentication)->{
    response.sendRedirect("/v1/index");
    });
    }
}
