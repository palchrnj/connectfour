package connectfour3;

import java.util.ArrayList;

import program.GridListener;
import program.GridProvider;

public class ConnectFour implements GridProvider {
	
	private ArrayList<ArrayList<Piece>> board;
	private char player;
	private String moves;
	private String undoneMoves;
	ArrayList<GridListener> listeners;
	
	public ConnectFour() {
		board = new ArrayList<ArrayList<Piece>>();
		for (int r = 0; r < 7; r++) {
			board.add(new ArrayList<Piece>());
			for (int c = 0; c < 7; c++) {
				board.get(r).add(new Piece(' '));
			}
		}
		player = 'o';
		moves = "";
		undoneMoves = "";
		listeners = new ArrayList<GridListener>();
	}
	
	public Piece getPiece(int r, int c) {
		return board.get(r).get(c);
	}
	
	public void setPiece(int r, int c, Piece piece) {
		board.get(r).set(c, piece);
		changePlayer();
		fireGridChanged(c, r);
	}
	
	public boolean drop(int c) {
		// Check whether a piece can be dropped in column c
		if (getPiece(0, c).getValue() != ' ' ) {
			return false;
		} else {
			// Find row the piece will drop to
			for (int r = 6; r >= 0; r--) {
				if (getPiece(r, c).getValue() == ' ') {
					setPiece(r, c, new Piece(player));
					moves = moves + c;
					undoneMoves = "";
					return true;
				}
			}
			throw new IllegalStateException();
		}
	}
	
	public void undo() {
		// Check if any moves to undo
		if (moves.length() > 0) {
			// Get column of last move
			int c = moves.charAt(moves.length()-1) - '0';
			// Remove piece
			for (int r = 0; r <= 6; r++) {
				if (getPiece(r,c).getValue() != ' ') {
					setPiece(r, c, new Piece(' '));
					break;
				}
			}
			// Add to the undone move to undoneMoves
			undoneMoves = undoneMoves + c;
			// Remove undone move from Moves
			moves = moves.substring(0, moves.length()-1);
			// Update next player to make a move
//			changePlayer();
		}
	}
	
	public void redo() {
		// Check if any undone moves to redo
		if (undoneMoves.length() > 0) {
			// Get column of last undone move
			int c = undoneMoves.charAt(undoneMoves.length()-1) - '0';
			// Temporarily save undoneMove so that it is not lost when drop
			// is invoked - drop(c) sets undoneMoves = "" 
			String tempUndoneMoves = undoneMoves;
			drop(c);
			undoneMoves = tempUndoneMoves;
			// Remove redone move from undone moves
			undoneMoves = undoneMoves.substring(0, undoneMoves.length()-1);
		}
	}
	
	public void changePlayer() {
		if (player == 'o') {
			player = 'x';
		} else {
			player = 'o';
		}
	}
	
	public char getPlayer() {
		return player;
	}
	
	public boolean isFinished() {
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 7; c++) {
				if (getPiece(r,c).getValue() == ' ') {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasWon() {
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 7; c++) {
				if (getPiece(r, c).getValue() != ' ' && hasWonFromPosition(r,c)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasWonFromPosition(int r, int c) {
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				if (dr != 0 || dc != 0) {
					if (hasWonFromPositionInDirection(r, c, dr, dc)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean hasWonFromPositionInDirection(int r, int c, int dr, int dc) {
		char value = getPiece(r, c).getValue();
		int counter = 0;
		while (0 <= r && r < 7 && 0 <= c && c < 7 && getPiece(r, c).getValue() == value) {
			r += dr;
			c += dc;
			counter++;
		}
		return counter >= 4;
	}
	
	public String toString() {
		String str = "";
		for (int r = 0; r < 7; r++) {
			str += "| ";
			for (int c = 0; c < 7; c++) {
				str += board.get(r).get(c).toString() + " ";
			}
			str += "|\n";
		}
		return str;
	}

	@Override
	public int getGridWidth() {
		return 7;
	}

	@Override
	public int getGridHeight() {
		return 7;
	}

	@Override
	public Object getGridElement(int x, int y) {
		return getPiece(y, x);
	}
	
	@Override
	public void addGridListener(GridListener gridListener) {
		listeners.add(gridListener);
	}

	@Override
	public void removeGridListener(GridListener gridListener) {
		listeners.remove(gridListener);
	}
	
	private void fireGridChanged(int x, int y) {
		for (GridListener listener : listeners) {
			listener.gridChanged(this, x, y, 1, 1);
		}
	}
}