import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ConnectFourApp {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("4-Gewinnt");

        // Eingabedialog für die Größe des Spielfelds
        Shell inputShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        inputShell.setText("Spielfeldgröße setzen");
        inputShell.setLayout(new GridLayout(2, false));

        Text rowsText = new Text(inputShell, SWT.BORDER);
        rowsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        rowsText.setMessage("Zeilen");

        Text columnsText = new Text(inputShell, SWT.BORDER);
        columnsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        columnsText.setMessage("Spalten");

        Button okButton = new Button(inputShell, SWT.PUSH);
        okButton.setText("OK");
        okButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    int rows = Integer.parseInt(rowsText.getText());
                    int columns = Integer.parseInt(columnsText.getText());
                    if (rows < 4 || columns < 4) {
                        showMessage(inputShell, "Die Spalten und Zeilen müssen mindestens 4 sein.");
                        return;
                    }
                    inputShell.dispose();
                    shell.setSize(columns * 100, rows * 100);
                    new GameUI(shell, rows, columns);
                } catch (NumberFormatException ex) {
                    showMessage(inputShell, "Ungültige Eingabe. Gebe in allen Feldern Zahlen an.");
                }
            }
        });

        inputShell.pack();
        inputShell.open();

        while (!inputShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private static void showMessage(Shell shell, String message) {
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
        messageBox.setMessage(message);
        messageBox.open();
    }
}
