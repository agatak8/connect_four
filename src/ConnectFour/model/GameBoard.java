package ConnectFour.model;

public class GameBoard
{
    /* 6 rows 7 columns */
    /* row 0 = top row */
	/* column 0 = leftmost column */
    private Field[][] board;
    private boolean won = false;
    private boolean draw = false;
    private boolean setup = false;
    private Field.State currentPlayer;
    public GameBoard()
    {
        board = new Field[6][7];
    }

    public boolean isWon()
    {
        return won;
    }

    public boolean isDraw()
    {
        return draw;
    }

    public boolean isSetup()
    {
        return setup;
    }

    private void initialize()
    {
        for (int i = 0; i <= 5; i++)
        {
            for (int j = 0; j <= 6; j++)
            {
                board[i][j] = new Field(i, j); // redundant? perhaps, however this allows to pass the Fields around
            }
        }
    }

    /* a win is when 4 or more subsequent identical pieces are on the board
     * horizontally, vertically or diagonally
     * this is only used by dropPiece */
    private boolean checkWin(int row, int column) throws IllegalArgumentException, IllegalStateException
    {
        if (row < 0 || row > 5 || column < 0 || column > 6)
            throw new IllegalArgumentException("Invalid board coordinates given");
        Field.State winCandidate = board[row][column].getState();
        if (winCandidate == Field.State.EMPTY) throw new IllegalStateException("WinCandidate was empty");
        int subsequentInDirection;

		/* vertical | (only downwards) */
        subsequentInDirection = 1; // includes winCandidate
        for (int i = row + 1; i <= 5; i++)
        {
            if (board[i][column].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }

		/* horizontal */
        subsequentInDirection = 1;
        // left
        for (int j = column - 1; j >= 0; j--)
        {
            if (board[row][j].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }
        // right
        for (int j = column + 1; j <= 6; j++)
        {
            if (board[row][j].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }

		/* diagonal / */
        subsequentInDirection = 1;
        // ascending to top-right
        for (int i = row - 1, j = column + 1; i >= 0 && j <= 6; i--, j++)
        {
            if (board[i][j].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }
        // descending to bottom-left
        for (int i = row + 1, j = column - 1; i <= 5 && j >= 0; i++, j--)
        {
            if (board[i][j].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }

		/* diagonal \ */
        subsequentInDirection = 1;
        // descending to bottom-right
        for (int i = row + 1, j = column + 1; i <= 5 && j <= 6; i++, j++)
        {
            if (board[i][j].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }
        // ascending to top-left
        for (int i = row - 1, j = column - 1; i >= 0 && j >= 0; i--, j--)
        {
            if (board[i][j].getState() == winCandidate) ++subsequentInDirection;
            else break;
            if (subsequentInDirection == 4) return true;
        }

        return false;
    }

    public boolean isFull(int column) throws IllegalArgumentException
    {
        if (column < 0 || column > 6) throw new IllegalArgumentException("Invalid column given");
        return (!(board[0][column].getState() == Field.State.EMPTY)); // true if top space in given column is not empty
    }

    /* pieces fall into columns and stack on top of each other */
	/* eg gravity works on them
	 *
	 * checks if win condition is met after dropping and sets won accordingly
	 *
	 * returns row the piece fell into */
    public Field dropPiece(int column) throws IllegalArgumentException, IllegalStateException
    {
        if (column < 0 || column > 6) throw new IllegalArgumentException("Invalid column given");
        if (isWon()) throw new IllegalStateException("Cannot continue playing a won game");
        if (isFull(column)) throw new IllegalStateException("Cannot drop to a full column");

        Field.State piece = currentPlayer;
        if (piece == Field.State.EMPTY) throw new IllegalStateException("Current player was not set");
        for (int i = 5; i >= 0; i--)
        {
            if (board[i][column].getState() == Field.State.EMPTY)
            {
                board[i][column].setState(piece);
                currentPlayer = (currentPlayer == Field.State.P1) ? Field.State.P2 : Field.State.P1;
                won = checkWin(i, column);
                // check if all columns are full
                boolean draw_tmp = true;
                for (int j = 0; j < 7; ++j)
                {
                    if (!isFull(j))
                    {
                        draw_tmp = false;
                    }
                }
                draw = draw_tmp;
                return board[i][column];
            }
        }
        throw new IllegalStateException();
    }

    Field.State getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setUp(Field.State firstPlayer) // set game
    {
        currentPlayer = firstPlayer;
        won = false;
        initialize();
        setup = true;
    }

    public static class Field
    {
        private final int row;
        private final int column;
        private State state;
        Field(int row, int column)
        {
            this.row = row;
            this.column = column;
            this.state = State.EMPTY;
        }

        public State getState()
        {
            return state;
        }

        void setState(State state)
        {
            this.state = state;
        }

        public int getRow()
        {
            return row;
        }

        public int getColumn()
        {
            return column;
        }

        public enum State
        {
            EMPTY, P1, P2 // P1 = Player 1's piece, P2 = Player 2's piece
        }
    }

}
