public class Main {
    public static void main(String[] args) {
        Grammar g = new Grammar();
        g.readFile("src/g2.txt");

        g.printTerminals();
        g.printNonTerminals();
        g.printProductions();
        g.printStartSymbol();
        if ( g.checkCFG())
            System.out.println("The grammar is CFG");
        else
            System.out.println("The grammar is not CFG");
    }
}