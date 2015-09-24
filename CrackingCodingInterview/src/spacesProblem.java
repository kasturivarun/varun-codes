import java.util.Scanner;


public class spacesProblem {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(System.in);
		String given = sc.nextLine();
		System.out.println("Enter the length: ");
		int lengthString = sc.nextInt();
		char[] givenChars = given.toCharArray();
		int spaceCount = 0;
		for(int i = 0; i < lengthString; i++){
			if(givenChars[i] == ' '){
				spaceCount++;
			}
		}
		int newLength = lengthString + spaceCount*2;
		int temp = newLength;
		givenChars[newLength] = '\0';
		for(int i = lengthString - 1; i >= 0; i--){
			if(givenChars[i] == ' '){
				givenChars[newLength - 1] = '0';
				givenChars[newLength - 2] = '2';
				givenChars[newLength - 3] = '%';
				newLength = newLength - 3;
			}
			else{
				givenChars[newLength - 1] = givenChars[i];
				newLength = newLength - 1;
			}
		}
		
		for(int i = 0; i < temp;i++ ){
			System.out.print(givenChars[i]);
		}
		
	}

}
