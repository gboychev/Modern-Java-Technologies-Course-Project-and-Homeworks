package bg.sofia.uni.fmi.mjt.game.recommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameRecommender {

    private List<Game> games;

    /**
     * Loads the dataset from the given {@code dataInput} stream.
     *
     * @param dataInput java.io.Reader input stream from which the dataset can be read
     */
    public GameRecommender(Reader dataInput) {
        try (var reader = new BufferedReader(dataInput)) {
            games = reader.lines()
                    .skip(1)
                    .map(Game::of)
                    .toList();

            System.out.println("Games loaded");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dataset", e);
        }
    }

    /**
     * @return All games from the dataset as an unmodifiable copy.
     * If the dataset is empty, return an empty collection
     */
    public List<Game> getAllGames() {
        return List.copyOf(games);
    }

    /**
     * Returns all games in the dataset released after the specified {@code date} as an unmodifiable list.
     * If no games have been released after the given date, returns an empty list.
     *
     * @param date
     * @return a list of all games released after {@code date}, in an undefined order.
     */
    public List<Game> getGamesReleasedAfter(LocalDate date) {
        return List.copyOf(
                games.stream()
                        .filter(g -> g.releaseDate().isAfter(date))
                        .toList());
    }

    /**
     * Returns the top {@code n} games by user review score.
     *
     * @param n maximum number of games to return
     *          If {@code n} exceeds the total number of games in the dataset, return all games.
     * @return unmodifiable list of the games sorted by user review score in descending order
     * @throws IllegalArgumentException in case {@code n} is a negative number.
     */
    public List<Game> getTopNUserRatedGames(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N can't be less than 0 in getTopNUserRatedGames");
        }
        return List.copyOf(games.stream()
                .sorted(Comparator.comparingDouble(Game::userReview).reversed())
                .limit(n).toList());
    }

    /**
     * Returns a list (without repetitions) of all years in which at least one game with meta score
     * {@code minimalScore} or higher has been released. The order of the years in the result is undefined.
     * If there are no such years, return an empty list.
     *
     * @param minimalScore
     * @return the years when a game with at least {@code minimalScore} meta score has been released
     */
    public List<Integer> getYearsWithTopScoringGames(int minimalScore) {
        return List.copyOf(games.stream()
                .filter(g -> g.metaScore() >= minimalScore)
                .map(Game::releaseDate)
                .map(LocalDate::getYear)
                .distinct().toList());
    }

    /**
     * Returns the names of all games in the dataset released in {@code year} as a comma-separated String.
     * Each comma in the result must be followed by a space. The order of the game names in the result is undefined.
     * If no games have been released in the given year, returns an empty String.
     *
     * @param year
     * @return a comma-separated String containing all game names released in {@code year}
     */
    public String getAllNamesOfGamesReleasedIn(int year) {
        StringBuilder sb = new StringBuilder();
        games.stream()
                .filter(g -> g.releaseDate().getYear() == year)
                .map(Game::name)
                .forEach(g -> sb.append(g).append(", "));
        return new String(sb);
    }

    /**
     * Returns the game for the specified {@code platform} with the highest user review score.
     *
     * @param platform the name of the platform
     * @return the game for the specified {@code platform} with the highest review score
     * @throws NoSuchElementException if there is no game at all released for the specified platform,
     *                                or if {@code platform} is null or an empty String.
     */
    public Game getHighestUserRatedGameByPlatform(String platform) {
        Game result = games.stream()
                .filter(g -> g.platform().equals(platform))
                .max(Comparator.comparingDouble(Game::userReview))
                .orElse(null);
        if (result == null) {
            throw new NoSuchElementException("Couldn't find a game, released for this platform");
        }
        return result;
    }

    /**
     * Returns all games by platform. The result should map a platform name to the set of all games
     * released for this platform.
     *
     * @return all games by platform
     */
    public Map<String, Set<Game>> getAllGamesByPlatform() {
        List<String> platforms = games.stream()
                .map(Game::platform)
                .distinct()
                .toList();
        Map<String, Set<Game>> result = new HashMap<>();

        for (String p : platforms) {
            result.put(p, new HashSet<Game>());
        }

        games.stream().forEach(g -> result.get(g.platform()).add(g));

        return result;
    }

    /**
     * Returns the number of years a game platform has been live.
     * The lifecycle of a platform is assumed to start and end with the release year of the oldest and newest game
     * released for this platform (the exact date is not significant).
     * In case all games for a given platform have been released in a single year, return 1.
     * In case {@code platform} is null, blank or unknown in the dataset, return 0.
     *
     * @return the number of years a game platform has been live
     */
    public int getYearsActive(String platform) {
        if (platform == null || platform.isEmpty()) {
            return 0;
        }

        int result = 0;

        Integer minYear = games.stream()
                .filter(g -> g.platform().equals(platform))
                .map(Game::releaseDate)
                .map(LocalDate::getYear)
                .min(Comparator.comparingInt(Integer::intValue))
                .orElse(0);

        Integer maxYear = games.stream()
                .filter(g -> g.platform().equals(platform))
                .map(Game::releaseDate)
                .map(LocalDate::getYear)
                .max(Comparator.comparingInt(Integer::intValue))
                .orElse(0);
        if (maxYear != 0 && minYear != 0) {
            result = maxYear - minYear + 1;
        }
        return result;
    }

    /**
     * Returns the games whose summary contains all {@code keywords} specified, as an unmodifiable list.
     * <p>
     * If there are no such games, return an empty list.
     * In case no keywords are specified, or any of the keywords is null or blank, the result is undefined.
     *
     * @param keywords the keywords to search for in the game summary
     * @return the games whose summary contains the specified keywords
     */
    public List<Game> getGamesSimilarTo(String... keywords) {
        List<Game> gamesList = new ArrayList<>(games.stream().toList());
        for (String s : keywords) {
            games.stream().filter(g -> !(g.summary().contains(s)))
                    .forEach(g -> gamesList.remove(g));
        }
        return Collections.unmodifiableList(gamesList);
    }
}