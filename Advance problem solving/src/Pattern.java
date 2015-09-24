
public class Pattern {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int countOfStars = 1;
		int length = 5;
		int countOfSpaces = length / 2;
		int flag = 1;
		while(countOfStars > 0){
			if(flag == 1){
				for(int i = 0; i < countOfSpaces ; i++){
					System.out.print("  ");
				}
				for(int i = 0 ; i < countOfStars ; i++){
					System.out.print("* ");
				}
				System.out.println();
				countOfStars = countOfStars + 2;
				countOfSpaces--;
				if(countOfStars == length){
					flag = 0;
					
				}
			}
			else{
				for(int i = 0; i < countOfSpaces ; i++){
					System.out.print("  ");
				}
				for(int i = 0 ; i < countOfStars ; i++){
					System.out.print("* ");
				}
				System.out.println();
				countOfStars = countOfStars - 2;
				countOfSpaces++;
				
			}
		}
		
	}

}
