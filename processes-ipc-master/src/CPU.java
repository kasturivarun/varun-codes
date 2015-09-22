
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Scanner;

public class CPU extends BaseClass {

    private int pc = 0;
    private int sp = 1000;
    private int ir = 0;
    private int ac = 0;
    private int x = 0;
    private int y = 0;
    final private BufferedWriter out;
    final private BufferedReader in;
    final private BufferedReader err;
    private final InputStream errStream;
    private final int instructionsWithOp[] = {1, 2, 3, 9, 14, 15, 16, 17, 21, 22};
    private boolean userMode = true;
    private boolean exit = false;
    final int PROGRAM_MEM_BOUNDARY = 1000;

    public static void main(String args[]) {
    }

    public CPU(InputStream inputStream, OutputStream outputStream, InputStream errorStream) {

        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.out = new BufferedWriter(new OutputStreamWriter(outputStream));
        this.err = new BufferedReader(new InputStreamReader(errorStream));

        this.errStream = errorStream;
    }

    public void run() throws IOException {
        while (true) {
            cycle();
            if (exit) {
                break;
            }
        }


        out.close();
        in.close();
        err.close();
    }

    /**
     * Determines if a given address is accessible
     *
     * @param address
     */
    private void isValidAddress(int address) {
        if (this.userMode) {
            if (address < 0 || address >= PROGRAM_MEM_BOUNDARY) {
                this.error("Invalid memory address: " + address);
            }
        }
    }

    private void cycle() throws IOException {
        boolean hasOP = false;
        this.ir = read(pc);
        for (int i = 0; i < instructionsWithOp.length; i++) {
            if (this.ir == instructionsWithOp[i]) {
                hasOP = true;
                break;
            }
        }
        if (hasOP) {
            this.pc++;
            processInstruction(this.ir, read(this.pc));
        } else {
            processInstruction(this.ir, 0);
        }
        this.pc++;
    }

    private void processInstruction(int instruction, int op) throws IOException {
        debug("Instruction", instruction);
        switch (instruction) {
            case 1: // Load the value into the AC
                this.ac = op;
                break;
            case 2: // Load the value at the address into the AC
                this.ac = this.read(op);
                break;
            case 3: // Store the value in the AC into the address
                this.write(op, this.ac);
                break;
            case 4: // Add the value in X to the AC
                this.ac += this.x;
                break;
            case 5: // Add the value in Y to the AC
                this.ac += this.y;
                break;
            case 6: // Subtract the value in X from the AC
                this.ac -= this.x;
                break;
            case 7: // Subtract the value in Y from the AC
                this.ac -= this.y;
                break;
            case 8: // Get a random int from 1 to 100
                this.ac = 1 + (int) (Math.random() * 100);
                break;
            case 9: // Write AC to the screen
                if (op == 1) {
                    System.out.print(this.ac);
                } else if (op == 2) {
                    System.out.print((char) this.ac);
                }
                break;
            case 10: // Copy the value in the AC to X
                this.x = this.ac;
                break;
            case 11: // Copy the value in the AC to y
                this.y = this.ac;
                break;
            case 12: // Copy the value in X to the AC
                this.ac = this.x;
                break;
            case 13: // Copy the value in Y to the AC
                this.ac = this.y;
                break;
            case 14: // Jump to the address
                this.pc = op - 1;
                break;
            case 15: // Jump to the address only if the value in the AC is zero
                if (this.ac == 0) {
                    this.pc = op - 1;
                }
                break;
            case 16: // Jump to the address only if the value in the AC is not zero
                if (this.ac != 0) {
                    this.pc = op - 1;
                }
                break;
            case 17: // Push return address onto stack, jump to the address
                this.write(this.sp, this.pc);
                this.sp++;
                this.pc = op - 1;
                break;
            case 18: // Pop the return address from the stack, jump to the address
                this.sp--;
                this.pc = this.read(sp) - 1;
                break;
            case 19: // Increment the value in X
                this.x++;
                break;
            case 20: // Decrement the value in X
                this.y--;
                break;
            case 21: //Load the value at (address+x) into the AC
                this.ac = this.read(op + this.x);
                break;
            case 22: //Load the value at (address+y) into the AC
                this.ac = this.read(op + this.y);
                break;
            case 23: //Push AC onto Stack
                this.write(this.sp, this.ac);
                this.sp++;
                break;
            case 24: //Pop from the stack into AC
                this.sp--;
                this.ac = this.read(sp);
                break;
            case 25: //Push return address, set system mode, set PC to int handler
                break;
            case 26: //Pop return address into PC, set user mode
                break;
            case 50: //End execution
                exit = true;
                break;
            default:
                this.error("Instruction Invalid: PC-" + this.pc + " IR-" + this.ir);
                break;

        }
    }

//    private int read(int address){
//    	Scanner s = new Scanner(System.in);
//    	System.out.println("MEM_READ: " + address);
//    	while(!s.hasNextInt());
//    	return s.nextInt();
//    }
//    
//    private void write(int address, int data){
//    	System.out.println("MEM_WRITE: " + address + " " + data);
//    }
    /**
     * read value from memory
     *
     * @param msg
     * @return int
     */
    protected int read(int address) throws IOException {
        out.write(String.format("%d\n", address));
        out.flush();
        String value = in.readLine();
        if (errStream.available() > 0) {
            error(err.readLine());
        }
        debug("READ " + address, value);
        return Integer.parseInt(value);

    }

    /**
     * write value to memory at given address
     *
     * @param address
     * @param value
     * @throws IOException
     */
    protected void write(int address, int value) throws IOException {
        out.write(String.format("%d %d\n", address, value));
        out.flush();
        if (errStream.available() > 0) {
            error(err.readLine());
        }
        debug("WRITE " + address, value);
    }
}
