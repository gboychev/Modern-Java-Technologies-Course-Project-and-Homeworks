package bg.sofia.uni.fmi.mjt.boardgames;

import bg.sofia.uni.fmi.mjt.boardgames.analyzer.BoardGamesStatisticsAnalyzer;
import bg.sofia.uni.fmi.mjt.boardgames.recommender.BoardGamesRecommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ManualBoardGamesTestClass {
    public static void main(String[] args) throws IOException {
        BufferedReader br = Files.newBufferedReader(
                Path.of("D:\\Downloads\\FMI\\Y3 S1\\JAVA\\HOMEWORK2\\data\\unzippedDataExtract.txt"));

        BufferedReader br2 = Files.newBufferedReader(
                Path.of("D:\\Downloads\\FMI\\Y3 S1\\JAVA\\HOMEWORK2\\data\\unzippedDataExtract.txt"));

        BufferedReader brStop = Files.newBufferedReader(
                Path.of("D:\\Downloads\\FMI\\Y3 S1\\JAVA\\HOMEWORK2\\data\\stopwords.txt"));
        BoardGamesRecommender bgr = new BoardGamesRecommender(br, brStop);
        //System.out.println(bgr.getGames().add(BoardGame.of(br2.lines().skip(1).toList().get(1))));

        System.out.println(bgr.getSimilarTo(
                BoardGame.of(br2.lines().skip(1).toList().get(1))
                       , 2));

        Path zipPath = Path.of("D:\\Downloads\\FMI\\Y3 S1\\JAVA\\HOMEWORK2\\data\\data.zip");
        String fileName = "data.csv";
        BoardGamesRecommender bgr3 = new BoardGamesRecommender(zipPath, fileName,
                Path.of("D:\\Downloads\\FMI\\Y3 S1\\JAVA\\HOMEWORK2\\data\\stopwords.txt"));

        //System.out.println(bgr3.getGames());

        BoardGamesStatisticsAnalyzer bgsa = new BoardGamesStatisticsAnalyzer(bgr.getGames());
        System.out.println(bgsa.getNMostPopularCategories(5));
        //System.out.println(bgr.getByDescription("dark", "tower", "warriors", "kingdom"));
        System.out.println("hello");
    }
}
