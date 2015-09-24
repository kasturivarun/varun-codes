import java.util.Scanner;


public class Problem1_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		String str = sc.next();
		boolean flag =  false;
		for(int i = 0; i < str.length(); i++){
			for(int j = i+1 ; j < str.length(); j++){
				if(str.charAt(i) == str.charAt(j)){
					flag = true;
				}
			}
		}
		
		if(flag){
			System.out.println("yes");
		}
		else{
			System.out.println("No");
		}

	}

}
