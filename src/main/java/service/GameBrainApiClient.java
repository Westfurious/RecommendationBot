package service;

import com.game.movie.buddy.BotConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import model.Game;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class GameBrainApiClient implements GameSearchClient {
    private static final String BASE_URL = BotConfig.getBaseUrl();
    private static final String API_KEY = BotConfig.getGameApiKey();

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); // JSON поля форматировались в camel
    }

    public List<Game> searchGames(String query) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addQueryParameter("query", query)
                .addQueryParameter("limit", "10")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-api-key", API_KEY)
                .addHeader("User-Agent", "GameMovieBot/1.0")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            GameBrainResponse wrapper = objectMapper.readValue(responseBody, GameBrainResponse.class);
            return wrapper.results;
        }
    }
    public Game getGameById(int id) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + id);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-api-key", API_KEY)
                .addHeader("User-Agent", "GameMovieBot/1.0")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Game not found " + response);

            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, Game.class);
        }
    }
    public List<Game> getSimilarGames(int gameId) throws IOException {
        HttpUrl url = HttpUrl.parse(String.format("%s/%s/similar", BASE_URL, gameId));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-api-key", API_KEY)
                .addHeader("User-Agent", "GameMovieBot/1.0")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Failed to fetch similar games " + response);

            String responseBody = response.body().string();
            GameBrainResponse wrapper = objectMapper.readValue(responseBody, GameBrainResponse.class);
            return wrapper.results;
        }
    }
}

