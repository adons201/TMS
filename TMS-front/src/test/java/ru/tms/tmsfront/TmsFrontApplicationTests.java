package ru.tms.tmsfront;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.tms.dto.ProjectDto;
import ru.tms.services.WebClientServiceBack;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TmsFrontApplicationTests {

    @Autowired
    private WebClientServiceBack projectService;

    @Test
    void requestProjectTestWebClient() {
        long projectId = 1; // Используем long для соответствия Long в контроллере
        String path = "/api/project/{projectId}";

        WebTestClient testClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8081")
                .build();

        testClient.get()
                .uri(path, projectId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("id").isEqualTo("1");
    }

    @Test
    void testFetchData() {
        String path;
        // Arrange (настройка)
        path = "/api/projects";

        // Act (действие)
        Mono<List<ProjectDto>> result = projectService.sendRequest(path, WebClientServiceBack.HttpMethod.GET,
                null, null,  new ParameterizedTypeReference<List<ProjectDto>>() {
        }, Collections.emptyList());

        // Assert (утверждение)
        List<ProjectDto> projects = result.block();
        assertThat(projects).isNotNull(); // Проверяем, что список не null
    }

}
