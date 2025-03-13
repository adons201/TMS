package ru.tms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.HeaderWriterServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@Configuration
@EnableWebFluxSecurity
@EnableRedisWebSession
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                  ServerOAuth2AuthorizationRequestResolver resolver,
                                                  ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository,
                                                  ServerLogoutSuccessHandler logoutSuccessHandler,
                                                  ServerLogoutHandler logoutHandler) {
        return httpSecurity
                .authorizeExchange(
                        authorizeExchange ->
                                authorizeExchange.pathMatchers(
                                                "/actuator/**",
                                                "/access-token/**",
                                                "/id-token")
                                        .permitAll()
                                        .pathMatchers(HttpMethod.POST, "/**").permitAll()
                                        .anyExchange().authenticated()
                ).oauth2Login(oauth2Login ->
                        oauth2Login.authorizationRequestResolver(resolver)
                                .authorizedClientRepository(auth2AuthorizedClientRepository)
                )
                .logout(logout ->
                        logout.logoutSuccessHandler(logoutSuccessHandler)
                                .logoutHandler(logoutHandler)
                )
                .csrf(csrf->csrf.disable())
                .build();
    }

    @Bean
    ServerOAuth2AuthorizationRequestResolver requestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var resolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return resolver;
    }

    @Bean
    ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    ServerLogoutSuccessHandler logoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/test");
        return oidcLogoutSuccessHandler;
    }

    @Bean
    ServerLogoutHandler logoutHandler() {
        return new DelegatingServerLogoutHandler(
                new SecurityContextServerLogoutHandler(),
                new WebSessionServerLogoutHandler(),
                new HeaderWriterServerLogoutHandler(
                        new ClearSiteDataServerHttpHeadersWriter(ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES)
                )
        );
    }
}
