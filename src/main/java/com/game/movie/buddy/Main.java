package com.game.movie.buddy;


import model.Game;
import service.RawgApiClient;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RawgApiClient rawgApiClient = new RawgApiClient();

        try {
            List<Game> games = rawgApiClient.searchGames("minecraft");
            for (Game game : games) {
                System.out.println(game);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запросе к API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}