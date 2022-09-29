# ReversiAI

CSC 242: Introduction to Artificial Intelligence
Project 1: Reversi Game Bot
Name: Cole Goodman
Email: cgoodma7@u.rochester.edu / coletianyi@gmail.com

Instructions: Run on command line with args of [Your Name]

This Project has 4 classes:
 - Board
 - Node
 - algorithm4x4
 - algorithm8x8

Each with their own purpose:

Board Class:
Board is the class that contains the Board used for reversi. It contains 8 instance variables:
 - int currentPlayer
 - int botColor
 - int playerColor
 - String[][] board
 - boolean big
 - columns
 - rows
 - colors

Board has 2 constructor classes:
- public Board (int currentPlayer, int botColor, boolean big)
    for making the initial board
- public Board (String[][] board, int currentPlayer, int botColor)
    for making a board with the board already constructed
 - more is explained in the comments

Other than the actual board, the Board class has 2 functions that are used really frequently, while the other classes
are just helper functions
 - public Board move (Board board, String location, int color)
    - finds the possible moves on any given board and the color
    - returns a HashMap with all the locations of the possible moves and the directions it took to find the pieces
 - public HashMap<String, ArrayList<Integer>> possibleMoves(Board board, int color)
    - places a piece on the board and the subsequent reversal of other pieces
    - returns a board with another piece on it
More is explained in the commments

Node class:
Although it is used just as much, the node class doesn't have much in it, however, it does use the state space system
like we discussed in class using 4 instance variables
 - Board state
 - Node parent
 - String action
 - int pathCost

Node has 2 Constructor classes:
 - public Node (Board state)
 - public Node (Board state, Node parent, String action, int cost)
More is explained in comments

algorithm4x4 & algorithm8x8 classes:
These two classes are relatively the same, but one of them uses the simple miniMax algorithm, while the other uses a
Heuristic search function and alpha beta pruning. I figured that the implementation for the algorithm8x8 function would
be much harder than it was. It was mostly copy&paste. More explained in comments.
