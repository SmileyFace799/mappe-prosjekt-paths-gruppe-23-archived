package no.ntnu.idata2001.g23.model.entities;

/**
 * Builds a desired player with all mandatory attributes and a combination of optional attributes.
 */
public class PlayerBuilder {
    private final String name; //required
    private int health = 100; //optional
    private int score = 0; //optional
    private int gold = 0; //optional

    public PlayerBuilder(String name) {
        this.name = name;
    }

    public PlayerBuilder withHealth(int health) {
        this.health = health;
        return this;
    }

    public PlayerBuilder withScore(int score) {
        this.score = score;
        return this;
    }

    public PlayerBuilder withGold(int gold) {
        this.gold = gold;
        return this;
    }

    //public Player build() {
    //    return Player(this);
    //}

    // How to use the PlayerBuilder in main app:

    // *creating a player with required quality "name" only*
    //Player player1 = new Player.PlayerBuilder("John Doe").build();
    //System.out.println(player1);

    // *creating a player with required quality "name" and optional qualities "health" and "gold"*
    //Player player2 = new Player.PlayerBuilder("Jane Smith").withHealth(150).withGold(500).build();
    //System.out.println(player2);

    // *creating a player with required quality "name" and optional quality "score"*
    //Player player3 = new Player.PlayerBuilder("Bob Johnson").withScore(1000).build();
    //System.out.println(player3);
}