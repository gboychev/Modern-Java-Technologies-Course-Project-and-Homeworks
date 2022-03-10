package bg.sofia.uni.fmi.mjt.game.recommender;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public record Game(String name, String platform, LocalDate releaseDate,
                   String summary, int metaScore, double userReview) {

    private static final int NAME = 0;
    private static final int PLATFORM = 1;
    private static final int RELEASE_DATE = 2;
    private static final int SUMMARY = 3;
    private static final int META_SCORE = 4;
    private static final int USER_REVIEW = 5;

    public static Game of(String line) {
        String[] splitLine = line.split(",");

        String name = splitLine[NAME];
        String platform = splitLine[PLATFORM];
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        //Date releaseDate = dateFormat.parse(splitLine[RELEASE_DATE], new ParsePosition(0));
        String[] dateSplit = splitLine[RELEASE_DATE].split("-");

        final int dayIndex = 0;
        final int monthIndex = 1;
        final int yearIndex = 2;
        String monthLetters = dateSplit[monthIndex];

        final int jan = 1;
        final int feb = 2;
        final int mar = 3;
        final int apr = 4;
        final int may = 5;
        final int jun = 6;
        final int jul = 7;
        final int aug = 8;
        final int sep = 9;
        final int oct = 10;
        final int nov = 11;
        final int dec = 12;
        int month = switch(monthLetters) {
            case "Jan" -> jan;
            case "Feb" -> feb;
            case "Mar" -> mar;
            case "Apr" -> apr;
            case "May" -> may;
            case "Jun" -> jun;
            case "Jul" -> jul;
            case "Aug" -> aug;
            case "Sep" -> sep;
            case "Oct" -> oct;
            case "Nov" -> nov;
            case "Dec" -> dec;
            default -> -1;
        };
        LocalDate releaseDate = LocalDate.of(
                Integer.parseInt(dateSplit[yearIndex]),
                month,
                Integer.parseInt(dateSplit[dayIndex]));
        String summary = splitLine[SUMMARY];
        int metaScore = Integer.parseInt(splitLine[META_SCORE]);
        double userReview = Double.parseDouble(splitLine[USER_REVIEW]);

        return new Game(name, platform, releaseDate, summary, metaScore, userReview);
    }
}
