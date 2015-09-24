import java.util.Scanner;


public class OddAndEvenOneSide {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc =  new Scanner(System.in);
		int size = sc.nextInt();
		int[] array = new int[size];
		System.out.println("Enter the elements now");
		for(int i = 0 ; i < size; i++){
			array[i] = sc.nextInt();
		}
		int odd = 0;
		int even = size - 1;
		while(odd < even){
			if(array[odd] % 2 == 0){
				if(array[even] % 2 != 0){
					exchange(array,odd,even);
				}
				else{
					even--;
					continue;
				}
			}
			else{
				odd++;
			}
		}
		for(int i = 0 ; i < size; i++){
			System.out.print(array[i]+" ");
		}
		

	}

	private static void exchange(int[] array, int odd, int even) {
		// TODO Auto-generated method stub
		
		int temp = array[even];
		array[even] = array[odd];
		array[odd] = temp;
		
	}

}
