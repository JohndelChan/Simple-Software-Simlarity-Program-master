import algorithm.ComparisonStrategy;
import algorithm.LineComparisonStrategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader file1 = new BufferedReader(new FileReader("test_program1.txt"));
        BufferedReader file2 = new BufferedReader(new FileReader("test_program2.txt"));

        BufferedReader file1cpp = new BufferedReader(new FileReader("test_program1.cpp"));
        BufferedReader file2cpp = new BufferedReader(new FileReader("test_program2.cpp"));
        ComparisonStrategy comparisonAlgo = new LineComparisonStrategy();
        ComparisonStrategy comparisonAlgo2 = new LineComparisonStrategy();
        Double result1 = comparisonAlgo.compare(file1, file2);
        Double result2 = comparisonAlgo2.compare(file1cpp, file2cpp);

        System.out.println("Java comparison: " + result1);
        System.out.println("C++ comparison: " + result2);

    }
}

package algorithm;

import java.io.IOException;
import java.io.Reader;

@FunctionalInterface
public interface ComparisonStrategy {
    Double compare(Reader str1, Reader str2) throws IOException;
}

package algorithm;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

class HashingTokenizer {
    private StreamTokenizer tokenizer;
    static final int EOF = 0;
    static final int EOL = 1;
    static final int IGNORE = 3;
    static final int OTHER = 2;
    static final int QUOTE = '"';

    Token getNextTokenInfo() throws IOException {
        int tokenNumber = tokenizer.nextToken();
        int hash = IGNORE;
        switch(tokenNumber){
            case StreamTokenizer.TT_EOF:
                hash = EOF;
                break;
            case StreamTokenizer.TT_EOL:
                hash = EOL;
                break;

            case StreamTokenizer.TT_NUMBER:
                Integer placeHolder = tokenNumber;
                hash = placeHolder.hashCode();
                break;

            case StreamTokenizer.TT_WORD:
                hash = tokenizer.sval.hashCode();
                break;


            default:
                //extra checks
                if (tokenNumber == QUOTE) hash = tokenizer.sval.hashCode();
                else hash = IGNORE;
                break;

        }

        return new Token(hash, tokenizer.lineno());
    }

    HashingTokenizer(Reader reader){
        tokenizer = new StreamTokenizer(reader);
        tokenizer.quoteChar(QUOTE);
    }
}

package algorithm;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

public class LineComparisonStrategy implements ComparisonStrategy {

    private HashMap<Token, Integer> occurences;

    public LineComparisonStrategy(){
        occurences = new HashMap<>();
    }
    private void tabulateOccurences(Reader reader) throws IOException {
        HashingTokenizer tokenizer = new HashingTokenizer(reader);
        Token currentToken = null;
        do {
            currentToken = tokenizer.getNextTokenInfo();
            if (currentToken.getHash() == HashingTokenizer.IGNORE) continue;
            occurences.putIfAbsent(currentToken, 0);
            occurences.put(currentToken,
                    occurences.get(currentToken) + 1);
        } while (currentToken.getHash() != HashingTokenizer.EOF);
    }


    //returns a score between 0.0 and 1.0
    @Override
    public Double compare(Reader str1, Reader str2) throws IOException {
        tabulateOccurences(str1);
        tabulateOccurences(str2);

        double collisionCount = 0;
        double total = 0;
        for (Integer count : occurences.values()){
            if (count >= 2) collisionCount++;
            total++;
        }

        return collisionCount / total;
    }


}

package algorithm;

import java.util.Objects;

class Token {
    private final int hash;
    private final int lineNo;

    Token(int hash, int lineNo) {
        this.hash = hash;
        this.lineNo = lineNo;
    }

    public int getLineNo() {
        return lineNo;
    }


    public int getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object other){
        Token ref = (Token)other;
        return ref.hash == this.hash &&  ref.lineNo == this.lineNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, lineNo);
    }
//    @Override
//    public int hashCode(){
//        return hash + lineNo;
//    }




}

package algorithm;

class TokenTest {
    public static void main(String[] args){
        Token token1 = new Token(123, 1);
        Token token2 = new Token(1234, 1);
        Token token3 = new Token(1234, 1);
        System.out.println(token1.hashCode());
        System.out.println(token2.hashCode());
        System.out.println(token3.hashCode());
    }
}
