public class Token {
    public TokenType tokenType;
    public String    lexeme;
    public int       lineNumber;

    public Token(TokenType t, String l, int n) {
        tokenType = t;
        lexeme = l;
        lineNumber = n;
    }
}
