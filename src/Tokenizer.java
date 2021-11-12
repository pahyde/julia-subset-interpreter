import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import lexer.TokenType;

public class Tokenizer {

    private HashMap<String, TokenType> keywordTokenMap = new HashMap<>();
    private HashMap<String, TokenType> operatorTokenMap = new HashMap<>();
    private ArrayList<Token> tokens = new ArrayList<Token>();

    public ArrayList<Token> getTokens() { return tokens; }

    public Tokenizer() {

        initializeOperatorTokenMap();
        initializeKeywordTokenMap();

        try {
            File myObj = new File("Test2.jl");
            Scanner myReader = new Scanner(myObj);

            int lineNumber = 0;
            while (myReader.hasNextLine()) {
                
                String data = myReader.nextLine();

                if (data.startsWith("//")||data.trim().isEmpty()) {
                    continue;
                }

                String line = data.replaceAll("\t", "");
        
                int i = 0;
                while (i < line.length()) {
                    
                    char c = line.charAt(i);
                    if (c == ' ') {i++; continue;}

                    String lexeme = "";
                    if (isAlpha(c)) {

                        while (isAlpha(c)) {
                            lexeme += c;
                            if (++i >= line.length()) break;
                            c = line.charAt(i);
                        }

                        if (keywordTokenMap.containsKey(lexeme)) {
                            tokens.add(new Token(
                                keywordTokenMap.get(lexeme),
                                lexeme,
                                lineNumber
                            ));
                        } else if (lexeme.length() == 1) {
                            tokens.add(new Token(
                                TokenType.identifier,
                                lexeme,
                                lineNumber
                            ));
                        } else {
                            //unidentified keyword error

                        }

                    } else if (isDigit(c)) {

                        while (isDigit(c)) {
                            lexeme += c;
                            if (++i >= line.length()) break;
                            c = line.charAt(i);
                        }
                        tokens.add(new Token(
                            TokenType.literal_Integers,
                            lexeme,
                            lineNumber
                        ));

                    } else if (isOperator(c)) {

                        if (c == '(' || c == ')') {
                            lexeme += c;
                            i++;
                        } else {
                            while (isOperator(c)) {
                                lexeme += c;
                                if (++i >= line.length()) break;
                                c = line.charAt(i);
                            }
                        }

                        if (operatorTokenMap.containsKey(lexeme)) {
                            tokens.add(new Token(
                                operatorTokenMap.get(lexeme),
                                lexeme,
                                lineNumber
                            ));
                        } else {
                            //unidentified keyword error
                        }

                    } else {
                        //Throw Error
                        i++;
                    }
                }
                lineNumber++;
            }
            
            // System.out.println("Lexemes/Tokens: \n");
            // for(int i = 0; i < tokens.size();i++) {
            //     System.out.printf(
            //         "line %3s: %-9s: %s\n", 
            //         tokens.get(i).lineNumber, 
            //         tokens.get(i).lexeme, 
            //         tokens.get(i).tokenType
            //     );
            // }

            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
    }

    private void initializeKeywordTokenMap() {
        keywordTokenMap.put("if", TokenType.IF_keyword);
        keywordTokenMap.put("then", TokenType.THEN_keyword);
        keywordTokenMap.put("else", TokenType.ELSE_keyword);
        keywordTokenMap.put("while", TokenType.WHILE_keyword);
        keywordTokenMap.put("do", TokenType.DO_keyword);
        keywordTokenMap.put("print", TokenType.PRINT_keyword);
        keywordTokenMap.put("function", TokenType.FUNCTION);
        keywordTokenMap.put("end", TokenType.END);
    }

    private void initializeOperatorTokenMap() {
        operatorTokenMap.put("(", TokenType.open_parenthesis);
        operatorTokenMap.put(")", TokenType.close_parenthesis);
        operatorTokenMap.put("<=", TokenType.le_operator);
        operatorTokenMap.put("<", TokenType.lt_operator);
        operatorTokenMap.put(">", TokenType.gt_operator);
        operatorTokenMap.put(">=", TokenType.ge_operator);
        operatorTokenMap.put("==", TokenType.eq_operator);
        operatorTokenMap.put("~=", TokenType.ne_operator);
        operatorTokenMap.put("+", TokenType.add_operator);
        operatorTokenMap.put("-", TokenType.sub_operator);
        operatorTokenMap.put("*", TokenType.mult_operator);
        operatorTokenMap.put("/", TokenType.div_operator);
        operatorTokenMap.put("=", TokenType.assignment_operator);
        operatorTokenMap.put("+=", TokenType.addEq_operator);
    }

    private boolean isAlpha(char c) {
        return 97 <= c && c < 97 + 26;
    }

    private boolean isDigit(char c) {
        int charCode = c - '0';
        return 0 <= charCode && charCode <= 9;
    }

    private boolean isOperator(char c) {
        String ops = "()<>=~+-*/";
        return ops.indexOf(c) != -1;
    }
}
