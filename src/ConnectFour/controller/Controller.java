package ConnectFour.controller;

import ConnectFour.model.GameBoard;
import ConnectFour.model.NetworkedGame;
import ConnectFour.view.View;

import java.io.IOException;
import java.util.Observer;
import java.util.Observable;

public class Controller implements Observer // for networkedGame
{
    private GameBoard gameBoard;
    private View view;
    private NetworkedGame networkedGame;

    public Controller(GameBoard gameBoard)
    {
        this.networkedGame = new NetworkedGame(this, false, 0, "");
        this.gameBoard = gameBoard;
    }

    public void displayError(String msg)
    {
        view.displayMsg(msg);
    }

    public void setView(View view)
    {
        this.view = view;
    }

    // react to networkedGame changing its state and inform view
    public void update(Observable obs, Object ob)
    {
        if (obs == networkedGame)
        {
            switch (networkedGame.getGameState())
            {
                case NO_CONNECTION:
                    view.setNoConnection();
                    break;
                case LOCAL_TURN:
                    view.setLocalTurn();
                    break;
                case REMOTE_TURN:
                    view.setRemoteTurn();
                    break;
                default:
                    break;
            }
        }
    }

    // simultaneously handles view who got an event that the local player dropped a piece
    // and networkedGame who received a msg that the remote player dropped a piece
    public boolean dropPiece(int column)
    {
        if (!gameBoard.isSetup())
        {
            view.displayMsg("Game was not initialized, can't play");
            return false;
        }
        if (gameBoard.isFull(column))
        {
            if (networkedGame.getGameState() == NetworkedGame.State.REMOTE_TURN)
            {
                view.displayMsg("Remote player made an illegal move, dropping connection");
                networkedGame.quitGame(); // remote player tried dropping to full column
            }
            return false;
        }
        GameBoard.Field field = gameBoard.dropPiece(column);
        view.updateField(field);

        // if local player dropped piece
        if (networkedGame.getGameState() == NetworkedGame.State.LOCAL_TURN)
        {
            networkedGame.sendLocalMove(column);
        }

        if (gameBoard.isWon())
        {
            view.showWin(field.getState());
            networkedGame.quitWonGame();
        } else if (gameBoard.isDraw())
        {
            view.showDraw();
            networkedGame.quitGame();
        }
        return true;
    }

    public boolean hostGame(int port)
    {
        networkedGame.setUp(true, port, "");
        try
        {
            networkedGame.startConnection();
            gameBoard.setUp(GameBoard.Field.State.P1);
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }

    public void quitGame()
    {
        networkedGame.quitGame();
    }

    public boolean joinGame(int port, String ip)
    {
        networkedGame.setUp(false, port, ip);
        try
        {
            networkedGame.startConnection();
            gameBoard.setUp(GameBoard.Field.State.P2);
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }
}
