package org.example;

import java.util.*;

public class RPN {
    private static final Map<Character, Integer> PRECEDENCE = Map.of(
            '+', 1, '-', 1,
            '*', 2, '/', 2,
            '^', 3
    );

    /** Konwertuje wyrażenie infix na postfix (ONP) */
    public static String infixToPostfix(String infix) {
        StringBuilder out = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            if (Character.isWhitespace(c)) continue;

            if (Character.isDigit(c) || c == '.') {
                // czytamy liczbę (wielocyfrową lub zmiennoprzecinkową)
                int j = i;
                while (j < infix.length() &&
                        (Character.isDigit(infix.charAt(j)) || infix.charAt(j)=='.')) {
                    out.append(infix.charAt(j++));
                }
                out.append(' ');
                i = j - 1;
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    out.append(stack.pop()).append(' ');
                }
                stack.pop(); // usuwamy '('
            } else if (PRECEDENCE.containsKey(c)) {
                while (!stack.isEmpty() && stack.peek() != '(' &&
                        (PRECEDENCE.get(stack.peek()) > PRECEDENCE.get(c) ||
                                (PRECEDENCE.get(stack.peek()).equals(PRECEDENCE.get(c)) && c != '^'))) {
                    out.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            out.append(stack.pop()).append(' ');
        }
        return out.toString().trim();
    }

    /** Ewaluacja wyrażenia w notacji postfix (ONP) */
    public static double evaluatePostfix(String postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        Scanner sc = new Scanner(postfix);
        while (sc.hasNext()) {
            if (sc.hasNextDouble()) {
                stack.push(sc.nextDouble());
            } else {
                String op = sc.next();
                double b = stack.pop(), a = stack.pop();
                switch (op) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> stack.push(a / b);
                    case "^" -> stack.push(Math.pow(a, b));
                    default  -> throw new IllegalArgumentException("Nieznany operator: " + op);
                }
            }
        }
        return stack.pop();
    }

    /** Pełne obliczenie: infix → wynik */
    public static double evaluate(String infix) {
        String postfix = infixToPostfix(infix);
        return evaluatePostfix(postfix);
    }

    /** Zwraca sformatowany ciąg "infix = wynik" */
    public static String formatResult(String infix) {
        double res = evaluate(infix);
        return infix + " " + res;
    }
}

