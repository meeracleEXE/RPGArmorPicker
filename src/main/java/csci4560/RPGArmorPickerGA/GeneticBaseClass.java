package csci4560.RPGArmorPickerGA;

import com.google.common.collect.MinMaxPriorityQueue;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public abstract class GeneticBaseClass<T> {
    int FIRST_PENALTY = 10;
    int SECOND_PENALTY = 5;
    int SMALL_PENALTY = 3;
    final int POPULATION_SIZE = 20;
    final int MAX_ITERATIONS = 1000;
    int SIZE = 50;
    final Comparator customComparator = (a,b) -> {
        if (calculateFitness((T) a) > calculateFitness((T)b))
            return -1;
        return 1;
    };
    MinMaxPriorityQueue<T> population = MinMaxPriorityQueue.orderedBy(customComparator)
            .maximumSize(POPULATION_SIZE)
            .create();
    int[] priority = {1,2};
    T currentBest;
    double bestFitnessRunningTotal;
    double meanBestFitness;
    double bestFitness;
    ArmorSet[] inv = new ArmorSet[4];
    int GENERATIONS = 0;

    public void run() {
        while (calculateFitness(population.peekFirst()) != 0 && GENERATIONS < MAX_ITERATIONS) {
            crossover();
            if (ThreadLocalRandom.current().nextInt(0,10) != 0) {
                mutation();
            }
            currentBest = population.peekFirst();
            bestFitnessRunningTotal += calculateFitness(currentBest);
            GENERATIONS++;
        }
        meanBestFitness = bestFitnessRunningTotal/GENERATIONS;
        bestFitness = calculateFitness(currentBest);
        printStats();
    }

    abstract void crossover();
    abstract void mutation();
    double calculateFitness(T t) {
        int[] statDistribution = calculateStatDistribution(t);

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

    abstract int[] calculateStatDistribution(T t);
    abstract void printStats();
}
