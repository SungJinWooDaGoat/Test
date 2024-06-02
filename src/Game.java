public class Game {
    private Board board;
    private char currentPlayer;
    private boolean gameOver;

    public Game(int rows, int columns) {
        this.board = new Board(rows, columns);
        this.currentPlayer = 'X'; // Startet das Spiel mit Spieler 'X'
        this.gameOver = false; // Setzt den Spielstatus auf nicht beendet
    }

    public boolean dropDisc(int column) {
        if (gameOver) return false; // Verhindert Züge, wenn das Spiel beendet ist
        boolean success = board.dropDisc(column, currentPlayer);
        if (success) {
            if (board.checkWin(currentPlayer)) {
                gameOver = true;
                return true; // Gibt an, dass ein Spieler gewonnen hat
            } else if (board.isFull()) {
                gameOver = true;
                return true; // Gibt an, dass das Spiel unentschieden ist
            } else {
                switchPlayer(); // Wechselt den Spieler
            }
        }
        return false;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Wechselt zwischen Spieler 'X' und 'O'
    }

    public Board getBoard() {
        return board;
    }

    public void resetGame() {
        board.initializeBoard(); // Setzt das Spielfeld zurück
        currentPlayer = 'X'; // Setzt den aktuellen Spieler zurück
        gameOver = false; // Setzt den Spielstatus zurück
    }

    public void setBoard(Board board) {
        this.board = board; // Setzt ein neues Spielfeld
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer; // Setzt den aktuellen Spieler
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
