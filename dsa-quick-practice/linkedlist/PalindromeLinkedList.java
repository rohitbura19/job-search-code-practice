package linkedlist;

// Palindrome Linked List  [Easy | Middle + reverse second half]
// O(n) time, O(1) extra space.
//
// [1,2,2,1] -> true
// [1,2]     -> false

public class PalindromeLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static boolean isPalindrome(ListNode head) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome(of(1, 2, 2, 1))); // true
        System.out.println(isPalindrome(of(1, 2)));       // false
    }

    static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : vals) t = t.next = new ListNode(v);
        return dummy.next;
    }
}
