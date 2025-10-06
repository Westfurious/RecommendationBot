package com.game.movie.buddy;


import model.Game;
import service.GameBrainApiClient;
import service.GameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameService gameService = new GameService();

        List<Game> games = gameService.searchGames("medieval strategy");
        for (Game game : games) {
            System.out.println(game.getId());
        }
        /*Game game = gameService.getGameById(622788);
        System.out.println(game);*/

        /*GameBrainApiClient gameBrainApiClient = new GameBrainApiClient();

        try {
            List<Game> games = gameBrainApiClient.searchGames("medieval strategy");
            for (Game game : games) {
                System.out.println(game);

                System.out.println("Скриншоты:");
                game.getScreenshots().forEach(System.out::println);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запросе к API: " + e.getMessage());
            e.printStackTrace();
        }*/
    }
}