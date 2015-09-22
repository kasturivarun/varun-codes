import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class RemovingSpaces {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		//Scanner sc = new Scanner(System.in);
		/*char c1 = '%';
		int i = 20;
		while(sc.hasNext()){
			String c =  sc.next();
			if(c.equals(" ")){
				System.out.print(c);
				System.out.println(i);
			}
			else{
				System.out.print(c);
			}
		}*/
		String s = "This is not correct";
		String r = s.replace(" ", "%20");
		System.out.println(r);

	}

}
