package ConnectFour.view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

import ConnectFour.model.GameBoard;
import ConnectFour.controller.Controller;

public class SwingView implements View, SwingConstants
{
    private MainMenuFrame mainMenu;
    private JoinGameFrame joinGame;
    private HostGameFrame hostGame;
    private MainGameFrame mainGame;
    private Controller controller;

    public SwingView()
    {
        mainMenu = new MainMenuFrame();
        joinGame = new JoinGameFrame();
        hostGame = new HostGameFrame();
        mainGame = new MainGameFrame();
    }

    private void showMainMenu()
    {
        mainMenu.display();
    }

    private void showJoinGame()
    {
        joinGame.setLocationRelativeTo(mainMenu);
        joinGame.display();
    }

    private void showHostGame()
    {
        hostGame.setLocationRelativeTo(mainMenu);
        hostGame.display();
    }

    private void showMainGame()
    {
        mainGame.setLocationRelativeTo(mainMenu);
        mainGame.display();
    }

    public void show()
    {
        showMainMenu();
    }

    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    public void updateField(GameBoard.Field field)
    {
        mainGame.updateField(field);
    }

    public void showWin(GameBoard.Field.State player)
    {
        mainGame.showWin(player);
    }

    public void showDraw()
    {
        mainGame.showDraw();
    }

    public void displayMsg(String msg)
    {
        final String message = msg;
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run ()
            {
                JOptionPane.showMessageDialog(mainGame, message);
            }
        });

    }

    public void clearFields()
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run ()
            {
                for (int i = 0; i < 6; ++i)
                {
                    for (int j = 0; j < 7; ++j)
                    {
                        mainGame.pieceComponents[i][j].setIcon(mainGame.emptyPiece);
                    }
                }
            }
        });
    }

    public void setLocalTurn()
    {
        mainGame.setLocalTurn();
    }

    public void setRemoteTurn()
    {
        mainGame.setRemoteTurn();
    }

    public void setNoConnection()
    {
        mainGame.setNoConnection();
    }

    /* main menu */
    private class MainMenuFrame extends JFrame
    {
        JPanel mainPanel;

        MainMenuFrame()
        {
            super("Connect Four");
            /* init panels */
            mainPanel = new JPanel();
            JPanel menuButtonsPanel = new JPanel();
            /* init layouts */
            BorderLayout mainLayout = new BorderLayout();
            GridLayout menuButtonsLayout = new GridLayout(3, 1);
            /* init buttons */
            JButton joinGameButton = new JButton("Join Game");
            JButton hostGameButton = new JButton("Host Game");
            JButton exitButton = new JButton("Exit");
            /* init images */
            ImageIcon logo = new ImageIcon("assets/logo.png");
            ImageIcon board = new ImageIcon("assets/boardPhoto.png");


			/* set up buttons */
            joinGameButton.addActionListener(new JoinGameButtonListener());
            hostGameButton.addActionListener(new HostGameButtonListener());
            exitButton.addActionListener(new ExitButtonListener());
            /* set up layouts */
            mainLayout.setVgap(40);
            mainLayout.setHgap(40);
            menuButtonsLayout.setVgap(20);

			/* set up panels */
            mainPanel.setLayout(mainLayout);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.add(new JLabel(logo, CENTER), BorderLayout.PAGE_START);
            mainPanel.add(new JLabel(board), BorderLayout.LINE_START);


            menuButtonsPanel.setLayout(menuButtonsLayout);
            menuButtonsPanel.add(joinGameButton);
            menuButtonsPanel.add(hostGameButton);
            menuButtonsPanel.add(exitButton);
            menuButtonsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));


            mainPanel.add(menuButtonsPanel, BorderLayout.LINE_END);
            mainPanel.setMinimumSize(new Dimension(440, 420));
            mainPanel.setPreferredSize(new Dimension(480, 420));
            mainPanel.setFocusable(true);

			/* set up frame */
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            getContentPane().add(mainPanel);
            pack();
            setResizable(false);
            setLocationRelativeTo(null);
        }

        void display()
        {
            setVisible(true);
            mainPanel.requestFocus();
        }
    }

    /* join game */
    private class JoinGameFrame extends JFrame
    {
        JPanel mainPanel;
        JTextField ipField;
        JSpinner portSpinner;

        JoinGameFrame()
        {
            super("Join Game");
            /* init panels */
            mainPanel = new JPanel();
            JPanel ipPanel = new JPanel();
            JPanel portPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
            /* init layouts */
            GridLayout mainLayout = new GridLayout(3, 1);
            BoxLayout ipLayout = new BoxLayout(ipPanel, BoxLayout.X_AXIS);
            BoxLayout portLayout = new BoxLayout(portPanel, BoxLayout.X_AXIS);
            GridLayout buttonLayout = new GridLayout(1, 2);
            /* init labels */
            JLabel ipLabel = new JLabel("IP:");
            JLabel portLabel = new JLabel("Port:");
			/* init fields */
            ipField = new JTextField();
            portSpinner = new JSpinner(new SpinnerNumberModel(4444, 1024, 65535, 1));
			/* init buttons */
            JButton joinButton = new JButton("Join");
            JButton cancelButton = new JButton("Cancel");

			/* set up buttons */
            joinButton.addActionListener(new JoinButtonListener());
            cancelButton.addActionListener(new CancelButtonListener());
			/* set up fields */
            portSpinner.setEditor(new JSpinner.NumberEditor(portSpinner, "#"));
			/* set up layouts */
            mainLayout.setVgap(20);
            buttonLayout.setHgap(20);

			/* set up panels */
            mainPanel.setLayout(mainLayout);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            ipPanel.setLayout(ipLayout);
            ipPanel.add(ipLabel);
            ipPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            ipPanel.add(ipField);

            portPanel.setLayout(portLayout);
            portPanel.add(portLabel);
            portPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            portPanel.add(portSpinner);

            buttonPanel.setLayout(buttonLayout);
            buttonPanel.add(cancelButton);
            buttonPanel.add(joinButton);

            mainPanel.add(ipPanel);
            mainPanel.add(portPanel);
            mainPanel.add(buttonPanel);

            //mainPanel.setMinimumSize(new Dimension(440,420));
            //mainPanel.setPreferredSize(new Dimension(480,420));
            mainPanel.setFocusable(true);

			/* set up frame */
            getContentPane().add(mainPanel);
            pack();
            setResizable(false);
        }

        void display()
        {
            setVisible(true);
            mainPanel.requestFocus();
        }

        private class CancelButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        }

        /* just for now */
        private class JoinButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                mainGame.msgText.setText("<html>Connecting...</html>");
                clearFields();
                boolean success = controller.joinGame((int) portSpinner.getValue(), ipField.getText());
                if (success)
                {
                    setVisible(false);
                    hostGame.setVisible(false); // just in case
                    mainMenu.setVisible(false);

                    mainGame.localPiece = mainGame.P2Piece;
                    mainGame.remotePiece = mainGame.P1Piece;
                    mainGame.localPlayer.setIcon(mainGame.localPiece);
                    mainGame.currentPlayer.setIcon(mainGame.remotePiece);
                    showMainGame();
                } else
                {
                    JOptionPane.showMessageDialog(JoinGameFrame.this, "Could not connect to server");
                }
            }
        }
    }

    /* host game */
    private class HostGameFrame extends JFrame
    {
        JPanel mainPanel;
        JSpinner portSpinner;

        HostGameFrame()
        {
            super("Host Game");
      /* init panels */
            mainPanel = new JPanel();
            JPanel portPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
      /* init layouts */
            GridLayout mainLayout = new GridLayout(2, 1);
            BoxLayout portLayout = new BoxLayout(portPanel, BoxLayout.X_AXIS);
            GridLayout buttonLayout = new GridLayout(1, 2);
      /* init labels */
            JLabel portLabel = new JLabel("Port:");
      /* init fields */
            portSpinner = new JSpinner(new SpinnerNumberModel(4444, 1024, 65535, 1));
      /* init buttons */
            JButton hostButton = new JButton("Host");
            JButton cancelButton = new JButton("Cancel");

      /* set up buttons */
            hostButton.addActionListener(new HostButtonListener());
            cancelButton.addActionListener(new CancelButtonListener());
      /* set up fields */
            portSpinner.setEditor(new JSpinner.NumberEditor(portSpinner, "#"));
      /* set up layouts */
            mainLayout.setVgap(20);
            buttonLayout.setHgap(20);

      /* set up panels */
            mainPanel.setLayout(mainLayout);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            portPanel.setLayout(portLayout);
            portPanel.add(portLabel);
            portPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            portPanel.add(portSpinner);

            buttonPanel.setLayout(buttonLayout);
            buttonPanel.add(cancelButton);
            buttonPanel.add(hostButton);

            mainPanel.add(portPanel);
            mainPanel.add(buttonPanel);

            mainPanel.setFocusable(true);

      /* set up frame */
            getContentPane().add(mainPanel);
            pack();
            setResizable(false);
        }

        void display()
        {
            setVisible(true);
            mainPanel.requestFocus();
        }

        private class CancelButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        }

        /* just for now */
        private class HostButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                clearFields();
                mainGame.msgText.setText("<html>Waiting for connection...</html>");
                setVisible(false);
                mainMenu.setVisible(false);
                joinGame.setVisible(false); // just in case

                mainGame.localPiece = mainGame.P1Piece;
                mainGame.localPlayer.setIcon(mainGame.localPiece);
                mainGame.currentPlayer.setIcon(mainGame.localPiece);
                mainGame.remotePiece = mainGame.P2Piece;
                showMainGame();

                controller.hostGame((int) portSpinner.getValue());
            }
        }

    }

    private class MainGameFrame extends JFrame
    {
        JPanel mainPanel;
        JLabel[][] pieceComponents;
        JButton[] dropButtons;
        JLabel msgText;
        JLabel localPlayer;
        JLabel currentPlayer;
        ImageIcon P1Piece;
        ImageIcon P2Piece;
        ImageIcon emptyPiece;
        ImageIcon localPiece;
        ImageIcon remotePiece;
        private boolean gameIsWon;

        MainGameFrame()
        {
            super("Connect Four");
            gameIsWon = false;
            addWindowListener(new windowClosedListener());
            P2Piece = new ImageIcon("assets/piece2.png");
            P1Piece = new ImageIcon("assets/piece1.png");
            localPiece = P1Piece;
            remotePiece = P2Piece;
            emptyPiece = new ImageIcon("assets/piece-blank.png");
      /* init panels */
            mainPanel = new JPanel();
            JPanel gameMenuPanel = new JPanel();
            JPanel boardButtonPanel = new JPanel();
            JPanel boardPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
      /* init labels */
            localPlayer = new JLabel(localPiece);
            currentPlayer = new JLabel(P1Piece);
            msgText = new JLabel("Connecting...");
            JLabel currentTurn = new JLabel("Current turn:");
      /* init layouts */
            BorderLayout mainLayout = new BorderLayout();
            BoxLayout gameMenuLayout = new BoxLayout(gameMenuPanel, BoxLayout.Y_AXIS);
            BoxLayout boardButtonLayout = new BoxLayout(boardButtonPanel, BoxLayout.Y_AXIS);
            GridLayout boardLayout = new GridLayout(6, 7);
            GridLayout dropButtonLayout = new GridLayout(1, 7);
      /* set up layouts */
            mainLayout.setHgap(4);
            mainLayout.setVgap(4);
            boardLayout.setHgap(4);
            boardLayout.setVgap(4);
            dropButtonLayout.setHgap(4);
      /* set up panels */
            mainPanel.setLayout(mainLayout);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            mainPanel.setFocusable(true);
            gameMenuPanel.setLayout(gameMenuLayout);
            boardButtonPanel.setLayout(boardButtonLayout);
            boardPanel.setLayout(boardLayout);
            boardPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            boardPanel.setBackground(Color.blue);
            buttonPanel.setLayout(dropButtonLayout);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      /* init piece board */
            pieceComponents = new JLabel[6][7];
            for (int i = 0; i < 6; ++i)
            {
                for (int j = 0; j < 7; ++j)
                {
                    pieceComponents[i][j] = new JLabel(emptyPiece, CENTER);
                    boardPanel.add(pieceComponents[i][j]);
                }
            }
        /* init buttons */
            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(new QuitButtonListener());
            dropButtons = new JButton[7];
            for (int i = 0; i < 7; ++i)
            {
                dropButtons[i] = new JButton("Drop");
                dropButtons[i].setEnabled(false);
                dropButtons[i].addActionListener(new DropButtonListener(i));
                buttonPanel.add(dropButtons[i]);
            }
            gameMenuPanel.add(Box.createRigidArea(new Dimension(100, 0)));
            gameMenuPanel.add(msgText);
            gameMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameMenuPanel.add(new JLabel("You are:"));
            gameMenuPanel.add(localPlayer);
            gameMenuPanel.add(currentTurn);
            gameMenuPanel.add(currentPlayer);
            gameMenuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameMenuPanel.add(quitButton);

            boardButtonPanel.add(boardPanel);
            boardButtonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            boardButtonPanel.add(buttonPanel);
            mainPanel.add(boardButtonPanel, BorderLayout.WEST);
            mainPanel.add(gameMenuPanel, BorderLayout.EAST);
      /* set up frame */
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().add(mainPanel);
            pack();
            setResizable(false);
        }

        void display()
        {
            gameIsWon = false;
            setVisible(true);
            mainPanel.requestFocus();
        }

        void close()
        {
            //clearFields();
            controller.quitGame();
            mainGame.setVisible(false);
            showMainMenu();
        }

        void setLocalTurn()
        {
            if (gameIsWon) return;
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run ()
                {
                    msgText.setText("<html>Game in progress</html>");
                    for (int i = 0; i < 7; ++i)
                    {
                        dropButtons[i].setEnabled(true);
                    }
                    currentPlayer.setIcon(localPiece);
                }
            });

        }

        void setRemoteTurn()
        {
            if (gameIsWon) return;
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run ()
                {
                    msgText.setText("<html>Game in progress</html>");
                    for (int i = 0; i < 7; ++i)
                    {
                        dropButtons[i].setEnabled(false);
                    }
                    currentPlayer.setIcon(remotePiece);
                }
            });

        }

        void setNoConnection()
        {
            if (gameIsWon) return;
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run ()
                {
                    msgText.setText("<html>CONNECTION LOST</html>");
                    for (int i = 0; i < 7; ++i)
                    {
                        dropButtons[i].setEnabled(false);
                    }
                }
            });
        }

        void updateField(GameBoard.Field field) throws IllegalStateException
        {
            final GameBoard.Field.State s = field.getState();
            final ImageIcon icon;
            final int row = field.getRow();
            final int column = field.getColumn();
            if (s == GameBoard.Field.State.P1)
            {
                icon = localPiece;
            } else if (s == GameBoard.Field.State.P2)
            {
                icon = remotePiece;
            } else
            {
                icon = emptyPiece;
            }
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run ()
                {
                    pieceComponents[row][column].setIcon(icon);
                }
            });

        }

        void showWin(GameBoard.Field.State player)
        {
            gameIsWon = true;
            final String msg;

            if (player == GameBoard.Field.State.P1)
            {
                msg = "You won!";
            } else if (player == GameBoard.Field.State.P2)
            {
                msg = "You lost!";
            }
            else msg = "";
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run ()
                {
                    msgText.setText("<html>" + msg + "</html>");
                    for (int i = 0; i < 7; ++i)
                    {
                        dropButtons[i].setEnabled(false);
                    }
                }
            });
        }

        void showDraw()
        {
            gameIsWon = true;
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run ()
                {
                    msgText.setText("<html>It's a draw!</html>");
                    for (int i = 0; i < 7; ++i)
                    {
                        dropButtons[i].setEnabled(false);
                    }
                }
            });

        }

        public class windowClosedListener extends WindowAdapter
        {
            public void windowClosed(WindowEvent e)
            {
                close();
            }
        }

        private class DropButtonListener implements ActionListener
        {
            private int column;

            DropButtonListener(int column)
            {
                super();
                this.column = column;
            }

            public void actionPerformed(ActionEvent e)
            {
                if (controller.dropPiece(column))
                {
                    setRemoteTurn();
                }
            }
        }
    }

    private class QuitButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            mainGame.close();
        }
    }

    private class ExitButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    private class JoinGameButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            showJoinGame();
        }
    }

    private class HostGameButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            showHostGame();
        }
    }
}
	
