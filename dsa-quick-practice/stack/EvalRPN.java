package stack;

// Evaluate Reverse Polish Notation  [Medium | Stack]
// Tokens + - * / and integers. Division truncates toward zero.
//
// [2,1,+,3,*]   -> 9
// [4,13,5,/,+]  -> 6
// [10,6,9,3,+,-11,*,/,*,17,+,5,+] -> 22

public class EvalRPN {

    static int evalRPN(String[] tokens) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(evalRPN(new String[]{"2", "1", "+", "3", "*"}));  // 9
        System.out.println(evalRPN(new String[]{"4", "13", "5", "/", "+"})); // 6
        System.out.println(evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"})); // 22
    }
}
