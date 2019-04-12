package components;

import utility.PieceColor;

public class Tuple {
	
	private int black;
	private int white;
	private int score;
		// 1 -  2 |  3 /  4 \
//	private int centerX;
//	private int centerY;
	private boolean dummy;
	
	public Tuple(int x, int y, int type) {
		black = 0;
		white = 0;
		score = 7;
//		centerX = x;
//		centerY = y;
		if (type == 1) {
			if (x <= 1 || x >= 13) {
				dummy = true;
			} else {
				dummy = false;
			}
		} else if (type == 2) {
			if (y <= 1 || y >= 13) {
				dummy = true;
			} else {
				dummy = false;
			}
		} else if (type == 3 || type == 4) {
			if (x <= 1 || x >= 14 || y <= 1 || y >= 13) {
				dummy = true;
			} else {
				dummy = false;
			}
		}
	}
	
	public int getScore() {
		if (dummy) {
			return 0;
		}
		return score;
	}
	
	public void add(PieceColor color) {
		if (!dummy) {
			checkAddable();
			if (color == PieceColor.BLACK) {
				black++;
			} else {
				white++;
			}
			evaluate();
		}
	}
	
	private void evaluate() {
		if(black == 0 && white == 0){
			score = 7;
		} else if(black > 0 && white > 0){
			score = 0;
		} else if(white == 1){
			score = 35;
		} else if(white == 2){
			score = 800;
		} else if(white == 3){
			score = 15000;
		} else if(white == 4){
			score = 800000;
		} else if(white == 5) {
		} else if(black == 1){
			score = 15;
		} else if(black == 2){
			score = 400;
		} else if(black == 3){
			score = 1800;
		} else if(black == 4){
			score = 100000;
		} else if(black == 5) {
		} else {
			throw new IllegalStateException("unknown");
		}
	}
	
	public void checkAddable() {
		if (black + white >= 5) {
			throw new IllegalStateException("more than 5 pieces" + black + white);
		}
	}
}
