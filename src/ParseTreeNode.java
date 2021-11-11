import java.util.ArrayList;

public class ParseTreeNode {
    public SymbolType symbol;
    public String value;
    public boolean isTerminal;
    public ArrayList<ParseTreeNode> children;

    //Non-terminal constructor
    public ParseTreeNode(SymbolType s) { 
        symbol = s;
        value = null;
        isTerminal = false;
    }

    //Non-terminal w/ children constructor
    public ParseTreeNode(SymbolType s, ArrayList<ParseTreeNode> c) { 
        this(s);
        children = c;
    }

    //Terminal Constructor w/ lexeme
    public ParseTreeNode(SymbolType s, String lexeme) { 
        this(s);
        value = lexeme;
        isTerminal = true;
    }

    public void setChildren(ArrayList<ParseTreeNode> c) {
        children = c;
    }
}



