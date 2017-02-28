package ConnectFour.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardTest
{
    @Test
    public void newBoardIsNotSetup() throws Exception
    {
        GameBoard b = new GameBoard();
        assertFalse(b.isSetup());
    }

    @Test
    public void newBoardIsNotWon() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        assertFalse(b.isWon());
    }

    @Test
    public void newBoardEachColumnIsNotFull() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 7; ++i)
        {
            assertFalse(b.isFull(i));
        }
    }

    @Test
    public void newBoardCanDropToEachColumn() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 7; ++i)
        {
            GameBoard.Field f = b.dropPiece(i);
            assertNotNull(f);
        }
    }

    @Test
    public void newBoardCurrentPlayerIsP1OrP2() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        assertTrue(b.getCurrentPlayer() == GameBoard.Field.State.P1 || b.getCurrentPlayer() == GameBoard.Field.State.P2);
    }

    @Test
    public void afterDroppingPieceHasCorrectCoordinates() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        GameBoard.Field f = b.dropPiece(0);
        assertEquals(f.getColumn(), 0);
        assertEquals(f.getRow(), 5);
    }

    @Test
    public void afterDropping1stPieceStateIsCurrentPlayer() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        GameBoard.Field.State currP = b.getCurrentPlayer();
        GameBoard.Field f = b.dropPiece(0);
        assertEquals(f.getState(), currP);
    }

    @Test
    public void afterDroppingCurrentPlayerChanges() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        GameBoard.Field.State prevCurrP = b.getCurrentPlayer();
        b.dropPiece(0);
        assertNotEquals(prevCurrP, b.getCurrentPlayer());
    }

    @Test
    public void afterDropping2PiecesTheirStatesAreDifferent() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        GameBoard.Field f1 = b.dropPiece(0);
        GameBoard.Field f2 = b.dropPiece(1);
        assertNotEquals(f1.getState(), f2.getState());
    }

    @Test
    public void afterDropping6PiecesColumnIsFull() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 6; ++i)
        {
            b.dropPiece(0);
        }
        assertTrue(b.isFull(0));
    }

    @Test
    public void afterDropping6PiecesCantDropMore() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 6; ++i)
        {
            b.dropPiece(0);
        }
        try
        {
            b.dropPiece(0);
            fail();
        } catch (IllegalStateException e)
        {
        }
    }

    @Test
    public void afterDropping4IdenticalPiecesHorizontallyGameIsWon() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 3; ++i)
        {
            b.dropPiece(i); // P1
            b.dropPiece(i); // P2
        }
        b.dropPiece(3); // P1
        assertTrue(b.isWon());
    }

    @Test
    public void afterDropping4IdenticalPiecesVerticallyGameIsWon() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 3; ++i)
        {
            b.dropPiece(0); //  P1
            b.dropPiece(1); // P2
        }
        b.dropPiece(0); // P1
        assertTrue(b.isWon());
    }

    @Test
    public void afterDropping4IdenticalPiecesDiagonallyGameIsWon() throws Exception
    {
        GameBoard b = new GameBoard();
        b.setUp(GameBoard.Field.State.P1);
        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < i; ++j)
            {
                b.dropPiece(i);
            }
        }
        for (int i = 0; i < 3; ++i)
        {
            b.dropPiece(i);
            b.dropPiece(i);
        }
        b.dropPiece(3);
        assertTrue(b.isWon());
    }
}