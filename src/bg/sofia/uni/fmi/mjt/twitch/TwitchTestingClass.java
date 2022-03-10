package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImplementation;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImplementation;
import bg.sofia.uni.fmi.mjt.twitch.user.UserAccount;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserServiceMap;

public class TwitchTestingClass {
    public static void main(String[] args) {
        UserAccount user1 = new UserAccount("streamer1");
        UserAccount xqc = new UserAccount("xqc");
        UserAccount forsen = new UserAccount("forsen");
        UserAccount baj = new UserAccount("baj");
        UserAccount baj2 = new UserAccount("baj2");
        UserServiceMap userMap = new UserServiceMap();
        userMap.addUser(user1);
        userMap.addUser(xqc);
        userMap.addUser(forsen);
        userMap.addUser(baj);
        userMap.addUser(baj2);
        StreamingPlatform twitchPlatform = new Twitch(userMap);
        Stream xqcStream = twitchPlatform.startStream("xqc", "JUICER WARLORD GAMING GOLEM CONQUERS EVERY DIMENSION",
                Category.IRL);
        Stream forsenStream = twitchPlatform.startStream("forsen", "Games n sht",
                Category.GAMES);
        Stream baj2Stream = twitchPlatform.startStream("baj2", "fpl",
                Category.ESPORTS);
        twitchPlatform.watch("baj", forsenStream);
        twitchPlatform.watch("baj", forsenStream);
        twitchPlatform.watch("baj", forsenStream);
        System.out.println(twitchPlatform.getMostWatchedCategoriesBy("baj"));

        twitchPlatform.watch("baj", xqcStream);
        twitchPlatform.watch("baj", xqcStream);
        twitchPlatform.watch("baj", xqcStream);
        System.out.println("GETNUMBER OF VIEWS " + xqcStream.getNumberOfViews());
        twitchPlatform.watch("baj", baj2Stream);

        System.out.println("Most watched streamer row 40: " + twitchPlatform.getMostWatchedStreamer());
        System.out.println("Most watched CONTENT row 41: " + twitchPlatform.getMostWatchedContent());
        System.out.println("sorted categories");
        System.out.println(twitchPlatform.getMostWatchedCategoriesBy("baj"));

        Video xqcVid = twitchPlatform.endStream("xqc", xqcStream);
        twitchPlatform.watch("streamer1", xqcVid );
        twitchPlatform.watch("streamer1", xqcVid );
        twitchPlatform.watch("streamer1", xqcVid );
        System.out.println("GETNUMBER OF VIEWS VID " + xqcVid.getNumberOfViews());
        System.out.println(twitchPlatform.getMostWatchedStreamer());
        System.out.println("Most WATCHED CONTENT ROW 50: " + twitchPlatform.getMostWatchedContent());
        System.out.println("row 48");
        System.out.println(twitchPlatform.getMostWatchedContentFrom("xqc"));
        System.out.println("success");

    }
}
