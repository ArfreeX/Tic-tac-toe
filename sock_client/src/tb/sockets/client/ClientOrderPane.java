package tb.sockets.client;

import javax.swing.JButton;

import javax.swing.JPanel;


import tb.sockets.server.TicTacToe;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClientOrderPane extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton[][] tictoe = new JButton[3][3];
	private static boolean check = true;
	private static boolean game = false;
	private Indexes indexation;

	public ClientOrderPane() {
		Color all_color = new Color(255, 255, 240);
		Font f_buttons = new Font("Arial", Font.BOLD, 80);
		setBackground(all_color);
		setLayout(null);
		{
			int y_reallocation = 0;
			for (int i = 0; i < 3; i++) {
				int x_reallocation = 0;
				for (int j = 0; j < 3; j++) {
					tictoe[i][j] = new JButton("");
					tictoe[i][j].setFont(f_buttons);
					tictoe[i][j].setBounds(100 + x_reallocation, 70 + y_reallocation, 100, 100);
					tictoe[i][j].setBackground(all_color);
					add(tictoe[i][j]);
					tictoe[i][j].addActionListener(this);
					x_reallocation += 100;
				}
				y_reallocation += 100;
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			ClientMainFrame.winnerCheck();
			indexation = checkIndex((JButton) e.getSource());
			if (game && !check && TicTacToe.checkEmpty(indexation.getI(), indexation.getJ())) {
				((JButton) e.getSource()).setText("X");
				((JButton) e.getSource()).setForeground(Color.green);
				indexation = checkIndex((JButton) e.getSource());
				check = true;
				ClientMainFrame.sendMove();
				ClientMainFrame.getMove();
			}
		}
	}

	public void enemyClick(int x, int y) {
		if (check) {
			tictoe[x][y].setText("O");
			tictoe[x][y].setForeground(Color.red);
			check = false;
			ClientMainFrame.winnerCheck();
		}
	}

	public Indexes checkIndex(JButton cell) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (cell == tictoe[i][j])
					return new Indexes(i, j);
		return null;
	}

	public Indexes getIndex() {
		return indexation;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public void startGame() {
		game = true;
	}
	
	public void endGame() {
		game = false;
	}
}
