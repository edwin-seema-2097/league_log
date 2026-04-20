package za.co.league;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            String label = "pts";
            if (logPoints == 1) {
                label = "pt";
            }
            System.out.println(ranking.getLogPosition() + ". " + ranking.getTeam() + ", " + logPoints + " " + label);
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

        System.out.println("Choose input method:");
        System.out.println("Enter \"1\" to enter inputs manually");
        System.out.println("Enter \"2\" to load inputs from file");
        System.out.print("Enter choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        Inputs inputs;

        String input;

        switch (choice) {
            case 1:
                input = readFromStdin(scanner);
                inputs = new Inputs("stdin");
                break;
            case 2:
                input = readFromFile(scanner);
                inputs = new Inputs("file");
                break;
            default:
                throw new RuntimeException("Invalid choice");
        }

        cacheInput(inputs, input);
    }

    private String readFromFile(Scanner scanner) {
        System.out.print("Enter file path: ");
        String path = scanner.nextLine();

        try {
            return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String readFromStdin(Scanner scanner) {
        StringBuilder builder = new StringBuilder();
        String input;
        System.out.println("Enter multiple lines, enter empty line to stop: ");

        while(!(input = scanner.nextLine()).isEmpty()) {
            builder.append(input).append("\n");
        }
        return builder.toString();
    }

    void cacheInput(Inputs inputs, String input) {
        String[] games = input.split("\n");
        for (String game:games) {
            inputs.getGames().add(loadMatch(game.trim()));
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
