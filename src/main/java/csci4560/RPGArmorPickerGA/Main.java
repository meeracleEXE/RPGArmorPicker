package csci4560.RPGArmorPickerGA;

import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.println("RPG Armor Picker using GAs by Jun Jie Wong");
        String op;
        do {
            int whichRepresentation;
            int firstPriority;
            int secondPriority;

            System.out.println("What operation would you like to carry out?\n" +
                    "A - run GA once with default priority {1,2}\n" +
                    "B - run GA once with custom priority\n" +
                    "C - run GA x times with default priority {1,2}\n" +
                    "D - run GA x times with custom priority\n" +
                    "E - compare representations of both GAs\n" +
                    "F - set penalties\n" +
                    "Q - quit");
            op = kb.nextLine();
            if (op.equalsIgnoreCase("A")) {
                System.out.println("Which representation would you like to use? (0-integer, 1-binary)");
                whichRepresentation = kb.nextInt();
                kb.nextLine();
                oneRun(whichRepresentation);
            } else if (op.equalsIgnoreCase("B")) {
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
                oneRun(priority, whichRepresentation);
            } else if (op.equalsIgnoreCase("C")) {
                System.out.println("How many iterations?");
                int iterations = kb.nextInt();
                kb.nextLine();
                testSuccessRate(iterations);
            } else if (op.equalsIgnoreCase("D")) {
                System.out.println("How many iterations?");
                int iterations = kb.nextInt();
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
                testSuccessRate(iterations, priority);
            } else if (op.equalsIgnoreCase("E")) {
                System.out.println("How many iterations?");
                int iterations = kb.nextInt();
                kb.nextLine();
                testRepresentations(iterations);
            } else if (op.equalsIgnoreCase("F")) {
                System.out.println("How many iterations?");
                int iterations = kb.nextInt();
                kb.nextLine();
                System.out.print("FIRST PENALTY: ");
                int first = kb.nextInt();
                kb.nextLine();
                System.out.print("SECOND PENALTY: ");
                int second = kb.nextInt();
                kb.nextLine();
                System.out.print("SMALL PENALTY: ");
                int small = kb.nextInt();
                kb.nextLine();
                testPenalty(iterations, new int[]{first, second, small});
            }
        } while (!op.equalsIgnoreCase("Q"));

    }

    private static void oneRun(int[] priority, int which) {
        if (which == 0) {
            IntGA armorPicker = new IntGA(priority);
            armorPicker.run();
        } else {
            BinaryStringGA armorPicker = new BinaryStringGA(priority);
            armorPicker.run();
        }
    }

    private static void oneRun(int which) {
        if (which == 0) {
            IntGA armorPicker = new IntGA();
            armorPicker.run();
        } else {
            BinaryStringGA armorPicker = new BinaryStringGA();
            armorPicker.run();
        }
    }

    private static void testSuccessRate(int iterations) {
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA();
            armorPicker.run();
            if (armorPicker.bestFitness == 0)
                counter++;
        }
        System.out.printf("%nSUCCESS RATE: %.2f%% (%d/%d)%n",(double)counter/iterations*100, counter, iterations);
    }

    private static void testSuccessRate(int iterations, int[] priority) {
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA(priority);
            armorPicker.run();
            if (armorPicker.bestFitness == 0)
                counter++;
        }
        System.out.printf("%nSUCCESS RATE: %.2f%% (%d/%d)%n",(double)counter/iterations*100, counter, iterations);
    }

    private static void testPenalty(int iterations, int[] penalties) {
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA(penalties[0],penalties[1],penalties[2]);
            armorPicker.run();
            if (armorPicker.bestFitness == 0)
                counter++;
        }
        System.out.printf("%nSUCCESS RATE: %.2f%% (%d/%d)%n",(double)counter/iterations*100, counter, iterations);
    }

    private static void testRepresentations(int iterations) {

        int intCounter = 0;
        int intMean = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA();
            armorPicker.run();
            intMean += armorPicker.meanBestFitness;
            if (armorPicker.bestFitness == 0)
                intCounter++;
        }
        long endTime = System.nanoTime();
        long intDuration = endTime - startTime;

        startTime = System.nanoTime();
        int binCounter = 0;
        int binMean = 0;
        for (int i = 0; i < iterations; i++) {
            BinaryStringGA armorPicker = new BinaryStringGA();
            armorPicker.run();
            binMean += armorPicker.meanBestFitness;
            if (armorPicker.bestFitness == 0)
                binCounter++;
        }
        endTime = System.nanoTime();
        long binDuration = endTime - startTime;

        System.out.printf("%nINT SUCCESS RATE: %.2f%% (%d/%d)%n",(double)intCounter/iterations*100, intCounter, iterations);
        System.out.println("INT TOTAL TIME ELAPSED: " + intDuration);
        System.out.println("INT AVG TIME ELAPSED: " + intDuration/iterations);
        System.out.println("AVG MEAN BEST FITNESS: " + intMean/iterations);
        System.out.println();
        System.out.printf("BIN SUCCESS RATE: %.2f%% (%d/%d)%n",(double)binCounter/iterations*100, binCounter, iterations);
        System.out.println("BIN TIME ELAPSED: " + binDuration);
        System.out.println("BIN AVG TIME ELAPSED: " + binDuration/iterations);
        System.out.println("AVG MEAN BEST FITNESS: " + binMean/iterations);
    }
}