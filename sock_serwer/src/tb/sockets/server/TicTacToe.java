package tb.sockets.server;

public class TicTacToe {

	private static int[][] tactoe = new int[3][3];;

	public TicTacToe() {
	}

	// podmienianie wartosci 0 tablicy na 1 ( pierwszy gracz ) lub 4 ( drugi gracz).
	// Uzycie 4 wynika z prostszego sprawdzania czy zaszla wygrana

	public static void addToTable(int i, int j, int player) {
		tactoe[i][j] = player;
	}

	public static boolean winCheck() {
		boolean check = false;
		int sumC = 0; // sum of columns values
		int sumR = 0; // sum of rows values
		int sumDL = 0; // sum of upper left to lower right diagonal values
		int sumDR = 0; // sum of upper right to lower left diagonal values

		sumDR = tactoe[0][2] + tactoe[1][1] + tactoe[2][0];
		sumDL = tactoe[0][0] + tactoe[1][1] + tactoe[2][2];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sumC += tactoe[i][j];
				sumR += tactoe[j][i];
			}
			if (sumDL != 0 && sumDR != 0
					&& (sumDL % 21 == 0 || sumDR % 21 == 0 || sumDL % 33 == 0 || sumDR % 33 == 0)) {
				check = true;
			} else if ((sumR != 0 && sumC != 0)
					&& (sumR % 21 == 0 || sumR % 33 == 0 || sumC % 21 == 0 || sumC % 33 == 0)) {
				check = true;
			}
			sumR = 0;
			sumC = 0;
		}
		return check;
	}

	public static boolean checkEmpty(int i, int j) {
		return tactoe[i][j] == 0;
	}

}
