
import java.util.Arrays;
import java.util.Scanner;


public class CommonPermutations {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String s1 = sc.nextLine();
			String s2 = sc.nextLine();
			boolean onceAdded = false;
			char c1[] = s1.toCharArray();
			char c2[] = s2.toCharArray();
			char c3[] = new char[c1.length + c2.length];
			int count = 0;
			Arrays.sort(c1);
			Arrays.sort(c2);
			for(int i = 0; i<c1.length ; i++){
				for(int j =0; j<c2.length ; j++){
					if(c1[i] == c2[j]){
						System.out.print(c1[i]);
						c2[j] = 0;
						break;
					}
					
					
				}
			}
		
		}
		long endTime = System.currentTimeMillis();
        long runTime =endTime - startTime; 
        System.out.println("Runtime:"+ runTime);

	}

}
