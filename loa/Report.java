/**
 * Write a description of class Report here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Report  
{
    // instance variables - replace the example below with your own
    public int depth;
    public int numNodes;
    public int numEvalMax;
    public int numEvalMin;
    public int numPrunMax;
    public int numPrunMin;
    /**
     * Constructor for objects of class Report
     */
    public Report(int d, int n, int eMax, int eMin, int pMax, int pMin)
    {
        depth = d;
        numNodes = n;
        numEvalMax = eMax;
        numEvalMin = eMin;
        numPrunMax = pMax;
        numPrunMin = pMin;
    }
    
    public String toString() {
        String str = "";
        str += "max depth: " + depth + "\n";
        str += "num of node: " + numNodes + "\n";
        str += "eval function in Max: " + numEvalMax + "\n";
        str += "eval function in Min: " + numEvalMin + "\n";
        str += "pruning in Max: " + numPrunMax + "\n";
        str += "pruning in Min: " + numPrunMin + "\n";
        return str;
    }

}
