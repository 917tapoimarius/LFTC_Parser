import java.io.IOException;
import java.util.*;

public class RecursiveDescendentParser {
    private final Grammar grammar;
    private final List<List<String>> working;
    private final List<String> input;
    private String state;
    private List<String> sequence;
    private int index;
    private int depth;
    private final boolean leftRecursive;


    public RecursiveDescendentParser(Grammar grammar) {
        this.grammar = grammar;
        this.working = new ArrayList<>(); // alpha
        this.input = new ArrayList<>(); // beta
        this.input.add(this.grammar.getStartSymbol());
        this.state = "q";
        this.index = 0;
        this.depth = 0;
        this.sequence = null;
        leftRecursive = checkGrammarLeftRecursive();
    }


    public boolean checkGrammarLeftRecursive() {
        for (Map.Entry<String, List<List<String>>> entry : grammar.getProductions().entrySet()) {
            String nonTerminal = entry.getKey();
            List<List<String>> productions = new ArrayList<>(entry.getValue());
            for (List<String> prod : productions) {
                if (prod.get(0).equals(nonTerminal)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getModel(){
        System.out.printf("(state: %s, index: %d, working: %s, input: %s)\n", state, index, working, input);
    }

    /**
     * Changes the state to BACK(B).
     */
    public void momentaryInsuccess() {
        System.out.println("Momentary insuccess");
        this.state = "b";
        System.out.println("Changed state to BACK.");
    }

    /**
     * Changes the state to FINAL(F).
     */
    public void success() {
        System.out.println("Success!");
        this.state = "f";
        System.out.println("Changed state to FINAL.");
    }

    /**
     * Increases the index with the value 1.
     * Pushes the top element of the input into the working.
     * Removes the element from the input.
     */
    private void advance(){
        System.out.println("Advance");
        this.working.add(List.of(this.input.get(0)));
        index++;
        if (index > depth)
            depth = index;
        this.input.remove(0);
    }

    /**
     * Takes the non-terminal from the input and searches for the first production.
     * Pushes on the working the first production of the non-terminal as a list containing the non-terminal and the
     * first production.
     * Removes the non-terminal from the input and puts the first production into the input.
     */
    private void expand(){
        System.out.println("Expand");
        String nonTerminal = input.remove(0);
        List<String> newProduction = grammar.getProductionForNonterminal(nonTerminal).get(0);
        this.working.add(new ArrayList<>((List.of(nonTerminal, String.join("", newProduction)))));
        input.addAll(0, newProduction);
    }

    public void back() {
        System.out.println("Back");
        String terminal = this.working.get(this.working.size()-1).get(0);
        this.input.add(0, terminal);
        this.working.remove(this.working.size()-1);
        index--;
    }

    public void anotherTry() {
        System.out.println("Another try");

        List<String> lastProduction = this.working.get(working.size() - 1);
        String nonTerminal = lastProduction.get(0);

        String next = this.grammar.getNextProduction(lastProduction.get(1), nonTerminal);
        //System.out.println("NEXT: " + next + "   LAST PROD: " + lastProduction);
        if (next != null) {
            System.out.println("Changing state to NORMAL");
            this.state = "q";
            this.working.remove(this.working.size() - 1);
            this.working.add(new ArrayList<>(List.of(nonTerminal, String.join("", next))));

            // Clear the input stack and add the new sequence
            this.input.subList(0, lastProduction.get(1).length()).clear();
            this.input.addAll(0, List.of(next.split("")));
        } else if (index == 0 && lastProduction.get(0).equals(grammar.getStartSymbol())) {
            System.out.println("Changing state to ERROR");
            if ( depth >= sequence.size())
                System.out.println("Error, length of sequence is smaller than the depth, " + (depth + 1) );
            else
                System.out.println("Error for term " + sequence.get(depth) + " (index = " + (depth + 1) + ")");
            state = "e";
        } else {
            System.out.println("_______________________________________________________________________________");
            this.working.remove(this.working.size() - 1);

            // Clear the input stack and add the last production
            this.input.subList(0, lastProduction.get(1).length()).clear();
            this.input.addAll(0, List.of(lastProduction.get(1).split("")));
        }
    }


    public boolean checkSequence(List<String> sequence) {
        if (leftRecursive) {
            System.out.println("Grammar is left recursive.");
            return false;
        }

        for (String elem : sequence) { // every element in the sequence exists in the terminals
            if (!grammar.getTerminals().contains(elem)) {
                System.out.println("e");
                System.out.println("Changing state to Error.");
                System.out.println("Error for term " + elem + " ,it's not a terminal");
                this.state = "e";
            }
        }
        this.sequence = sequence;

        int sequenceSize = this.sequence.size();
        while (!state.equals("f") && !state.equals("e")) {
            getModel();
            if (state.equals("q")) {
                if (input.isEmpty() && index == sequenceSize) {
                    success();
                } else {
                    if (!input.isEmpty() && grammar.getNonTerminals().contains(input.get(0)) && index < sequenceSize) {
                        expand();
                    } else {
                        if (!input.isEmpty() && index < sequenceSize && input.get(0).equals(sequence.get(index))) {
                            advance();
                        } else {
                            momentaryInsuccess();
                        }
                    }
                }
            } else if (state.equals("b")) {
                if (grammar.getTerminals().contains(working.get(working.size() - 1).get(0))) {
                    back();
                } else {
                    anotherTry();
                }
            }
        }
        if (state.equals("e")) {
            System.out.println("Sequence is not accepted, Error");
        } else {
            System.out.println("Sequence is accepted");
            ParserOutput parserOutput = new ParserOutput(grammar, working);
            parserOutput.parsingTable();
            try {
                parserOutput.printParsingTable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }
}