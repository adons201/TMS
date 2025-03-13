package ru.tms.security;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.context.SecurityContextHolderStrategy;
//import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
//import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.io.IOException;

//@RequiredArgsConstructor
public class OAuthClientHttpRequestInterceptor
    //    implements ClientHttpRequestInterceptor
{

//    private final OAuth2AuthorizedClientManager authorizedClientManager;
//
//    private final String registrationId;
//
//    @Setter
//    private SecurityContextHolderStrategy securityContextHolderStrategy =
//            SecurityContextHolder.getContextHolderStrategy();
//
//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
//            throws IOException {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        try {
//            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
//                    .withClientRegistrationId(this.registrationId)
//                    .principal(authentication)
//                    .build();
//            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
//            if (authorizedClient != null) {
//                request.getHeaders().setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
//            }
//        } catch (ClientAuthorizationRequiredException ex) {
//            System.err.println("Authorization required: " + ex.getMessage());
//            throw ex; // Re-throw the exception or handle it as needed
//        }
//
//        return execution.execute(request, body);
//    }
}
