package ru.tms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.tms.security.OAuthClientHttpRequestInterceptor;
import ru.tms.services.ProjectService;
import ru.tms.services.SuiteService;
import ru.tms.services.TestService;

@Configuration
public class WebClientConfig {


    @Configuration
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "false")
    public static class StandaloneClientConfig {

        @Bean
        @Scope("prototype")
        public RestClient.Builder tmsServicesRestClientBuilder(
                ClientRegistrationRepository clientRegistrationRepository,
                OAuth2AuthorizedClientRepository authorizedClientRepository,
                LoadBalancerClient loadBalancerClient,
                @Value("${tms.services.back.registration-id}") String registrationId
        ) {
            return RestClient.builder()
                    .requestInterceptor(
                            new OAuthClientHttpRequestInterceptor(
                                    new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                            authorizedClientRepository), registrationId))
                    ;
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
    public static class CloudClientConfig {

        @Bean
        @Scope("prototype")
        public RestClient.Builder tmsServicesRestClientBuilder(
                ClientRegistrationRepository clientRegistrationRepository,
                OAuth2AuthorizedClientRepository authorizedClientRepository,
                LoadBalancerClient loadBalancerClient,
                @Value("${tms.services.back.registration-id}") String registrationId
        ) {
            return RestClient.builder()
                    .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                    .requestInterceptor(
                            new OAuthClientHttpRequestInterceptor(
                                    new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                            authorizedClientRepository), registrationId))
            ;
        }

    }

    @Bean
    public ProjectService projectServiceClient(@Value("${tms.services.back.url}") String backUrl,
                                               RestClient.Builder tmsRestClientBuilder) {
        return new ProjectService(tmsRestClientBuilder
                .baseUrl(backUrl)
                .build());
    }

    @Bean
    public TestService testServiceClient(@Value("${tms.services.back.url}") String backUrl,
                                         RestClient.Builder tmsRestClientBuilder) {
        return new TestService(tmsRestClientBuilder
                .baseUrl(backUrl)
                .build());
    }

    @Bean
    public SuiteService suiteServiceClient(@Value("${tms.services.back.url}") String backUrl,
                                           RestClient.Builder tmsRestClientBuilder,
                                           TestService testServiceClient) {
        return new SuiteService(testServiceClient, tmsRestClientBuilder
                .baseUrl(backUrl)
                .build());
    }

}
