package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.recommender.BoardGamesRecommender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardGamesStatisticsAnalyzerTest {


    private static BoardGamesRecommender boardGamesRecommenderZip;
    private static BoardGamesStatisticsAnalyzer boardGamesStatisticsAnalyzer;

    private static final String datasetZipName = "data.zip";
    private static final String fileNameInDatasetZip = "data.csv";
    private static final String stopwordsFileName = "stopwords.txt";

    @BeforeAll
    static void setUpTestCase() throws IOException {

        boardGamesRecommenderZip = new BoardGamesRecommender(Path.of(datasetZipName),
                fileNameInDatasetZip, Path.of(stopwordsFileName));
        boardGamesStatisticsAnalyzer = new BoardGamesStatisticsAnalyzer(boardGamesRecommenderZip.getGames());
    }

    @Test
    void testGetNMostPopularCategories() {
        assertEquals(boardGamesStatisticsAnalyzer.getNMostPopularCategories(1), List.of("Economic"));
        assertEquals(boardGamesStatisticsAnalyzer.getNMostPopularCategories(5), List.of("Economic", "Ancient",
                "Negotiation", "Abstract Strategy", "Card Game"));
    }

    @Test
    void testGetAverageMinAge() {
        assertEquals(12 , boardGamesStatisticsAnalyzer.getAverageMinAge());
    }

    @Test
    void testGetAveragePlayingTimeByCategorySingle() {
        assertEquals(120, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("Economic"));
        assertEquals(150, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("Negotiation"));
        assertEquals(60, boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory("Ancient"));
    }

    @Test
    void testGetAveragePlayingTimeByCategoryAll() {
        Map<String, Double> test = boardGamesStatisticsAnalyzer.getAveragePlayingTimeByCategory();
        assertEquals(120, test.get("Economic"));
        assertEquals(150, test.get("Negotiation"));
        assertEquals(60, test.get("Ancient"));
    }
}