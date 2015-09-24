
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
public class Factors {
    public static void main(String[] args) throws IOException {
        int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 };
        boolean[] isP = new boolean[101];
        int[] factors = new int[101];
 
        for (int i = 0; i < primes.length; i++)
            isP[primes[i]] = true;
 
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String s = bf.readLine();
        while (!s.equals("0")) {
            int N = Integer.parseInt(s);
            factors = new int[101];
            for (int i = 0; i < primes.length; i++)
                factors[primes[i]] = count(N, primes[i]);
            int i = 0;
            System.out.printf("%3d! =", N);
            for (i = 0; i < primes.length && factors[primes[i]] != 0; i++) {
                if (i == 15)
                    System.out.print("\n      ");
                System.out.printf("%3d", factors[primes[i]]);
            }
            System.out.println("");
            s = bf.readLine();
        }
    }
 
    static int count(int N, int x) {
        int count = 0;
        for (int i = x; i <= N; i *= x)
            count += N / i;
        return count;
    }
}