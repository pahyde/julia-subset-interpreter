import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Parser {
    
    public static ArrayList<Token> tokens;
    public static int i = 0;

    public static ParseTreeNode root;

    public static void main(String[] args) {

        Tokenizer tokenizer = new Tokenizer();
        tokens = tokenizer.getTokens();        
        
        program();

        bfs(root);
    }

    private static void bfs(ParseTreeNode root) {

        Queue<ParseTreeNode> q = new LinkedList<ParseTreeNode>();
        q.add(root);

        System.out.println();

        while (!q.isEmpty()) {
            ParseTreeNode curr = q.remove();
            System.out.printf("<%s> -> ", curr.symbol);

            for (ParseTreeNode child : curr.children) {
                if (child.isTerminal) {
                    System.out.printf("%s ", child.value);
                } else {
                    System.out.printf("<%s> ", child.symbol);
                    q.add(child);
                }
            }
            System.out.println();
        }

    }

    public static void program() {

        ArrayList<ParseTreeNode> children = new ArrayList<>();

        TokenType expectedTokens[] = {
            TokenType.FUNCTION,
            TokenType.identifier,
            TokenType.open_parenthesis,
            TokenType.close_parenthesis
        };

        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                children.add(tokenToParseTreeNode(tokens.get(i++)));
            } else {
                terminateWithError();
            }
        }

        block();
        children.add(root);

        if (currTokenType(TokenType.END)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.program);
        root.setChildren(children);
    }

    private static void block() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        
        statement();
        children.add(root);

        if (!currTokenType(TokenType.END) && !currTokenType(TokenType.ELSE_keyword)) {
            block();
            children.add(root);
        }

        root = new ParseTreeNode(SymbolType.block);
        root.setChildren(children);
    }

    private static void statement() {
        if (currTokenType(TokenType.IF_keyword)) {
            ifStatement();
        } else if (currTokenType(TokenType.identifier)) {
            assignmentStatement();
        } else if (currTokenType(TokenType.WHILE_keyword)) {
            whileStatement();
        } else if (currTokenType(TokenType.PRINT_keyword)) {
            printStatement();
        } else if (currTokenType(TokenType.REPEAT_keyword)) {
            repeatStatement();
        } else {
            terminateWithError();
        }
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        children.add(root);
        root = new ParseTreeNode(SymbolType.statement, children);
    }

    private static void repeatStatement() {

        ArrayList<ParseTreeNode> children = new ArrayList<>();
        children.add(tokenToParseTreeNode(tokens.get(i++)));

        block();
        children.add(root);
        
        if (currTokenType(TokenType.UNTIL_keyword)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        booleanExpression();
        children.add(root);

        root = new ParseTreeNode(SymbolType.repeat_statement);
        root.setChildren(children);
    }

    private static void booleanExpression() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        relativeOp();
        children.add(root);
        arithmeticExpression();
        children.add(root);
        arithmeticExpression();
        children.add(root);
        root = new ParseTreeNode(SymbolType.boolean_expression, children);
    }

    private static void relativeOp() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();

        TokenType expectedTokens[] = {
            TokenType.le_operator,
            TokenType.lt_operator,
            TokenType.ge_operator,
            TokenType.gt_operator
        };

        boolean found = false;
        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                children.add(tokenToParseTreeNode(tokens.get(i++)));
                found = true;
                break;
            }
        }

        if (!found) {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.relative_op, children);
    }

    private static void printStatement() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        children.add(tokenToParseTreeNode(tokens.get(i++)));
        
        if (currTokenType(TokenType.open_parenthesis)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        arithmeticExpression();
        children.add(root);

        if (currTokenType(TokenType.close_parenthesis)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.print_statement);
        root.setChildren(children);
    }

    
    private static void whileStatement() {

        ArrayList<ParseTreeNode> children = new ArrayList<>();
        children.add(tokenToParseTreeNode(tokens.get(i++)));

        booleanExpression();
        children.add(root);
        
        if (currTokenType(TokenType.DO_keyword)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        block();
        children.add(root);

        if (currTokenType(TokenType.END)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.while_statement);
        root.setChildren(children);
    }
    
    private static void assignmentStatement() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        children.add(tokenToParseTreeNode(tokens.get(i++)));

        assignmentOp();
        children.add(root);

        arithmeticExpression();
        children.add(root);

        root = new ParseTreeNode(SymbolType.assignment_statement);
        root.setChildren(children);
    }
    
    private static void assignmentOp() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();

        TokenType expectedTokens[] = {
            TokenType.assignment_operator,
            TokenType.addEq_operator
        };

        boolean found = false;

        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                children.add(tokenToParseTreeNode(tokens.get(i++)));
                found = true;
                break;
            } 
        }

        if (!found) {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.assignment_operator, children);
    }

    private static void ifStatement() {
        
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        children.add(tokenToParseTreeNode(tokens.get(i++)));

        booleanExpression();
        children.add(root);
        
        if (currTokenType(TokenType.THEN_keyword)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        block();
        children.add(root);

        if (currTokenType(TokenType.ELSE_keyword)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        block();
        children.add(root);

        if (currTokenType(TokenType.END)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.if_statement);
        root.setChildren(children);
    }
    
    private static void arithmeticExpression() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();
        if (currTokenType(TokenType.identifier)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else if (currTokenType(TokenType.literal_Integers)) {
            children.add(tokenToParseTreeNode(tokens.get(i++)));
        } else {
            arithmeticOp();
            children.add(root);
            arithmeticExpression();
            children.add(root);
            arithmeticExpression();
            children.add(root);
        }
        root = new ParseTreeNode(SymbolType.arithmetic_expression);
        root.setChildren(children);
    }

    private static void arithmeticOp() {
        ArrayList<ParseTreeNode> children = new ArrayList<>();

        TokenType expectedTokens[] = {
            TokenType.add_operator,
            TokenType.sub_operator,
            TokenType.mult_operator,
            TokenType.div_operator
        };

        boolean found = false;

        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                children.add(tokenToParseTreeNode(tokens.get(i++)));
                found = true;
                break;
            }
        }

        if (!found) {
            terminateWithError();
        }

        root = new ParseTreeNode(SymbolType.arithmetic_op, children);
    }

    private static ParseTreeNode tokenToParseTreeNode(Token token) {
        return new ParseTreeNode(SymbolType.values()[token.tokenType.ordinal()], token.lexeme);
    }
    
    private static void terminateWithError() {
        System.out.println(tokens.get(i).lexeme);
        System.out.println(i);
        throw new Error("bad input");
    }

    public static boolean currTokenType(TokenType t) {
        if (i == tokens.size()) return false;
        Token curr = tokens.get(i);
        return curr.tokenType == t;
    }

    public static boolean nextTokenType(TokenType t) {
        Token next = peek();
        if (next == null) return false;
        return next.tokenType == t;
    }

    public static Token peek() {
        if (i >= tokens.size()-1) {
            return null;
        }
        return tokens.get(i+1);
    }

}

/*

<block> - <statement> | <statement> <block>



*/
