package connectfour3;

public class Piece {
	
	// Please note that this class is strictly not necessary for this game as pieces
	// simply could've been represented by a char. It has been added, as mentioned 
	// in the lecture, for illustrative purposes relevant for the latest exercise.
	
	private char value;
	
	public Piece(char value) {
		this.value = value;
	}
	
	public void setValue(char value) {
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public String toString() {
		return "" + value;
	}
	
	public String getImageString() {
		if (value == ' ') {
			return "empty16x16.png";
		} else if (value == 'x') {
			return "box16x16.png";
		} else if (value == 'o') {
			return "box_on_target16x16.png";
		} else {
			return null;
		}
	}
}
