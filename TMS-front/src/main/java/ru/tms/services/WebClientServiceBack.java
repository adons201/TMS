package ru.tms.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class WebClientServiceBack {

    private final WebClient webClientBack;

    public WebClientServiceBack(WebClient.Builder webClientBuilder, @Value("${service_back.url}") String serviceBackUrl) {
        this.webClientBack = webClientBuilder.baseUrl(serviceBackUrl).build();
    }

    public <T> Mono<T> sendRequest(String path, HttpMethod method,
                                   Map<String, Object> pathVariables,
                                   Object queryParams,
                                   ParameterizedTypeReference<T> responseType, T defaultValue) {

        return requestDataFromServiceBack(path, method, pathVariables, queryParams,
                null, responseType, defaultValue);
    }

    public <T> Mono<T> sendRequest(String path, HttpMethod method,
                                   Map<String, Object> pathVariables,
                                   Object queryParams,
                                   Class<T> responseType, T defaultValue) {
        return  requestDataFromServiceBack(path, method, pathVariables, queryParams,
                responseType, null, defaultValue);
    }


    private  <T> Mono<T> requestDataFromServiceBack(String path, HttpMethod method,
                                                  Map<String, Object> pathVariables,
                                                  Object queryParams,
                                                  Class<T> responseType1,
                                                  ParameterizedTypeReference<T> responseType2, T defaultValue) {

        if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
            WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
            WebClient.RequestHeadersSpec<?> requestHeadersSpec;

            if (method == HttpMethod.DELETE)  requestHeadersUriSpec = webClientBack.delete();
            else requestHeadersUriSpec = webClientBack.get();

            if (pathVariables != null && !pathVariables.isEmpty())
                requestHeadersSpec = requestHeadersUriSpec.uri(path, pathVariables);
            else requestHeadersSpec = requestHeadersUriSpec.uri(path);

            return requestHeadersSpec.exchangeToMono(response -> {
                            // Обработка ответа и конвертация в нужный тип
                            if(response.statusCode().is2xxSuccessful()) {
                                if (responseType1 != null ) return response.bodyToMono(responseType1);
                                else return response.bodyToMono(responseType2);
                            } else {
                                return Mono.just(defaultValue); // Возврат дефолтного значения
                            }
                        })
                        .onErrorResume(error -> {
                            System.err.println("Error: " + error.getMessage());
                            return Mono.just(defaultValue); // Возврат дефолтного значения при ошибке
                        });

        }
        String uriString;
        WebClient.RequestBodySpec requestBodySpec;
        // 1. Строим URI, используя как переменные пути, так и параметры запроса.
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(path);

        // 2. Добавляем параметры запроса в URI и преобразуем URI builder в строку URI
        if (pathVariables != null && !pathVariables.isEmpty()) {
            uriString = uriBuilder.buildAndExpand(pathVariables).toUriString();
        } else  uriString = uriBuilder.build().toUriString();

        // 3. Создаем спецификацию запроса (RequestBodySpec), указывая метод PUT и URI
        if (method == HttpMethod.PUT) {
            requestBodySpec = webClientBack.put().uri(uriString);
        } else {
            requestBodySpec = webClientBack.post().uri(uriString);
        }

        // Устанавливаем заголовок Content-Type
        if (queryParams != null) {
            requestBodySpec.contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(queryParams); // Предполагаем, что queryParams - это тело запроса
        }


        // 4. Отправляем запрос и обрабатываем ответ
        return requestBodySpec.exchangeToMono(response -> {
                    // Обрабатываем ответ и преобразуем его в нужный тип
                    if (response.statusCode().is2xxSuccessful()) {
                        if (responseType1 != null ) return response.bodyToMono(responseType1);
                        else return response.bodyToMono(responseType2);
                    } else {
                        return Mono.just(defaultValue); // Возвращаем значение по умолчанию для неуспешных ответов (не 2xx)
                    }
                })
                .onErrorResume(error -> {
                    System.err.println("Error: " + error.getMessage());
                    return Mono.just(defaultValue); // Возвращаем значение по умолчанию при возникновении ошибки
                });

    }

    public static enum HttpMethod {
        POST,
        GET,
        PUT,
        DELETE;

        private HttpMethod() {
        }
    }}
