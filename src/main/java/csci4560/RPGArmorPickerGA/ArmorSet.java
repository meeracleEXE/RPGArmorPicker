package csci4560.RPGArmorPickerGA;

import java.util.Arrays;

public class ArmorSet {
    Armor[] inventory;

    public ArmorSet(int size) {
        int[] runningAvg = {0,0,0};
        inventory = new Armor[size];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new Armor();
        }
    }

    public String toString() {
        String s = "[";
        for (Armor a : inventory) {
            s += a + ", ";
        }
        return s.substring(0,s.length()-2) + "]";
    }
}
