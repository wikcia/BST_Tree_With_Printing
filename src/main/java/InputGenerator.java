import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
  Class to generate 3 types of input: random, ascending ang descending
 */
public class InputGenerator {

    public static int[] randomGenerator(int n) {
        List<Integer> list = new ArrayList<>();
        Random generator = new Random();

        for(int i = 0; i < n; i++) {

            list.add(generator.nextInt(2 *n - 1)); // klucze są liczbami całkowitymi od zera do 2n-1
        }
        int[] arr = new int[list.size()];

        // Converting ArrayList to Array
        // using get() method
        for (int i = 0; i < list.size(); i++)
            arr[i] = list.get(i);

        return arr;

    }

    public static int[] ascendingGenerator(int n) {
        List<Integer> list = new ArrayList<>();
        Random generator = new Random();

        for(int i = 0; i < n; i++) {

            list.add(generator.nextInt(2*n - 1)); // klucze są liczbami całkowitymi od zera do 2n-1
        }
        Collections.sort(list);
        int[] arr = new int[list.size()];

        // Converting ArrayList to Array
        // using get() method
        for (int i = 0; i < list.size(); i++)
            arr[i] = list.get(i);

        return arr;
    }
}
