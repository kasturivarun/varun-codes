import java.util.Scanner;


public class MineSweeper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		int size = sc.nextInt();
		char [][]c1 = new char[size+2][size+2];
		for(int i=0; i<size+2;i++){
			for(int j=0; j<size+2;j++){
				
				if(i == 0 || j == 0 || i == size+1 || j == size+1){
					c1[i][j] = '.';
				}
				else{
				String s1 = sc.next();
				char c2[] = s1.toCharArray();
				for(int k=0; k < size; k++){
					c1[i][j] = c2[k];
					j++;
				}
				c1[i][j] = '.';
				}
			}
		}
		
		
		System.out.println();
		for(int i=0; i<size+2;i++){
			for(int j=0; j<size+2;j++){
				
				System.out.print(c1[i][j]);
			}
			System.out.println();
			}

	}

}
