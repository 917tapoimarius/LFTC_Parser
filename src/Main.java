import java.io.*;
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
        System.out.println("8 - Parse a sequence from file");
        System.out.println("9 - Parse from file using PIF");
        System.out.println("0 - EXIT");
    }
    public static void main(String[] args) throws IOException {
        printGrammarMenu();
        Grammar g = new Grammar();
        // g.readFile("src/g1.txt")
         g.readFile("src/g2.txt");
        // g.readFile("src/g3.txt");
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
                case "8":
                    RecursiveDescendentParser fileParser = new RecursiveDescendentParser(g);
                    // Scanner fileScanner = new Scanner(new File("src/parse.out"));
                    Scanner fileScanner = new Scanner(new File("src/seq.txt"));
                    String inputSequence = fileScanner.nextLine();
                    boolean isInputValid = fileParser.checkSequence(Arrays.asList(inputSequence.split("\\s+")));
                    if (isInputValid)
                        System.out.println("Parser result: true");
                    else
                        System.out.println("Parser result: false");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
                case "9":
                    SymbolTable st = new SymbolTable(30);
                    st.addSym("Result");
                    System.out.println(st);
                    MyScanner s = new MyScanner("src/prog_seq.txt");
                    s.scan();
                    RecursiveDescendentParser pif_parser = new RecursiveDescendentParser(g);
                    List<String> seq = new ArrayList<>();
                    BufferedReader reader = new BufferedReader(new FileReader( "PIF.out"));
                    String line = reader.readLine();
                    while(line != null){
                        seq.add(line.split("=>")[0].strip());
                        line = reader.readLine();
                    }
                    System.out.println(seq);

                    Scanner pif_fileScanner = new Scanner(new File("src/seq.txt"));

                    String pif_inputSequence = pif_fileScanner.nextLine();
                    System.out.println(pif_inputSequence);
                    boolean pif_isValid = pif_parser.checkSequence(seq);
                    if (pif_isValid)
                        System.out.println("Parser result: true");
                    else
                        System.out.println("Parser result: false");
                    break;

            }
            printGrammarMenu();
            System.out.println("Select:");
            option = scanner.nextLine();
        }
    }
}