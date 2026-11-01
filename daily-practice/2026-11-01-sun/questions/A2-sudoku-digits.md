# A2. Valid Sudoku, plus one, longest common prefix (10 min)

`a2/GridAndDigits.java`

1. `isValidSudoku(char[][] 9x9)`: '.' is empty; no repeated digit in a row, column or 3x3 box. The board need not be solvable. Do not mutate the input.
2. `plusOne(int[] digits)`: big-endian digits, return a new array. `[9,9] -> [1,0,0]`. Length up to 200 000.
3. `longestCommonPrefix(String[])`: `["flower","flow","flight"] -> "fl"`; empty array or any empty string -> `""`.
