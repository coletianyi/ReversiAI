import java.util.*;

public class ReversiAI {
    // Board Class
    static class Board {
        int currentPlayer;  // the current move
        int botColor;       // the program's color
        int playerColor;    // the opponent's color
        String[][] board;   // the reversi board
        boolean big;        // the indicator for if it will be a 4x4 or 8x8 board

        String[] columns = new String[]{"a", "b", "c", "d"};    // the columns for the 4x4 board
        String[] rows = new String[]{"1", "2", "3", "4"};       // the rows for the 4x4 board
        String[] colors = new String[]{"B", "W"};               // the colors of the pieces to be put on the board

        /** Board constructor class
         * @param currentPlayer indication of whom the method is called for
         * @param botColor the program's color
         * @param big indicator for if the Board will be 4x4 or 8x8
         * */
        public Board (int currentPlayer, int botColor, boolean big){
            this.big = big;
            this.currentPlayer = currentPlayer;
            this.botColor = botColor;
            playerColor = (botColor + 1) % 2;

            // small board
            if (!big) {
                board = new String[][]{
                        {" ", " ", " ", " "},
                        {" ", "B", "W", " "},
                        {" ", "W", "B", " "},
                        {" ", " ", " ", " "},
                };
            }
            // big board
            else {
                columns = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"}; // the columns for the 8x8 board
                rows = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};    // the rows for the 8x8 board
                board = new String[][]{
                        {" ", " ", " ", " ", " ", " ", " ", " "},
                        {" ", " ", " ", " ", " ", " ", " ", " "},
                        {" ", " ", " ", " ", " ", " ", " ", " "},
                        {" ", " ", " ", "B", "W", " ", " ", " "},
                        {" ", " ", " ", "W", "B", " ", " ", " "},
                        {" ", " ", " ", " ", " ", " ", " ", " "},
                        {" ", " ", " ", " ", " ", " ", " ", " "},
                        {" ", " ", " ", " ", " ", " ", " ", " "},
                };
            }
        }

        /** Board constructor class with board as parameter
         * @param board the new board of the board class
         * @param currentPlayer indication of whom the method is called for
         * @param botColor the program's color
         * */
        public Board (String[][] board, int currentPlayer, int botColor){
            this.currentPlayer = currentPlayer;
            this.botColor = botColor;
            playerColor = (botColor+1)%2;
            this.board = board;
            if (board.length == 8){
                columns = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
                rows = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
            }
        }

        /** numSquares method
         * - finds the number of squares on the board that are the desired piece
         * @param board the board
         * @param piece the piece/color that the program is trying to find
         * */
        private int numSquares (String[][] board, String piece){
            int ret = 0;
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board[i].length; j++){
                    if (board[i][j].equals(piece)){
                        ret++;
                    }
                }
            }
            return ret;
        }

        /** possibleMoves method
         * - finds the possible moves on any given board and the color
         * @param board the board
         * @param color the color that the program is trying to find the possible moves for
         * @return HashMap with all the locations of the possible moves and the directions it took to find the pieces
         * */
        public HashMap<String, ArrayList<Integer>> possibleMoves(Board board, int color){
            HashMap<String, ArrayList<Integer>> ret = new HashMap<>();
            String[][] b = board.board;
            // find the locations of all the colors
            ArrayList<int[]> colorLocations = colorLocations(b, color);

            // for each desired colored square
            for (int[] location : colorLocations){
                int row = location[0];
                int column = location[1];

                // find if there are adjacent opposite colors
                boolean[] oppositeColorAdjacent = adjacent(b, row, column, colors[(color+1)%2]);
                for (int i = 0; i < oppositeColorAdjacent.length; i++){
                    if (oppositeColorAdjacent[i]){
                        int[] line = expandLine(b, row, column, i, colors[(color+1)%2]);
                        // if there is an opposite color in that location, go in that direction until there is a blank square
                        if (line != null){
                            String newEntry = columns[line[1]]+rows[line[0]];
                            if (ret.containsKey(newEntry)){
                                ret.get(newEntry).add(8-i);
                            }
                            else {
                                ArrayList<Integer> directions = new ArrayList<>();
                                directions.add(8-i);
                                ret.put(newEntry, directions);
                            }
                        }
                    }
                }
            }
            return ret;
        }

        /** colorLocations method
         * - helper method for possibleMoves
         * - finds all the locations of the desired color on the board
         * @param board the board
         * @param color the desired color
         * */
        private ArrayList<int[]> colorLocations(String[][] board, int color){
            ArrayList<int[]> ret = new ArrayList<>();
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board[i].length; j++){
                    if (board[i][j].equals(colors[color])){
                        ret.add(new int[]{i, j});
                    }
                }
            }
            return ret;
        }

        /** adjacent method
         * - helper method for possibleMoves
         * - finds the directions of all the pieces adjacent to a desired piece
         * @param board the board
         * @param row the row of the desired piece
         * @param column the column of the desired piece
         * @param oppositeColor the color of the piece the program is trying to find
         * */
        private boolean[] adjacent(String[][] board, int row, int column, String oppositeColor){
            boolean[] ret = new boolean[]{false, false, false, false, false, false, false, false, false};
            try {
                if (board[row-1][column-1].equals(oppositeColor)){
                    ret[0] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row-1][column].equals(oppositeColor)){
                    ret[1] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row-1][column+1].equals(oppositeColor)){
                    ret[2] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row][column-1].equals(oppositeColor)){
                    ret[3] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row][column].equals(oppositeColor)){
                    ret[4] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row][column+1].equals(oppositeColor)){
                    ret[5] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row+1][column-1].equals(oppositeColor)){
                    ret[6] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row+1][column].equals(oppositeColor)){
                    ret[7] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            try{
                if (board[row+1][column+1].equals(oppositeColor)){
                    ret[8] = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            return ret;
        }

        /** expandLine method
         * - helper method for possibleMoves
         * - goes in a certain direction until there is either a blank space, a piece of the same color,
         *   or the edge of the board
         * @param board the board
         * @param row the row of the desired piece
         * @param column the column of the desired piece
         * @param direction the direction the function would have to go
         * @param oppositeColor the color of the piece the program is trying to go until otherwise
         * */
        private int[] expandLine (String[][] board, int row, int column, int direction, String oppositeColor){
            try {
                if (direction == 0) {
                    do {
                        row--;
                        column--;
                    } while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e){
                return null;
            }
            try{
                if (direction == 1) {
                    do {
                        row--;
                    } while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e){
                return null;
            }
            try {
                if (direction == 2) {
                    do{
                        row--;
                        column++;
                    } while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e){
                return null;
            }
            try {
                if (direction == 3) {
                    do {
                        column--;
                    } while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e){
                return null;
            }
            try {
                if (direction == 5) {
                    do {
                        column++;
                    } while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            try {
                if (direction == 6) {
                    do {
                        row++;
                        column--;
                    }
                    while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            try {
                if (direction == 7) {
                    do {
                        row++;
                    }
                    while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            try {
                if (direction == 8) {
                    do {
                        row++;
                        column++;
                    }
                    while (board[row][column].equals(oppositeColor));
                    if (board[row][column].equals(" "))
                        return new int[]{row, column};
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            return null;
        }

        /** copy method
         * - helper method for move
         * - used to make a deep copy of the board
         * */
        private String[][] copy (String[][] board){
            String[][] ret = new String[board.length][board[0].length];
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board[i].length; j++){
                    ret[i][j] = board[i][j];
                }
            }
            return ret;
        }

        /** move method
         * - places a piece on the board and the subsequent reversal of other pieces
         * @param board the board
         * @param location the location the program would like to place a piece on the board
         * @param color the color of the piece
         * @return board with another piece on it
         * */
        public Board move (Board board, String location, int color) {
            // deep copy of the board
            String[][] nodeBoard = copy(board.board);
            int p = board.currentPlayer;
            int c = board.botColor;
            Board b = new Board(nodeBoard, p, c);

            // finds if the move desired can't move
            if (b.possibleMoves(b, color).keySet().size() == 0){
                System.out.println(colors[color]+" is out of moves!");
                b.currentPlayer = (b.currentPlayer+1)%2;
                return b;
            }

            // finds if b is a valid move
            if (!b.possibleMoves(b, color).containsKey(location)){
                System.out.println(b.possibleMoves(b, color).keySet());
                System.out.println(location);
                return null;
            }

            // finding the correct position [row, column]
            int row = Integer.parseInt(location.charAt(1)+"")-1;
            int column = 0;
            String x = location.charAt(0)+"";
            for (int i = 0; i < this.columns.length; i++){
                if (this.columns[i].equals(x)){
                    column = i;
                    break;
                }
            }

            // for each direction in the possible directions
            ArrayList<Integer> directions = possibleMoves(b, color).get(location);
            for (int dir : directions){
                // change the board to the desired color
                b = changeBoard(b, row, column, dir, colors[color]);
            }
            // change the current player to the opposite color
            b.currentPlayer = (b.currentPlayer+1)%2;
            return b;
        }

        /** changeBoard method
         * - helper method for changeBoard
         * - helps change the board to the correct pieces after a move is made
         * */
        private Board changeBoard (Board board, int row, int column, int dir, String color){
            Board ret = board;
            ret.board[row][column] = color;
            if (dir == 0){
                row--;
                column--;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row--][column--] = color;
                }
            }
            if (dir == 1){
                row--;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row--][column] = color;
                }
            }
            if (dir == 2){
                row--;
                column++;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row--][column++] = color;
                }
            }
            if (dir == 3){
                column -= 1;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row][column--] = color;
                }
            }
            if (dir == 5){
                column++;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row][column++] = color;
                }
            }
            if (dir == 6){
                row++;
                column--;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row++][column--] = color;
                }
            }
            if (dir == 7){
                row++;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row++][column] = color;
                }
            }if (dir == 8){
                row++;
                column++;
                while (!ret.board[row][column].equals(color) && !ret.board[row][column].equals(" ")){
                    ret.board[row++][column++] = color;
                }
            }
            return ret;
        }

        public String toString(){
            String ret = "";
            if (board.length==8)
                ret += "  a b c d e f g h\n";
            else
                ret += "  a b c d\n";
            for (int i = 0; i < board.length; i++){
                ret += (i+1) + " ";
                for (int j = 0; j < board[i].length; j++){
                    ret += board[i][j] + " ";
                }
                ret += (i+1)+"\n";
            }
            if (board.length==8)
                ret += "  a b c d e f g h\n";
            else
                ret += "  a b c d\n";
            return ret;
        }
    }

    // Node Class
    static class Node {
        Board state;        // the current state of the Node
        Node parent;        // the parent of the node
        String action;      // the action the node went
        int pathCost;       // the pathCost

        // the initial node set
        public Node (Board state){
            this.state = state;
            this.parent = null;
            action = "";
            pathCost = 0;
        }

        // the deep copy node set
        public Node (Board state, Node parent, String action, int cost){
            this.state = state;
            this.parent = parent;
            this.action = action;
            pathCost = cost;
        }
        public String toString(){
            String ret = "Node Stats:\n";
            ret += "Node State: \n" + state.toString();
            if (parent == null)
                ret += "\nNode Parent: null";
            else
                ret += "\nNode Parent: \n" + parent.state.toString();
            ret += "\nNode action:\n"+action;
            ret += "\nPath Cost: "+pathCost;
            return ret;
        }
    }

    // miniMax Algorithm class
    static class algorithm4x4 {
        int ai;             // the index of the ai's color
        int opponent;       // the index of the player's color
        Node initialState;  // the initial state of the algorithm

        // Constructor class
        public algorithm4x4(Node initialState){
            this.initialState = initialState;
            ai = initialState.state.botColor;
            opponent = initialState.state.playerColor;
        }

        /** isGoal method
         * - to determine whether the state the function is looking at is a goal state or not
         * @param state the state we are determining
         * */
        public boolean isGoal(Node state){
            Board board = state.state;

            // if neither size has any available moves -> return true
            if (board.possibleMoves(board, 0).keySet().size() == 0 &&
                    board.possibleMoves(board, 1).keySet().size() == 0)
                return true;

            // if there are no more available spaces -> return true
            return state.state.numSquares(state.state.board, " ") == 0;
        }

        /** result method
         * - to find the result after putting a piece on the board
         * @param node the state that the program is currently in
         * @param location the place in which the program wishes to place the piece
         * @param currentPlayer indication of whom the method is called for
         * */
        public Node result (Node node, String location, int currentPlayer){
            // tempNode that creates a deep copy of the node to store as a parent
            Node tempNode = new Node(node.state, node.parent, node.action, node.pathCost);

            // board instantiation to use the move method in the Board class
            Board board = new Board(node.state.board, currentPlayer, node.state.botColor);
            board=new Board(board.move(node.state, location, board.currentPlayer).board, board.currentPlayer, board.botColor);

            // return the move method
            return new Node(board, tempNode, location, node.pathCost+1);
        }

        /** miniMaxSearch method
         * - to find the correct move after searching the entire tree recursively
         * @param s the state that the program is currently in
         * @param possibleMoves a set of all the possible moves the state can possibly move
         * */
        public String miniMaxSearch(Node s, Set<String> possibleMoves) {
            // filler variables that are to be replaced later
            int bestValue = -10000;
            String bestMove = "FILLER";
            String valueMove = bestValue+","+bestMove;

            // find the maximum possible value to move given the state the program is in and returning it
            bestMove = maxValue(s, possibleMoves, valueMove);
            return bestMove;
        }

        /** maxValue method
         * - to find the maximum possible value for the program to move
         * @param s the state that the program is currently in
         * @param possibleMoves a set of all the possible moves the state can possibly move
         * @param valueMove a string comprised of two variables: value and move
         * */
        public String maxValue (Node s, Set<String> possibleMoves, String valueMove){
            // if the current state is a goal state, then return the utility value
            if (isGoal(s)) {
                return utility(s, s.state.currentPlayer == 0) + "," + valueMove.split(",")[1];
            }

            int v = -10000;
            String[] vM = valueMove.split(",");
            // for every action in the possible moves the state can make
            for (String a : possibleMoves){
                // find the result of the move and get the worst case scenario the opponent can make
                Node result = result(s, a, ai);
                String v2a2 = minValue(result, result.state.possibleMoves(result.state, opponent).keySet(), valueMove);

                // split up the string and find if the value is better than v
                String[] vM2 = v2a2.split(",");
                if (Integer.parseInt(vM2[0]) > v){
                    // set the values to the best possible value
                    vM[1] = a;
                    vM[0] = vM2[0];
                }
            }
            return vM[0]+","+vM[1];
        }

        /** minValue method
         * - to find the minimum possible value for the opponent to move (hoping for the worst)
         * @param s the state that the program is currently in
         * @param possibleMoves a set of all the possible moves the state can possibly move
         * @param valueMove a string comprised of two variables: value and move
         * */
        public String minValue (Node s, Set<String> possibleMoves, String valueMove){
            // if the current state is a goal state, then return the utility value
            if (isGoal(s)) {
                return utility(s, s.state.currentPlayer == 0) + "," + valueMove.split(",")[1];
            }
            int v = 10000;
            String[] vM = valueMove.split(",");
            // for every action in the possible moves the state can make
            for (String a : possibleMoves){
                // find the result of the move and get the best case move the function can make
                Node result = result(s, a, opponent);
                String v2a2 = maxValue(result, result.state.possibleMoves(result.state, ai).keySet(), valueMove);

                // split up the string and find if the value is better than v
                String[] vM2 = v2a2.split(",");
                if (Integer.parseInt(vM2[0]) < v){
                    // set the values to the worst possible value
                    vM[0] = vM2[0];
                    vM[1] = a;
                }
            }
            return vM[0]+","+vM[1];
        }

        /** utility method
         * - to return a certain value for when a goal state is reached
         * @param s the state that the program is currently in
         * @param black to determine if the piece is black or not
         * */
        public int utility  (Node s, boolean black) {
            // finds the number of black and white squares on the board
            int whiteSquares = s.state.numSquares(s.state.board, "W");
            int blackSquares = s.state.numSquares(s.state.board, "B");

            // if the computer wins, it returns 1, if the computer loses -1, if it is a tie, 0
            if (black && whiteSquares > blackSquares) return -1;
            if (black && whiteSquares < blackSquares) return 1;
            if (whiteSquares > blackSquares) return 1;
            if (whiteSquares < blackSquares) return -1;
            return 0;
        }
    }

    // H-MiniMax w/a-B pruning Algorithm class
    static class algorithm8x8{
        int ai;             // the index of the ai's color
        int opponent;       // the index of the player's color
        Node initialState;  // the initial state of the algorithm

        int depth;          // the depth of the current node
        int depthLimit;     // the desired depth of the algorithm

        // Constructor class
        public algorithm8x8 (Node initialState, int depthLimit){
            this.initialState = initialState;
            ai = initialState.state.botColor;
            opponent = initialState.state.playerColor;
            depth = 0;
            this.depthLimit = depthLimit;
        }

        /** hAlphaBetaSearch method
         * - to find the most correct move after searching the entire tree recursively
         * @param s the state that the program is currently in
         * @param possibleMoves a set of all the possible moves the state can possibly move
         * */
        public String hAlphaBetaSearch(Node s, Set<String> possibleMoves){
            // filler variables that are to be replaced later
            int bestValue = -10000;
            String bestMove = "FILLER";
            String valueMove = bestValue+","+bestMove;

            // setting the alpha and beta for pruning
            int alpha = -10000;
            int beta = 10000;

            // find the maximum possible value to move given the state the program is in and returning it
            valueMove = maxValue (s, possibleMoves, alpha, beta, valueMove);
            return valueMove;
        }

        /** maxValue method
         * - to find the maximum possible value for the program to move
         * @param s the state that the program is currently in
         * @param possibleMoves a set of all the possible moves the state can possibly move
         * @param valueMove a string comprised of two variables: value and move
         * */
        public String maxValue(Node s, Set<String> possibleMoves, int alpha, int beta, String valueMove){
            // increase the depth by 1 every time
            depth+=1;

            // if the current state is at its cutoff/goal state, then return the evaluation function
            if (isCutOff(s, depth)) {
                return eval(s, s.state.currentPlayer) + "," + valueMove.split(",")[1];
            }

            int v = -10000;
            String[] vM = valueMove.split(",");
            // for every action in the possible moves the state can make
            for (String a : possibleMoves){
                // find the result of the move and get the worst case scenario the opponent can make
                Node result = result(s, a, ai);
                String v2a2 = minValue(result, result.state.possibleMoves(result.state, opponent).keySet(), alpha, beta, valueMove);
                // split up the string and find if the value is better than v
                String[] vM2 = v2a2.split(",");
                if (Integer.parseInt(vM2[0]) > v){
                    // set the values to the best possible value
                    vM[1] = a;
                    vM[0] = vM2[0];
                    v = Integer.parseInt(vM2[0]);
                    // set alpha to the highest of v and alpha
                    alpha = Math.max(alpha, v);
                }
                // if v is getting bigger than beta, then kill the program and send up the valueMove
                if (v >= beta){
                    return v+","+vM[1];
                }
            }
            return vM[0]+","+vM[1];
        }

        /** minValue method
         * - to find the minimum possible value for the opponent to move (hoping for the worst)
         * @param s the state that the program is currently in
         * @param possibleMoves a set of all the possible moves the state can possibly move
         * @param valueMove a string comprised of two variables: value and move
         * */
        public String minValue (Node s, Set<String> possibleMoves, int alpha, int beta, String valueMove){
            // increase the depth by 1 every time
            depth+=1;

            // if the current state is at its cutoff/goal state, then return the evaluation function
            if (isCutOff(s, depth)) {
                return eval(s, s.state.currentPlayer) + "," + valueMove.split(",")[1];
            }
            String[] vM = valueMove.split(",");
            int v = 10000;
            // for every action in the possible moves the state can make
            for (String a : possibleMoves){
                // find the result of the move and get the best case move the function can make
                Node result = result(s, a, opponent);
                String v2a2 = maxValue(result, result.state.possibleMoves(result.state, ai).keySet(), alpha, beta, valueMove);
                String[] vM2 = v2a2.split(",");
                // set the values to the worst possible value
                if (Integer.parseInt(vM2[0]) < v){
                    vM[0] = vM2[0];
                    vM[1] = a;
                    v = Integer.parseInt(vM2[0]);
                    // set beta to the lowest of v and beta
                    beta = Math.min(alpha, v);
                }
                // if v is getting smaller than alpha, then kill the program and send up the valueMove
                if (v <= alpha){
                    return v+","+vM[1];
                }
            }
            return vM[0]+","+vM[1];
        }

        /** isGoal method
         * - to determine whether the state the function is at its cutOff point
         * @param state the state we are determining
         * @param depth the depth
         * */
        public boolean isCutOff(Node state, int depth){
            // if the program has reached its depth limit, it has reached its cutoff point
            if (depth >= depthLimit) return true;
            Board board = state.state;

            // if neither size has any available moves -> return true
            if (board.possibleMoves(board, 0).keySet().size() == 0 &&
                    board.possibleMoves(board, 1).keySet().size() == 0)
                return true;
            // if there are no more available spaces -> return true
            return state.state.numSquares(state.state.board, " ") == 0;
        }

        /** result method
         * - to find the result after putting a piece on the board
         * @param node the state that the program is currently in
         * @param location the place in which the program wishes to place the piece
         * @param currentPlayer indication of whom the method is called for
         * */
        public Node result (Node node, String location, int currentPlayer){
            // tempNode that creates a deep copy of the node to store as a parent
            Node tempNode = new Node(node.state, node.parent, node.action, node.pathCost);

            // board instantiation to use the move method in the Board class
            Board board = new Board(node.state.board, currentPlayer, node.state.botColor);
            board=new Board(board.move(node.state, location, board.currentPlayer).board, board.currentPlayer, board.botColor);

            // return the move method
            return new Node(board, tempNode, location, node.pathCost+1);
        }

        /** eval method
         * - to give a reasonable heuristic function to determine what the best move is
         * @param s the state
         * @param currentPlayer the color of the current player
         * */
        public int eval  (Node s, int currentPlayer) {
            /* Weighting is based on 3 things:
            * - discCount : 1
            * - amount of legal moves : 100
            * - corner piece count : 1000
            * */
            int discCount = s.state.numSquares(s.state.board, s.state.colors[currentPlayer]);
            int legalMoves = s.state.possibleMoves(s.state, (currentPlayer+1)%2).keySet().size();
            int cornerSquareCount = 0;
            if (s.state.board[0][0].equals(s.state.colors[currentPlayer]))
                cornerSquareCount+=1;
            if (s.state.board[s.state.board.length-1][0].equals(s.state.colors[currentPlayer]))
                cornerSquareCount+=1;
            if (s.state.board[0][s.state.board.length-1].equals(s.state.colors[currentPlayer]))
                cornerSquareCount+=1;
            if (s.state.board[s.state.board.length-1][s.state.board.length-1].equals(s.state.colors[currentPlayer]))
                cornerSquareCount+=1;

            //weighting
            int ret = 0;
            ret += discCount;
            ret += (legalMoves*100);
            ret += (cornerSquareCount*1000);
            return ret;
        }
    }

    public static void run4x4(boolean black) {
        int cP;
        if (black)
            cP = 0;
        else
            cP = 1;
        Board b = new Board(cP, (cP + 1) % 2, false);
        Node state = new Node(b);
        System.out.println(state.state);
        System.out.println("Possible Moves: " + state.state.possibleMoves(state.state, b.currentPlayer).keySet());
        Scanner in = new Scanner(System.in);
        String[] pieces = new String[]{"B", "W"};
        System.out.println("Enter a move: (q to quit)");
        String move = in.next();
        try {
            if (move.equalsIgnoreCase("q")) {
                System.out.println("Ending program");
                throw new ArrayIndexOutOfBoundsException();
            } else {
                b = new Board(b.move(b, move, b.currentPlayer).board, b.currentPlayer, b.botColor);
                state = new Node(b, state, state.action, state.pathCost);
                b.currentPlayer = (b.currentPlayer+1)%2;
                while (!move.equalsIgnoreCase("q")) {
                    if (b.currentPlayer == b.playerColor) {
                        System.out.println(state.state);
                        System.out.println("Enter a move: (q to quit)");
                        if (b.possibleMoves(b, b.currentPlayer).keySet().size() != 0) {
                            System.out.println("Possible Moves: " + state.state.possibleMoves(state.state, b.currentPlayer).keySet());
                            move = in.next();
                            if (move.equalsIgnoreCase("q"))
                                throw new ArrayIndexOutOfBoundsException();
                            b = new Board(b.move(b, move, b.currentPlayer).board, b.currentPlayer, b.botColor);
                            state = new Node(b, state, state.action, state.pathCost);
                            b.currentPlayer = (b.currentPlayer + 1) % 2;
                        } else {
                            System.out.println(pieces[b.currentPlayer++ % 2] + " is out of moves!!");
                            Board temp = new Board(b.board, b.currentPlayer % 2, b.botColor);
                            if (temp.possibleMoves(b, b.currentPlayer).keySet().size() == 0) {
                                System.out.println(pieces[b.currentPlayer % 2] + " is out of moves!!");
                                break;
                            }
                        }
                    } else {
                        try {
                            System.out.println(state.state);
                            algorithm4x4 aiTurn = new algorithm4x4(state);
                            String aiMove = aiTurn.miniMaxSearch(aiTurn.initialState, b.possibleMoves(b, b.currentPlayer).keySet());
                            String[] ai = aiMove.split(",");
                            System.out.println("AI Move: " + ai[1]);
                            b = new Board(b.move(b, ai[1], b.currentPlayer).board, b.currentPlayer, b.botColor);
                            state = new Node(b, state, state.action, state.pathCost);
                            b.currentPlayer = (b.currentPlayer + 1) % 2;
                        } catch (StackOverflowError e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
                if (b.numSquares(b.board, "B") > b.numSquares(b.board, "W"))
                    System.out.println("Black wins!");
                else
                    System.out.println("White wins!");
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Game ended");
        }
    }

    public static void run8x8(boolean black){
        int cP;
        if (black)
            cP = 0;
        else
            cP=1;
        Board b = new Board(cP, (cP+1)%2, true);
        Node state = new Node(b);
        System.out.println(state.state);
        System.out.println(state.state.possibleMoves(state.state, b.currentPlayer).keySet());
        Scanner in  = new Scanner(System.in);
        String[] pieces = new String[]{"B", "W"};
        System.out.println("Enter a move: ");
        String move = in.next();
        b = new Board(b.move(b, move, b.currentPlayer).board, b.currentPlayer, b.botColor);
        state = new Node(b, state, state.action, state.pathCost);
        b.currentPlayer = (b.currentPlayer+1)%2;
        while (!move.equals("q")){
            if (b.currentPlayer == b.playerColor){
                System.out.println(state.state);
                System.out.println("Enter a move: ");
                if (b.possibleMoves(b, b.currentPlayer).keySet().size() != 0) {
                    System.out.println(state.state.possibleMoves(state.state, b.currentPlayer).keySet());
                    move = in.next();
                    b = new Board(b.move(b, move, b.currentPlayer).board, b.currentPlayer, b.botColor);
                    state = new Node(b, state, state.action, state.pathCost);
                    b.currentPlayer = (b.currentPlayer+1)%2;
                }
                else {
                    System.out.println(pieces[b.currentPlayer++%2]+ " is out of moves!!");
                    Board temp = new Board(b.board,b.currentPlayer%2, b.botColor);
                    if (temp.possibleMoves(b, b.currentPlayer).keySet().size() == 0){
                        System.out.println(pieces[b.currentPlayer%2] +" is out of moves!!");
                        break;
                    }
                }
            }
            else {
                try {
                    System.out.println(state.state);
                    algorithm8x8 aiTurn = new algorithm8x8(state, 12);
                    String aiMove = aiTurn.hAlphaBetaSearch(aiTurn.initialState, b.possibleMoves(b, b.currentPlayer).keySet());
                    String[] ai = aiMove.split(",");
                    System.out.println(ai[1]);
                    b = new Board(b.move(b, ai[1], b.currentPlayer).board, b.currentPlayer, b.botColor);
                    state = new Node(b, state, state.action, state.pathCost);
                    b.currentPlayer = (b.currentPlayer + 1) % 2;
                }
                catch (StackOverflowError e){
                    e.printStackTrace();
                    break;
                }
            }
        }
        if (b.numSquares(b.board, "B")>b.numSquares(b.board, "W"))
            System.out.println("Black wins!");
        else
            System.out.println("White wins!");
    }

    public static void main(String[] args) {
        System.out.println("HELLO " + args[0]+"!!!!!");
        System.out.println("Reversi/Othello by Cole Goodman");
        Scanner in = new Scanner(System.in);
        System.out.print("""
                Choose your game:
                1: 4x4
                2: 8x8
                """);
        String boardSize = in.next();
        System.out.println("Do you want to be Black (B) or White (W)");
        boolean black = in.next().equalsIgnoreCase("B");
        if (boardSize.equals("1")){
            run4x4(black);
        }
        else
            run8x8(black);
    }
}