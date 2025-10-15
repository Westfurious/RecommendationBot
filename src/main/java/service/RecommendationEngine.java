package service;

import model.Game;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationEngine {

    private final GameSearchClient gameSearchClient;

    public RecommendationEngine(GameSearchClient gameSearchClient) {
        this.gameSearchClient = gameSearchClient;
    }

    public static class SearchResults {
        private final Game game;
        private final List<Game> alternatives;
        private final SearchStatus status;

        public enum SearchStatus {
            FOUND,
            MULTIPLE_MATCHES,
            NOT_FOUND
        }

        private SearchResults(Game game, List<Game> alternatives, SearchStatus status) {
            this.game = game;
            this.alternatives = (alternatives == null) ? List.of() : List.copyOf(alternatives);
            this.status = status;
        }

        public static SearchResults found(Game game){
            return new SearchResults(game, null, SearchStatus.FOUND);
        }

        public static SearchResults multipleMatches(List<Game> alternatives){
            return new SearchResults(null, alternatives, SearchStatus.MULTIPLE_MATCHES);
        }

        public static  SearchResults notFound(){
            return new SearchResults(null, null, SearchStatus.NOT_FOUND);
        }

        public Game getGame() {return game;}
        public List<Game> getAlternatives() {return alternatives;}
        public SearchStatus getStatus() {return status;}
        public boolean isFound() {return status == SearchStatus.FOUND;}
        public boolean isMultiMatch() {return status == SearchStatus.MULTIPLE_MATCHES;}
    }

    public SearchResults findBestGame(String query) throws IOException {

        // Ничего не нашли
        List<Game> results = gameSearchClient.searchGames(query);
        if (results.isEmpty()) {
            return SearchResults.notFound();
        }

        // Убираем мусор
        List<Game> validGames = filterValidGames(results);
        if (validGames.isEmpty()) {
            return SearchResults.notFound();
        }

        // Точное совпадение
        Game exactMatch = findExactMatch(validGames, query);
        if (exactMatch != null) {
            return SearchResults.found(exactMatch);
        }

        // Проверка на неоднозначность
        if (isAmbiguous(validGames, query)) {
            List<Game> topAlternatives = validGames.stream()
                    .limit(5)
                    .toList();
            return SearchResults.multipleMatches(topAlternatives);
        }
        
        //Лучший вариант
        Game bestMatch = selectBestMatch(validGames);
        return SearchResults.found(bestMatch);

    }
        private List<Game> filterValidGames (List<Game> games) {
            return games.stream()
                    .filter(g -> g.getName() != null)
                    .filter(g -> g.getRating() != null && g.getRating().getCount() > 100)
                    .filter(g -> g.getShortDescription() != null && !g.getShortDescription().isEmpty())
                    .sorted(Comparator
                            .comparingDouble((Game g) -> g.getRating().getCount())
                            .reversed())
                    .toList();
        }

        private Game findExactMatch(List<Game> games, String query) {
            // Точное совпадение
            String queryLower = query.toLowerCase().trim();
            for (Game game : games) {
                if(game.getName().equalsIgnoreCase(queryLower)) {
                    return game;
                }
            }

            // Почти точное название (Например введёт Dota а выдаст Dota 2)
            /*for(Game game : games) {
                String nameLower = game.getName().toLowerCase();
                if(nameLower.startsWith(queryLower + " ") || nameLower.startsWith(queryLower)) {
                    return game;
                }
            }*/

            return null;
        }

        // Проверка на неоднозначность (Пример с GTA) ну или несколько частей какой-либо игры
        private boolean isAmbiguous(List<Game> games, String query) {
            if (query.length() < 3) return false; // слишком короткий запрос не должен отрабатывать (например "aa" "ab")
            String queryLower = query.toLowerCase().trim();

            long similarGames = games.stream()
                    .filter(g -> g.getName().toLowerCase().startsWith(queryLower))
                    .count();

            return similarGames >= 2;
        }
        
        private Game selectBestMatch(List<Game> games) {
            return games.getFirst(); //уже отсортированные в filterValidGames()
        }

}