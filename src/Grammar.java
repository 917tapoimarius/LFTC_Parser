import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    private final List<String> nonTerminals;
    private final List<String> terminals;
    private String startSymbol;
    private final Map<String, List<List<String>>> productions;

    public Grammar() {
        nonTerminals = new ArrayList<>();
        terminals = new ArrayList<>();
        productions = new HashMap<>();
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public Map<String, List<List<String>>> getProductions() {
        return productions;
    }
    public List<List<String>> getProductionForNonterminal(String nonterminal) {
        return productions.get(nonterminal);
    }


    public void readFile(String filename){
        String line;
        try(FileReader fileReader = new FileReader(filename); BufferedReader bufferedReader = new BufferedReader(fileReader)){
            nonTerminals.addAll(List.of(bufferedReader.readLine().split(" ")));
            terminals.addAll(List.of(bufferedReader.readLine().split(" ")));
            startSymbol = bufferedReader.readLine().strip();

            while((line = bufferedReader.readLine()) != null){
                String leftSide = line.split("->")[0].strip();
                String rightSide = line.split("->")[1].strip();
                if(productions.containsKey(leftSide)){
                    productions.get(leftSide).add(Arrays.asList(rightSide.strip().split(" ")));
                } else {
                    List<List<String>> rightSideList = new ArrayList<>();
                    rightSideList.add(Arrays.asList(rightSide.strip().split(" ")));
                    productions.put(leftSide, rightSideList);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean checkCFG(){
        boolean okStartSymbol = false;
        //checks all the keys in the map
        for (String key: productions.keySet()) {
            if (Objects.equals(key, startSymbol)) {
                okStartSymbol = true;
            }
            if (!nonTerminals.contains(key)) {
                return false;
            }
        }
        if (!okStartSymbol)
            return false;
        //checks all the values in the map
        for(List<List<String>> production : productions.values()){
            for(List<String> rightSide : production){
                for(String value : rightSide){
                    if( !nonTerminals.contains(value) && !terminals.contains(value) ){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void printNonTerminals(){
        System.out.println("Nonterminals: ");
        for (String nonTerminal: nonTerminals){
            System.out.print(nonTerminal + " ");
        }
        System.out.println();
    }

    public void printTerminals(){
        System.out.println("Terminals: ");
        for (String terminal: terminals){
            System.out.print(terminal + " ");
        }
        System.out.println();
    }

    public void printProductions(){
        System.out.println("Productions: ");
        for (String key: productions.keySet()){
            System.out.print(key + " -> ");
            for(List<String> rightSide : productions.get(key)){
                for(String value : rightSide){
                    System.out.print(value + " ");
                }
                System.out.print("| ");
            }
            System.out.println();
        }
    }

    public void printStartSymbol(){
        System.out.println("Start symbol: " + startSymbol);
    }

    public void printProductionsForNonterminal(String nonTerminal) {
        System.out.println("Productions for " + nonTerminal);
        StringBuilder rightSide = new StringBuilder();
        List<List<String>> production = productions.get(nonTerminal);
        if(production != null)
            for (List<String> productionRightSide : production) {
                for (String val : productionRightSide) {
                    rightSide.append(val).append(" ");
                }
                System.out.println(nonTerminal + "->" + rightSide);
                rightSide.setLength(0);
            }
        else
            System.out.println("No productions for " + nonTerminal);
    }

    public String getNextProduction(String production, String nonTerminal) { // changes
        List<List<String>> allProductions = new ArrayList<>(this.getProductionForNonterminal(nonTerminal)); // [[a, S, b, S], [a, S], [c]]
        for (int i = 0; i < allProductions.size(); i++) {
            String prod = String.join("", allProductions.get(i));
            if(prod.equals(production) && i < allProductions.size() - 1)
                return String.join("", allProductions.get(i+1));
        }
        return null;
    }
}
