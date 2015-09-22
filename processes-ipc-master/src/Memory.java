
/**
 * It will consist of 2000 integer entries, 0-999 for the program, 1000-1999 for
 * the interrupt handler. The program cannot access addresses above 999 (exits
 * with error message). Memory will initialize itself by reading a file for the
 * program and one for the interrupt handler (if used). Each line in the file
 * will hold one integer instruction or operand, optionally followed by a
 * comment. It will support two operations: read(address) returns the value at
 * the address write(address, data) writes the data to the address
 *
 * @author Blessing Osakue
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory extends BaseClass {

    private static int[] data = new int[2000];
    private int programBoundary = 0;

    public static void main(String args[]) throws FileNotFoundException {
        Memory mem = new Memory();
        mem.run(args);

    }

    /**
     * Validate arguments passed to the program and creates program object
     *
     * @param args
     */
    public void run(String[] args) throws FileNotFoundException {

        if (args.length > 0) {
            loadProgram(args[0]);
        }

        Scanner sc = new Scanner(System.in);
//        int lineCount = 0;
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            int address;
            //its a write instruction
            if(str.isEmpty() || str.startsWith("#")) {
                continue;
            }
            if (str.matches("(\\d+) (\\d+).*")) {
                Scanner strSc = new Scanner(str);
                address = Integer.valueOf(strSc.next());
                write(address, strSc.next());
                //its a read instruction
            } else {
                address = Integer.valueOf(str);
                System.out.println(read(address));
            }
//            lineCount++;
        }
//        debug("Lines Read", lineCount);
    }

    public int read(int address) {
        return data[address];
    }

    public void write(int address, String value) {
        data[address] = Integer.valueOf(value.replaceFirst(".*?(\\d+).*", "$1"));
    }

    protected void loadProgram(String path) throws FileNotFoundException {
        File program = new File(path);
        if (!program.exists()) {
            error("Program does not exist. [" + path + "]");
        }
        Scanner scan = new Scanner(program);
        int i = 0;
        while (scan.hasNext()) {
            write(i, scan.nextLine());
            i++;
        }
        scan.close();
        programBoundary = i;

    }

    protected void error(String msg) {
        System.err.println(msg);
        System.exit(-1);
    }
}
