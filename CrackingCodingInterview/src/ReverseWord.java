import java.util.ArrayList;
import java.util.Scanner;


public class ReverseWord {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc =  new Scanner(System.in);
		String line = sc.nextLine();
		char[] chars = line.toCharArray();
		int j = chars.length - 1;
		for(int i= 0; i < chars.length/2 ; i++ ){
			char temp = chars[j];
			chars[j] = chars[i];
			chars[i] = temp;
			j--;
		}
		int spaceCount = 0;
		ArrayList<Integer> spaceIndex = new ArrayList<Integer>();
		for(int i= 0; i < chars.length ; i++ ){
			if(chars[i] == ' '){
				spaceCount++;
				spaceIndex.add(i);
			}
		}
		int start = 0;
		for(int i = 0; i < spaceIndex.size();i++ ){
			reverse(chars,start,spaceIndex.get(i));
			start = spaceIndex.get(i)+1;
		}
		if(start != (chars.length - 1)){
			reverse(chars,start,(chars.length));
		}
		for(int i = 0; i < chars.length ; i++){
			System.out.print(chars[i]);
		}
		

	}

	private static void reverse(char[] chars, int start, Integer space) {
		if((space - start) > 1){
			int  j = space - 1;
			for(int i = start; j-i>0; i++){
				char temp = chars[j];
				chars[j] = chars[i];
				chars[i] = temp;
				j--;
			}
		}
		
	}

}
