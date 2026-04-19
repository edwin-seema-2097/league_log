package za.co.league;


import java.util.*;

public class LogRankProcessor {
    private Inputs inputs;

    private Map<String,TeamRanking> log = new HashMap<>();

    public void run() {
        ingestInputs();
        processTeamPoint();
        rankTeam();
        publishLogRankings();
    }

    public void rankTeam() {
        List<TeamRanking> rankings = new ArrayList<>();
        for (String team: log.keySet()) {
            rankings.add(log.get(team));
        }

        Collections.sort(rankings, (t1, t2) -> {
            int pointCompare = t2.getLogPoints().compareTo(t1.getLogPoints());
            if (pointCompare != 0) {
                return pointCompare;
            }
            return t1.getTeam().compareToIgnoreCase(t2.getTeam());
        });

        int position = 1;
        int prevPosition = 0;
        int prevPoints = 0;
        for (TeamRanking team : rankings) {
            if (prevPoints == team.getLogPoints()) {
                team.setLogPosition(prevPosition);
                position++;
            } else {
                team.setLogPosition(position++);
            }
            prevPosition = team.getLogPosition();
            prevPoints = team.getLogPoints();
        }
    }

    public void publishLogRankings() {
        List<TeamRanking> rankings = new ArrayList<>(log.values());
        rankings.sort((r1, r2) -> {
            int posCompare = Integer.compare(r1.getLogPosition(), r2.getLogPosition());
            if (posCompare != 0) {
                return posCompare;
            }
            return r1.getTeam().compareToIgnoreCase(r2.getTeam());
        });

        for (TeamRanking ranking : rankings) {
            final Integer logPoints = ranking.getLogPoints();
            String lable = "pts";
            if (logPoints == 1) {
                lable = "pt";
            }
            System.out.println(ranking.getLogPosition() + ". " + ranking.getTeam() + ", " + logPoints + " " + lable);
        }
    }

    public void processTeamPoint() {
        List<GameStats> games = inputs.getGames();

        for (GameStats game:games) {
            int homePoints;
            int awayPoints;

            if (game.getHomeGoals() == game.getAwayGoals()) {
                homePoints = 1;
                awayPoints = 1;
            } else if (game.getHomeGoals() > game.getAwayGoals()) {
                homePoints = 3;
                awayPoints = 0;
            } else {
                homePoints = 0;
                awayPoints = 3;
            }

            final String homeTeam = game.getHomeTeam();
            final String awayTeam = game.getAwayTeam();
            processTeam(game,homePoints, homeTeam);
            processTeam(game,awayPoints, awayTeam);
        }

    }


    private void processTeam(GameStats game, int teamPoints, String teamName) {
        TeamRanking teamRanking = log.get(teamName);
        if (teamRanking == null) {
            teamRanking = new TeamRanking(teamName);
            teamRanking.setLogPoints(0);
        }
        teamPoints = teamPoints + teamRanking.getLogPoints();
        teamRanking.setLogPoints(teamPoints);
        log.put(teamName, teamRanking);
    }


    private void ingestInputs() {
        Scanner scanner = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        String input;
        Inputs inputs = new Inputs("stdin");
        System.out.println("Enter multiple lines, enter empty line to stop: ");

        while(!(input = scanner.nextLine()).isEmpty()) {
            builder.append(input).append("\n");
        }
        cacheInput(inputs, builder.toString());
    }

    void cacheInput(Inputs inputs, String input) {
        String[] games = input.split("\n");
        for (String game:games) {
            inputs.getGames().add(loadMatch(game));
        }
        this.inputs = inputs;
    }

    public Map<String,TeamRanking> getLog() {
        Map<String, TeamRanking> logCopy = new HashMap<>();
        for (String teamName : log.keySet()) {
            TeamRanking rankingOriginal = log.get(teamName);
            TeamRanking rankingCopy = new TeamRanking(teamName);

            rankingCopy.setLogPosition(rankingOriginal.getLogPosition());
            rankingCopy.setLogPoints(rankingOriginal.getLogPoints());
            logCopy.put(teamName, rankingCopy);
        }
        return logCopy;
    }

    public GameStats loadMatch(String game) {
        GameStats gameStats;
        String[] teams = game.split(", ");
        String[] home = teams[0].split(" (?=\\d+$)");
        String[] away = teams[1].split(" (?=\\d+$)");

        String homeTeam = home[0];
        Integer homeGoals = Integer.parseInt(home[1]);

        String awayTeam = away[0];
        Integer awayGoals = Integer.parseInt(away[1]);
        gameStats = new GameStats(homeTeam, awayTeam, homeGoals, awayGoals);
        return gameStats;
    }


}
