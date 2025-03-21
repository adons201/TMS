package ru.tms.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
//import ru.tms.security.OAuthClientHttpRequestInterceptor;

import java.util.Map;

//@Service
public class WebClientServiceBack {

    private final RestClient restClient;

    public WebClientServiceBack(RestClient.Builder restClientBuilder,
                                @Value("${tms.services.back.url}") String serviceBackUrl,
//                                ClientRegistrationRepository clientRegistrationRepository,
//                                OAuth2AuthorizedClientRepository authorizedClientRepository,
                                @Value("${tms.services.back.registration-id}") String registrationId) {

        this.restClient = restClientBuilder
                .baseUrl(serviceBackUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                .requestInterceptor(new OAuthClientHttpRequestInterceptor(
//                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
//                                authorizedClientRepository), registrationId))
                .build();
    }

    public <T> T sendRequest(String path, HttpMethod method,
                             Map<String, Object> pathVariables,
                             Object queryParams,
                             ParameterizedTypeReference<T> responseType, T defaultValue) {

        return requestDataFromServiceBack(path, method, pathVariables, queryParams,
                null, responseType, defaultValue);
    }

    public <T> T sendRequest(String path, HttpMethod method,
                             Map<String, Object> pathVariables,
                             Object queryParams,
                             Class<T> responseType, T defaultValue) {
        return requestDataFromServiceBack(path, method, pathVariables, queryParams,
                responseType, null, defaultValue);
    }

    private <T> T requestDataFromServiceBack(String path, HttpMethod method,
                                             Map<String, Object> pathVariables,
                                             Object queryParams,
                                             Class<T> responseType1,
                                             ParameterizedTypeReference<T> responseType2, T defaultValue) {

        RestClient.RequestBodySpec requestBodySpec = null;
        RestClient.RequestHeadersSpec<?> requestHeadersSpec = null;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(path);

        if (pathVariables != null && !pathVariables.isEmpty()) {
            uriBuilder.uriVariables(pathVariables);
        }

        String uriString = uriBuilder.build().toUriString();

        if (method == HttpMethod.GET) {
            requestHeadersSpec = restClient.get().uri(uriString);
        } else if (method == HttpMethod.POST) {
            requestBodySpec = restClient.post().uri(uriString);
        } else if (method == HttpMethod.PUT) {
            requestBodySpec = restClient.put().uri(uriString);
        } else if (method == HttpMethod.DELETE) {
            requestHeadersSpec = restClient.delete().uri(uriString);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        RestClient.ResponseSpec responseSpec = null;

        if (requestHeadersSpec!=null) {
            responseSpec = requestHeadersSpec.retrieve();
        }
        if (requestBodySpec != null){
            if (queryParams != null)
                responseSpec = requestBodySpec.contentType(MediaType.APPLICATION_JSON).body(queryParams).retrieve();
            else responseSpec = requestBodySpec.retrieve();
        }

        try {
            if (responseType1 != null) {
                return responseSpec.body(responseType1);
            } else {
                return responseSpec.body(responseType2);
            }
        } catch (Exception e) {
            System.err.println("Error during RestClient request: " + e.getMessage());
            return defaultValue;
        }
    }

    public static enum HttpMethod {
        POST,
        GET,
        PUT,
        DELETE
    }
}
