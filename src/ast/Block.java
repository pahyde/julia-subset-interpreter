package ast;

import java.util.ArrayList;

public class Block implements ASTNode {

    private ArrayList<Statement> statements;

    public Block(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public ArrayList<ASTNode> getChildren() { 
        return null;
    }

    public void execute() {
        for (Statement s : statements) {
            s.execute();
        }
    }
    
}
