// von Ben Ameti, 10238878

import java.io.Serializable;

public class Board implements Serializable {
    private final int rows;
    private final int columns;
    private final char[][] board;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new char[rows][columns];
        initializeBoard(); // Initialisiert das Spielbrett
    }

    void initializeBoard() {
        // Setzt alle Felder des Spielbretts auf leer (' ')
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean isFull() {
        // Überprüft, ob das gesamte Spielfeld voll ist
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean dropDisc(int column, char player) {
        // Lässt eine Scheibe in die angegebene Spalte fallen
        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][column] == ' ') {
                board[i][column] = player;
                return true;
            }
        }
        return false;
    }

    public boolean checkWin(char player) {
        // Überprüft, ob der angegebene Spieler gewonnen hat
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == player) {
                    if (checkDirection(i, j, 1, 0, player) || // horizontal
                        checkDirection(i, j, 0, 1, player) || // vertikal
                        checkDirection(i, j, 1, 1, player) || // diagonal /
                        checkDirection(i, j, 1, -1, player)) { // diagonal \
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char player) {
        // Überprüft, ob vier aufeinanderfolgende Scheiben in die angegebene Richtung vorhanden sind
        for (int i = 1; i < 4; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= columns || board[newRow][newCol] != player) {
                return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
