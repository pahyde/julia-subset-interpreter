package ast;

import java.util.ArrayList;

import lexer.TokenType;

public class AssignmentStatement implements Statement {

    private Identifier identifier;
    private TokenType assignmentOp;
    private ArithmeticExpression expression;

    public AssignmentStatement(Identifier id, TokenType op, ArithmeticExpression expr) {
        identifier = id;
        assignmentOp = op;
        expression = expr;
    }
 
    public ArrayList<ASTNode> getChildren() {
        return null;
    }

    public void execute() {
        if (assignmentOp == TokenType.assignment_operator) {
            identifier.setValue(expression.getValue());
        } else if (assignmentOp == TokenType.addEq_operator) {
            identifier.setValue(identifier.getValue() + expression.getValue());
        } else {
            throw new Error();
        }
    }
    
}
