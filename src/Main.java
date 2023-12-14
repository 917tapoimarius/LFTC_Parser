import java.util.*;

public class Main {

    public static void printGrammarMenu() {
        System.out.println("1 - Print the nonterminals");
        System.out.println("2 - Print the terminals");
        System.out.println("3 - Print the list of productions");
        System.out.println("4 - Print the Start Symbol");
        System.out.println("5 - CFG check");
        System.out.println("6 - Check sequence of nonterminal");
        System.out.println("7 - Parse a sequence");
        System.out.println("0 - EXIT");
    }
    public static void main(String[] args) {
        printGrammarMenu();
        Grammar g = new Grammar();
        // g.readFile("src/g1.txt");
        g.readFile("src/g3.txt");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select:");
        String option = scanner.nextLine();

        while(!Objects.equals(option, "0"))
        {
            switch (option) {
                case "1":
                    g.printTerminals();
                    break;
                case "2":
                    g.printNonTerminals();
                    break;
                case "3":
                    g.printProductions();
                    break;
                case "4":
                    g.printStartSymbol();
                    break;
                case "5":
                    if ( g.checkCFG())
                        System.out.println("The grammar is CFG");
                    else
                        System.out.println("The grammar is not CFG");
                    break;

                case "6":
                    System.out.println("Input the nonterminal:");
                    String nonTerminal = scanner.nextLine();
                    g.printProductionsForNonterminal(nonTerminal);
                    break;

                case "7":
                    RecursiveDescendentParser parser = new RecursiveDescendentParser(g);
                    System.out.println("Input the sequence:");
                    String sequence = scanner.nextLine();
                    boolean isValid = parser.checkSequence(Arrays.asList(sequence.split("\\s+")));
                    if (isValid)
                        System.out.println("Parser result: true");
                    else
                        System.out.println("Parser result: false");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;

            }
            printGrammarMenu();
            System.out.println("Select:");
            option = scanner.nextLine();
        }
    }
}