package ast;

import java.util.ArrayList;
import lexer.TokenType;

public class ArithmeticOp implements ArithmeticExpression {

    private TokenType op;
    private ArithmeticExpression operand1;
    private ArithmeticExpression operand2;

    public ArithmeticOp(TokenType o, ArithmeticExpression rand1, ArithmeticExpression rand2) {
        op = o;
        operand1 = rand1;
        operand2 = rand2;
    }

    public ArrayList<ASTNode> getChildren() {
        return null;
    }

    public int getValue() {

        int a = operand1.getValue();
        int b = operand2.getValue();

        if (op == TokenType.add_operator)  return a + b;
        if (op == TokenType.sub_operator)  return a - b;
        if (op == TokenType.mult_operator) return a * b;
        if (op == TokenType.div_operator)  return a / b;

        throw new Error();

    }
    
}
