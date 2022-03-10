package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.*;

public class BoardGamesStatisticsAnalyzer implements StatisticsAnalyzer {

    Set<BoardGame> gameSet;

    public BoardGamesStatisticsAnalyzer(Collection<BoardGame> games) {
        gameSet = new HashSet<>(games);
    }

    @Override
    public List<String> getNMostPopularCategories(int n) {
        List<String> result = new ArrayList<>();
        Map<String, Integer> categoryScores = scoreCategories();

        Set<PairIntegerString> ranking = new TreeSet<>((a, b) -> {
            if (a.num() - b.num() != 0) {
                return b.num() - a.num();
            } else {
                return a.str().compareTo(b.str());
            }
        });

        for (var e : categoryScores.entrySet()) {
            ranking.add(new PairIntegerString(e.getValue(), e.getKey()));
        }

        for (var p : ranking) {
            result.add(p.str());
            if (result.size() == n) {
                break;
            }
        }
        return result;
    }

    @Override
    public double getAverageMinAge() {
        return gameSet.stream().mapToInt(g -> g.minAge()).average().orElse(0.0);
    }

    @Override
    public double getAveragePlayingTimeByCategory(String category) {
        return gameSet.stream()
                .filter(g -> g.categories().contains(category))
                .mapToInt(g -> g.playingTimeMins())
                .average()
                .orElse(0.0);
    }

    @Override
    public Map<String, Double> getAveragePlayingTimeByCategory() {
        Map<String, Double> result = new HashMap<>();
        Set<String> categories = new HashSet<>();

        gameSet.stream().map(g -> g.categories())
                .forEach(catList -> categories.addAll(catList));

        categories.forEach(c -> result.put(c, getAveragePlayingTimeByCategory(c)));
        return result;
    }

    private Map<String, Integer> scoreCategories() {
        Map<String, Integer> categoryScores = new HashMap<>();
        gameSet.stream().map(game -> game.categories())
                .forEach(catList -> catList.forEach(cat -> {
                    if (categoryScores.get(cat) == null) {
                        categoryScores.put(cat, 1);
                    } else {
                        categoryScores.put(cat, categoryScores.get(cat) + 1);
                    }
                }));
        return categoryScores;
    }
}
