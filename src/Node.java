public class Node {
    String info;
    int parent;
    int index;
    int rightSibling;

    public Node(String info){
        this.info = info;
        this.parent = -1;
        this.index = 0;
        this.rightSibling = 0;
    }

    @Override
    public String toString(){

        StringBuilder result = new StringBuilder();
        result.append("index ").append(index)
                .append(" info ").append(info)
                .append(" parent ").append(parent)
                .append(" right Sibling ").append(rightSibling)
                .append("\n");
        return result.toString();
    }

}