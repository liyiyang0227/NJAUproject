package Gobang;

import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

/**
 * Created by DELL on 2019/2/14.
 */
public class ChessBoard extends Pane{
    public char whichColor = ' ';
    public int count;
    private Alert winner = new Alert(Alert.AlertType.INFORMATION);
    private Piece piece = null;
    private Media winSong = new Media(getClass().getResource("WinSong.wav").toString());

    public ChessBoard(int x, int y) {
        setStyle("-fx-border-color:black");
        this.setPrefSize(50, 50);
        this.setOnMouseClicked(e -> {
            handleMouseClick(x, y);
        });
    }
    public void handleMouseClick(int x, int y) {

        // whoseTurn一开始为空 当点击开始游戏按钮时置为'B' 即黑棋开始下棋 进入下面的if分支
        // 若不点击开始游戏按钮，即whoseTurn为空 则无法进入下面的if分支 即无法开始游戏
        if (whichColor == ' ' && Main.whoseTurn != ' ' && Main.AIPlay == false) {
            // 此处为当人下了一子后 若AIPlay为true 电脑开始下棋 即人机对战 否则为人人对战
            setToken(Main.whoseTurn, x, y,true);
            Main.textArea.insertText(0, "估值:" + Main.robot.Evaluate(x, y, Main.whoseTurn) + " ");

            if (judge(Main.whoseTurn, x, y) == true)
                printWinner();
            else
                Main.whoseTurn = (Main.whoseTurn == 'B') ? 'W' : 'B'; // 改变落子者

        }

        //若AIPlay为True 则为人机对战
        if (whichColor == ' ' && Main.whoseTurn != ' ' && Main.AIPlay == true) {
            if (Main.whoseTurn != ' ') {
                setToken(Main.whoseTurn, x, y,true);
                Main.textArea.insertText(0, "估值:" + Main.robot.Evaluate(x, y, Main.whoseTurn) + " ");
                if (judge(Main.whoseTurn, x, y) == true)
                    printWinner();
                else
                    Main.whoseTurn = (Main.whoseTurn == 'B') ? 'W' : 'B'; // 改变落子者

            }
            //若whoseTurn不为空 即未分出胜负，则执行搜索函数，根据人下的子，搜索AI的下一步落子
            if (Main.whoseTurn != ' ')
                Main.robot.search(x,y);
        }
    }

    // setToken函数 若token为B则在棋盘上鼠标点击位置绘制黑子 为W则绘制白子
    public void setToken(char c, int x, int y,boolean flag) {

        whichColor = c;
        Piece circle = null;
        int row = x + 1, column = y + 1;// 行数 列数
        Main.step++;
        Main.board[x][y].count = Main.step;// 落一子步数+1 thisStep用于判断悔棋的操作
        if (whichColor == 'B') {
            circle = new Piece(20, Color.BLACK);
            // 在textArea上输出相应的落子位置 具体效果为——步数:1 黑棋: 行 3, 列 10
            if(flag==true)
                Main.textArea.insertText(0, "步数:" + Main.step + " 黑棋:行" + column + ",列" + row + '\n');
        } else if (whichColor == 'W') {
            circle = new Piece(20, Color.WHITE);
            if(flag==true)
                Main.textArea.insertText(0, "步数:" + Main.step + " 白棋:行" + column + ",列" + row + '\n');
        }
        // 将圆心绑定到落子的方格中间
        circle.centerXProperty().bind(Main.board[x][y].widthProperty().divide(2));
        circle.centerYProperty().bind(Main.board[x][y].heightProperty().divide(2));
        Main.board[x][y].getChildren().add(circle);
        Main.board[x][y].whichColor = c;
        Main.arrayCircle[x][y] = circle;
    }

    /*
     * -------胜 负 判 断函数----------
     */
    public boolean judge(char whoseTurn, int x, int y) {
        boolean flag = false;
        if (checkCount(whoseTurn, x, y, 1, 0) >= 5)
            flag = true;
        else if (checkCount(whoseTurn, x, y, 0, 1) >= 5)
            flag = true;
        else if (checkCount(whoseTurn, x, y, 1, -1) >= 5)
            flag = true;
        else if (checkCount(whoseTurn, x, y, 1, 1) >= 5)
            flag = true;
        return flag;
    }

    // 判断连子函数
    public int checkCount(char whoseTurn, int x, int y, int xChange, int yChange) {
        int count = 1;
        int tempX = xChange;
        int tempY = yChange;
        while (x + xChange >= 0 && x + xChange < Main.rows && y + yChange >= 0 && y + yChange < Main.rows
                && whoseTurn == Main.board[x + xChange][y + yChange].whichColor) {
            count++;
            if (xChange != 0)
                xChange++;
            if (yChange != 0) {
                if (yChange > 0)
                    yChange++;
                else
                    yChange--;
            }
        }

        xChange = tempX;
        yChange = tempY;

        while (x - xChange >= 0 && x - xChange < Main.rows && y - yChange >= 0 && y - yChange < Main.rows
                && whoseTurn == Main.board[x - xChange][y - yChange].whichColor) {
            count++;
            if (xChange != 0)
                xChange++;
            if (yChange != 0) {
                if (yChange > 0)
                    yChange++;
                else
                    yChange--;
            }
        }
        return count;
    }

    // 在文本域输出是谁获胜 若无人获胜则改变下棋方继续游戏
    public void printWinner() {

        //输出胜利音效
        MediaPlayer mediaPlayer = new MediaPlayer(winSong);
        mediaPlayer.setVolume(50);
        mediaPlayer.play();

        if (Main.whoseTurn == 'B') {
            Main.whoseTurn = ' ';
            Main.textArea.insertText(0, "黑棋获胜！点击开始游戏进行新对战！" + '\n' + '\n');
            winner.setContentText("黑棋获胜！！！");
            winner.setHeaderText(null);
            winner.showAndWait();
        } else if (Main.whoseTurn == 'W') {
            Main.whoseTurn = ' ';
            Main.textArea.insertText(0, "白棋获胜！点击开始游戏进行新对战！" + '\n' + '\n');
            winner.setContentText("白棋获胜！！！");
            winner.setHeaderText(null);
            winner.showAndWait();
        }


    }
}
