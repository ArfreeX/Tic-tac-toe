package tb.sockets.server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField;
import java.awt.Color;

public class ServerMainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// JFrame components
	private JPanel contentPane;
	private JLabel lblNotConnected;
	private JButton btnCreate;
	private JFormattedTextField ftf_port;
	private static ServerOrderPane panel;
	
	// Class variables
	private static int counter = 9;
	
	// Socket service
	private static ServerSocket sSock = null;
	private static Socket sock = null;
	private static DataInputStream in = null;
	private static DataOutputStream os = null;
	
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerMainFrame frame = new ServerMainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerMainFrame() {
		super("Tic-Tac-Toe Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnCreate = new JButton("Run");
		btnCreate.setBounds(10, 70, 90, 23);
		contentPane.add(btnCreate);
		btnCreate.addActionListener(this);

		ftf_port = new JFormattedTextField();
		ftf_port.setText("Insert Port");
		ftf_port.setBounds(10, 39, 90, 20);
		contentPane.add(ftf_port);

		panel = new ServerOrderPane();
		panel.setBounds(150, 0, 550, 460);
		contentPane.add(panel);

		lblNotConnected = new JLabel(" Enemy Not Connected ");
		lblNotConnected.setForeground(new Color(255, 255, 255));
		lblNotConnected.setBackground(new Color(128, 128, 128));
		lblNotConnected.setOpaque(true);
		lblNotConnected.setBounds(10, 227, 130, 23);
		contentPane.add(lblNotConnected);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCreate)
			try {
				sSock = new ServerSocket(Integer.parseInt(ftf_port.getText()));
				sock = sSock.accept();
				enemyConnected();
				in = new DataInputStream(sock.getInputStream());
				os = new DataOutputStream(sock.getOutputStream());
			} catch (IOException e3) {
				e3.printStackTrace();
			}
	}
	
	private void enemyConnected() {
		lblNotConnected.setText(" Enemy Connected ");
		lblNotConnected.setForeground(Color.black);
		lblNotConnected.setBackground(Color.green);
	}
	
	public static void sendMove() {

		if (counter > 0) {
			TicTacToe.addToTable(panel.getIndex().getI(), panel.getIndex().getJ(), 7);
			winnerCheck();
			counter--;
			try {
				os.writeInt(panel.getIndex().getI());
				os.writeInt(panel.getIndex().getJ());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void getMove() {
		//winnerCheck();
		if (counter > 0) {
			int i = 0;
			int j = 0;
			try {
				i = in.readInt();
				j = in.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			panel.enemyClick(i, j);
			TicTacToe.addToTable(i, j, 11);
			counter--;
		}
	}
	
	public static void winnerCheck() {
		if (TicTacToe.winCheck()) {
			counter = 0;
			panel.endGame();
			if (!panel.getCheck())
				JOptionPane.showMessageDialog(null, "Wygrales!");
			else
				JOptionPane.showMessageDialog(null, "Przegrales! :(");
		} else if (counter == 0) {
			panel.endGame();
			JOptionPane.showMessageDialog(null, "Remis!");
		}
	}
}
