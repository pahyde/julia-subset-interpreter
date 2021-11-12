package ast;

import java.util.ArrayList;

import lexer.TokenType;

public class RelativeOp implements BooleanExpression {

    TokenType op;
    ArithmeticExpression left;
    ArithmeticExpression right;

    public RelativeOp(TokenType o, ArithmeticExpression l, ArithmeticExpression r) {
        op = o;
        left = l;
        right = r;
    }
    
    public ArrayList<ASTNode> getChildren() {  
        return null;
    }

    public boolean evaluate() {

        int leftValue = left.getValue();
        int rightValue = right.getValue();

        if (op == TokenType.lt_operator) return leftValue <  rightValue;
        if (op == TokenType.le_operator) return leftValue <= rightValue;
        if (op == TokenType.gt_operator) return leftValue >  rightValue;
        if (op == TokenType.ge_operator) return leftValue >= rightValue;
        if (op == TokenType.ne_operator) return leftValue != rightValue;
        if (op == TokenType.eq_operator) return leftValue == rightValue;

        throw new Error();
    }
    
}
