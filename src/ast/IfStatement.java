package ast;

import java.util.ArrayList;

public class IfStatement implements Statement {

    BooleanExpression condition;

    Block thenBlock, elseBlock;

    public IfStatement(BooleanExpression cond, Block tb, Block eb) {
        condition = cond;
        thenBlock = tb; elseBlock = eb;
    }
    
    public ArrayList<ASTNode> getChildren() {
        return null;
    }

    public void execute() {
        if (condition.evaluate()) {
            thenBlock.execute();
        } else {
            elseBlock.execute();
        }
    }
    
}
