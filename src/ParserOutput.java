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
            int prodIndex = 0;
            this.root = new Node(productions.get(prodIndex).get(0));
            this.root.index = 1;
            this.root.parent = 0;
            this.root.rightSibling = 0;
            tree.add(this.root);
            parseTableRecursive(root, productions, prodIndex);


        }
    }


    private int parseTableRecursive(Node parent, List<List<String>>productions, int prodIndex){
//        System.out.println(productions.size());
//        System.out.println(prodIndex);
//        System.out.println("REAAAF");
        if (prodIndex == productions.size())
            return productions.size();
//        System.out.println(productions);

//        String NewString = String.join("", lastProduction);
//        NewString= NewString.substring(1);
//        System.out.println(productions.get(prodIndex));
        List<String> prod = productions.get(prodIndex); //RIGHT side
        prod = prod.subList(1, prod.size());
//        System.out.println(prod);
        System.out.println("prod = " + prod);
        Node rightSibling = null;

        for (String s : prod) {
            String term = String.valueOf(s);
            System.out.println("term = " + term);

            Node newChild = new Node(term);
            nodeIndex++;
            newChild.index = nodeIndex;
            newChild.parent = parent.index;

            if (rightSibling != null) {
                newChild.rightSibling = rightSibling.index;
            }

            tree.add(newChild);
            rightSibling = newChild;

            if (grammar.getNonTerminals().contains(term))
                prodIndex = parseTableRecursive(newChild, productions, prodIndex + 1);
        }
        return  prodIndex;
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
            fileWriter.append(node.toString()).append("\n");
        }
        fileWriter.close();
    }

}
