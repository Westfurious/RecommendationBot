package service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.Game;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Без этой аннотации Jackson упадёт если увидит поле в Json которого нету в классе
public class GameBrainResponse {
    public List<Game> results;
}
