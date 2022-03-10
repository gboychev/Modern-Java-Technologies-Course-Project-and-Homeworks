package bg.sofia.uni.fmi.mjt.twitch.user;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImplementation;

import java.util.*;

public class UserAccount implements User {

    private static final int ESPORTS_NUM = 0;
    private static final int GAMES_NUM = 1;
    private static final int MUSIC_NUM = 2;
    private static final int IRL_NUM = 3;
    private static final int NUMBER_OF_CATEGORIES = 4;

    private String name;
    private UserStatus userStatus;
    private int viewsUserHasOnHisContent;
    private int[] categoriesWatchCounter;


    public UserAccount() {
        name = null;
        userStatus = null;
        viewsUserHasOnHisContent = 0;
        categoriesWatchCounter = null;
    }

    public UserAccount(String name) {
        this.name = name;
        this.userStatus = UserStatus.OFFLINE;
        this.viewsUserHasOnHisContent = 0;
        categoriesWatchCounter = new int[NUMBER_OF_CATEGORIES];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UserStatus getStatus() {
        return userStatus;
    }

    @Override
    public void setStatus(UserStatus status) {
        userStatus = status;
    }

    public int getViews() {
        return viewsUserHasOnHisContent;
    }

    public List<Category> getMostWatchedCategories() {
        List<Integer> categoriesCounterCopy = new ArrayList<Integer>();
        List<Category> result = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            categoriesCounterCopy.add(categoriesWatchCounter[i]);
        }
        //Finding the max value of the list, getting its index, then setting the max value in the list to -1,
        //so that we can find the second-highest value, then setting it to -1, so we can find the third-highest...
        for (int i = 0; i < categoriesCounterCopy.size(); i++) {
            int curMax = Integer.MIN_VALUE;
            int curMaxIndex = -1;
            for (int j = 0; j < categoriesCounterCopy.size(); j++) {
                if (categoriesCounterCopy.get(j) > 0) {
                    if (curMax < categoriesCounterCopy.get(j)) {
                        curMax = categoriesCounterCopy.get(j);
                        curMaxIndex = j;
                    }
                }
            }

            if (curMaxIndex > -1) {
                categoriesCounterCopy.set(curMaxIndex, -1);
                Category cat = switch (curMaxIndex) {
                    case ESPORTS_NUM -> Category.ESPORTS;
                    case GAMES_NUM -> Category.GAMES;
                    case IRL_NUM -> Category.IRL;
                    case MUSIC_NUM -> Category.MUSIC;
                    default -> null;
                };
                result.add(cat);
            }
        }
        return result;
    }

    public void startWatching(Content content) {
        if (content == null) {
            throw new IllegalArgumentException();
        }
        int contentCategoryNum = switch (content.getMetadata().getCategory()) {
            case GAMES -> GAMES_NUM;
            case ESPORTS -> ESPORTS_NUM;
            case IRL -> IRL_NUM;
            case MUSIC -> MUSIC_NUM;
            default -> -1;
        };
        categoriesWatchCounter[contentCategoryNum]++;
    }

    public void receiveView() {
        viewsUserHasOnHisContent++;
    }

    public void endStream(Stream stream) {
        if (viewsUserHasOnHisContent >= stream.getNumberOfViews()) {
            viewsUserHasOnHisContent -= stream.getNumberOfViews();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;

        UserAccount that = (UserAccount) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "name='" + name + '\'' +
                ", userStatus=" + userStatus +
                ", viewsUserHasOnHisContent=" + viewsUserHasOnHisContent +
                ", categoriesWatchCounter=" + Arrays.toString(categoriesWatchCounter) +
                '}';
    }
}
