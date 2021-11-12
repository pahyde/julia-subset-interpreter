package ast;

import java.util.ArrayList;

public class PrintStatement implements Statement {

    private ArithmeticExpression expression;

    public PrintStatement(ArithmeticExpression expr) {
        expression = expr;
    }
    
    public ArrayList<ASTNode> getChildren() {   
        return null;
    }

    public void execute() {
        System.out.println(expression.getValue());
    }
    
}
