package tb.sockets.client;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import tb.sockets.server.TicTacToe;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField;
import java.awt.Color;

public class ClientMainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Class variables
	private static int counter = 9;
	private String ip, port;

	// JFrame components
	private JPanel contentPane;
	private JButton btnConnect, btnStart;
	private JLabel lblNotConnected, lblHost, lblPort;
	private JFormattedTextField ftf_ip, ftf_port;
	private static ClientOrderPane panel;

	// Socket service
	private static Socket sock = null;
	private static DataOutputStream os = null;
	private static DataInputStream in = null;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMainFrame frame = new ClientMainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientMainFrame() {
		super("Tic-Tac-Toe Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblHost = new JLabel("Host:");
		lblHost.setBounds(10, 14, 26, 14);
		contentPane.add(lblHost);

		try {
			ftf_ip = new JFormattedTextField(new MaskFormatter("###.###.###.###"));
			ftf_ip.setBounds(43, 11, 90, 20);
			ftf_ip.setText("xxx.xxx.xxx.xxx");
			contentPane.add(ftf_ip);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		btnConnect = new JButton("Connect");
		btnConnect.setBounds(10, 70, 90, 23);
		contentPane.add(btnConnect);
		btnConnect.addActionListener(this);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(10, 137, 90, 23);
		contentPane.add(btnStart);
		btnStart.addActionListener(this);

		ftf_port = new JFormattedTextField();
		ftf_port.setText("xxxx");
		ftf_port.setBounds(43, 39, 90, 20);
		contentPane.add(ftf_port);

		lblPort = new JLabel("Port:");
		lblPort.setBounds(10, 42, 26, 14);
		contentPane.add(lblPort);

		panel = new ClientOrderPane();
		panel.setBounds(145, 14, 487, 448);
		contentPane.add(panel);

		lblNotConnected = new JLabel(" Not Connected");
		lblNotConnected.setForeground(new Color(255, 255, 255));
		lblNotConnected.setBackground(new Color(128, 128, 128));
		lblNotConnected.setOpaque(true);
		lblNotConnected.setBounds(10, 104, 123, 23);
		contentPane.add(lblNotConnected);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect)
			try {
				ip = ftf_ip.getText();
				port = ftf_port.getText();
				sock = new Socket(ip, Integer.parseInt(port));
				os = new DataOutputStream(sock.getOutputStream());
				in = new DataInputStream(sock.getInputStream());
				lblNotConnected.setText("Connected");
				lblNotConnected.setBackground(Color.green);
			} catch (IOException e3) {
				lblNotConnected.setText("Not Connected");
				lblNotConnected.setBackground(Color.red);
			}
		else if (e.getSource() == btnStart) {
			panel.startGame();
			JOptionPane.showMessageDialog(null, "Wait for enemy move");
			getMove();
		}
	}

	public static void sendMove() {
		if (counter > 0) {
			TicTacToe.addToTable(panel.getIndex().getI(), panel.getIndex().getJ(), 11);
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
			TicTacToe.addToTable(i, j, 7);
			counter--;
		}
	}

	public static void winnerCheck() {
		if (TicTacToe.winCheck()) {
			counter = 0;
			panel.endGame();
			if (panel.getCheck())
				JOptionPane.showMessageDialog(null, "Wygrales!");
			else
				JOptionPane.showMessageDialog(null, "Przegrales :(");
		} else if (counter == 0) {
			panel.endGame();
			JOptionPane.showMessageDialog(null, "Remis!");
		}
	}
}
