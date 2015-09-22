

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author iyo
 */
public class ProcessesIPC extends BaseClass {

    private Runtime runtime;
    private Process memProcess;
    private Process cpuProcess;
    private CPU cpu;

    public static void main(String args[]) {
        try {
            ProcessesIPC bridge = new ProcessesIPC(args);

            int exitVal = bridge.run();
            System.out.println("Process exited: " + exitVal);
            System.exit(0);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public ProcessesIPC(String args[]) throws IOException, InterruptedException {
        validateArguments(args);

        runtime = Runtime.getRuntime();

        //initialize memory
        memProcess = runtime.exec("java Memory " + args[0]);
//        memProcess = exec(Memory.class);
        debug("READ PROGRAM");

    }

    public int run() throws IOException, InterruptedException {
        cpu = new CPU(memProcess.getInputStream(),
                memProcess.getOutputStream(), memProcess.getErrorStream());
        cpu.setDebug(this.debugMode);
        cpu.run();

        //finished writing and reading

        memProcess.waitFor();

        return memProcess.exitValue();

    }

    /**
     * Validate arguments passed to the program and creates program object
     *
     * @param args
     */
    private void validateArguments(String[] args) {

        if (args.length < 1) {
            error("IO [path to program]");
        }

        if (args.length > 1) {
            this.setDebug(args[args.length - 1].equals("-debug"));
        }

        debug("PROGRAM VALIDATED");
    }

    public Process exec(Class klass) throws IOException,
            InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome
                + File.separator + "bin"
                + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = klass.getCanonicalName();
        debug(classpath);
        debug(className);
        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);

        return builder.start();
    }
}