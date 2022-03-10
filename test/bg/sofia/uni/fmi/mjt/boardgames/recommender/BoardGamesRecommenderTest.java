package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.analyzer.BoardGamesStatisticsAnalyzer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardGamesRecommenderTest {

    private static BoardGamesRecommender boardGamesRecommenderZip;
    private static BoardGamesRecommender boardGamesRecommender;

    private static BoardGame dieMacher = BoardGame.of("1;5;14;3;Die Macher;240;Economic,Negotiation,Political;Area Control " +
            "/ Area Influence,Auction/Bidding,Dice Rolling,Hand Management,Simultaneous Action Selection;Die Macher is a" +
            " game about seven sequential political races in different regions of Germany 1 1 first second third fourth.");
    private static BoardGame dragon = BoardGame.of("2;4;12;3;Dragonmaster;30;Economic,Card Game,Fantasy;Trick-taking;" +
            "Dragonmaster is a trick-taking card game based on an older game called Coup d'etat 1 3 2 2 2.");
    private static BoardGame sam = BoardGame.of("3;4;10;2;Samurai;60;Abstract Strategy,Ancient,Medieval;Area Control" +
            " / Area Influence,Hand Management,Set Collection,Tile Placement;Part of the Knizia tile-laying trilogy, Samurai is set in medieval Japan 4 3 3 4.");
    private static BoardGame tal = BoardGame.of("4;4;12;2;Tal der Könige;60;Ancient,Negotiation;Action Point Allowance" +
            " System,Area Control / Area Influence,Auction/Bidding,Set Collection;When you" +
            " see the triangular box and the luxurious, large blocks, you can tell this game was designed to be beautiful as well as functional 2 3 33.");
    private static BoardGame acq = BoardGame.of("5;6;12;3;Acquire;90;Economic;Hand Management,Stock Holding,Tile " +
            "Placement;In Acquire, each player strategically invests in businesses 1.");

    @BeforeAll
    static void setUp() throws IOException {
        final String datasetZipName = "data.zip";
        final String fileNameInDatasetZip = "data.csv";
        final String stopwordsFileName = "stopwords.txt";

        boardGamesRecommenderZip = new BoardGamesRecommender(Path.of(datasetZipName),
                fileNameInDatasetZip, Path.of(stopwordsFileName));

        try (InputStream is = Files.newInputStream(Path.of(datasetZipName))) {
            BufferedInputStream bis = new BufferedInputStream(is);
            ZipInputStream zis = new ZipInputStream(bis);

            ZipEntry ze;
            do {
                ze = zis.getNextEntry();
            } while (ze != null && !ze.getName().equals(fileNameInDatasetZip));
            if (ze == null) {
                throw new FileNotFoundException("Could not find the dataset file in the zip archive.");
            }

            ZipFile zf = new ZipFile(Path.of(datasetZipName).toString());
            InputStream is2 = zf.getInputStream(ze);
            InputStreamReader isr = new InputStreamReader(is2);
            BufferedReader bf = new BufferedReader(isr);
            BufferedReader brStopwords = Files.newBufferedReader(Path.of(stopwordsFileName));
            boardGamesRecommender = new BoardGamesRecommender(bf, brStopwords);
            is2.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dataset in setup test");
        }
    }

    @Test
    void testGetGames() {
        assertEquals(boardGamesRecommender.getGames(), boardGamesRecommenderZip.getGames());

        assertEquals(List.of(dieMacher, dragon, sam, tal, acq),boardGamesRecommender.getGames());
    }

    @Test
    void testGetSimilarTo() {
        BoardGame editedGameEntryCategoriesIncludeAllOtherGames = BoardGame.of("2;4;12;3;Dragonmaster;30;Economic" +
                ",Ancient,Fantasy;Trick-taking;Dragonmaster is a " +
                "trick-taking card game based on an older game called Coup d'etat 1 3 2 2 2.");
        assertEquals(List.of(tal, sam), boardGamesRecommender.getSimilarTo(editedGameEntryCategoriesIncludeAllOtherGames,2));

        BoardGame editedGameEntryCategoriesExcludeSomeGames = BoardGame.of("2;4;12;3;Dragonmaster;30;Economic," +
                "Abstract Strategy,Card Game,Fantasy;Trick-taking;Dragonmaster is a " +
                "trick-taking card game based on an older game called Coup d'etat 1 3 2 2 2.");
        assertEquals(List.of(sam, acq), boardGamesRecommender.getSimilarTo(editedGameEntryCategoriesExcludeSomeGames,2));

        //TODO if no match
    }

    @Test
    void testGetByDescription() {
        assertEquals(List.of(dragon, acq, tal,dieMacher), boardGamesRecommender.getByDescription("1", "2"));
    }

    @Test
    void testStoreGamesIndex() throws IOException {
        final String fileName = "indexFile.txt";
        final String detectCorrectFormatRegex = "^[^\\p{IsPunctuation}\\p{IsWhite_Space}]+:( [0-9]+$|( [0-9]+,)+ [0-9]+$)";
        File file = new File(fileName);
        FileWriter fw = new FileWriter(fileName);
        boardGamesRecommender.storeGamesIndex(fw);

        BufferedReader br = Files.newBufferedReader(Path.of(fileName));
        br.lines().toList().forEach(g -> {
            if(!g.matches(detectCorrectFormatRegex)) {
                fail();
            }
        });
    }
}