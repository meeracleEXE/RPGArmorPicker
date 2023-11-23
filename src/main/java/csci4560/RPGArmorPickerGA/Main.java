package csci4560.RPGArmorPickerGA;

import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        int whichRepresentation;
        int firstPriority;
        int secondPriority;

        System.out.println("RPG Armor Picker using GAs by Jun Jie Wong");
        System.out.println("Which representation would you like to use? (0-integer, 1-binary)");
        whichRepresentation = kb.nextInt();
        kb.nextLine();
        System.out.println("Which stat would be your first priority? (0-Strength, 1-Dexterity, 2-Intelligence)");
        firstPriority = kb.nextInt();
        kb.nextLine();
        do {
            System.out.println("Which stat would be your second priority? (0-Strength, 1-Dexterity, 2-Intelligence)");
            secondPriority = kb.nextInt();
            kb.nextLine();
        } while (firstPriority == secondPriority);
        int[] priority = {firstPriority, secondPriority};
        oneRun(whichRepresentation, priority);
        //testSuccessRate(1000);
    }

    private static void oneRun(int which, int[] priority) {
        CustomGA armorPicker = new CustomGA(which, priority);
        armorPicker.run();
    }

    private static void testSuccessRate(int iterations) {
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            CustomGA armorPicker = new CustomGA();
            armorPicker.run();
            if (armorPicker.bestFitness == 0)
                counter++;
        }
        System.out.printf("%nSUCCESS RATE: %.2f%% (%d/%d)%n",(double)counter/iterations*100, counter, iterations);
    }
}