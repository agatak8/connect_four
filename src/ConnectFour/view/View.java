package ConnectFour.view;

import ConnectFour.model.GameBoard;

public interface View
{
    void setNoConnection();

    void setLocalTurn();

    void setRemoteTurn();

    void updateField(GameBoard.Field field);

    void showWin(GameBoard.Field.State state);

    void showDraw();

    void clearFields();

    void displayMsg(String msg);
}
