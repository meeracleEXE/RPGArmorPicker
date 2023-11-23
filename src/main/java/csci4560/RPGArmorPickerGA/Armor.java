package csci4560.RPGArmorPickerGA;

import java.util.concurrent.ThreadLocalRandom;

public class Armor {
    /* minimum value for stat of Armor */
    private final int MIN_STAT = 2;
    /* maximum value for stat of Armor */
    private final int MAX_STAT = 30;
    /* maximum total value for all stats of Armor */
    private final int TOTAL_STAT = 55;

    /* Strength stat of Armor piece */
    int STR;
    /* Dexterity stat of Armor piece */
    int DEX;
    /* Intelligence stat of Armor piece */
    int INT;
    int[] stats;

    /**
     * Initializes an {@code Armor} object to have a stat between {@code MIN_STAT} and {@code MAX_STAT} inclusive,
     * with total of all stats adhering to {@code TOTAL_STAT}
     */
    public Armor() {
        this.STR = ThreadLocalRandom.current().nextInt(MIN_STAT, MAX_STAT + 1);
        this.DEX = ThreadLocalRandom.current().nextInt(MIN_STAT, Math.min(TOTAL_STAT - STR - 1, MAX_STAT + 1));
        this.INT = Math.min(MAX_STAT, TOTAL_STAT - STR - DEX);
        int remainder = TOTAL_STAT - STR - DEX - INT;
        stats = new int[]{STR, DEX, INT};

        while (remainder != 0) {
            int statChosen = ThreadLocalRandom.current().nextInt(0,3);
            if (stats[statChosen] < MAX_STAT) {
                stats[statChosen]++;
                remainder--;
            }
        }
        this.STR = stats[0];
        this.DEX = stats[1];
        this.INT = stats[2];
    }

    public String toString() {
        return "{" + this.stats[0] + ", " + this.stats[1] + ", " + this.stats[2] + "}";
    }
}
