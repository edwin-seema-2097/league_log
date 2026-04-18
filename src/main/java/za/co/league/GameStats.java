package za.co.league;

public class GameStats {
    private final String homeTeam;
    private final String awayTeam;
    private final Integer homeGoals;
    private final Integer awayGoals;

    public GameStats(String homeTeam, String awayTeam, Integer homeGoals, Integer awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    @Override
    public String toString() {
        return "GameStats{" +
                "homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeGoals=" + homeGoals +
                ", awayGoals=" + awayGoals +
                '}';
    }
}
