package ast;

import java.util.ArrayList;

public class RepeatStatement implements Statement {

    private Block repeatBlock;
    private BooleanExpression condition;

    public RepeatStatement(Block rp, BooleanExpression cond) {
        repeatBlock = rp;
        condition = cond;
    }

    public ArrayList<ASTNode> getChildren() {
        return null;
    }

    public void execute() {
        do {
            repeatBlock.execute();
        } while (!condition.evaluate());
    }
    
}
