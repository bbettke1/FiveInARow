package CS504;

import java.util.Scanner;

public class FiveInARow {
	
	private static final int BOARD_SIZE 		= 8;
	private static final int NUMBER_IN_A_ROW 	= 5;
	private static final int NUMBER_OF_PLAYERS	= 2;
	
	class Square {
		char color;
		int row;
		int column;
	}
	
	
	
	class Player {
		public void getMove(int playerNumber, Square square)
		{	
			String input;
			Scanner scan = new Scanner(System.in);
			System.out.println("Player " + (playerNumber+1) + " please input a row");
			input = scan.next();
			square.row = Integer.parseInt(input);
			
			System.out.println("Now input column");
			input = scan.next();
			square.column = Integer.parseInt(input);
			
			square.color = color[playerNumber];
		}		
	}
	
	
	
	class ComputerPlayer extends Player
	{
		@Override
		public void getMove(int player, Square square){
			System.out.println("Computer get move");
			if(!getWinner(square, color[player])){
				if(!getBlock(square, color[player])){
					if(!getAvailable(square, color[player])){
						getAny(square, color[player]);
					}
				} 
			}
			
		}
		
		private boolean getWinner(Square square, char color){
			int available =0, count=0;
			Square found = null;
			for(int i=0;i<96;i++){
				for(int j=0;j<NUMBER_IN_A_ROW;j++){
					if(solutions[i][j].color == ' '){
						available++;
						found = solutions[i][j];
					}
					if(solutions[i][j].color == color){
						count++;
					}
				}
				if (count == 4 && available == 1){
					square.color = color;
					square.row = found.row;
					square.column = found.column;
					
					return true;
				}
				available = 0;
				count = 0;
				found = null;
			}
			
			return false;
		}
		
		private boolean getBlock(Square square, char color){
			int available =0, count=0;
			Square found = null;
			for(int i=0;i<96;i++){
				for(int j=0;j<NUMBER_IN_A_ROW;j++){
					if(solutions[i][j].color == ' '){
						available++;
						found = solutions[i][j];
					}
					if(solutions[i][j].color != color && solutions[i][j].color != ' '){
						count++;
					}
				}
				if (count == 4 && available == 1){
					square.color = color;
					square.row = found.row;
					square.column = found.column;
					
					return true;
				}
				available = 0;
				count = 0;
				found = null;
			}
			
			return false;
		}
		
		private boolean getAvailable(Square square, char color){
			int available =0, count=0;
			Square found = null;
			for(int i=0;i<96;i++){
				for(int j=0;j<NUMBER_IN_A_ROW;j++){
					if(solutions[i][j].color == ' '){
						available++;
						found = solutions[i][j];
					}
					if(solutions[i][j].color == color){
						count++;
					}
				}
				if (count>0 && count + available == 5){
					square.color = color;
					square.row = found.row;
					square.column = found.column;
					
					return true;
				}
				available = 0;
				count = 0;
				found = null;
			}
			
			return false;
		}
		
		private boolean getAny(Square square, char color){
		
			Square found = null;
			for(int i=0;i<96;i++){
				for(int j=0;j<NUMBER_IN_A_ROW;j++){
					if(solutions[i][j].color == ' '){
						found = solutions[i][j];

						square.color = color;
						square.row = found.row;
						square.column = found.column;
					
						return true;
					}
			
				}
			}
			return false;
			
		}	
	}
	
	
	
	private char[] color = {'b','w'};
	private Player[] players; 
	
	private Square[][] solutions;
	
	private int freeSpaces = BOARD_SIZE * BOARD_SIZE;
	
	
	
	public static void main(String args[])
	{
		FiveInARow game = new FiveInARow();
		game.playGame();
	}
	
	
	
	public void playGame() {

		char[][]board = new char[BOARD_SIZE][BOARD_SIZE];
		initializeBoard(board);
		
		populateSolutions();
//		showSolutions();
		
		players = getPlayers();
		
		Square theSquare = new Square();
		
		int turn = 0;
		boolean isWinner = false;
		
		while(!isWinner && freeSpaces > 0) {
			
			do {
				getMove(turn%2, theSquare);
			} while (!isLegal(board, turn%2, theSquare));
			
			makeMove(board, theSquare);
			isWinner = hasWon(solutions, turn%2);
			display(board);
			turn++;
		}
		
		if (freeSpaces == 0){
			System.out.println("Tie game!");
		}
		
		if (isWinner){
			System.out.println("Player "+ (((turn-1)%2)+1) + " wins!");
		}
	}
	
	
	
	private void initializeBoard(char[][] board) {
		for(int i=0; i<board.length;i++) {
			for (int j=0; j<board[i].length; j++) {
				board[i][j] = ' ';
			}
		}
	}
	
	
	
	public void showSolutions(){
		for(int i=0; i<solutions.length; i++){
			System.out.printf("solution %d ", i);
			for(int j=0; j<NUMBER_IN_A_ROW; j++){
				System.out.printf("[%d,%d,%c] ", solutions[i][j].row, solutions[i][j].column, solutions[i][j].color);
			}
				System.out.println("");
		}
	}
	
	
	
	public void populateSolutions(){
		solutions = new Square[96][5];
			int currentSolution = 0;
			
			// Initialize Row solutions.
			for (int i=0; i<BOARD_SIZE; i++)
			{
				for (int j=0;j<=BOARD_SIZE-NUMBER_IN_A_ROW; j++)
				{

					for (int k=j; k<NUMBER_IN_A_ROW+j; k++)
					{				
						solutions[currentSolution][k % NUMBER_IN_A_ROW] = createSquare(i, k);
					}
					currentSolution++;
				}
			}
			
			// Initialize Columns
			for (int i=0; i<BOARD_SIZE; i++)
			{
				for (int j=0;j<=BOARD_SIZE-NUMBER_IN_A_ROW; j++)
				{
					
					for (int k=j; k<NUMBER_IN_A_ROW+j; k++)
					{
						solutions[currentSolution][ k % NUMBER_IN_A_ROW] = createSquare(k, i);
				
					}
					currentSolution++;
				}
			}
			
			// Initialize Diagonal solutions.
			// Two biggest diagonals
			// Top Left to bottom right
			for (int i=0; i<BOARD_SIZE-NUMBER_IN_A_ROW+1; i++)
			{			
				for(int j=i; j<NUMBER_IN_A_ROW+i; j++)
				{
					solutions[currentSolution][j % NUMBER_IN_A_ROW] = createSquare(j,j);
					
				}
				currentSolution++;
			}
			// Top right to bottom left.
			for (int i=0; i<BOARD_SIZE-NUMBER_IN_A_ROW+1; i++)
			{
				for(int j=i; j<NUMBER_IN_A_ROW+i; j++)
				{
					solutions[currentSolution][j % NUMBER_IN_A_ROW] = createSquare(j,(BOARD_SIZE-1)-j);
				}
				currentSolution++;
			}
			
			// Remaining Top left to bottom right diagonals.  Process each side of the big
			// diagonal at the same time.
			for(int i=1; i<BOARD_SIZE-NUMBER_IN_A_ROW+1; i++)
			{
				for (int j=i; j<BOARD_SIZE-NUMBER_IN_A_ROW+1; j++)
				{
					for (int k=j; k<NUMBER_IN_A_ROW+j; k++)					
					{						
						solutions[currentSolution][k % NUMBER_IN_A_ROW] 	= createSquare(k,k-i);
						solutions[currentSolution+1][k % NUMBER_IN_A_ROW] 	= createSquare(k-i, k);
					}
					currentSolution+=2;
				}
			}
			
			//{ [0,4], [1,3], [2,2], [3,1], [4,0] },
			solutions[currentSolution][0]=createSquare(0,4);
			solutions[currentSolution][1]=createSquare(1,3);
			solutions[currentSolution][2]=createSquare(2,2);
			solutions[currentSolution][3]=createSquare(3,1);
			solutions[currentSolution][4]=createSquare(4,0);
			currentSolution++;
			//{ [0,5], [1,4], [2,3], [3,2], [4,1] },
			solutions[currentSolution][0]=createSquare(0,5);
			solutions[currentSolution][1]=createSquare(1,4);
			solutions[currentSolution][2]=createSquare(2,3);
			solutions[currentSolution][3]=createSquare(3,2);
			solutions[currentSolution][4]=createSquare(4,1);
			currentSolution++;
			//{ [1,4], [2,3], [3,2], [4,1], [5,0] },
			solutions[currentSolution][0]=createSquare(1,4);
			solutions[currentSolution][1]=createSquare(2,3);
			solutions[currentSolution][2]=createSquare(3,2);
			solutions[currentSolution][3]=createSquare(4,1);
			solutions[currentSolution][4]=createSquare(5,0);
			currentSolution++;
			//{ [0,6], [1,5], [2,4], [3,3], [4,2] },
			solutions[currentSolution][0]=createSquare(0,6);
			solutions[currentSolution][1]=createSquare(1,5);
			solutions[currentSolution][2]=createSquare(2,4);
			solutions[currentSolution][3]=createSquare(3,3);
			solutions[currentSolution][4]=createSquare(4,2);
			currentSolution++;
			//{ [1,5], [2,4], [3,3], [4,2], [5,1] },
			solutions[currentSolution][0]=createSquare(0,4);
			solutions[currentSolution][1]=createSquare(1,3);
			solutions[currentSolution][2]=createSquare(2,2);
			solutions[currentSolution][3]=createSquare(3,1);
			solutions[currentSolution][4]=createSquare(4,0);
			currentSolution++;
			//{ [2,4], [3,3], [4,2], [5,1], [6,0] },
			solutions[currentSolution][0]=createSquare(2,4);
			solutions[currentSolution][1]=createSquare(3,3);
			solutions[currentSolution][2]=createSquare(4,2);
			solutions[currentSolution][3]=createSquare(5,1);
			solutions[currentSolution][4]=createSquare(6,0);
			currentSolution++;
			//{ [3,7], [4,6], [5,5], [6,4], [7,3] },
			solutions[currentSolution][0]=createSquare(3,7);
			solutions[currentSolution][1]=createSquare(4,6);
			solutions[currentSolution][2]=createSquare(5,5);
			solutions[currentSolution][3]=createSquare(6,4);
			solutions[currentSolution][4]=createSquare(7,3);
			currentSolution++;
			//{ [2,7], [3,6], [4,5], [5,4], [6,3] },
			solutions[currentSolution][0]=createSquare(2,7);
			solutions[currentSolution][1]=createSquare(3,6);
			solutions[currentSolution][2]=createSquare(4,5);
			solutions[currentSolution][3]=createSquare(5,4);
			solutions[currentSolution][4]=createSquare(6,3);
			currentSolution++;
			//{ [3,6], [4,5], [5,4], [6,3], [7,2] },
			solutions[currentSolution][0]=createSquare(3,6);
			solutions[currentSolution][1]=createSquare(4,5);
			solutions[currentSolution][2]=createSquare(5,4);
			solutions[currentSolution][3]=createSquare(6,3);
			solutions[currentSolution][4]=createSquare(7,2);
			currentSolution++;
			//{ [1,7], [2,6], [3,1], [4,4], [5,3] },
			solutions[currentSolution][0]=createSquare(1,7);
			solutions[currentSolution][1]=createSquare(2,6);
			solutions[currentSolution][2]=createSquare(3,1);
			solutions[currentSolution][3]=createSquare(4,4);
			solutions[currentSolution][4]=createSquare(5,3);
			currentSolution++;
			//{ [2,6], [3,1], [4,4], [5,3], [6,2] },
			solutions[currentSolution][0]=createSquare(2,6);
			solutions[currentSolution][1]=createSquare(3,1);
			solutions[currentSolution][2]=createSquare(4,4);
			solutions[currentSolution][3]=createSquare(5,3);
			solutions[currentSolution][4]=createSquare(6,2);
			currentSolution++;
			//{ [3,1], [4,4], [5,3], [6,2], [7,1] },
			solutions[currentSolution][0]=createSquare(3,1);
			solutions[currentSolution][1]=createSquare(4,4);
			solutions[currentSolution][2]=createSquare(5,3);
			solutions[currentSolution][3]=createSquare(6,2);
			solutions[currentSolution][4]=createSquare(7,1);
			currentSolution++;
	}
	
	
	
	private Player[] getPlayers()
	{
		Scanner scan = new Scanner(System.in);
		Player[] players = new Player[NUMBER_OF_PLAYERS];
		
		int numberOfPlayers = -1;
		
		do {
			
			System.out.println("How many players?  Enter 0 for two computer players, 1 for one computer player or 2 for two human players:");
			numberOfPlayers = scan.nextInt();
			
		} while (numberOfPlayers <0 || numberOfPlayers >	 NUMBER_OF_PLAYERS);
		
	    switch(numberOfPlayers)
	    {
	    	case 0:
	    		players[0] = new ComputerPlayer();
	    		players[1] = new ComputerPlayer();
	    		break;
	    	case 1:
	    		players[0] = new Player();
	    		players[1] = new ComputerPlayer();
	    		break;
	    	case 2:
	    		players[0] = new Player();
	    		players[1] = new Player();
		}
	    
		return players;
	}
	
	
	
	private Square createSquare(int row, int col){
		Square s = new Square();
		s.row = row;
		s.column = col;
		s.color = ' ';
		
		return s;
	}
	
	
	
	private boolean hasWon(Square[][]solutions, int player){
		for(int i=0; i<solutions.length; i++){
			int count = 0;
			for(int j=0; j<NUMBER_IN_A_ROW; j++){
				if(solutions[i][j].color==color[player]){
					count++;
				}		
			}
			if(count == NUMBER_IN_A_ROW){
				return true;
			}
		}
		return false;
	}
	
	
	
	private void getMove(int player, Square square){
		players[player].getMove(player, square);
	}
	
	
	
	private boolean isLegal(char[][]board,int player,Square square){
		if (board[square.row][square.column] == ' ')
			return true;
		else {
			System.out.println("Please input a valid move.");
			System.out.println(" ");
			return false;
		}
	}
	
	
	
	private void makeMove(char[][]board, Square square){
		board[square.row][square.column] = square.color;
		freeSpaces--;
		for(int i=0; i<solutions.length; i++){
			for(int j=0; j<NUMBER_IN_A_ROW; j++){
				if(square.row==solutions[i][j].row && square.column==solutions[i][j].column){
					solutions[i][j].color=square.color;
				}
			}
		}
	}
	
	
	
	private void display(char[][]board){
		//print f?
		System.out.println("display");
	
		System.out.println("*************************************************");
		for(int row=0;row<8;row++){
			displayRow(board[row]);	
			System.out.println("*************************************************");
		}			
	}
	
	
	
	private void displayRow(char[]row){
		System.out.println("*     *     *     *     *     *     *     *     *");
		for (int col=0;col<row.length;col++){
			System.out.printf("*  %c  ", row[col]);
		}
		System.out.println("*");
		System.out.println("*     *     *     *     *     *     *     *     *");
	}
	
	
}

