package csci4560.RPGArmorPickerGA;

import java.util.concurrent.ThreadLocalRandom;

public class BinaryStringGA extends GeneticBaseClass<String>{
    final int BIT_LENGTH = 5;
    int SIZE = (int) Math.pow(2,BIT_LENGTH);

    public BinaryStringGA() {
        for (int i = 0; i < SIZE; i++) {
            currentBest += "0";
        }
        for (int i = 0; i < inv.length; i++) {
            inv[i] = new ArmorSet(SIZE);
        }
        for (int i = 0; i < POPULATION_SIZE; i++) {
            String s = "";
            for (int j = 0; j < BIT_LENGTH*4; j++) {
                s+= ThreadLocalRandom.current().nextInt(2);
            }
            population.add(s);
        }
        System.out.println("STARTING SETS OF ARMOR: \n" +
                "HELMET: " + inv[0] + "\n" +
                "CHESTPLATE: "+ inv[1] + "\n" +
                "LEGGINGS: "+ inv[2] + "\n" +
                "BOOTS: "+ inv[3] + "\n");
    }

    public BinaryStringGA(int[] priority) {
        this();
        this.priority = priority;
    }

    public BinaryStringGA(int first, int second, int small) {
        this();
        this.FIRST_PENALTY = first;
        this.SECOND_PENALTY = second;
        this.SMALL_PENALTY = small;
    }

    void crossover() {
        // Pick 2 individuals from TOP 5 individuals
        int TOP_SIZE = 5;
        Object[] arr = population.toArray();
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
        population.add(c1);
        population.add(c2);
    }

    void mutation() {
        Object[] arr = population.toArray();
        String item = (String) arr[ThreadLocalRandom.current().nextInt(10, arr.length)];
        for (int i = 0; i < item.length(); i++) {
            if (ThreadLocalRandom.current().nextInt(2) == 0) {
                item = item.charAt(i) == '0' ? item.substring(0,i) + '1' + item.substring(i+1) : item.substring(0,i) + '0' + item.substring(i+1);
            }
        }
        population.add(item);
    }

    @Override
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
                "HELMET[" + currentBest.substring(0,BIT_LENGTH) + "]: " + inv[0].inventory[Integer.parseInt(currentBest.substring(0,BIT_LENGTH),2)] + "\n" +
                "CHESTPLATE[" + currentBest.substring(BIT_LENGTH, 2*BIT_LENGTH) + "]: " + inv[1].inventory[Integer.parseInt(currentBest.substring(BIT_LENGTH, 2*BIT_LENGTH),2)] + "\n" +
                "LEGGINGS[" + currentBest.substring(2*BIT_LENGTH, 3*BIT_LENGTH) + "]: " + inv[2].inventory[Integer.parseInt(currentBest.substring(2*BIT_LENGTH, 3*BIT_LENGTH),2)] + "\n" +
                "BOOTS[" + currentBest.substring(3*BIT_LENGTH, 4*BIT_LENGTH) + "]: " + inv[3].inventory[Integer.parseInt(currentBest.substring(3*BIT_LENGTH, 4*BIT_LENGTH),2)] + "\n");
    }
}
