package no.ntnu.idata2001.g23;

import no.ntnu.idata2001.g23.items.Item;
import no.ntnu.idata2001.g23.items.weapons.Sword;

/**
 * Main class that boots the game.
 */
public class DungeonApp {
    public static void main(String[] args) {
        Item item = new Sword(69, 0.25, 500, "Test sword", "Test description");
        System.out.println(item);
    }
}
