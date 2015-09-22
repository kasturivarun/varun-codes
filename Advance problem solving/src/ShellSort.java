import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
 
public class ShellSort {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int cases = cin.nextInt();
        while (cases-- != 0) {
            int n = cin.nextInt();
            cin.nextLine();
            String[] src = new String[n];
            String[] trg = new String[n];
            for (int i = 0; i < n; ++i)
                src[i] = cin.nextLine();
            for (int i = 0; i < n; ++i)
                trg[i] = cin.nextLine();
            int srcIndex = n - 1;
            int trgIndex = n - 1;
            int res = 0;
            while (srcIndex >= 0 && trgIndex >= 0) {
                if (src[srcIndex].equals(trg[trgIndex])) {
                    --srcIndex;
                    --trgIndex;
                } else {
                    ++res;
                    --srcIndex;
                }
            }
            for (int i = res - 1; i >= 0; --i)
                System.out.println(trg[i]);
            System.out.println();
        }
    }
 
}