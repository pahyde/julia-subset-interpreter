package ast;

import java.util.ArrayList;

public class Identifier implements ArithmeticExpression {

    private char id;
    private static int[] SymbolTable = new int[26];
    private static boolean[] initialized = new boolean[26];

    public Identifier(char c) { id = c; }

    public int getValue() { 
        if (!initialized[id-97]) {
            throw new Error("identifier uninitialized!");
        }
        return SymbolTable[id-97]; 
    }

    public void setValue(int v) { 
        initialized[id-97] = true;
        SymbolTable[id-97] = v; 
    }
   
    public ArrayList<ASTNode> getChildren() {
        return null;
    }
    
}
