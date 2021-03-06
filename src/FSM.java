import java.util.ArrayList;
import java.util.Arrays;

public class FSM {

    private char[] input;
    private int cur = 0;
    private int state = 1;
    private String buf = "";
    private String[] symbols = {"/=", "+", "-", "*", "=", "/", ">", "<", ">=", "<=",
                                ":=", "(", ")", "[", "]", "{", "}", ",", ";", ":", "%"};
    private String[] keywords = {"and", "or", "not", "and",
                                "or", "var", "is", "type",
                                "integer", "real", "boolean",
                                "true", "false", "record",
                                "end", "array", "while",
                                "loop", "in", "reverse",
                                "if", "then", "else",
                                "routine", "not"
    };

    private ArrayList<String> tokens = new ArrayList<>();


    public FSM(char[] input) {
        this.input = input;
    }

    public FSM(String input) {
        this.input = input.toCharArray();
    }

    private boolean isKeyword(String s){
        for (int i = 0; i < keywords.length; i++) {
            if(keywords[i].equals(s)){
                return true;
            }
        }
        return false;
    }

    private boolean in(char ch){
        for (int i = 0; i < symbols.length; i++) {
            String s = "";
            s = Character.toString(ch);
            if (ch=='/'){
                if (input[cur + 1] == '='){
                    s = ch + "=";
                }
            } else if(ch==':'){
                if (input[cur + 1] == '='){
                    s = ch + "=";
                }
            } else if (ch == '>') {
                if (input[cur + 1] == '='){
                    s = ch + "=";
                }
            } else if (ch == '<') {
                if (input[cur + 1] == '=') {
                    s = ch + "=";
                }
            }

            if (symbols[i].equals(s)) return true;
        }

        return false;
    }

    private boolean isComplex(char ch){
        String s;
        s = Character.toString(ch);
        if (ch=='/' || ch == ':' || ch == '>' || ch == '<'){
            if (input[cur + 1] == '=') {
                return true;
            }
        }
        return false;
    }

    private String getComplex(){
        return input[cur] + "=";
    }

    private void initialState(){
        if (Character.isDigit(input[cur]) || input[cur]=='.'){
            state = input[cur]=='.' ? 6 : 2;
            buf += input[cur];
        }
        if (in(input[cur])) {
            state=1;
            if (isComplex(input[cur])) {
                tokens.add(getComplex());
                cur += 2;
            } else {
                tokens.add(Character.toString(input[cur]));
            }
        }
        if(Character.isAlphabetic(input[cur]) || input[cur] == '"'){
            if (input[cur] == '"'){
                state=7;
            }else{
                state=3;
            }
            buf += input[cur];
        }
    }


    private void flush(){
        state = 1;
        tokens.add(buf);
        buf = "";
    }

    private void parseState(){
        if (state == 2){
            if (Character.isDigit(input[cur])) {
                buf+=input[cur];
            }else if (input[cur] == '.'){
                state = 6;
                buf+=input[cur];
            }else{
               flush();
               --cur;
            }
        } else if (state == 3){
            if(Character.isAlphabetic(input[cur]) || input[cur]=='"'){
                if (input[cur]=='"'){
                    state=7;
                }
                buf += input[cur];
                if (isKeyword(buf)) {
                    flush();
                }
            }else{
                flush();
                --cur;
            }
        } else if (state == 4){
            flush();
        }else if(state == 6){
            if (Character.isDigit(input[cur])){
                buf += input[cur];
            }else{
                flush();
                --cur;
            }
        }else if(state == 1){
            initialState();
        }else if(state==7){
            buf += input[cur];
            if (input[cur]=='"'){
                flush();
            }
        }
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }


    public void parse(){
        initialState();
        moveNext();
        while(hasNextToken()){
            parseState();
            moveNext();
        }
        if (!buf.equals("")){
            flush();
        }
    }

    public boolean hasNextToken(){
        return cur < input.length;
    }

    private void moveNext(){
        ++cur;
    }
}
