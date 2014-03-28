package connectfour3;

import java.util.Scanner;

import program.GameOutput;
import program.GridGame;
import program.GridGameGUI;
import program.GridProvider;

public class ConnectFourProgram implements GridGame {
	
	ConnectFour connectFour;
	GameOutput output;

	public void init() {
		connectFour = new ConnectFour();
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while (! connectFour.isFinished()) {
			System.out.println(connectFour);
			System.out.println("Player " + connectFour.getPlayer() + ", enter column of next drop:");
			int c = scanner.nextInt();
			// -1 to undo
			if (c == -1) {
				connectFour.undo();
			// -1 to redo
			} else if (c == -2) {
				connectFour.redo();
			} else {
				connectFour.drop(c);
			}
		}
		System.out.println(connectFour);
		connectFour.changePlayer();
		System.out.println("Congratulations player " + connectFour.getPlayer() + "! You have won the game.");
	}
	

	public static void main(String[] args) throws Exception {
		GridGameGUI.main(new String[]{ConnectFourProgram.class.getName()});
	}

	@Override
	public void init(String level) {
		connectFour = new ConnectFour();
	}

	@Override
	public void run(GameOutput output) {
		this.output = output;
		output.info("Player " + connectFour.getPlayer() + ", please make your move: ");
	}

	@Override
	public Integer doCommand(String command) {
		int c = Integer.parseInt(command);
		if (c == -1) {
			connectFour.undo();
		} else if (c == -2) {
			connectFour.redo();
		} else {
			connectFour.drop(c);
		}
		if (connectFour.hasWon()) {
			output.info("Player " + connectFour.getPlayer() + ", has won the game!");
			return 1;	
		} 
		if (connectFour.isFinished()) {
			output.info("The game ended in a draw.");
			return 1;	
		}
		output.info("Player " + connectFour.getPlayer() + ", please make your next move: ");
		return null;
	}

	@Override
	public GridProvider getGridProvider() {
		return connectFour;
	}

	@Override
	public String getTextFor(Object o) {
		return " " + o.toString() + " ";
	}

	@Override
	public String getImageFor(Object o) {
//		return null;
		return ((Piece) o).getImageString();
	}

	@Override
	public Integer gridElementInput(int x, int y) {
		return doCommand(x + "");
	}

	@Override
	public Integer directionInput(int dx, int dy) {
		// Just to illustrate how key strokes work - just add to column 1 no matter which key is pressed
		return doCommand(1+"");
	}
}