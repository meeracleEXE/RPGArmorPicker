package csci4560.RPGArmorPickerGA;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
                    "G - run GA competition\n" +
                    "H - run time taken to reach success\n" +
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
            } else if (op.equalsIgnoreCase("G")) {
                System.out.println("How many iterations?");
                int iterations = kb.nextInt();
                kb.nextLine();
                runCompetitionMultiple(iterations);
            } else if (op.equalsIgnoreCase("H")) {
                System.out.println("How many iterations?");
                int iterations = kb.nextInt();
                kb.nextLine();
                testTimeTakenSuccess(iterations);
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

    private static void testTimeTakenSuccess(int iterations) {
        int counter = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA();
            armorPicker.run("RUNTIME");
            if (armorPicker.bestFitness == 0) {
                counter ++;
            }
        }
        long endTime = System.nanoTime();
        System.out.printf("%nSUCCESS RATE: %.2f%% (%d/%d)%n",(double)counter/iterations*100, counter, iterations);
        System.out.println("AVERAGE TIME TO REACH: " + (endTime - startTime)/iterations);
    }

    private static void testSuccessRate(int iterations) {
        int gen = 0;
        int counter = 0;
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA();
            armorPicker.run("SUCCESS");
            if (armorPicker.bestFitness == 0) {
                counter++;
                gen += armorPicker.GENERATIONS;
            }
        }
        System.out.printf("%nSUCCESS RATE: %.2f%% (%d/%d)%n",(double)counter/iterations*100, counter, iterations);
        System.out.printf("AVERAGE # OF GEN: %d%n",gen/counter);
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
        int top5MeanInt = 0;
        int top5MeanBin = 0;
        int intCounter = 0;
        int intMean = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            IntGA armorPicker = new IntGA();
            armorPicker.run();
            intMean += armorPicker.bestFitness;
            top5MeanInt += armorPicker.meanTop5Fitness;
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
            binMean += armorPicker.bestFitness;
            top5MeanBin += armorPicker.meanTop5Fitness;
            if (armorPicker.bestFitness == 0)
                binCounter++;
        }
        endTime = System.nanoTime();
        long binDuration = endTime - startTime;

        System.out.printf("%nINT SUCCESS RATE: %.2f%% (%d/%d)%n",(double)intCounter/iterations*100, intCounter, iterations);
        System.out.println("INT TOTAL TIME ELAPSED: " + intDuration);
        System.out.println("INT AVG TIME ELAPSED: " + intDuration/iterations);
        System.out.println("AVG MEAN BEST FITNESS: " + intMean/iterations);
        System.out.println("AVG TOP 5 FITNESSES: " + top5MeanInt/iterations);
        System.out.println();
        System.out.printf("BIN SUCCESS RATE: %.2f%% (%d/%d)%n",(double)binCounter/iterations*100, binCounter, iterations);
        System.out.println("BIN TIME ELAPSED: " + binDuration);
        System.out.println("BIN AVG TIME ELAPSED: " + binDuration/iterations);
        System.out.println("AVG MEAN BEST FITNESS: " + binMean/iterations);
        System.out.println("AVG TOP 5 FITNESSES: " + top5MeanBin/iterations);
    }

    private static void runCompetition() {
        CompareGA cp = new CompareGA();
        cp.run();
    }

    private static void runCompetitionMultiple(int iterations) {
        int intWin = 0, binWin = 0;
        for (int i = 0; i < iterations; i++) {
            CompareGA cp = new CompareGA();
            cp.run();
            if (cp.whoWon.equalsIgnoreCase("INT")) {
                intWin++;
            } else if (cp.whoWon.equalsIgnoreCase("BIN")) {
                binWin++;
            }
        }
        System.out.println(iterations + " COMPETITION STATS:" );
        System.out.printf("INT WON %d TIMES! (%.2f%%)%n", intWin, (double)intWin/iterations * 100);
        System.out.printf("BIN WON %d TIMES! (%.2f%%)%n", binWin, (double)binWin/iterations * 100);
        System.out.printf("TIMEOUT OR DRAW %d TIMES! (%.2f%%)%n", iterations-intWin-binWin, (double)(iterations-intWin-binWin)/iterations*100);
    }
}