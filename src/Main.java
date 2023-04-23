import javax.swing.plaf.IconUIResource;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;
    public static final int ZERO_ASCII = 48;

    /**
     * This method gets the board and the battleships amount and sizes
     * and calls shipLocation method
     */
    public static void getBoard(){
        System.out.println("Enter the board size");
        String size = scanner.nextLine();
        int n = (int)size.charAt(0)-ZERO_ASCII;
        int m = (int)size.charAt(2)-ZERO_ASCII;
        System.out.println("Enter the battleships sizes");
        String battleships = scanner.nextLine();
        int max = 0;
        for(int i = 2; i < battleships.length(); i+=4){
            if(max < battleships.charAt(i))
                max = battleships.charAt(i);
        }
        int[] arrShips = new int[max];
        for(int i = 0; i < battleships.length(); i+=4) {
            arrShips[(int)battleships.charAt(i+2) - ZERO_ASCII] = battleships.charAt(i);
        }
        shipLocation(arrShips, n, m);
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
        if(x < 0 || x > n || y < 0 || y > m){
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
        for(int i = 0; i < 11; i++){
            if(i == 0){
                System.out.print(" ");
            }
            else{
                System.out.print(i-1);
            }
            for(int j = 0; j < 10; j++){
                if(i == 0){
                    System.out.print(" " + j);
                }
                else{
                    System.out.print(" " + boardChar[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * initializes the board by the size given.
     * @param n num of rows.
     * @param m num of columns.
     * @return new board consisting of '–'.
     */
    public static char[][] initializeBoard(int n, int m){
        char[][] boardChar = new char[n][m];
        for (int i = 0; i < n; i++) { //til 75 meth
            for (int j = 0; j < m; j++) {
                boardChar[i][j] = '–';
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
                if (boardChar[j - 1][k] !='–' || boardChar[j + 1][k] != '–'){
                    return true;
                }
            }
        }
        else {
            if(inBounds(j,k+1,n,m) && inBounds(j,k-1,n,m)) {
                if (boardChar[j][k + 1] != '–' || boardChar[j][k - 1] != '–') {
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
                    if(boardChar[x+j][y-1] != '–'){
                        return true;
                    }
                }
                if(inBounds(x+j,endShipY+1,n,m)){
                    if(boardChar[x+j][endShipY+1] != '–'){
                        return true;
                    }
                }
            }
        }
        if(orientation == 1){
            for(int j = -1; j < 2; j++){
                if(inBounds(x-1,y+j,n,m)){
                    if(boardChar[x-1][y+j] != '–'){
                        return true;
                    }
                }
                if(inBounds(endShipX+1,y+j,n,m)){
                    if(boardChar[endShipX+1][y+j] != '–'){
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
    public static boolean isLegalPlacement(int endShipX, int endShipY, int x, int y, int orientation, int n, int m, char[][] boardChar){
        for (int j = x; j <= endShipX; j++) {
            for (int k = y; k <= endShipY; k++) {
                if (boardChar[j][k] != '–') {
                    System.out.println("Battleship overlaps another battleship, try again!");
                    return true;
                }
                if(adjCheckLoop(boardChar, n, m, orientation, j, k)){
                    System.out.println("Adjacent battleship detected, try again!");
                    return true;
                }
            }
        }
        if(adjCheckEdges(boardChar,n,m,orientation,x,y,endShipX,endShipY)){
            System.out.println("Adjacent battleship detected, try again!");
            return true;
        }
        return false;
    }

    public static void shipLocation(int[] arrShips, int n, int m) {
        char[][] boardChar = initializeBoard(n,m);
        printBoard(boardChar, n, m);
        for (int i = 1; i < arrShips.length; i++) {
            while (arrShips[i] > 0) {
                System.out.println("Enter the location and orientation for battleship of size " + i);
                String place = scanner.nextLine();
                int x = (int) place.charAt(0) - ZERO_ASCII;
                int y = (int) place.charAt(2) - ZERO_ASCII;
                int orientation = (int) place.charAt(4) - ZERO_ASCII;
                int endShipX = x, endShipY = y;
                switch (orientation) {
                    case 0: {
                        endShipY = y + i - 1;
                    }
                    case 1: {
                        endShipX = x + i - 1;
                    }
                }
                boolean error = isLegalForBoard(orientation, x, y, endShipX, endShipY, n, m);
                if (error) {
                    continue;
                }
                //todo add method
                error = isLegalPlacement(endShipX,endShipY, x, y,orientation, n, m, boardChar);

                if (!error) {
                    for (int j = x; j <= endShipX; j++) {
                        for (int k = y; k <= endShipY; k++) {
                            boardChar[j][k] = '#';
                        }
                    }
                }
                else {
                    continue;
                }
                printBoard(boardChar, n, m);
                arrShips[i]--; //ship placement had no problems, proceeding to next ship
            }
        }
    }





    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).
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



