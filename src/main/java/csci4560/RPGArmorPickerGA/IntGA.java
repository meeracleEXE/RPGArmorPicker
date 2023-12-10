package csci4560.RPGArmorPickerGA;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class IntGA extends GeneticBaseClass<int[]> {

    public IntGA() {
        currentBest = new int[]{0, 0, 0, 0};
        SIZE = 32;
        for (int i = 0; i < inv.length; i++) {
            inv[i] = new ArmorSet(SIZE);
        }
        double fitnessTotal = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] member = new int[]{
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE)
            };
            population.add(member);
            fitnessTotal += calculateFitness(member);
        }
        System.out.println("STARTING MEAN FITNESS: " + fitnessTotal/POPULATION_SIZE);
        System.out.println("STARTING SETS OF ARMOR: \n" +
                "HELMET: " + inv[0] + "\n" +
                "CHESTPLATE: "+ inv[1] + "\n" +
                "LEGGINGS: "+ inv[2] + "\n" +
                "BOOTS: "+ inv[3] + "\n");
    }

    public IntGA(int[] priority) {
        this();
        this.priority = priority;
    }

    public IntGA(int first, int second, int small) {
        this();
        this.FIRST_PENALTY = first;
        this.SECOND_PENALTY = second;
        this.SMALL_PENALTY = small;
    }
    /**
     * 1 - point crossover, with random line drawn.
     */
    void crossover() {
        int TOP_SIZE = 5;
        Object[] arr = population.toArray();
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
            population.add(c1);
        if (!contains(c2))
            population.add(c2);
    }

    boolean contains(int[] ind) {
        for (int[] item : population) {
            if (Arrays.equals(item, ind))
                return true;
        }
        return false;
    }

    /* uniformMutation for random member of population */
    void mutation() {
        Object[] arr = population.toArray();
        int[] item = (int[]) arr[ThreadLocalRandom.current().nextInt(10, arr.length)];
        item[ThreadLocalRandom.current().nextInt(0, 4)] = ThreadLocalRandom.current().nextInt(0, SIZE);
        population.add(item);
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

    void printStats() {
        int[] bestStats = calculateStatDistribution(currentBest);
        System.out.println("BEST ARMOR SET FOUND: \n" +
                "STR: " + bestStats[0] + "\n" +
                "DEX: " + bestStats[1] + "\n" +
                "INT: " + bestStats[2] + "\n" +
                "Fitness: " + calculateFitness(currentBest) + "\n" +
                "Mean Best Fitness: " + meanBestFitness + "\n" +
                "# of Generations: " + GENERATIONS);

        System.out.println("\nAbove was achieved using following armor pieces: \n" +
                "HELMET[" + currentBest[0] + "]: " + inv[0].inventory[currentBest[0]] + "\n" +
                "CHESTPLATE[" + currentBest[1] + "]: " + inv[1].inventory[currentBest[1]] + "\n" +
                "LEGGINGS[" + currentBest[2] + "]: " + inv[2].inventory[currentBest[2]] + "\n" +
                "BOOTS[" + currentBest[3] + "]: " + inv[3].inventory[currentBest[3]]+"\n");
    }

    void printMember(int[] member) {
        System.out.print("[");
        for (int i = 0; i < member.length; i++) {
            System.out.print(member[i] + ", ");
        }
        System.out.print("]");
    }
}
