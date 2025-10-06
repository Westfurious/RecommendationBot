package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import model.Game;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class GameBrainApiClient {
    private static final String BASE_URL = "https://api.gamebrain.co";
    private static final String API_KEY = "654132cb936947bd95b742766be5e4f8";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); // JSON поля форматировались в camel
    }

    public List<Game> searchGames(String query) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/v1/games")
                .newBuilder()
                .addQueryParameter("query", query)
                .addQueryParameter("limit", "5")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-api-key",API_KEY)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            GameBrainResponse wrapper = objectMapper.readValue(responseBody, GameBrainResponse.class);
            return wrapper.results;
        }
    }
    public Game getGameById(int id) throws IOException {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/v1/games/" + id);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-api-key",API_KEY)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Game not found " + response);

            String responseBody = response.body().string();
            //GameBrainResponse wrapper = objectMapper.readValue(responseBody, GameBrainResponse.class);
            return objectMapper.readValue(responseBody, Game.class);
        }
    }
}

