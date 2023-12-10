package csci4560.RPGArmorPickerGA;

import com.google.common.collect.MinMaxPriorityQueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class CompareGA<T>{
    int FIRST_PENALTY = 10;
    int SECOND_PENALTY = 5;
    int SMALL_PENALTY = 3;
    final int POPULATION_SIZE = 20;
    final int MAX_ITERATIONS = 10000;
    final int BIT_LENGTH = 5;
    int SIZE = (int) Math.pow(2,BIT_LENGTH);
    final Comparator customComparatorInt = (a, b) -> {
        if (calculateFitness((int[])a) > calculateFitness((int[])b))
            return -1;
        return 1;
    };
    final Comparator customComparatorBin = (a, b) -> {
        if (calculateFitness((String)a) > calculateFitness((String)b))
            return -1;
        return 1;
    };
    MinMaxPriorityQueue<int[]> populationInt = MinMaxPriorityQueue.orderedBy(customComparatorInt)
            .maximumSize(POPULATION_SIZE)
            .create();
    MinMaxPriorityQueue<String> populationBin = MinMaxPriorityQueue.orderedBy(customComparatorBin)
            .maximumSize(POPULATION_SIZE)
            .create();
    int[] priority = {1,2};
    int[] currentBestInt;
    String currentBestBin;
    double bestFitnessInt;
    double bestFitnessBin;
    String whoWon = "";
    ArmorSet[] inv = new ArmorSet[4];
    int GENERATIONS = 0;
    public CompareGA() {
        currentBestInt = new int[]{0, 0, 0, 0};
        currentBestBin = "";
        for (int i = 0; i < SIZE; i++) {
            currentBestBin += "0";
        }
        for (int i = 0; i < inv.length; i++) {
            inv[i] = new ArmorSet(SIZE);
        }
        // Intentionally replace last armor of each armorset with a perfect set
        inv[0].inventory[SIZE - 1] = new Armor(new int[]{6,27,22});
        inv[1].inventory[SIZE - 1] = new Armor(new int[]{4,23,28});
        inv[2].inventory[SIZE - 1] = new Armor(new int[]{3,28,24});
        inv[3].inventory[SIZE - 1] = new Armor(new int[]{7,22,26});
        double fitnessTotalInt = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] member = new int[]{
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE)
            };
            String s = "";
            for (int m : member) {
                for (int j = 0; j < 5 - Integer.toBinaryString(m).length(); j++) {
                    s += "0";
                }
                s += Integer.toBinaryString(m);
            }
            populationInt.add(member);
            populationBin.add(s);
        }
    }

    public void run() {
        while (calculateFitness(populationInt.peekFirst()) != 0 && calculateFitness(populationBin.peekFirst()) != 0 && GENERATIONS < MAX_ITERATIONS) {
            crossoverInt();
            crossoverBin();
            if (ThreadLocalRandom.current().nextInt(0,10) != 0) {
                mutationInt();
                mutationBin();
            }
            currentBestInt = populationInt.peekFirst();
            currentBestBin = populationBin.peekFirst();
            GENERATIONS++;
        }
        bestFitnessBin = calculateFitness(currentBestBin);
        bestFitnessInt = calculateFitness(currentBestInt);
        printStatsInt();
        printStatsBin();
        if (bestFitnessBin == 0 && bestFitnessInt != 0) {
            System.out.println("BINARY REPRESENTATION WON!");
            whoWon = "BIN";
        }
        else if (bestFitnessInt == 0 && bestFitnessBin != 0) {
            System.out.println("INTEGER REPRESENTATION WON!");
            whoWon = "INT";
        }
        else
            System.out.println("IT WAS A TIE!");
    }

        void crossoverInt() {
            int TOP_SIZE = 5;
            Object[] arr = populationInt.toArray();
            int p1Num = ThreadLocalRandom.current().nextInt(0, TOP_SIZE);
            int[] p1 = (int[]) arr[p1Num];
            int p2Num = ThreadLocalRandom.current().nextInt(0, TOP_SIZE);
            while (p2Num == p1Num) {
                p2Num = ThreadLocalRandom.current().nextInt(0, TOP_SIZE);
            }
            int[] p2 = (int[]) arr[p2Num];

            // CROSSOVER
            int crossoverPoint = ThreadLocalRandom.current().nextInt(0, 3);
            int[] c1 = new int[p1.length];
            int[] c2 = new int[p2.length];
            for (int i = 0; i < crossoverPoint; i++) {
                c1[i] = p1[i];
                c2[i] = p2[i];
            }
            for (int i = crossoverPoint; i < p1.length; i++) {
                c1[i] = p2[i];
                c2[i] = p1[i];
            }

            if (!contains(c1))
                populationInt.add(c1);
            if (!contains(c2))
                populationInt.add(c2);
        }

        boolean contains(int[] ind) {
            for (int[] item : populationInt) {
                if (Arrays.equals(item, ind))
                    return true;
            }
            return false;
        }

    void crossoverBin() {
        // Pick 2 individuals from TOP 5 individuals
        int TOP_SIZE = 5;
        Object[] arr = populationBin.toArray();
        int p1Num = ThreadLocalRandom.current().nextInt(0, TOP_SIZE);
        String p1 = (String) arr[p1Num];
        int p2Num = 0;
        do {
            p2Num = ThreadLocalRandom.current().nextInt(0, TOP_SIZE);
        } while (p2Num == p1Num);
        String p2 = (String) arr[p2Num];

        // 1-POINT CROSSOVER
        int crossoverPoint = ThreadLocalRandom.current().nextInt(1,BIT_LENGTH*4-1);
        String c1 = p1.substring(0,crossoverPoint) + p2.substring(crossoverPoint, p2.length());
        String c2 = p2.substring(0,crossoverPoint) + p1.substring(crossoverPoint, p1.length());
        boolean childAdded = false;

        if (!populationBin.contains(c1))
            populationBin.add(c1);
        if (!populationBin.contains(c2))
            populationBin.add(c2);
    }

        void mutationInt() {
            Object[] arr = populationInt.toArray();
            int[] item = (int[]) arr[ThreadLocalRandom.current().nextInt(10, arr.length)];
            item[ThreadLocalRandom.current().nextInt(0, 4)] = ThreadLocalRandom.current().nextInt(0, SIZE);
            populationInt.add(item);
    }

    void mutationBin() {
        Object[] arr = populationBin.toArray();
        String item = (String) arr[ThreadLocalRandom.current().nextInt(10, arr.length)];
        for (int i = 0; i < item.length(); i++) {
            if (ThreadLocalRandom.current().nextInt(2) == 0) {
                item = item.charAt(i) == '0' ? item.substring(0,i) + '1' + item.substring(i+1) : item.substring(0,i) + '0' + item.substring(i+1);
            }
        }
        populationBin.add(item);
    }
    int[] calculateStatDistribution(int[] equipped) {
        Armor helmet = inv[0].inventory[equipped[0]];
        Armor chestplate = inv[1].inventory[equipped[1]];
        Armor leggings = inv[2].inventory[equipped[2]];
        Armor boots = inv[3].inventory[equipped[3]];
        Armor[] currentEquipment = {helmet,chestplate,leggings,boots};

        int[] statDistribution = new int[3];
        for (Armor a : currentEquipment) {
            statDistribution[0] += a.stats[0];
            statDistribution[1] += a.stats[1];
            statDistribution[2] += a.stats[2];
        }

        return statDistribution;
    }

    int[] calculateStatDistribution(String equipped) {
        Armor helmet = inv[0].inventory[Integer.parseInt(equipped.substring(0,BIT_LENGTH),2)];
        Armor chestplate = inv[1].inventory[Integer.parseInt(equipped.substring(BIT_LENGTH, 2*BIT_LENGTH),2)];
        Armor leggings = inv[2].inventory[Integer.parseInt(equipped.substring(2*BIT_LENGTH, 3*BIT_LENGTH),2)];
        Armor boots = inv[3].inventory[Integer.parseInt(equipped.substring(3*BIT_LENGTH, 4*BIT_LENGTH),2)];
        Armor[] currentEquipment = {helmet, chestplate, leggings, boots};

        int[] statDistribution = new int[3];
        for (Armor a : currentEquipment) {
            statDistribution[0] += a.stats[0];
            statDistribution[1] += a.stats[1];
            statDistribution[2] += a.stats[2];
        }
        return statDistribution;
    }

    void printStatsInt() {
        int[] bestStats = calculateStatDistribution(currentBestInt);
        System.out.println("BEST ARMOR SET FOUND: \n" +
                "STR: " + bestStats[0] + "\n" +
                "DEX: " + bestStats[1] + "\n" +
                "INT: " + bestStats[2] + "\n" +
                "Fitness: " + calculateFitness(currentBestInt) + "\n" +
                "# of Generations: " + GENERATIONS);

        System.out.println("\nAbove was achieved using following armor pieces: \n" +
                "HELMET[" + currentBestInt[0] + "]: " + inv[0].inventory[currentBestInt[0]] + "\n" +
                "CHESTPLATE[" + currentBestInt[1] + "]: " + inv[1].inventory[currentBestInt[1]] + "\n" +
                "LEGGINGS[" + currentBestInt[2] + "]: " + inv[2].inventory[currentBestInt[2]] + "\n" +
                "BOOTS[" + currentBestInt[3] + "]: " + inv[3].inventory[currentBestInt[3]]+"\n");
    }

    void printStatsBin() {
        int[] bestStats = calculateStatDistribution(currentBestBin);
        System.out.println("BEST ARMOR SET FOUND: \n" +
                "STR: " + bestStats[0] + "\n" +
                "DEX: " + bestStats[1] + "\n" +
                "INT: " + bestStats[2] + "\n" +
                "Fitness: " + calculateFitness(currentBestBin) + "\n" +
                "# of Generations: " + GENERATIONS);

        System.out.println("\nAbove was achieved using following armor pieces: \n" +
                "HELMET[" + currentBestBin.substring(0,BIT_LENGTH) + "]: " + inv[0].inventory[Integer.parseInt(currentBestBin.substring(0,BIT_LENGTH),2)] + "\n" +
                "CHESTPLATE[" + currentBestBin.substring(BIT_LENGTH, 2*BIT_LENGTH) + "]: " + inv[1].inventory[Integer.parseInt(currentBestBin.substring(BIT_LENGTH, 2*BIT_LENGTH),2)] + "\n" +
                "LEGGINGS[" + currentBestBin.substring(2*BIT_LENGTH, 3*BIT_LENGTH) + "]: " + inv[2].inventory[Integer.parseInt(currentBestBin.substring(2*BIT_LENGTH, 3*BIT_LENGTH),2)] + "\n" +
                "BOOTS[" + currentBestBin.substring(3*BIT_LENGTH, 4*BIT_LENGTH) + "]: " + inv[3].inventory[Integer.parseInt(currentBestBin.substring(3*BIT_LENGTH, 4*BIT_LENGTH),2)] + "\n");
    }

    double calculateFitness(String s) {
        int[] statDistribution = calculateStatDistribution(s);

        int currentTotal = 0;
        currentTotal -= Math.max(0,((100 - statDistribution[priority[0]]) * FIRST_PENALTY));
        if (statDistribution[priority[0]] > 100) {
            currentTotal -= (statDistribution[priority[0]] - 100) * SMALL_PENALTY;
        }
        currentTotal -= Math.max(0,((100 - statDistribution[priority[1]]) * SECOND_PENALTY));
        if (statDistribution[priority[1]] > 100) {
            currentTotal -= (statDistribution[priority[1]] - 100) * SMALL_PENALTY;
        }
        return currentTotal;
    }

    double calculateFitness(int[] item) {
        int[] statDistribution = calculateStatDistribution(item);

        int currentTotal = 0;
        currentTotal -= Math.max(0,((100 - statDistribution[priority[0]]) * FIRST_PENALTY));
        if (statDistribution[priority[0]] > 100) {
            currentTotal -= (statDistribution[priority[0]] - 100) * SMALL_PENALTY;
        }
        currentTotal -= Math.max(0,((100 - statDistribution[priority[1]]) * SECOND_PENALTY));
        if (statDistribution[priority[1]] > 100) {
            currentTotal -= (statDistribution[priority[1]] - 100) * SMALL_PENALTY;
        }
        return currentTotal;
    }
}
