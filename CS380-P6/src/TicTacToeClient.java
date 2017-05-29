import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author cesar
 *
 */
public class TicTacToeClient {

	private static Scanner kb = new Scanner(System.in);
	private static ObjectOutputStream os;

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("incomplete-switch")
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub

		try(Socket socket = new Socket("codebank.xyz", 38006)){
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			os = new ObjectOutputStream(socket.getOutputStream());
			
			os.writeObject(new ConnectMessage(username()));
			
			os.writeObject(new CommandMessage(CommandMessage.Command.NEW_GAME));
			System.out.println("\n*New Game!*");
			
			Message received;
			
		label: 	while(true){
				received = (Message) is.readObject();
				
				switch(received.getType()){
				case BOARD:
					BoardMessage bm = (BoardMessage)received;
					
					if(bm.getStatus()!=BoardMessage.Status.IN_PROGRESS)
						break label;
					
					playGame(bm);
					break;
				
				case ERROR:
					System.out.println(((ErrorMessage)received).getError());
					System.exit(0);
					break;
	
				}
				
			}
			
			BoardMessage bm = (BoardMessage)received;
			printBoard(bm.getBoard());
			System.out.println(bm.getStatus());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void playGame(BoardMessage received) throws IOException {
		// TODO Auto-generated method stub
		
		printBoard(received.getBoard());
		
		int row = 0;
		int col = 0;
		
		while(!(row>0 && row<4)){
			System.out.print("Please select a row[1-3]: ");
			row = kb.nextInt();
			//System.out.println();
		}
		while(!(col>0 && col<4)){
			System.out.print("Please select a column[1-3]: ");
			col = kb.nextInt();
			System.out.println();
		}
		
		os.writeObject(new MoveMessage((byte)(row-1), (byte)(col-1)));
	
	}

	private static void printBoard(byte[][] board) {
		// TODO Auto-generated method stub
		char c=' ';
		for(int i = 0; i<board.length; i++){
			System.out.println("---+---+---");
			for(int j = 0; j < board[0].length; j++){
				switch(board[i][j]){
				case 0:
					c = ' ';
					break;
				case 1:
					c = 'X';
					break;
				case 2:
					c = 'O';
					break;
				default:
					break;
				
				}
				if(j!=2)
					System.out.print(" "+c+" |");
				else
					System.out.print(" "+c+" ");
			}
			System.out.println();
		}System.out.println("---+---+---");
		
	}

	private static String username() {
		// TODO Auto-generated method stub
		System.out.print("Please Enter a Username: ");
		return kb.nextLine();
	}

}
