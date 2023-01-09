package com.nighthawk.spring_portfolio.mvc.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/* In mathematics,
    an expression or mathematical expression is a finite combination of symbols that is well-formed
    according to rules that depend on the context.
   In computers,
    expression can be hard to calculate with precedence rules and user input errors
    to handle computer math we often convert strings into reverse polish notation
    to handle errors we perform try / catch or set default conditions to trap errors
     */
public class Calculator {
    // Key instance variables
    private final String expression;
    private ArrayList<String> tokens;
    private ArrayList<String> reverse_polish;
    private Double result = 0.0;
    private boolean error = false;
    private boolean sqrtYes = false;

    // Helper definition for supported operators
    private final Map<String, Integer> OPERATORS = new HashMap<>();
    {
        // Map<"token", precedence>
        OPERATORS.put("*", 3);
        OPERATORS.put("/", 3);
        OPERATORS.put("%", 3);
        OPERATORS.put("+", 4);
        OPERATORS.put("-", 4);
        OPERATORS.put("^", 2);
        OPERATORS.put("s", 2);
        OPERATORS.put("sqrt", 2);
    }

    // Helper definition for supported operators
    private final Map<String, Integer> SEPARATORS = new HashMap<>();
    {
        // Map<"separator", not_used>
        SEPARATORS.put(" ", 0);
        SEPARATORS.put("(", 0);
        SEPARATORS.put(")", 0);
    }

    // Create a 1 argument constructor expecting a mathematical expression
    public Calculator(String expression) {
        // original input
        this.expression = expression;

        // parse expression into terms
        this.termTokenizer();

        // place terms into reverse polish notation
        this.tokensToReversePolishNotation();

        // calculate reverse polish notation
        this.rpnToResult();
    }

    // Test if token is an operator
    private boolean isOperator(String token) {
        // find the token in the hash map
        return OPERATORS.containsKey(token);
    }

    // Test if token is an separator
    private boolean isSeparator(String token) {
        // find the token in the hash map
        return SEPARATORS.containsKey(token);
    }

    // Compare precedence of operators.
    private Boolean isPrecedent(String token1, String token2) {
        // token 1 is precedent if it is greater than token 2
        return (OPERATORS.get(token1) - OPERATORS.get(token2) >= 0);
    }

    // Term Tokenizer takes original expression and converts it to ArrayList of
    // tokens
    private void termTokenizer() {
        // contains final list of tokens
        this.tokens = new ArrayList<>();

        int start = 0; // term split starting index
        StringBuilder multiCharTerm = new StringBuilder(); // term holder
        for (int i = 0; i < this.expression.length(); i++) {
            Character c = this.expression.charAt(i);
            if (isOperator(c.toString()) || isSeparator(c.toString())) {
                // 1st check for working term and add if it exists
                // IMPORTANT: (no occur in this program), but if start + i same in
                // substring(start, i)
                // will output null
                if (multiCharTerm.length() > 0) {
                    tokens.add(this.expression.substring(start, i));
                }
                // Add operator or parenthesis term to list
                if (c != ' ') {
                    if (c == 's') {
                        tokens.add("sqrt");
                        i += 3;
                    } else {
                        tokens.add(c.toString());
                    }
                }
                // Get ready for next term
                start = i + 1;
                // IMPORTANT: if do new StringBuilder, reset multiCharTerm (this way no put
                // _ (space) in tokens)
                multiCharTerm = new StringBuilder();
            } else {
                // multi character terms: numbers, functions, perhaps non-supported elements
                // Add next character to working term
                multiCharTerm.append(c);
            }

        }
        // Add last term
        if (multiCharTerm.length() > 0) {
            tokens.add(this.expression.substring(start));
        }
    }

    // Takes tokens and converts to Reverse Polish Notation (RPN), this is one where
    // the operator follows its operands.
    private void tokensToReversePolishNotation() {
        // contains final list of tokens in RPN
        this.reverse_polish = new ArrayList<>();

        // stack is used to reorder for appropriate grouping and precedence
        Stack<String> tokenStack = new Stack<String>();
        // IMPORTANT: you can access variables declared in class (ex: tokens)
        for (String token : tokens) {
            switch (token) {
                // If left bracket push token on to stack
                case "(":
                    tokenStack.push(token);
                    break;
                case ")":
                    while (tokenStack.empty() == false && !tokenStack.peek().equals("(")) {
                        reverse_polish.add(tokenStack.pop());
                    }
                    /*
                     * while (tokenStack.peek() != null && !tokenStack.peek().equals("("))
                     * {
                     * reverse_polish.add( tokenStack.pop() );
                     * }
                     */
                    if (tokenStack.empty() == false) {
                        tokenStack.pop();
                        if (sqrtYes == true) {
                            reverse_polish.add("sqrt");
                            sqrtYes = false;
                        }
                    } else {
                        tokenStack.push(token);
                    }
                    break;
                case "sqrt":
                    sqrtYes = true;
                    break;
                // IMPORTANT: Many case together = run same code
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                case "^":
                    // While stack
                    // not empty AND stack top element
                    // and is an operator
                    while (tokenStack.size() > 0 && isOperator(tokenStack.peek())) {
                        if (isPrecedent(token, tokenStack.peek())) {
                            reverse_polish.add(tokenStack.pop());
                            continue;
                        }
                        break;
                    }
                    // Push the new operator on the stack
                    tokenStack.push(token);
                    break;
                default: // Default should be a number, there could be test here
                    this.reverse_polish.add(token);
            }
        }
        // Empty remaining tokens
        while (tokenStack.size() > 0) {
            if (tokenStack.peek().equals("(") || tokenStack.peek().equals(")")) {
                this.error = true;
                tokenStack.pop();
            } else {
                reverse_polish.add(tokenStack.pop());
            }
        }

    }

    // Takes RPN and produces a final result
    private void rpnToResult() {
        // stack is used to hold operands and each calculation
        Stack<Double> calcStack = new Stack<Double>();
        calcStack.push(0.0);
        calcStack.push(0.0);
        // RPN is processed, ultimately calcStack has final result
        for (String token : this.reverse_polish) {
            // If the token is an operator, calculate
            if (isOperator(token)) {
                // Pop the two top entries
                Double num2 = calcStack.pop();
                Double num = calcStack.pop();

                if (token.equals("+")) {
                    result = num + num2;

                }

                if (token.equals("-")) {
                    result = num - num2;

                }

                if (token.equals("*")) {
                    result = num * num2;

                }

                if (token.equals("/")) {
                    result = num / num2;

                }

                if (token.equals("%")) {
                    result = num % num2;

                }

                if (token.equals("^")) {
                    result = Math.pow(num, num2);
                }

                if (token.equals("sqrt")) {
                    // sqrtYes = true;
                    result = Math.sqrt(num2);
                }

                // IMPORTANT: unsure why c't use switch case
                /*
                 * switch (token) {
                 * case "+":
                 * result = num + num2;
                 * case "-":
                 * result = num - num2;
                 * case "*":
                 * result = num * num2;
                 * case "/":
                 * result = num / num2;
                 * case "%":
                 * result = num % num2;
                 * 
                 * }
                 */

                // Calculate intermediate results

                // Push intermediate result back onto the stack
                calcStack.push(result);
            }
            // else the token is a number push it onto the stack
            else {
                calcStack.push(Double.valueOf(token));
                /*
                 * if (sqrtYes == true) {
                 * calcStack.push(Double.valueOf(token));
                 * result = Math.sqrt(Double.valueOf(calcStack.pop()));
                 * calcStack.push(result);
                 * sqrtYes = false;
                 * } else {
                 * calcStack.push(Double.valueOf(token));
                 * }
                 */
            }
        }
        // Pop final result and set as final result for expression
        this.result = calcStack.pop();
    }

    // Print the expression, terms, and result
    public String toString() {
        if (this.error) {
            /*
             * return ("Original expression: " + this.expression + "\n" +
             * "Tokenized expression: " + this.tokens.toString() + "\n" +
             * "Reverse Polish Notation: " +this.reverse_polish.toString() + "\n" +
             * "Final result: " + (new RuntimeException("Error")));
             */

            return ("Original expression: " + this.expression + "\n" +
                    "Tokenized expression: " + this.tokens.toString() + "\n" +
                    "Reverse Polish Notation: " + this.reverse_polish.toString() + "\n" +
                    "Final result: " + String.format("Error"));

        } else {
            return ("Original expression: " + this.expression + "\n" +
                    "Tokenized expression: " + this.tokens.toString() + "\n" +
                    "Reverse Polish Notation: " + this.reverse_polish.toString() + "\n" +
                    "Final result: " + String.format("%.2f", this.result));
        }

    }

    public String toStringJson() {
        if (this.error) {
            return ("{ \"result\": " + "Error" + " }");
        } else {
            return ("{ \"result\": " + this.result + " }");
        }

    }

    // Tester method
    public static void main(String[] args) {
        // Random set of test cases
        Calculator simpleMath = new Calculator("100 + 200  * 3");
        System.out.println("Simple Math\n" + simpleMath);

        System.out.println();

        Calculator parenthesisMath = new Calculator("(100 + 200)  * 3");
        System.out.println("Parenthesis Math\n" + parenthesisMath);

        System.out.println();

        Calculator decimalMath = new Calculator("100.2 - 99.3");
        System.out.println("Decimal Math\n" + decimalMath);

        System.out.println();

        Calculator moduloMath = new Calculator("300 % 200");
        System.out.println("Modulo Math\n" + moduloMath);

        System.out.println();

        Calculator divisionMath = new Calculator("300/200");
        System.out.println("Division Math\n" + divisionMath);

        System.out.println();
        Calculator extraMath = new Calculator("2^0");
        System.out.println("Extra Math\n" + extraMath);

        System.out.println();
        Calculator wrongMath = new Calculator("(2+3))");
        System.out.println("Wrong Math\n" + wrongMath);

        System.out.println();
        Calculator sqrtMath = new Calculator("sqrt(4 + 5)");
        System.out.println("Sqrt Math\n" + sqrtMath);
    }
}