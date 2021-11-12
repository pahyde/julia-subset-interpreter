package ast;

import java.util.ArrayList;

public class LiteralInteger implements ArithmeticExpression {

    private int value;

    public LiteralInteger(int v) {
        value = v;
    }
   
    public ArrayList<ASTNode> getChildren() {
        return null;
    }

    public int getValue() {
        return value;
    }
    
}
