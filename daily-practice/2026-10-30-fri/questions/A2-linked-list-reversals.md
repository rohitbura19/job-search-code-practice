# A2. Linked list: reverse, reverse a range, palindrome (10 min)

`ListNode` is given. Implement in `a2/Lists.java`.

1. `reverse(head)`: iterative, in place.
2. `reverseBetween(head, left, right)`: positions are 1-based and inclusive; one pass; `[1,2,3,4,5]`, 2, 4 -> `[1,4,3,2,5]`.
3. `isPalindrome(head)`: O(n) time, O(1) extra space (find the middle, reverse the second half, compare). Restoring the list is a bonus.

Constraints: up to 10^6 nodes (recursion overflows the stack), values may repeat, null is a valid list.
