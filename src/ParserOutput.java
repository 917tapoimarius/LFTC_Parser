import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserOutput {
    private Node root;
    private final Grammar grammar;
    private final List<List<String>> working;
    private final List<Node> tree;
    private int nodeIndex = 1;

    public ParserOutput(Grammar grammar, List<List<String>> working) {
        this.root = null;
        this.grammar = grammar;
        this.working = working;
        this.tree = new ArrayList<>();
    }

    public List<List<String>> parseProductionString(){
        List<List<String>> productions = new ArrayList<>();
        for(List<String> production : working){
            if(production.size() > 1){
                productions.add(production);
            }
        }
        return productions;
    }

    public void parsingTable(){
        List<List<String>> productions = parseProductionString();

        if (productions.isEmpty())
            root = new Node("empty");
        else {
            int productionIndex = 0;
            this.root = new Node(productions.get(productionIndex).get(0));
            this.root.index = 1;
            this.root.parent = 0;
            this.root.rightSibling = 0;
            tree.add(this.root);
            parseTableRecursive(root, productions, productionIndex);

        }
    }


    private int parseTableRecursive(Node parent, List<List<String>>productions, int productionIndex){
        if (productionIndex == productions.size())
            return productionIndex;

        String production = productions.get(productionIndex).get(1); //RIGHT side
        System.out.println("production = " + production);
        Node rightSibling = null;
        for( int i = 0; i< production.length(); i++){
            String term = String.valueOf(production.charAt(i));
            System.out.println("term = " + term);

            Node newChild = new Node(term);
            nodeIndex++;
            newChild.index = nodeIndex;
            newChild.parent = parent.index;

            if (rightSibling != null){
                newChild.rightSibling = rightSibling.index;
            }

            tree.add(newChild);
            rightSibling = newChild;

            if(grammar.getNonTerminals().contains(term))
                productionIndex = parseTableRecursive(newChild, productions, productionIndex + 1);
        }
        return productionIndex;
    }

    public void printParsingTable() throws IOException{
        for (Node node : tree) {
            System.out.println(node.toString());
        }
        printParsingTableToFile();
    }

    private void printParsingTableToFile() throws IOException{
        FileWriter fileWriter = new FileWriter("parsingTreeTableOutput.txt");
        for (Node node : tree) {
            fileWriter.append(node.toString());
        }
        fileWriter.close();
    }

}