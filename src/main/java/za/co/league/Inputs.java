package za.co.league;

import java.util.ArrayList;
import java.util.List;

public class Inputs {
    private final String inputType;
    private List<GameStats> games;

    public Inputs(String inputType) {
        this.inputType = inputType;
        this.games = new ArrayList<>();
    }

    public List<GameStats> getGames(){
        return games;
    }
}
