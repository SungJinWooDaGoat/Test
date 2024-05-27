package prüfung;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Startmenü {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Startmenü");
        shell.setSize(400, 200);
        shell.setLayout(new GridLayout(2, false));

        Label player1Label = new Label(shell, SWT.NONE);
        player1Label.setText("Spieler Blau:");
        Text player1Text = new Text(shell, SWT.BORDER);
        player1Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Label player2Label = new Label(shell, SWT.NONE);
        player2Label.setText("Spieler Rot:");
        Text player2Text = new Text(shell, SWT.BORDER);
        player2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Button startButton = new Button(shell, SWT.PUSH);
        startButton.setText("Start");
        GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gridData.horizontalSpan = 2;
        startButton.setLayoutData(gridData);

        Label resultLabel = new Label(shell, SWT.NONE);
        resultLabel.setAlignment(SWT.CENTER);
        resultLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        startButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String player1Name = player1Text.getText().trim();
                String player2Name = player2Text.getText().trim();

                if (player1Name.isEmpty() || player2Name.isEmpty()) {
                    resultLabel.setText("Beide Spieler benötigen einen Namen.");
                } else {
                    shell.dispose();
                    
                    
                }
            }
        });

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}