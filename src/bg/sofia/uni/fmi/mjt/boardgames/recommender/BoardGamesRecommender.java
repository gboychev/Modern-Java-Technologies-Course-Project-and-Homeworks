package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.*;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class BoardGamesRecommender implements Recommender {

    private List<BoardGame> boardGamesList;
    private Set<String> stopWordsSet;
    private final String splitWordsRegex = "[\\p{IsPunctuation}\\p{IsWhite_Space}]+";

    /**
     * Constructs an instance using the provided file names.
     *
     * @param datasetZipFile  ZIP file containing the board games dataset file
     * @param datasetFileName the name of the dataset file (inside the ZIP archive)
     * @param stopwordsFile   the stopwords file
     */
    public BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopwordsFile) {
        try (InputStream is = Files.newInputStream(datasetZipFile)) {
            BufferedInputStream bis = new BufferedInputStream(is);
            ZipInputStream zis = new ZipInputStream(bis);

            ZipEntry ze;
            do {
                ze = zis.getNextEntry();
            } while (ze != null && !ze.getName().equals(datasetFileName));
            if (ze == null) {
                throw new FileNotFoundException("Could not find the dataset file in the zip archive.");
            }

            ZipFile zf = new ZipFile(datasetZipFile.toString());
            InputStream is2 = zf.getInputStream(ze);
            InputStreamReader isr = new InputStreamReader(is2);
            BufferedReader bf = new BufferedReader(isr);

            boardGamesList = bf.lines()
                    .skip(1)
                    .map(BoardGame::of)
                    .toList();

            bf.close();
            zis.close();
            zf.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dataset");
        }
        try (BufferedReader br = Files.newBufferedReader(stopwordsFile)) {
            stopWordsSet = new HashSet<>(br.lines().toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load stopwords");
        }
    }

    /**
     * Constructs an instance using the provided Reader streams.
     *
     * @param dataset   Reader from which the dataset can be read
     * @param stopwords Reader from which the stopwords list can be read
     */
    public BoardGamesRecommender(Reader dataset, Reader stopwords) {
        try (BufferedReader br = new BufferedReader(dataset)) {
            boardGamesList = br.lines()
                    .skip(1)
                    .map(BoardGame::of)
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dataset");
        }

        try (BufferedReader br = new BufferedReader(stopwords)) {
            stopWordsSet = new HashSet<>(br.lines().toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load stopwords");
        }
    }

    @Override
    public Collection<BoardGame> getGames() {
        return Collections.unmodifiableList(boardGamesList);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        Set<Pair> scores = new TreeSet<Pair>((a, b) -> {
            if (Double.compare(a.score(), b.score()) != 0) {
                return Double.compare(a.score(), b.score());
            } else {
                return a.game().id() - b.game().id();
            }
        });

        Set<BoardGame> filteredGamesSet = filterGameList(game);

        for (BoardGame g : filteredGamesSet) {
            double gScore = calculateGameDistance(g, game);

            if (g.id() != game.id()) {
                scores.add(new Pair(gScore, g));
            }
        }

        List<BoardGame> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            BoardGame wantedGame = ((TreeSet<Pair>) scores).first().game();
            result.add(wantedGame);
            scores.remove(((TreeSet<Pair>) scores).first());
            if (scores.isEmpty()) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        Set<String> keywordsSet = new HashSet<>();
        for (String s : keywords) {
            keywordsSet.add(s.toLowerCase());
        }

        if (keywordsSet.isEmpty()) {
            return new ArrayList<BoardGame>();
        }

        stopWordsSet.stream().forEach(w -> keywordsSet.remove(w.toLowerCase()));

        Set<Pair> score = calculateScores(keywordsSet);

        List<BoardGame> result = new ArrayList<>();
        for (var p : score) {
            result.add(p.game());
        }

        return result;
    }

    @Override
    public void storeGamesIndex(Writer writer) {
        Map<String, Set<Integer>> allWords = mapWordToGameIDsWhereThatWordIsFound();

        for (var s : allWords.entrySet()) {
            StringBuilder line = new StringBuilder();
            line.append(s.getKey());
            line.append(":");
            for (var id : s.getValue()) {
                line.append(" ").append(id).append(",");
            }
            line.deleteCharAt(line.length() - 1);
            String result = new String(line);
            try {
                writer.write(result);
                writer.write("\n");
                writer.flush();
            } catch (IOException e) {
                throw new IllegalStateException("There was an error in writing to the writer in storeGameIndex");
            }
        }
    }

    private Map<String, Set<Integer>> mapWordToGameIDsWhereThatWordIsFound() {
        Map<String, Set<Integer>> allWords = new HashMap<>();
        for (BoardGame bg : boardGamesList) {
            Set<String> currentGameDescriptionWords = new HashSet<>(
                    List.of(bg.description().toLowerCase().split(splitWordsRegex)));
            for (String s : currentGameDescriptionWords) {
                if (stopWordsSet.contains(s)) {
                    continue;
                }
                if (allWords.containsKey(s)) {
                    allWords.get(s).add(bg.id());
                } else {
                    allWords.put(s, new HashSet<>());
                    allWords.get(s).add(bg.id());
                }
            }
        }
        return allWords;
    }

    private Set<Pair> calculateScores(Set<String> keywordsSet) {
        Set<Pair> score = new TreeSet<>((a, b) -> {
            if (Double.compare(a.score(), b.score()) != 0) {
                return Double.compare(b.score(), a.score());
            } else {
                return b.game().id() - a.game().id();
            }
        });

        for (BoardGame g : boardGamesList) {
            String[] descriptionWordsArr = g.description().toLowerCase().split(splitWordsRegex);
            Set<String> descriptionWordsLowerCase = new HashSet<>(List.of(descriptionWordsArr));

            stopWordsSet.stream().forEach(w -> descriptionWordsLowerCase.remove(w));

            int matches = 0;
            for (String s : keywordsSet) {
                if (descriptionWordsLowerCase.contains(s)) {
                    matches++;
                }
            }

            if (matches > 0) {
                score.add(new Pair(matches, g));
            }
        }
        return score;
    }

    private Set<BoardGame> filterGameList(BoardGame game) {
        Set<BoardGame> clearedGamesSet = new HashSet<>(boardGamesList);
        for (BoardGame g : boardGamesList) {
            boolean hasAtLeastOneSharedCategory = false;
            for (String s : game.categories()) {
                if (g.categories().contains(s)) {
                    hasAtLeastOneSharedCategory = true;
                    break;
                }
            }
            if (!hasAtLeastOneSharedCategory) {
                clearedGamesSet.remove(g);
            }
        }
        return clearedGamesSet;
    }

    private double calculateGameDistance(BoardGame to, BoardGame from) {
        double gScore = 0;

        gScore += Math.sqrt(Math.pow(to.playingTimeMins() - from.playingTimeMins(), 2)
                + Math.pow(to.maxPlayers() - from.maxPlayers(), 2)
                + Math.pow(to.minAge() - from.minAge(), 2)
                + Math.pow(to.minPlayers() - from.minPlayers(), 2));

        gScore += calculateDisjunctionConjunctionDifference(to, from);

        return gScore;
    }

    private int calculateDisjunctionConjunctionDifference(BoardGame to, BoardGame from) {
        int result = 0;

        Set<String> disjunctionCategories = new HashSet<>();
        disjunctionCategories.addAll(to.categories());
        disjunctionCategories.addAll(from.categories());

        Set<String> disjunctionMechanics = new HashSet<>();
        disjunctionMechanics.addAll(to.mechanics());
        disjunctionMechanics.addAll(from.mechanics());

        Set<String> disjunctionCategoriesMechanics = new HashSet<>(disjunctionMechanics);
        disjunctionCategoriesMechanics.addAll(disjunctionCategories);

        Set<String> conjunctionCategoriesMechanics = new HashSet<>();
        for (String s : disjunctionCategories) {
            if (to.categories().contains(s) && from.categories().contains(s)) {
                conjunctionCategoriesMechanics.add(s);
            }
        }
        for (String s : disjunctionMechanics) {
            if (to.mechanics().contains(s) && from.mechanics().contains(s)) {
                conjunctionCategoriesMechanics.add(s);
            }
        }

        result = disjunctionCategoriesMechanics.size() - conjunctionCategoriesMechanics.size();
        return result;
    }
}
