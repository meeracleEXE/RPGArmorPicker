package csci4560.RPGArmorPickerGA;

public class Main{

    public static void main(String[] args) {
        oneRun();
        //testSuccessRate(1000);
    }

    private static void oneRun() {
        CustomGA armorPicker = new CustomGA();
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