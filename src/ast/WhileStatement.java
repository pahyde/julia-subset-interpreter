package ast;

import java.util.ArrayList;

public class WhileStatement implements Statement {

    BooleanExpression condition;
    Block doBlock;

    public WhileStatement(BooleanExpression cond, Block block) {
        condition = cond;
        doBlock = block;
    }

    public ArrayList<ASTNode> getChildren() {
        return null;
    }

    public void execute() {
        while (condition.evaluate()) {
            doBlock.execute();
        }
    }
    
}
