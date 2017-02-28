package ConnectFour.model;

import ConnectFour.controller.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class NetworkedGame extends Observable
{
    private State state = State.NO_CONNECTION;
    private boolean isHost;
    private int port;
    private String ip;
    private boolean isWon;
    private ClientServer clientServer;
    private Controller controller;

    public NetworkedGame(Controller controller, boolean isHost, int port, String ip)
    {
        isWon = false;
        this.controller = controller;
        addObserver(controller);
        setUp(isHost, port, ip);
    }

    public void setUp(boolean isHost, int port, String ip)
    {
        isWon = false;
        this.isHost = isHost;
        this.port = port;
        this.ip = ip;
        this.state = State.NO_CONNECTION;
    }

    public void startConnection() throws IOException
    {
        if (isHost)
        {
            ServerSocket socket = new ServerSocket();
            clientServer = new GameServer(socket);
        } else
        {
            Socket socket = new Socket();
            clientServer = new GameClient(socket);
        }
        clientServer.start();
    }

    public synchronized void sendLocalMove(int column)
    {
        clientServer.sendLocalMove(column);
    }

    public synchronized void quitGame()
    {
        clientServer.quitGame();
    }

    public synchronized void quitWonGame() { clientServer.quitWonGame(); }

    public synchronized State getGameState()
    {
        return state;
    }

    private synchronized void setGameState(State state)
    {
        this.state = state;
        setChanged();
        notifyObservers();
    }

    private synchronized void displayError(String error)
    {
        controller.displayError(error);
    }

    public enum State
    {
        NO_CONNECTION, LOCAL_TURN, REMOTE_TURN
    }

    public class ClientServer extends Thread implements NetworkedGameConstants
    {
        Socket socket;
        PrintWriter out;

        synchronized void sendLocalMove(int column) throws IllegalStateException
        {
            if (getGameState() != NetworkedGame.State.LOCAL_TURN)
            {
                displayError("Can't make a move, it's not local player's turn.\nNetwork desync?");
                return;
            }
            out.println(String.format("%s%d", dropMsg, column));
            setGameState(NetworkedGame.State.REMOTE_TURN);
        }

        public synchronized void quitGame()
        {
            if (getGameState() != NetworkedGame.State.NO_CONNECTION)
            {
                out.println(quitMsg);
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close socket:\n" + e.getMessage());
                } finally
                {
                    setGameState(NetworkedGame.State.NO_CONNECTION);
                }
            }
        }

        public synchronized void quitWonGame()
        {
            isWon = true;
            if (getGameState() != NetworkedGame.State.NO_CONNECTION)
            {
                out.println(winMsg);
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close socket:\n" + e.getMessage());
                } finally
                {
                    setGameState(NetworkedGame.State.NO_CONNECTION);
                }
            }
        }

        synchronized boolean parseMsg(String msg)
        {
            if (msg.length() == 5 && msg.substring(0, 4).equalsIgnoreCase(dropMsg))
            {
                if (getGameState() != NetworkedGame.State.REMOTE_TURN)
                {
                    displayError("Remote player tried to make a move during local player's turn.\nNetwork desync?");
                    return false;
                }
                int column = Integer.parseInt(msg.substring(4, 5));
                controller.dropPiece(column);
                setGameState(NetworkedGame.State.LOCAL_TURN);
                return true;

            } else if (msg.length() == 4 && msg.equalsIgnoreCase(quitMsg))
            {
                displayError("Remote player closed their game");
                return false;
            } else if (msg.length() == 3 && msg.equalsIgnoreCase(winMsg))
            {
                return false;
            }
            else
            {
                displayError("Remote player sent strange data:\n" + msg);
                return false;
            }
        }

        void parseInputInLoop(BufferedReader in) throws IOException
        {
            while (true)
            {
                String input = in.readLine();
                if (input == null) break;
                if (!parseMsg(input)) break;
            }
        }

    } // ClientServer

    public class GameServer extends ClientServer
    {
        private ServerSocket serverSocket;

        GameServer(ServerSocket s)
        {
            serverSocket = s;
        }

        public synchronized void quitGame()
        {
            if (getGameState() != NetworkedGame.State.NO_CONNECTION)
            {
                out.println(quitMsg);
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close socket:\n" + e.getMessage());
                } finally
                {
                    setGameState(NetworkedGame.State.NO_CONNECTION);
                }
            } else if (serverSocket.isBound())
            {
                try
                {
                    serverSocket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close server socket:\n" + e.getMessage());
                }
            }
        }

        public synchronized void quitWonGame()
        {
            isWon = true;
            if (getGameState() != NetworkedGame.State.NO_CONNECTION)
            {
                out.println(winMsg);
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close socket:\n" + e.getMessage());
                } finally
                {
                    setGameState(NetworkedGame.State.NO_CONNECTION);
                }
            } else if (serverSocket.isBound())
            {
                try
                {
                    serverSocket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close server socket:\n" + e.getMessage());
                }
            }
        }


        public void run()
        {
            try
            {
                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                serverSocket.bind(new InetSocketAddress("localhost", port));
                socket = serverSocket.accept();
                System.out.println(socket.isBound());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                setGameState(NetworkedGame.State.LOCAL_TURN);

                parseInputInLoop(in);

            } catch (IOException e)
            {
                if(isWon) displayError("Game finished - connection closed");
                else displayError("Connection ended:\n" + e.getMessage());
            } finally
            {
                setGameState(NetworkedGame.State.NO_CONNECTION);
                try
                {
                    if (socket != null) socket.close();
                    if (serverSocket.isBound()) serverSocket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close socket:\n" + e.getMessage());
                }
            }
        }
    }

    public class GameClient extends ClientServer
    {
        GameClient(Socket s)
        {
            socket = s;
        }

        boolean waitForConnection(int timeout) throws IOException
        {
            try
            {
                socket.connect(new InetSocketAddress(ip, port), timeout);
                return true;
            } catch (SocketTimeoutException e)
            {
                displayError("Timed out waiting on connection");
                setGameState(NetworkedGame.State.NO_CONNECTION);
                return false;
            }
        }

        public void run()
        {
            try
            {
                if (!waitForConnection(1000 * 20)) return;

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                setGameState(NetworkedGame.State.REMOTE_TURN);

                parseInputInLoop(in);

            } catch (IOException e)
            {
                if(isWon) displayError("Game finished - connection closed");
                else displayError("Connection ended:\n" + e.getMessage());
            } finally
            {
                setGameState(NetworkedGame.State.NO_CONNECTION);
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    displayError("Couldn't close socket:\n" + e.getMessage());
                }
            }
        }
    }

}
