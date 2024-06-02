import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class GameUI {
    private Game game;
    private Button[][] buttons;
    private final Color player1Color;
    private final Color player2Color;
    private final Shell shell;

    public GameUI(Shell shell, int rows, int columns) {
        this.shell = shell;
        this.game = new Game(rows, columns);
        this.buttons = new Button[rows][columns];
        Display display = shell.getDisplay();
        this.player1Color = display.getSystemColor(SWT.COLOR_RED);
        this.player2Color = display.getSystemColor(SWT.COLOR_BLUE);
        createBoard(); // Erstellt das Spielfeld-UI
    }

    private void createBoard() {
        shell.setLayout(new GridLayout(2, false));

        Composite gridComposite = new Composite(shell, SWT.NONE);
        gridComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gridLayout = new GridLayout(game.getBoard().getColumns(), true);
        gridComposite.setLayout(gridLayout);

        for (int i = 0; i < game.getBoard().getRows(); i++) {
            for (int j = 0; j < game.getBoard().getColumns(); j++) {
                buttons[i][j] = new Button(gridComposite, SWT.NONE);
                buttons[i][j].setText(" ");
                buttons[i][j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                final int column = j;
                buttons[i][j].addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        handleButtonClick(column); // Behandelt Klicks auf die Spaltenknöpfe
                    }
                });
            }
        }

        Composite controlComposite = new Composite(shell, SWT.NONE);
        controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        GridLayout controlLayout = new GridLayout(1, true);
        controlComposite.setLayout(controlLayout);

        Button saveButton = new Button(controlComposite, SWT.PUSH);
        saveButton.setText("Save Game");
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveGame(); // Speichert das aktuelle Spiel
            }
        });

        Button loadButton = new Button(controlComposite, SWT.PUSH);
        loadButton.setText("Load Game");
        loadButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        loadButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadGame(); // Lädt ein gespeichertes Spiel
            }
        });
    }

    private void recreateBoard() {
        // Erstellt das Spielfeld-UI neu
        for (Control control : shell.getChildren()) {
            control.dispose();
        }
        buttons = new Button[game.getBoard().getRows()][game.getBoard().getColumns()];
        createBoard();
        updateBoardUI();
        shell.layout();
        adjustShellSize(); // Passt die Fenstergröße an die Spielfeldgröße an
    }

    private void adjustShellSize() {
        // Passt die Fenstergröße basierend auf der Spielfeldgröße an
        int width = game.getBoard().getColumns() * 100;
        int height = game.getBoard().getRows() * 100;
        shell.setSize(width, height);
    }

    private void handleButtonClick(int column) {
        // Handhabt den Klick auf eine Spalte, um eine Scheibe fallen zu lassen
        new Thread(() -> {
            Display display = Display.getDefault();
            for (int i = game.getBoard().getRows() - 1; i >= 0; i--) {
                if (game.getBoard().getBoard()[i][column] == ' ') {
                    for (int k = 0; k <= i; k++) {
                        final int row = k;
                        display.syncExec(() -> {
                            if (row > 0) {
                                buttons[row - 1][column].setText(" ");
                                buttons[row - 1][column].setBackground(null);
                            }
                            buttons[row][column].setText(String.valueOf(game.getCurrentPlayer()));
                            buttons[row][column].setBackground(game.getCurrentPlayer() == 'X' ? player1Color : player2Color);
                        });
                        try {
                            Thread.sleep(50); // Adjust the speed of the animation here
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    display.syncExec(() -> {
                        if (game.dropDisc(column)) {
                            updateBoardUI();
                            if (game.isGameOver()) {
                                if (game.getBoard().checkWin(game.getCurrentPlayer())) {
                                    showMessage("Spieler " + game.getCurrentPlayer() + " hat gewonnen!");
                                } else {
                                    showMessage("Das Spiel ist unentschieden!");
                                }
                            }
                        }
                    });
                    return;
                }
            }
            display.syncExec(() -> showMessage("Die Spalte ist voll!"));
        }).start();
    }

    private void updateBoardUI() {
        // Aktualisiert die UI basierend auf dem aktuellen Zustand des Spielfelds
        char[][] board = game.getBoard().getBoard();
        for (int i = 0; i < game.getBoard().getRows(); i++) {
            for (int j = 0; j < game.getBoard().getColumns(); j++) {
                buttons[i][j].setText(board[i][j] == ' ' ? " " : String.valueOf(board[i][j]));
                if (board[i][j] == 'X') {
                    buttons[i][j].setBackground(player1Color);
                } else if (board[i][j] == 'O') {
                    buttons[i][j].setBackground(player2Color);
                } else {
                    buttons[i][j].setBackground(null);
                }
            }
        }
    }

    private void showMessage(String message) {
        // Zeigt eine Nachricht in einem Dialogfenster an
        Display.getDefault().syncExec(() -> {
            MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
            messageBox.setMessage(message);
            messageBox.open();
        });
    }

    private void saveGame() {
        // Speichert das aktuelle Spiel in eine Datei
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setFilterExtensions(new String[]{"*.txt"});
        String path = dialog.open();
        if (path != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeObject(game.getBoard());
                oos.writeObject(game.getCurrentPlayer());
                showMessage("Der Spielstand wurde erfolgreich gespeichert.");
            } catch (IOException e) {
                showMessage("Ein Fehler beim speichern des Spielstandes ist aufgetreten: " + e.getMessage());
            }
        }
    }

    private void loadGame() {
        // Lädt ein gespeichertes Spiel aus einer Datei
        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(new String[]{"*.txt"});
        String path = dialog.open();
        if (path != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
                Board loadedBoard = (Board) ois.readObject();
                char loadedPlayer = (char) ois.readObject();
                game.setBoard(loadedBoard);
                game.setCurrentPlayer(loadedPlayer);
                recreateBoard(); // Passt die UI an die neue Spielfeldgröße an
                showMessage("Der Spielstand wurde erfolgreich geladen.");
            } catch (IOException | ClassNotFoundException e) {
                showMessage("Ein Fehler beim laden des Spielstandes ist aufgetreten: " + e.getMessage());
            }
        }
    }
}
