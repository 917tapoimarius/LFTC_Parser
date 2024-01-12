public class SymbolTable {
    private final Hashtable<String, Integer> symbolTable;

    public SymbolTable(int capacity) {
        symbolTable = new Hashtable<>( capacity);
    }

    public Integer addSym(String key) {
        return symbolTable.add(key, symbolTable.hash(key));
    }

    public void remove(String key) {
        symbolTable.remove(key);
    }

    public Integer getPos(String key) {
        return symbolTable.get(key);
    }

    public String toString(){return symbolTable.toString();}
    public boolean contains(String key) {
        return symbolTable.get(key) != null;
    }
}
