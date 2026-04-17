from pathlib import Path
import json

path = Path('src/main/resources/blind75.json')
text = path.read_text()
marker = '"title": "Merge Intervals"'
idx = text.find(marker)
if idx == -1:
    raise SystemExit('marker not found')

prefix = text[:idx]
problems = [
    {
        'title': 'Merge Intervals',
        'chapterTitle': 'Sorting',
        'problemType': 'fundamentals',
        'difficulty': 'MEDIUM',
        'tags': 'sorting,intervals',
        'description': 'Given a collection of intervals, merge all overlapping intervals.',
        'solutionTemplate': 'class Solution {\n    public int[][] merge(int[][] intervals) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[][] intervals = new int[n][2];\n        for (int i = 0; i < n; i++) {\n            intervals[i][0] = sc.nextInt();\n            intervals[i][1] = sc.nextInt();\n        }\n\n        Solution sol = new Solution();\n        int[][] merged = sol.merge(intervals);\n        for (int[] interval : merged) {\n            System.out.print("[" + interval[0] + "," + interval[1] + "] ");\n        }\n    }\n}',
        'testCases': [
            {'input': '4\n1 3\n2 6\n8 10\n15 18', 'output': '[1,6] [8,10] [15,18] ', 'sample': True},
            {'input': '2\n1 4\n4 5', 'output': '[1,5] ', 'sample': False}
        ],
        'quizQuestion': 'What sort order is required before merging intervals?',
        'quizOptions': 'By start time, By end time, By length, No sort needed',
        'quizCorrectAnswer': 'By start time'
    },
    {
        'title': 'Binary Search',
        'chapterTitle': 'Binary Search',
        'problemType': 'logic',
        'difficulty': 'EASY',
        'tags': 'binary-search',
        'description': 'Implement binary search to find the target value in a sorted array.',
        'solutionTemplate': 'class Solution {\n    public int binarySearch(int[] nums, int target) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[] nums = new int[n];\n        for (int i = 0; i < n; i++) nums[i] = sc.nextInt();\n        int target = sc.nextInt();\n\n        Solution sol = new Solution();\n        System.out.print(sol.binarySearch(nums, target));\n    }\n}',
        'testCases': [
            {'input': '5\n1 2 3 4 5\n3', 'output': '2', 'sample': True},
            {'input': '4\n1 2 3 4\n7', 'output': '-1', 'sample': False}
        ],
        'quizQuestion': 'What is the time complexity of binary search?',
        'quizOptions': 'O(log n), O(n), O(n log n), O(1)',
        'quizCorrectAnswer': 'O(log n)'
    },
    {
        'title': 'Factorial',
        'chapterTitle': 'Recursion',
        'problemType': 'fundamentals',
        'difficulty': 'EASY',
        'tags': 'recursion,math',
        'description': 'Compute the factorial of a given non-negative integer using recursion.',
        'solutionTemplate': 'class Solution {\n    public long factorial(int n) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n\n        Solution sol = new Solution();\n        System.out.print(sol.factorial(n));\n    }\n}',
        'testCases': [
            {'input': '5', 'output': '120', 'sample': True},
            {'input': '0', 'output': '1', 'sample': False}
        ],
        'quizQuestion': 'What is the base case for factorial recursion?',
        'quizOptions': 'n == 0, n == 1, n < 0, n == 2',
        'quizCorrectAnswer': 'n == 0'
    },
    {
        'title': 'Merge Two Sorted Lists',
        'chapterTitle': 'Linked-List',
        'problemType': 'logic',
        'difficulty': 'EASY',
        'tags': 'linked-list',
        'description': 'Merge two sorted linked lists and return it as a new sorted list.',
        'solutionTemplate': 'class Solution {\n    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {\n        \n    }\n}\n\nclass ListNode {\n    int val;\n    ListNode next;\n    ListNode(int x) { val = x; }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        ListNode l1 = null, tail1 = null;\n        for (int i = 0; i < n; i++) {\n            ListNode node = new ListNode(sc.nextInt());\n            if (l1 == null) l1 = tail1 = node;\n            else { tail1.next = node; tail1 = node; }\n        }\n        int m = sc.nextInt();\n        ListNode l2 = null, tail2 = null;\n        for (int i = 0; i < m; i++) {\n            ListNode node = new ListNode(sc.nextInt());\n            if (l2 == null) l2 = tail2 = node;\n            else { l2 = node; tail2 = node; }\n        }\n\n        Solution sol = new Solution();\n        ListNode merged = sol.mergeTwoLists(l1, l2);\n        while (merged != null) {\n            System.out.print(merged.val + " ");\n            merged = merged.next;\n        }\n    }\n}',
        'testCases': [
            {'input': '3\n1 2 4\n3\n1 3 4', 'output': '1 1 2 3 4 4 ', 'sample': True},
            {'input': '0\n0', 'output': '', 'sample': False}
        ],
        'quizQuestion': 'What is the time complexity of merging two sorted lists?',
        'quizOptions': 'O(n+m), O(nm), O(n^2), O(log n)',
        'quizCorrectAnswer': 'O(n+m)'
    },
    {
        'title': 'Single Number',
        'chapterTitle': 'Bit Manipulation',
        'problemType': 'fundamentals',
        'difficulty': 'EASY',
        'tags': 'bitwise,xor',
        'description': 'Given a non-empty array of integers, every element appears twice except for one. Find that single one.',
        'solutionTemplate': 'class Solution {\n    public int singleNumber(int[] nums) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[] nums = new int[n];\n        for (int i = 0; i < n; i++) nums[i] = sc.nextInt();\n\n        Solution sol = new Solution();\n        System.out.print(sol.singleNumber(nums));\n    }\n}',
        'testCases': [
            {'input': '3\n2 2 1', 'output': '1', 'sample': True},
            {'input': '5\n4 1 2 1 2', 'output': '4', 'sample': False}
        ],
        'quizQuestion': 'Which bitwise operator helps find the single number most efficiently?',
        'quizOptions': 'XOR, AND, OR, NOT',
        'quizCorrectAnswer': 'XOR'
    },
    {
        'title': 'Gas Station',
        'chapterTitle': 'Greedy Algorithms',
        'problemType': 'logic',
        'difficulty': 'MEDIUM',
        'tags': 'greedy',
        'description': 'Find the starting gas station index if you can travel around the circuit once.',
        'solutionTemplate': 'class Solution {\n    public int canCompleteCircuit(int[] gas, int[] cost) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[] gas = new int[n];\n        int[] cost = new int[n];\n        for (int i = 0; i < n; i++) gas[i] = sc.nextInt();\n        for (int i = 0; i < n; i++) cost[i] = sc.nextInt();\n\n        Solution sol = new Solution();\n        System.out.print(sol.canCompleteCircuit(gas, cost));\n    }\n}',
        'testCases': [
            {'input': '5\n1 2 3 4 5\n3 4 5 1 2', 'output': '3', 'sample': True},
            {'input': '3\n2 3 4\n3 4 3', 'output': '-1', 'sample': False}
        ],
        'quizQuestion': 'What type of algorithm solves Gas Station efficiently?',
        'quizOptions': 'Greedy, Dynamic Programming, Backtracking, Divide & Conquer',
        'quizCorrectAnswer': 'Greedy'
    },
    {
        'title': 'Minimum Size Subarray Sum',
        'chapterTitle': 'Sliding Window / 2 Pointer',
        'problemType': 'fundamentals',
        'difficulty': 'MEDIUM',
        'tags': 'sliding-window',
        'description': 'Find the minimal length of a contiguous subarray with sum >= target.',
        'solutionTemplate': 'class Solution {\n    public int minSubArrayLen(int target, int[] nums) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int target = sc.nextInt();\n        int n = sc.nextInt();\n        int[] nums = new int[n];\n        for (int i = 0; i < n; i++) nums[i] = sc.nextInt();\n\n        Solution sol = new Solution();\n        System.out.print(sol.minSubArrayLen(target, nums));\n    }\n}',
        'testCases': [
            {'input': '7\n6\n2 3 1 2 4 3', 'output': '2', 'sample': True},
            {'input': '11\n3\n1 2 3', 'output': '0', 'sample': False}
        ],
        'quizQuestion': 'What is the best sliding window time complexity?',
        'quizOptions': 'O(n), O(n^2), O(log n), O(1)',
        'quizCorrectAnswer': 'O(n)'
    },
    {
        'title': 'Valid Parentheses',
        'chapterTitle': 'Stack / Queues',
        'problemType': 'fundamentals',
        'difficulty': 'EASY',
        'tags': 'stack,string',
        'description': 'Determine if the input string of brackets is valid.',
        'solutionTemplate': 'class Solution {\n    public boolean isValid(String s) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String s = sc.nextLine();\n\n        Solution sol = new Solution();\n        System.out.print(sol.isValid(s));\n    }\n}',
        'testCases': [
            {'input': '()[]{}', 'output': 'true', 'sample': True},
            {'input': '(]', 'output': 'false', 'sample': False}
        ],
        'quizQuestion': 'What data structure is best for matching parentheses?',
        'quizOptions': 'Stack, Queue, LinkedList, HashMap',
        'quizCorrectAnswer': 'Stack'
    },
    {
        'title': 'Maximum Depth of Binary Tree',
        'chapterTitle': 'Binary Trees',
        'problemType': 'fundamentals',
        'difficulty': 'EASY',
        'tags': 'tree,dfs',
        'description': 'Return the maximum depth of a binary tree.',
        'solutionTemplate': 'class Solution {\n    public int maxDepth(TreeNode root) {\n        \n    }\n}\n\nclass TreeNode {\n    int val;\n    TreeNode left;\n    TreeNode right;\n    TreeNode(int x) { val = x; }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    private static TreeNode buildTree(String[] vals) {\n        if (vals.length == 0 || vals[0].equals("null")) return null;\n        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));\n        Queue<TreeNode> queue = new LinkedList<>();\n        queue.add(root);\n        int index = 1;\n        while (!queue.isEmpty() && index < vals.length) {\n            TreeNode node = queue.poll();\n            if (!vals[index].equals("null")) {\n                node.left = new TreeNode(Integer.parseInt(vals[index]));\n                queue.add(node.left);\n            }\n            index++;\n            if (index < vals.length && !vals[index].equals("null")) {\n                node.right = new TreeNode(Integer.parseInt(vals[index]));\n                queue.add(node.right);\n            }\n            index++;\n        }\n        return root;\n    }\n\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String line = sc.nextLine();\n        String[] vals = line.split(" ");\n        TreeNode root = buildTree(vals);\n\n        Solution sol = new Solution();\n        System.out.print(sol.maxDepth(root));\n    }\n}',
        'testCases': [
            {'input': '3 9 20 null null 15 7', 'output': '3', 'sample': True},
            {'input': '1 null 2', 'output': '2', 'sample': False}
        ],
        'quizQuestion': 'Which traversal is easiest to use for tree depth?',
        'quizOptions': 'DFS, BFS, Inorder, Preorder',
        'quizCorrectAnswer': 'DFS'
    },
    {
        'title': 'Validate Binary Search Tree',
        'chapterTitle': 'Binary Search Trees',
        'problemType': 'logic',
        'difficulty': 'MEDIUM',
        'tags': 'bst,tree',
        'description': 'Determine if a binary tree is a valid binary search tree.',
        'solutionTemplate': 'class Solution {\n    public boolean isValidBST(TreeNode root) {\n        \n    }\n}\n\nclass TreeNode {\n    int val;\n    TreeNode left;\n    TreeNode right;\n    TreeNode(int x) { val = x; }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    private static TreeNode buildTree(String[] vals) {\n        if (vals.length == 0 || vals[0].equals("null")) return null;\n        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));\n        Queue<TreeNode> queue = new LinkedList<>();\n        queue.add(root);\n        int index = 1;\n        while (!queue.isEmpty() && index < vals.length) {\n            TreeNode node = queue.poll();\n            if (!vals[index].equals("null")) {\n                node.left = new TreeNode(Integer.parseInt(vals[index]));\n                queue.add(node.left);\n            }\n            index++;\n            if (index < vals.length && !vals[index].equals("null")) {\n                node.right = new TreeNode(Integer.parseInt(vals[index]));\n                queue.add(node.right);\n            }\n            index++;\n        }\n        return root;\n    }\n\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        String line = sc.nextLine();\n        String[] vals = line.split(" ");\n        TreeNode root = buildTree(vals);\n\n        Solution sol = new Solution();\n        System.out.print(sol.isValidBST(root));\n    }\n}',
        'testCases': [
            {'input': '2 1 3', 'output': 'true', 'sample': True},
            {'input': '5 1 4 null null 3 6', 'output': 'false', 'sample': False}
        ],
        'quizQuestion': 'For a BST, left subtree values must be _____ than the root.',
        'quizOptions': 'Smaller, Larger, Equal, Random',
        'quizCorrectAnswer': 'Smaller'
    },
    {
        'title': 'Top K Frequent Elements',
        'chapterTitle': 'Heaps',
        'problemType': 'logic',
        'difficulty': 'MEDIUM',
        'tags': 'heap,priority-queue',
        'description': 'Return the k most frequent elements in the array.',
        'solutionTemplate': 'class Solution {\n    public int[] topKFrequent(int[] nums, int k) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        int[] nums = new int[n];\n        for (int i = 0; i < n; i++) nums[i] = sc.nextInt();\n        int k = sc.nextInt();\n\n        Solution sol = new Solution();\n        int[] result = sol.topKFrequent(nums, k);\n        for (int num : result) System.out.print(num + " ");\n    }\n}',
        'testCases': [
            {'input': '6\n1 1 1 2 2 3\n2', 'output': '1 2 ', 'sample': True},
            {'input': '4\n1 2 3 4\n1', 'output': '1 ', 'sample': False}
        ],
        'quizQuestion': 'Which data structure is typically used for top K frequent elements?',
        'quizOptions': 'Heap, Stack, LinkedList, Array',
        'quizCorrectAnswer': 'Heap'
    },
    {
        'title': 'Number of Islands',
        'chapterTitle': 'Graphs',
        'problemType': 'fundamentals',
        'difficulty': 'MEDIUM',
        'tags': 'graph,dfs,bfs',
        'description': 'Count the number of islands in a 2D grid of 1s and 0s.',
        'solutionTemplate': 'class Solution {\n    public int numIslands(char[][] grid) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int rows = sc.nextInt();\n        int cols = sc.nextInt();\n        char[][] grid = new char[rows][cols];\n        for (int i = 0; i < rows; i++) {\n            for (int j = 0; j < cols; j++) {\n                grid[i][j] = sc.next().charAt(0);\n            }\n        }\n\n        Solution sol = new Solution();\n        System.out.print(sol.numIslands(grid));\n    }\n}',
        'testCases': [
            {'input': '3 3\n1 1 0\n1 0 0\n0 0 1', 'output': '2', 'sample': True},
            {'input': '1 1\n0', 'output': '0', 'sample': False}
        ],
        'quizQuestion': 'Which traversal can count connected components in a grid?',
        'quizOptions': 'DFS, Binary Search, Greedy, Sorting',
        'quizCorrectAnswer': 'DFS'
    },
    {
        'title': 'Climbing Stairs',
        'chapterTitle': 'Dynamic Programming',
        'problemType': 'fundamentals',
        'difficulty': 'EASY',
        'tags': 'dp',
        'description': 'Count how many distinct ways to climb n stairs taking 1 or 2 steps.',
        'solutionTemplate': 'class Solution {\n    public int climbStairs(int n) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n\n        Solution sol = new Solution();\n        System.out.print(sol.climbStairs(n));\n    }\n}',
        'testCases': [
            {'input': '2', 'output': '2', 'sample': True},
            {'input': '3', 'output': '3', 'sample': False}
        ],
        'quizQuestion': 'What DP technique is most appropriate for Climbing Stairs?',
        'quizOptions': 'Tabulation, Backtracking, Greedy, Sorting',
        'quizCorrectAnswer': 'Tabulation'
    },
    {
        'title': 'Implement Trie',
        'chapterTitle': 'Tries',
        'problemType': 'logic',
        'difficulty': 'MEDIUM',
        'tags': 'trie,string',
        'description': 'Design a trie with insert, search, and startsWith operations.',
        'solutionTemplate': 'class Trie {\n    public Trie() {\n        \n    }\n\n    public void insert(String word) {\n        \n    }\n\n    public boolean search(String word) {\n        \n    }\n\n    public boolean startsWith(String prefix) {\n        \n    }\n}',
        'driverCode': 'import java.util.*;\n\n{{SOLUTION}}\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        Trie trie = new Trie();\n        for (int i = 0; i < n; i++) {\n            String op = sc.next();\n            String word = sc.next();\n            if (op.equals("insert")) trie.insert(word);\n            else if (op.equals("search")) System.out.print(trie.search(word) + " ");\n            else if (op.equals("startsWith")) System.out.print(trie.startsWith(word) + " ");\n        }\n    }\n}',
        'testCases': [
            {'input': '5\ninsert apple\nsearch apple\nsearch app\nstartsWith app\ninsert app', 'output': 'true false true ', 'sample': True},
            {'input': '2\ninsert bat\nsearch bat', 'output': 'true ', 'sample': False}
        ],
        'quizQuestion': 'What is the common branching factor used in a trie for lowercase letters?',
        'quizOptions': '26, 10, 2, 52',
        'quizCorrectAnswer': '26'
    }
]
'
with open('/tmp/fix_blind75_json.py', 'w') as f:
    f.write(text)
print('script written')
