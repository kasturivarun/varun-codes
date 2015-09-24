import java.util.Scanner;


public class Problem1_2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(System.in);
		String str = sc.next();
		char[] c = str.toCharArray();
		int size = str.length();
		int end = size - 1;
		for(int i = 0 ; i < size/2 ; i++){
			char temp = c[i];
			c[i] = c[end];
			c[end] = temp;
			end--;
		}
		for(int i = 0 ; i < size ;i++){
			System.out.print(c[i]);
		}
		
	}

}
