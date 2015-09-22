
/**
 *
 * @author iyo
 */
public abstract class BaseClass {
    
    protected boolean debugMode = false;
    
    protected void setDebug(boolean value){
        debugMode = value;
    }

    /**
     * Exits the system with an error message
     *
     * @param msg
     */
    protected void error(Object msg) {
        System.err.println(msg);
        System.exit(-1);
    }

    protected void debug(Object msg) {
        if (debugMode) {
            System.out.println(msg);
        }
    }

    protected void debug(Object key, Object msg) {
        if (debugMode) {
            System.out.printf("[%s] %s\n", key, msg);
        }

    }
    
}
