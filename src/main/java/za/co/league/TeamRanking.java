package za.co.league;

public class TeamRanking {
    private final String team;
    private Integer logPoints;
    private Integer logPosition;

    public TeamRanking(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public Integer getLogPoints() {
        return logPoints;
    }

    public void setLogPoints(Integer logPoints) {
        this.logPoints = logPoints;
    }


    public Integer getLogPosition() {
        return logPosition;
    }

    public void setLogPosition(Integer logPosition) {
        this.logPosition = logPosition;
    }
}
