package csci4560.RPGArmorPickerGA;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import com.google.common.collect.MinMaxPriorityQueue;

public class CustomGA {
    private final int SIZE = 25;
    private final int FIRST_PENALTY = 10;
    private final int SECOND_PENALTY = 5;
    private final int POPULATION_SIZE = 30;
    private final int MAX_ITERATIONS = 300;
    private int GENERATIONS = 0;
    private final Comparator customComparator = (a,b) -> {
        if (calculateFitness((int[]) a) > calculateFitness((int[]) b))
            return -1;
        return 1;
    };
    private MinMaxPriorityQueue<int[]> population = MinMaxPriorityQueue.orderedBy(customComparator)
            .maximumSize(POPULATION_SIZE)
            .create();
    private int[] priority = {1,2}; // Default: STR highest priority, DEX second priority
    private int[] currentBest = new int[]{0,0,0,0};
    private double bestFitnessRunningTotal;
    private double meanBestFitness;
    public double bestFitness;
    ArmorSet[] inv = new ArmorSet[4];

    public CustomGA() {
        for (int i = 0; i < inv.length; i++) {
            inv[i] = new ArmorSet(25);
        }
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new int[]{
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
                    ThreadLocalRandom.current().nextInt(0, SIZE),
            });
        }
        System.out.println("STARTING SETS OF ARMOR: \n" +
                "HELMET: " + inv[0] + "\n" +
                "CHESTPLATE: "+ inv[1] + "\n" +
                "LEGGINGS: "+ inv[1] + "\n" +
                "BOOTS: "+ inv[1] + "\n");
    }

    public CustomGA(int which, int[] priority) {
        this();
        this.priority = priority;
    }

    public void run() {
        while (calculateFitness(population.peekFirst()) != 0 && GENERATIONS < MAX_ITERATIONS) {
            crossover();
            if (ThreadLocalRandom.current().nextInt(0, 20) != 0) {
                uniformMutation();
            }
            currentBest = population.peekFirst();
            bestFitnessRunningTotal += calculateFitness(currentBest);
            GENERATIONS++;
        }
        meanBestFitness = bestFitnessRunningTotal/GENERATIONS;
        bestFitness = calculateFitness(currentBest);
        printStats();
    }

    /**
     * 1 - point crossover, with random line drawn.
     */
    private void crossover() {
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
        population.add(c1);
        population.add(c2);
    }

    /* uniformMutation for random member of population */
    private void uniformMutation() {
        Object[] arr = population.toArray();
        int[] item = (int[]) arr[ThreadLocalRandom.current().nextInt(10, arr.length)];
        item[ThreadLocalRandom.current().nextInt(0, 4)] = ThreadLocalRandom.current().nextInt(0, SIZE);
        population.add(item);
    }

    public double calculateFitness(int[] equipped) {
        int[] statDistribution = calculateStatDistribution(equipped);

        int currentTotal = 0;
        currentTotal -= Math.max(0,((100 - statDistribution[priority[0]]) * FIRST_PENALTY));
        currentTotal -= Math.max(0,((100 - statDistribution[priority[1]]) * SECOND_PENALTY));

        return currentTotal;
    }

    private int[] calculateStatDistribution(int[] equipped) {
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

    public void printStats() {
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
                "BOOTS[" + currentBest[3] + "]: " + inv[3].inventory[currentBest[3]]);
    }
}
