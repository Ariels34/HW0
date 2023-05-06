import javax.swing.plaf.IconUIResource;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;
    public static final char NO_SHIP = '–';
    public static final char SHIP = '#';
    public static final char HIT_PLAYER = 'X';
    public static final char HIT_GUESSING = 'V';
    public static final char MISS_GUESSING = 'X';




    /**
     * gets the board and the battleships amount and sizes from user
     * @return int array that contains the sizes and amount of ships
     */
    public static int[] getBoard(){
        System.out.println("Enter the board size");
        String size = scanner.nextLine();
        String[] newSize = size.split("X");
        int n = (Integer.parseInt(newSize[0]));
        int m = (Integer.parseInt(newSize[1]));
        System.out.println("Enter the battleships sizes");
        String battleships = scanner.nextLine();
        int max = 0;
        String[] sizes = battleships.split(" ");
        int[][] newSizes = new int[sizes.length][2];
        for(int i = 0; i < sizes.length; i++){
            String[] temp = sizes[i].split("X");
            newSizes[i][0] = Integer.parseInt(temp[0]);
            newSizes[i][1] = Integer.parseInt(temp[1]);
        }


        for(int i = 0; i < newSizes.length; i++){
            if(max <= newSizes[i][1])
                max = newSizes[i][1];
        }
        int[] arrShips = new int[max+3];
        for(int i = 0; i < newSizes.length; i++) {
            arrShips[newSizes[i][1]] = newSizes[i][0];
        }

        arrShips[max+1] = n;
        arrShips[max+2] = m;
        return arrShips;

    }

    /**
     * checks if location given is on the board.
     * @param x num of row
     * @param y num of column
     * @param n size of board - rows
     * @param m size of board - columns
     * @return true if in bounds, false otherwise.
     */
    public static boolean inBounds(int x, int y, int n, int m){
        if(x < 0 || x > n-1 || y < 0 || y > m-1){
            return false;
        }
        return true;
    }

    /**
     * checks if the location of the ship is possible in regard to the input and the board given.
     * @param orientation direction of the ship: 0 - horizontal, 1 - vertical
     * @param x num of row where ship starts
     * @param y num of column where ship starts
     * @param endShipX num of row where ship ends
     * @param endShipY num of column where ship ends
     * @param n num of rows in board
     * @param m num of columns in board
     * @return true if placement is illegal, false otherwise.
     */
    public static boolean isLegalForBoard( int orientation, int x, int y,int endShipX, int endShipY, int n, int m){
        if(orientation != 0 && orientation != 1) {
            System.out.println("Illegal orientation, try again!");
            return true;
        }
        if(!inBounds(x, y, n, m)){
            System.out.println("Illegal tile, try again!");
            return true;
        }
        if(!inBounds(endShipX, endShipY, n, m)){
            System.out.println("Battleship exceeds the boundaries of the board, try again!");
            return true;
        }
        return false;
    }



    /**
     * prints the current board as requested.
     * @param boardChar the current board
     * @param n num of rows in board
     * @param m num of columns in board
     */
    public static void printBoard(char[][] boardChar, int n ,int m){
        int len = String.valueOf(n).length();
        for(int k = 0; k < len; k++){
            System.out.print(" ");
        }
        for(int i = 0; i < m; i++){
            System.out.print(" " + i);
        }
        System.out.println();
        for(int i = 0; i < n; i++){
            for(int k = 0; k < len-String.valueOf(i).length(); k++){
                System.out.print(" ");
            }
            for(int j = 0; j < m+1; j++){
                if(j==0){
                    System.out.print(i);
                }
                else{
                    System.out.print(" " +boardChar[i][j-1]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * initializes the board by the size given.
     * @param n num of rows.
     * @param m num of columns.
     * @return new board consisting of '–'.
     */
    public static char[][] initializeBoard(int n, int m){
        char[][] boardChar = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                boardChar[i][j] = NO_SHIP;
            }
        }
        return boardChar;
    }

    /**
     * checks for adjacent ships above or beside of the ship.
     * @param boardChar the current board
     * @param n n num of rows in board
     * @param m n num of columns in board
     * @param orientation direction of the ship: 0 - horizontal, 1 - vertical
     * @param j the loop pointer(vertical axis)
     * @param k the loop pointer(horizontal axis)
     * @return true if placement is illegal, false otherwise.
     */
    public static boolean adjCheckLoop(char[][] boardChar,int n, int m, int orientation, int j, int k){
        if (orientation == 0) {
            if(inBounds(j-1,k,n,m) && inBounds(j+1,k,n,m)) {
                if (boardChar[j - 1][k] !=NO_SHIP || boardChar[j + 1][k] != NO_SHIP){
                    return true;
                }
            }
        }
        else {
            if(inBounds(j,k+1,n,m) && inBounds(j,k-1,n,m)) {
                if (boardChar[j][k + 1] != NO_SHIP || boardChar[j][k - 1] != NO_SHIP) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks for adjacent ships above or beside the "edges" of the ship.
     * @param boardChar the current board
     * @param n num of rows in board
     * @param m num of columns in board
     * @param orientation direction of the ship: 0 - horizontal, 1 - vertical
     * @param x num of row where ship starts
     * @param y num of column where ship starts
     * @param endShipX num of row where ship ends
     * @param endShipY num of column where ship ends
     * @return true if placement is illegal, false otherwise.
     */
    public static boolean adjCheckEdges(char[][] boardChar,int n, int m, int orientation, int x, int y, int endShipX,int endShipY ){
        if(orientation == 0){
            for(int j = -1; j < 2; j++){
                if(inBounds(x+j,y-1,n,m)){
                    if(boardChar[x+j][y-1] != NO_SHIP){
                        return true;
                    }
                }
                if(inBounds(x+j,endShipY+1,n,m)){
                    if(boardChar[x+j][endShipY+1] != NO_SHIP){
                        return true;
                    }
                }
            }
        }
        if(orientation == 1){
            for(int j = -1; j < 2; j++){
                if(inBounds(x-1,y+j,n,m)){
                    if(boardChar[x-1][y+j] != NO_SHIP){
                        return true;
                    }
                }
                if(inBounds(endShipX+1,y+j,n,m)){
                    if(boardChar[endShipX+1][y+j] != NO_SHIP){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * the method combines all other methods used to determine if ships placement is illegal.
     * @param endShipX num of row where ship ends
     * @param endShipY num of columns where ship ends
     * @param x num of row where ship starts
     * @param y num of column where ship starts
     * @param orientation direction of the ship: 0 - horizontal, 1 - vertical
     * @param n num of rows in board
     * @param m num of columns in board
     * @param boardChar the current board
     * @return true if placement is illegal, false otherwise.
     */
    public static boolean isLegalPlacement(int endShipX, int endShipY, int x, int y, int orientation, int n, int m, char[][] boardChar, boolean player){
        for (int j = x; j <= endShipX; j++) {
            for (int k = y; k <= endShipY; k++) {
                if (boardChar[j][k] != NO_SHIP) {
                    if(player)
                        System.out.println("Battleship overlaps another battleship, try again!");
                    return true;
                }
            }
        }

        for (int j = x; j <= endShipX; j++) {
            for (int k = y; k <= endShipY; k++) {
                if(adjCheckLoop(boardChar, n, m, orientation, j, k)){
                    if(player)
                        System.out.println("Adjacent battleship detected, try again!");
                    return true;
                }
            }
        }

        if(adjCheckEdges(boardChar,n,m,orientation,x,y,endShipX,endShipY)){
            if(player)
                System.out.println("Adjacent battleship detected, try again!");
            return true;
        }
        return false;
    }


    /**
     * places the user's ships according to the location the user inserts
     * @param arrShips sizes and amount of ships
     * @param n num of row in board
     * @param m num of columns in board
     * @return char[][] board - updated user's game board with the ship
     */
    public static char[][] shipLocation(int[] arrShips, int n, int m) {
        char[][] boardChar = initializeBoard(n,m);
        System.out.println("Your current game board:");
        printBoard(boardChar, n, m);

        boolean error = false;
        int[] tempShips = new int[arrShips.length];
        for(int i = 0; i < arrShips.length; i++){
            tempShips[i] = arrShips[i];
        }


        for (int i = 1; i < arrShips.length; i++) {
            while (arrShips[i] > 0) {
                System.out.println("Enter the location and orientation for battleship of size " + i);
                String place = scanner.nextLine();
                String[] newPlace = place.split(", ");
                int x = Integer.parseInt(newPlace[0]);
                int y = Integer.parseInt(newPlace[1]);
                int orientation = Integer.parseInt(newPlace[2]);
                int endShipX = x, endShipY = y;
                switch (orientation) {
                    case 0:
                        endShipY = y + i - 1;
                        break;
                    case 1:
                        endShipX = x + i - 1;
                        break;
                }
                error = isLegalForBoard(orientation, x, y, endShipX, endShipY, n, m);
                if (error) {
                    continue;
                }

                error = isLegalPlacement(endShipX,endShipY, x, y,orientation, n, m, boardChar,true);

                if (!error) {
                    for (int j = x; j <= endShipX; j++) {
                        for (int k = y; k <= endShipY; k++) {
                            boardChar[j][k] = SHIP;
                        }
                    }
                }
                else {
                    continue;
                }
                System.out.println("Your current game board:");
                printBoard(boardChar, n, m);
                arrShips[i]--; //ship placement had no problems, proceeding to next ship
            }
        }

        for(int i = 0; i < arrShips.length; i++){
            arrShips[i] = tempShips[i];
        }

        return boardChar;
    }

    /**
     * places the computer's ships randomly
     * @param arrShips sizes and amount of ships
     * @param n num of row in board
     * @param m num of columns in board
     * @return char[][] board - updated computer's game board with the ship
     */
    public static char[][] shipLocationComputer(int[] arrShips, int n, int m) {
        char[][] boardChar = initializeBoard(n,m);

        int[] tempShips = new int[arrShips.length];
        for(int i = 0; i < arrShips.length; i++){
            tempShips[i] = arrShips[i];
        }

        for (int i = 1; i < arrShips.length; i++) {
            while (arrShips[i] > 0) {
                int x = rnd.nextInt(n);
                int y = rnd.nextInt(m);
                int orientation = rnd.nextInt(2);
                int endShipX = x, endShipY = y;
                switch (orientation) {
                    case 0:
                        endShipY = y + i - 1;
                        break;
                    case 1:
                        endShipX = x + i - 1;
                        break;
                }
                if(!inBounds(endShipX, endShipY, n, m)){
                    continue;
                }

                boolean error = isLegalPlacement(endShipX,endShipY, x, y,orientation, n, m, boardChar, false);

                if (!error) {
                    for (int j = x; j <= endShipX; j++) {
                        for (int k = y; k <= endShipY; k++) {
                            boardChar[j][k] = SHIP;
                        }
                    }
                }
                else {
                    continue;
                }
                arrShips[i]--; //ship placement had no problems, proceeding to next ship
            }
        }

        for(int i = 0; i < arrShips.length; i++){
            arrShips[i] = tempShips[i];
        }

        return boardChar;
    }


    /**
     * simulates the user's turn - guessing and attacking
     * @param guessingBoardPlayer the guessing board of the player
     * @param gameBoardComputer the game board of the computer
     * @param n num of rows in board
     * @param m num of columns in board
     * @param numOfShips num of ships in current game
     * @return updated num of ships
     */
    public static int playerTurn(char[][] guessingBoardPlayer, char[][] gameBoardComputer, int n, int m, int numOfShips) {
        int flag = 0;
        int x = 0,y = 0;
        System.out.println("Your current guessing board:");
        printBoard(guessingBoardPlayer, n, m);
        System.out.println("Enter a tile to attack");
        while(flag==0) {
            String place = scanner.nextLine();
            String[] newPlace = place.split(", ");
            x = Integer.parseInt(newPlace[0]);
            y = Integer.parseInt(newPlace[1]);
            if (!inBounds(x, y, n, m)) {
                System.out.println("Illegal tile, try again!");
                continue;
            }
            if (alreadyAttacked(guessingBoardPlayer, x, y)) {
                System.out.println("Tile already attacked, try again!");
                continue;
            }
            flag = 1;
        }
        int result = hitOrMiss(gameBoardComputer, x, y, n, m, numOfShips,true);
        if(result != 0){
            if(result == 2)
                numOfShips--;
            }
            guessingBoardPlayer[x][y] = HIT_GUESSING;
        }
        else {
            guessingBoardPlayer[x][y] = MISS_GUESSING;
        }
        return numOfShips;
    }


    /**
     * checks if the ship has already been attacked
     * @param guessingBoard the guessing board we are checking
     * @param x index of rows
     * @param y index of columns
     * @return true if te ship has already been attacked, false otherwise
     */
    public static boolean alreadyAttacked(char[][] guessingBoard, int x, int y){
        return guessingBoard[x][y] == HIT_GUESSING || guessingBoard[x][y] == MISS_GUESSING;
    }

    /**
     * checks if the guess is a hit or a miss, and if the attacked ship drowned
     * @param gameBoard the game board we are checking
     * @param x index of rows
     * @param y index of columns
     * @param n num of rows in board
     * @param m num of columns in board
     * @param numOfShips num of ships in current game
     * @param isPlayer - flag is true if the player called the method, false otherwise
     * @return 0 if it's a miss, 1 if it's a hit, 2 if it's a hit and the ship has drowned
     */
    public static int hitOrMiss(char[][] gameBoard, int x, int y, int n, int m, int numOfShips, boolean isPlayer){
        if(gameBoard[x][y] == SHIP){
            gameBoard[x][y] = HIT_PLAYER;
            System.out.println("That is a hit!");
            if(!shipLeft(gameBoard, x, y, n, m)){
                if(isPlayer){
                    System.out.println("The computer's battleship has been drowned, "+ (numOfShips-1) + " more battleships to go!");
                }
                else{
                    System.out.println("Your battleship has been drowned, you have left " + (numOfShips-1) + " more battleships!");
                }
                return 2;
            }
            return 1;
        }
        else{
            System.out.println("That is a miss!");
            return 0;
        }
    }

    /**
     * checks if there is a ship left in all directions surrounding the current sell
     * @param gameBoard the current game board we are checking
     * @param x index of rows
     * @param y index of columns
     * @param n num of rows in board
     * @param m num of columns in board
     * @return true if there is a ship left, false otherwise
     */
    public static boolean shipLeft(char[][] gameBoard, int x, int y, int n, int m){
        int up = x-1, down =x+1;
        int left = y-1, right = y+1;
        boolean rightFlag = false;
        boolean leftFlag = false;
        boolean upFlag = false;
        boolean downFlag = false;

        if(inBounds(up, y, n, m) && gameBoard[up][y] != NO_SHIP){
            upFlag = true;
            while(inBounds(up, y, n, m) && gameBoard[up][y] == HIT_PLAYER){
                up--;
            }
            if(!inBounds(up, y, n, m) || (inBounds(up, y, n,m) && gameBoard[up][y] == NO_SHIP)){
                upFlag = false;
            }
        }

        if(inBounds(down, y, n, m) && gameBoard[down][y] != NO_SHIP){
            downFlag = true;
            while(inBounds(down, y, n, m) && gameBoard[down][y] == HIT_PLAYER){
                down++;
            }
            if(!inBounds(down, y, n, m) || (inBounds(down, y, n,m) && gameBoard[down][y] == NO_SHIP)){
                downFlag = false;
            }
        }

        if(inBounds(x, left, n, m) && gameBoard[x][left] != NO_SHIP){
            leftFlag = true;
            while(inBounds(x, left, n, m) && gameBoard[x][left] == HIT_PLAYER){
                left--;
            }
            if(!inBounds(x, left, n, m)|| (inBounds(x, left, n, m) && gameBoard[x][left] == NO_SHIP)){
                leftFlag = false;
            }
        }

        if(inBounds(x, right, n, m) && gameBoard[x][right] != NO_SHIP){
            rightFlag = true;
            while(inBounds(x, right, n, m) && gameBoard[x][right] == HIT_PLAYER){
                right++;
            }
            if(!inBounds(x, right, n, m)|| (inBounds(x, right, n, m) && gameBoard[x][right] == NO_SHIP)){
                rightFlag = false;
            }
        }

        if(!upFlag && !downFlag && !rightFlag && !leftFlag){
            return false;
        }

        return true;
    }

    /**
     * simulates the computer's turn - guessing and attacking
     * @param guessingBoardComputer updated guessing board of the computer
     * @param gameBoardPlayer updated game board of the player
     * @param n num of rows in board
     * @param m num of columns in board
     * @param numOfShips num of ships in current game
     * @return updated num of ships
     */
    public static int computerTurn(char[][] guessingBoardComputer, char[][] gameBoardPlayer, int n, int m, int numOfShips){
        int flag = 0;
        int x = 0,y = 0;
        while(flag==0) {
            x = rnd.nextInt(n);
            y = rnd.nextInt(m);
            if (alreadyAttacked(guessingBoardComputer, x, y)) {
                continue;
            }
            flag = 1;
        }
        System.out.println("The computer attacked (" + x + ", " + y+")");
        int result = hitOrMiss(gameBoardPlayer, x,y, n, m, numOfShips, false);
        if(result != 0){
            if(result == 2){
                numOfShips--;
            }
            guessingBoardComputer[x][y] = HIT_GUESSING;
        }
        else {
            guessingBoardComputer[x][y] = MISS_GUESSING;
        }
        return numOfShips;
    }


    /**
     * simulates the battleship game
     */
    public static void battleshipGame() {
        /* getting the sizes ad amount of ships */
        int numOfShips = 0;
        int[] arr = getBoard();
        int n = arr[arr.length-2];
        int m = arr[arr.length-1];
        int[] arrShips = new int[arr.length-2];
        for(int i = 0; i < arrShips.length; i++){
            arrShips[i] = arr[i];
            numOfShips += arr[i];
        }
        int numOfShipsComputer = numOfShips;
        int numOfShipsPlayer = numOfShips;
        /* initializing all game and guessing boards */
        char[][] gameBoardPlayer = shipLocation(arrShips, n, m);
        char[][] gameBoardComputer = shipLocationComputer(arrShips, n, m);
        char[][] guessingBoardPlayer = initializeBoard(n,m);
        char[][] guessingBoardComputer = initializeBoard(n,m);
        while(true) {
            /* the player plays his turn */
            numOfShipsComputer = playerTurn(guessingBoardPlayer, gameBoardComputer, n, m, numOfShipsComputer);
            if (numOfShipsComputer == 0) {
                System.out.println("You won the game!");
                break;
            }
            /* the computer plays his turn */
            numOfShipsPlayer = computerTurn(guessingBoardComputer, gameBoardPlayer, n, m, numOfShipsPlayer);
            if (numOfShipsPlayer == 0) {
                System.out.println("Your current game board:");
                printBoard(gameBoardPlayer, n, m);
                System.out.println("You lost ):");
                break;
            }
            System.out.println("Your current game board:");
            printBoard(gameBoardPlayer, n, m);
        }
    }




    public static void main(String[] args) throws IOException {
        String path = args[0];
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Total of " + numberOfGames + " games.");

        for (int i = 1; i <= numberOfGames; i++) {
            scanner.nextLine();
            int seed = scanner.nextInt();
            rnd = new Random(seed);
            scanner.nextLine();
            System.out.println("Game number " + i + " starts.");
            battleshipGame();
            System.out.println("Game number " + i + " is over.");
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("All games are over.");
    }
}


