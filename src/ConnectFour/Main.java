package ConnectFour;
import ConnectFour.controller.Controller;
import ConnectFour.model.GameBoard;
import ConnectFour.view.SwingView;

public class Main
{
	private static GameBoard createModel()
	{
		return new GameBoard();
	}
	
	private static Controller createController(GameBoard board)
	{
		return new Controller(board);
	}
	
	private static SwingView createModelViewController()
	{
		GameBoard board = createModel();
		Controller controller = createController(board);
		SwingView view = new SwingView();
		view.setController(controller);
		controller.setView(view);
		return view;
	}
	
	private static void createAndShowGUI()
	{
		SwingView view = createModelViewController();
		view.show();

	}
	
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run ()
			{
				createAndShowGUI();
			}
		});
	}
}
