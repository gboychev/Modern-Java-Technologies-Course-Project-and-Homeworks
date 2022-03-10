package bg.sofia.uni.fmi.mjt.game.recommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class ManualTestClass {
    public static void main(String[] args) throws IOException {
        BufferedReader br = Files.newBufferedReader(Path.of("D:\\Downloads\\FMI\\Y3 S1\\JAVA\\lab8\\Dataset\\testFile.txt"));
        GameRecommender gameRecommender = new GameRecommender(br);
        System.out.println(gameRecommender.getAllGames());
        LocalDate.of(1,1,1);
        gameRecommender.getGamesSimilarTo("as", "all");
    }
}
