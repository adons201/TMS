package ru.tms.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.tms.services.client.LogoutClient;


@RequiredArgsConstructor
public class LogoutService implements LogoutClient {

    private final RestClient restClient;

    @Override
    public void logout() {
         this.restClient.post().uri("/logout")
                .retrieve()
                .toBodilessEntity();
    }
}
