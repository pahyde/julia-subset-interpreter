import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.Queue;

import ast.ArithmeticExpression;
import ast.ArithmeticOp;
import ast.AssignmentStatement;
import ast.Block;
import ast.BooleanExpression;
import ast.Identifier;
import ast.IfStatement;
import ast.LiteralInteger;
import ast.PrintStatement;
import ast.RelativeOp;
import ast.RepeatStatement;
import ast.Statement;
import ast.WhileStatement;
import lexer.TokenType;

public class Parser {
    
    public static ArrayList<Token> tokens;
    public static int i = 0;

    public static void main(String[] args) {

        Tokenizer tokenizer = new Tokenizer();
        tokens = tokenizer.getTokens();        
        
        Block AbstractSyntaxTree = parse();
        AbstractSyntaxTree.execute();
    }

    
    public static Block parse() {
        
        // System.out.println("parse");
        
        TokenType expectedTokens[] = {
            TokenType.FUNCTION,
            TokenType.identifier,
            TokenType.open_parenthesis,
            TokenType.close_parenthesis
        };
        
        for (TokenType expectedTokenType : expectedTokens) {
            consumeToken(expectedTokenType);
        }
        
        Block ast = block();
        
        if (!currTokenType(TokenType.END)) {
            terminateWithError();
        }
        
        return ast;
    }
    
    private static Block block() {
        
        // System.out.println("block");
        ArrayList<Statement> statements = new ArrayList<>();
        
        while (true) {
            if (currTokenType(TokenType.IF_keyword)) {
                statements.add(ifStatement());
            } else if (currTokenType(TokenType.identifier)) {
                statements.add(assignmentStatement());
            } else if (currTokenType(TokenType.WHILE_keyword)) {
                statements.add(whileStatement());
            } else if (currTokenType(TokenType.PRINT_keyword)) {
                statements.add(printStatement());
            } else if (currTokenType(TokenType.REPEAT_keyword)) {
                statements.add(repeatStatement());
            } else if (statements.size() == 0) {
                terminateWithError();
            } else {
                break;
            }
        }
        
        return new Block(statements);
    }
    
    private static RepeatStatement repeatStatement() {
        consumeToken(TokenType.REPEAT_keyword);
        Block repeatBlock = block();
        consumeToken(TokenType.UNTIL_keyword);
        BooleanExpression condition = booleanExpression();
        
        return new RepeatStatement(repeatBlock, condition);
    }
    
    private static BooleanExpression booleanExpression() {
        // System.out.println("bool");
        return new RelativeOp(relativeOp(), arithmeticExpression(), arithmeticExpression());
    }
    
    private static TokenType relativeOp() {
        
        // System.out.println("rel op token");
        
        TokenType expectedTokens[] = {
            TokenType.le_operator,
            TokenType.lt_operator,
            TokenType.ge_operator,
            TokenType.gt_operator,
            TokenType.eq_operator,
            TokenType.ne_operator
        };
        
        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                nextToken();
                return expectedTokenType;
            }
        }
        
        throw new Error();
    }
    
    private static PrintStatement printStatement() {
        
        // System.out.println("print");
        
        consumeToken(TokenType.PRINT_keyword);
        consumeToken(TokenType.open_parenthesis);
        ArithmeticExpression printable = arithmeticExpression();
        consumeToken(TokenType.close_parenthesis);
        
        return new PrintStatement(printable);
    }
    
    
    private static WhileStatement whileStatement() {
        
        // System.out.println("while");
        
        consumeToken(TokenType.WHILE_keyword);
        BooleanExpression condition = booleanExpression();
        consumeToken(TokenType.DO_keyword);
        Block doBlock = block();
        consumeToken(TokenType.END);
        
        return new WhileStatement(condition, doBlock);
    }
    
    private static AssignmentStatement assignmentStatement() {
        
        // System.out.println("assignment");
        Identifier id = new Identifier(consumeToken(TokenType.identifier).lexeme.charAt(0));
        TokenType op = assignmentOp();
        ArithmeticExpression expression = arithmeticExpression();
        
        return new AssignmentStatement(id, op, expression);
    }
    
    private static TokenType assignmentOp() {
        
        // System.out.println("assignment op");
        
        TokenType expectedTokens[] = {
            TokenType.assignment_operator,
            TokenType.addEq_operator
        };
        
        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                nextToken();
                return expectedTokenType;
            } 
        }
        
        throw new Error();
    }
    
    private static IfStatement ifStatement() {
        
        // System.out.println("if");
        
        consumeToken(TokenType.IF_keyword);
        BooleanExpression condition = booleanExpression();
        consumeToken(TokenType.THEN_keyword);
        Block thenBlock = block();
        consumeToken(TokenType.ELSE_keyword);
        Block elseBlock = block();
        consumeToken(TokenType.END);
        
        return new IfStatement(condition, thenBlock, elseBlock);
        
    }
    
    private static ArithmeticExpression arithmeticExpression() {
        
        // System.out.println("arith expr");
        
        if (currTokenType(TokenType.identifier)) {
            return new Identifier(consumeToken(TokenType.identifier).lexeme.charAt(0));
        } else if (currTokenType(TokenType.literal_Integers)) {
            int value = Integer.parseInt(consumeToken(TokenType.literal_Integers).lexeme);
            return new LiteralInteger(value);
        } else {
            return new ArithmeticOp(arithmeticOp(), arithmeticExpression(), arithmeticExpression());
        }
        
    }
    
    private static TokenType arithmeticOp() {
        
        // System.out.println("arith op");
        
        TokenType expectedTokens[] = {
            TokenType.add_operator,
            TokenType.sub_operator,
            TokenType.mult_operator,
            TokenType.div_operator
        };
        
        for (TokenType expectedTokenType : expectedTokens) {
            if (currTokenType(expectedTokenType)) {
                nextToken();
                return expectedTokenType;
            }
        }
        
        throw new Error();
    }
    
    private static void terminateWithError() {
        // System.out.println(tokens.get(i).lexeme);
        // System.out.println(i);
        throw new Error("bad input");
    }
    
    public static boolean currTokenType(TokenType t) {
        if (i == tokens.size()) return false;
        Token curr = tokens.get(i);
        return curr.tokenType == t;
    }
    
    private static Token nextToken() { return tokens.get(i++); }
    private static Token consumeToken(TokenType t) {
        if (!currTokenType(t)) {
            terminateWithError();
        }
        return nextToken();
    }
    
}

// private static void bfs(ParseTreeNode root) {

//     Queue<ParseTreeNode> q = new LinkedList<ParseTreeNode>();
//     q.add(root);

//     System.out.println();

//     while (!q.isEmpty()) {
//         ParseTreeNode curr = q.remove();
//         System.out.printf("<%s> -> ", curr.symbol);

//         for (ParseTreeNode child : curr.children) {
//             if (child.isTerminal) {
//                 System.out.printf("%s ", child.value);
//             } else {
//                 System.out.printf("<%s> ", child.symbol);
//                 q.add(child);
//             }
//         }
//         System.out.println();
//     }

// }