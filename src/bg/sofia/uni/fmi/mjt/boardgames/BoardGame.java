package bg.sofia.uni.fmi.mjt.boardgames;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record BoardGame(int id, String name, String description, int maxPlayers, int minAge, int minPlayers,
                        int playingTimeMins, Collection<String> categories, Collection<String> mechanics) {

    private static final int ID = 0;
    private static final int MAX_PLAYERS = 1;
    private static final int MIN_AGE = 2;
    private static final int MIN_PLAYERS = 3;
    private static final int NAME = 4;
    private static final int PLAYING_TIME = 5;
    private static final int CATEGORY = 6;
    private static final int MECHANIC = 7;
    private static final int DESCRIPTION = 8;

    public BoardGame(int id, String name, String description, int maxPlayers, int minAge, int minPlayers,
                     int playingTimeMins, Collection<String> categories, Collection<String> mechanics) {
        if (name == null || name.isEmpty() || description == null || maxPlayers < 0 ||
                minAge < 0) {
            throw new IllegalArgumentException("Error in arguments for constructing a BoardGame object");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.minAge = minAge;
        this.minPlayers = minPlayers;
        this.playingTimeMins = playingTimeMins;
        this.categories = categories;
        this.mechanics = mechanics;
    }

    public static BoardGame of(String line) {
        String[] tokens = line.split(";");

        int id = Integer.parseInt(tokens[ID]);
        String name = tokens[NAME];
        String description = tokens[DESCRIPTION];
        int maxPlayers = Integer.parseInt(tokens[MAX_PLAYERS]);
        int minAge = Integer.parseInt(tokens[MIN_AGE]);
        int minPlayers = Integer.parseInt(tokens[MIN_PLAYERS]);
        int playingTimeMins = Integer.parseInt(tokens[PLAYING_TIME]);

        Set<String> categories = new HashSet<>();
        String[] categoriesSplit = tokens[CATEGORY].split(",");
        for (String s : categoriesSplit) {
            categories.add(s);
        }

        Set<String> mechanics = new HashSet<>();
        String[] mechanicsSplit = tokens[MECHANIC].split(",");
        for (String s : mechanicsSplit) {
            mechanics.add(s);
        }

        return new BoardGame(id, name, description, maxPlayers, minAge,
                minPlayers, playingTimeMins, categories, mechanics);
    }

}
