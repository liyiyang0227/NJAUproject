package Gobang;

import java.util.Random;

/**
 * Created by DELL on 2019/2/14.
 */
public class Robot {
    int[][] temp = new int[Main.rows][Main.rows];
    int AIx = 0, AIy = 0;
    boolean level = true;// level为true 则该层是max层 若level为false则该层是min层

    public Robot() {
    }

    public int getEvaluate(int x, int y, char whoseTurn) {
        int score = 0; // 落子权值
        score = evaluate(x, y, whoseTurn) + evaluate(x, y, (char) (153 - whoseTurn));
        return score;
    }

    public int Evaluate(int x, int y, char whoseTurn) {
        // 第一个getValue为当前落子的人的估值 第二个getValue是若对手在此落子的估值 （可理解为防守估值）
        // 153为unicode编码 153-'B'='W' 153-'W'='B'
        return evaluate(x, y, whoseTurn) + evaluate(x, y, (char) (153 - whoseTurn));
    }

    //估值函数 棋型判断
    private int evaluate(int x, int y, char whoseTurn) {
        int value = 0;
        for (int i = 1; i <= 8; i++) {
            // 8个方向

            // 活四 01111* *代表当前空位置 0代表其他空位置 下同
            if (getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn
                    && getLine(x, y, i, -4) == whoseTurn
                    && getLine(x, y, i, -5) == ' '){
                value += 300000;
                if((Main.AIToken==false&&whoseTurn=='W')||(Main.AIToken==true&&whoseTurn=='B'))
                    value+=100000;
                //避免出现当电脑和人都出现四连子的时候电脑去拦截而不是直接获胜的选择
            }


            // 死四A 21111*
            if (getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn
                    && getLine(x, y, i, -4) == whoseTurn
                    && (getLine(x, y, i, -5) == (char) (153 - whoseTurn) || getLine(x, y, i, -5) == 'E')){
                value += 250000;
                if((Main.AIToken==false&&whoseTurn=='W')||(Main.AIToken==true&&whoseTurn=='B'))
                    value+=100000;
            }

            // 死四B 111*1
            if (getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn
                    && getLine(x, y, i, 1) == whoseTurn) {
                value += 240000;
                if((Main.AIToken==false&&whoseTurn=='W')||(Main.AIToken==true&&whoseTurn=='B'))
                    value+=100000;
            }

            // 死四C 11*11
            if (getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, 1) == whoseTurn
                    && getLine(x, y, i, 2) == whoseTurn) {
                value += 115000; //这里因为顺序关系会计算两遍value 实际value是115000*2=230000
                if((Main.AIToken==false&&whoseTurn=='W')||(Main.AIToken==true&&whoseTurn=='B'))
                    value+=50000;
            }

            // 活三 近3位置 0111*0
            if (getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn) {
                if (getLine(x, y, i, 1) == ' '&&getLine(x, y, i, -4) == ' ')
                    //若两边都有空位
                    value += 30000;
                else if ((getLine(x, y, i, 1) == (char) (153 - whoseTurn) || getLine(x, y, i, 1) == 'E')
                        &&(getLine(x, y, i, -4) == (char) (153 - whoseTurn) || getLine(x, y, i, -4) == 'E'))
                    value -= 100;
                else
                    value+=3000;
            }
            // 活三 远3位置 1110*
            if (getLine(x, y, i, -1) == ' '
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn
                    && getLine(x, y, i, -4) == whoseTurn){
                if(getLine(x, y, i, 1)==' '&&getLine(x, y, i, -5)==' '){
                    if(Main.whoseTurn==whoseTurn)//可以理解为进攻方
                        value+=28000;
                    else//否则防守方的估值不高
                        value+=2850;
                }
                else{
                    if(Main.whoseTurn==whoseTurn)
                        value+=2800;
                    else
                        value+=1400;
                }
            }



            // 死三 11*1
            if (getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, 1) == whoseTurn) {
                if (getLine(x, y, i, -3) == ' ' && getLine(x, y, i, 2) == ' ')
                    value += 29000;
                else if ((getLine(x, y, i, -3) == (char) (153 - whoseTurn) || getLine(x, y, i, -3) == 'E')
                        && (getLine(x, y, i, 2) == (char) (153 - whoseTurn) || getLine(x, y, i, 2) == 'E'))
                    value -= 100;
                else
                    value+=2900;
            }
            //10*11
            if (getLine(x, y, i, 1) == whoseTurn
                    && getLine(x, y, i, 2) == whoseTurn
                    && getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -1) == ' ') {
                if (getLine(x, y, i, 3) == ' '){
                    if(Main.whoseTurn==whoseTurn)
                        value += 27000;
                    else
                        value+=2750;
                }
                else if (getLine(x, y, i, 3) == (char) (153 - whoseTurn) || getLine(x, y, i, 3) == 'E'){
                    if(Main.whoseTurn==whoseTurn)
                        value+=2700;
                    else
                        value+=1350;
                }

            }

            // 101*1
            if (getLine(x, y, i, 1) == whoseTurn
                    && getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn
                    && getLine(x, y, i, -2) == ' ') {
                if (getLine(x, y, i, 2) == ' '){
                    if(Main.whoseTurn==whoseTurn)
                        value+=26000;
                    else
                        value+=2650;
                }
                else if (getLine(x, y, i, 2) == (char) (153 - whoseTurn) || getLine(x, y, i, 2) == 'E'){
                    if(Main.whoseTurn==whoseTurn)
                        value+=2600;
                    else
                        value+=1300;
                }
            }

            // 1*011
            if (getLine(x, y, i, 2) == whoseTurn
                    && getLine(x, y, i, 3) == whoseTurn
                    && getLine(x, y, i, -1) == whoseTurn
                    && getLine(x, y, i, 1) == ' ') {
                if (getLine(x, y, i, -2) == ' '
                        && getLine(x, y, i, 4) == ' '){
                    if(Main.whoseTurn==whoseTurn)
                        value+=25000;
                    else
                        value+=2550;
                }
                else{
                    if(Main.whoseTurn==whoseTurn)
                        value+=2500;
                    else
                        value+=1250;
                }
            }
            // 011*0
            if (getLine(x, y, i, -1) == whoseTurn && getLine(x, y, i, -2) == whoseTurn) {
                if (getLine(x, y, i, -3) == ' ' && getLine(x, y, i, 1) == ' ')
                    value += 6000;
                else if ((getLine(x, y, i, -3) == (char) (153 - whoseTurn) || getLine(x, y, i, -3) == 'E')
                        && (getLine(x, y, i, 1) == (char) (153 - whoseTurn) || getLine(x, y, i, 1) == 'E'))
                    value -= 100;//两边都被堵了 估值-100 落子没有意义
                else
                    value += 600;
            }
            // 01*10
            if (getLine(x, y, i, -1) == whoseTurn && getLine(x, y, i, 1) == whoseTurn) {
                if (getLine(x, y, i, -2) == ' ' && getLine(x, y, i, 2) == ' ')
                    value += 5000;
                else if ((getLine(x, y, i, -2) == (char) (153 - whoseTurn) || getLine(x, y, i, -2) == 'E')
                        && (getLine(x, y, i, 2) == (char) (153 - whoseTurn) || getLine(x, y, i, 2) == 'E'))
                    value -= 100;
                else
                    value += 500;
            }

            // 110*
            if (getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, -3) == whoseTurn
                    && getLine(x, y, i, -1) == ' ') {
                if (getLine(x, y, i, 1) == ' ' && getLine(x, y, i, -4) == ' ')
                    value += 4000;
                else if ((getLine(x, y, i, -4) == (char) (153 - whoseTurn) || getLine(x, y, i, -4) == 'E')
                        && (getLine(x, y, i, 1) == (char) (153 - whoseTurn) || getLine(x, y, i, 1) == 'E'))
                    value -= 100;
                else
                    value += 400;
            }

            //10*1
            if (getLine(x, y, i, -2) == whoseTurn
                    && getLine(x, y, i, 1) == whoseTurn
                    && getLine(x, y, i, -1) == ' ') {
                if (getLine(x, y, i, 2) == ' ' && getLine(x, y, i, -3) == ' ')
                    value += 3500;
                else if ((getLine(x, y, i, -3) == (char) (153 - whoseTurn) || getLine(x, y, i, -3) == 'E')
                        && (getLine(x, y, i, 2) == (char) (153 - whoseTurn) || getLine(x, y, i, 2) == 'E'))
                    value -= 100;
                else
                    value += 350;
            }

            // 1*
            if (getLine(x, y, i, -1) == whoseTurn) {
                if (getLine(x, y, i, 1) == ' ' && getLine(x, y, i, -2) == ' ')
                    value += 200;
                else if ((getLine(x, y, i, 1) == (char) (153 - whoseTurn) || getLine(x, y, i, 1) == 'E')
                        && (getLine(x, y, i, -2) == (char) (153 - whoseTurn) || getLine(x, y, i, -2) == 'E'))
                    value -= 100;
                else
                    value += 20;
            }

            // 10*
            if (getLine(x, y, i, -2) == whoseTurn && getLine(x, y, i, -1) == ' ') {
                if (getLine(x, y, i, 1) == ' ' && getLine(x, y, i, -3) == ' ')
                    value += 150;
                else if ((getLine(x, y, i, 1) == (char) (153 - whoseTurn) || getLine(x, y, i, 1) == 'E')
                        && (getLine(x, y, i, -3) == (char) (153 - whoseTurn) || getLine(x, y, i, -3) == 'E'))
                    value -= 100;
                else
                    value += 15;
            }

            // 100*
            if (getLine(x, y, i, -3) == whoseTurn && getLine(x, y, i, -2) == ' ' && getLine(x, y, i, -1) == ' ') {
                if (getLine(x, y, i, 1) == ' ' && getLine(x, y, i, -4) == ' ')
                    value += 100;
                else if ((getLine(x, y, i, 1) == (char) (153 - whoseTurn) || getLine(x, y, i, 1) == 'E')
                        && (getLine(x, y, i, -4) == (char) (153 - whoseTurn) || getLine(x, y, i, -4) == 'E'))
                    value -= 100;
                else
                    value += 10;
            }
        }
/*			// 活二的个数
			if (getLine(x, y, i, -1) == whoseTurn
					&& getLine(x, y, i, -2) == whoseTurn
					&& getLine(x, y, i, -3) != (char) (153 - whoseTurn)
					&& getLine(x, y, i, 1) != (char) (153 - whoseTurn)) {
				numoftwo++;
			}


			// 其余散棋
			int numOfplyer = 0; // 因为方向会算两次？
			for (int k = -4; k <= 0; k++) { // ++++* +++*+ ++*++ +*+++ *++++
				int temp = 0;
				for (int l = 0; l <= 4; l++) {
					if (getLine(x, y, i, k + l) == whoseTurn) {
						temp++;
					} else if (getLine(x, y, i, k + l) == (char) (153 - whoseTurn)|| getLine(x, y, i, k+1) == 'E') {
						temp = 0;
						break;
					}
				}
				numOfplyer += temp;
			}
			value += numOfplyer * 15;
			if (numOfplyer != 0) {
			}

		if(numoftwo==1)
			value+=100;
		if (numoftwo >= 2)
			value += 3000;*/

        // 给棋盘的每个格子一个估值 越接近中心估值越高 越接近边界估值越低
        if ((x == 0 || x == 13) && y != 0 && y != 13)
            value += 1;
        else if (y == 0 || y == 13)
            value += 1;
        else if ((x == 1 || x == 12) && y != 1 && y != 12)
            value += 2;
        else if (y == 1 || y == 12)
            value += 2;
        else if ((x == 2 || x == 11) && y != 2 && y != 11)
            value += 3;
        else if (y == 2 || y == 11)
            value += 3;
        else if ((x == 3 || x == 10) && y != 3 && y != 10)
            value += 4;
        else if (y == 3 || y == 10)
            value += 4;
        else if ((x == 4 || x == 9) && y != 5 && y != 9)
            value += 5;
        else if (y == 4 || y == 9)
            value += 5;
        else if ((x == 5 || x == 8) && y != 6 && y != 8)
            value += 6;
        else if (y == 5 || y == 8)
            value += 6;
        else
            value += 7;

        return value;
    }

    private char getLine(int x, int y, int i, int j) { // i:方向 j:相对x,y的顺序值
        // x,y:当前点
        switch (i) {
            case 1:
                x = x + j;
                break;
            case 2:
                x = x + j;
                y = y + j;
                break;
            case 3:
                y = y + j;
                break;
            case 4:
                x = x - j;
                y = y + j;
                break;
            case 5:
                x = x - j;
                break;
            case 6:
                x = x - j;
                y = y - j;
                break;
            case 7:
                y = y - j;
                break;
            case 8:
                x = x + j;
                y = y - j;
        }
        if (x < 0 || y < 0 || x > Main.rows-1 || y > Main.rows-1) { // 越界处理
            return 'E';// Error 越界
        }
        return Main.board[x][y].whichColor;
    }

    // X Y为人下的子，电脑根据xy得到要下的子
    public void search(int x, int y) {
        if (Main.step == 1) {// 规定电脑落的第一颗子 跳过搜索 提高效率
            Random r = new Random();
            int a = r.nextInt(3)-1;
            int b = r.nextInt(3)-1;
            Main.board[1][ 1].setToken(Main.whoseTurn, x + a, y + b, true);

            Main.whoseTurn = (Main.whoseTurn == 'B') ? 'W' : 'B'; // 改变落子者
        } else {
            int val = 0;
            for(int i = 0;i<Main.rows;i++) {
                for (int j = 0; j < Main.rows; j++) {
                    temp[i][j] = 0;
                }
            }
            Alpha_Beta(0, -10000000, 10000000, x, y, (char)(153-Main.whoseTurn));
            for (int i = 0; i < Main.rows; i++) {
                for (int j = 0; j < Main.rows; j++) {
                    if ((val < temp[i][j]) && Main.board[i][j].whichColor == ' ') {
                        val = temp[i][j];
                        AIx = i;
                        AIy = j;
                    }
                }
            }
            Main.board[AIx][AIy].setToken(Main.whoseTurn, AIx, AIy, true);
            System.out.println("打印：");
            for(int i=0;i<Main.rows;i++){
                for(int j=0;j<Main.rows;j++){
                    System.out.print(temp[j][i]+" ");
                }
                System.out.println("\n");
            }
            Main.textArea.insertText(0, "估值:" + Main.robot.Evaluate(AIx, AIy, Main.whoseTurn) + " ");

            if (Main.board[AIx][AIy].judge(Main.whoseTurn, AIx, AIy) == true)
                Main.board[AIx][AIy].printWinner();
            else
                Main.whoseTurn = (Main.whoseTurn == 'B') ? 'W' : 'B';
        }
    }

    // negaMax模式的递归α-β剪枝树
    private int Alpha_Beta(int depth, int alpha, int beta, int x, int y, char whoseTurn) {

        if (depth == 3 || Main.board[x][y].judge(whoseTurn, x, y) == true) {
            temp[x][y] = Evaluate(x, y, whoseTurn);
            return Evaluate(x, y, whoseTurn);
        }
        for (int i = 0; i < Main.rows; i++) {
            for (int j = 0; j < Main.rows; j++) {
                if (Main.board[i][j].whichColor != ' ')
                    continue;
                Main.board[i][j].setToken(whoseTurn, i, j, false);
                whoseTurn = (whoseTurn == 'B') ? 'W' : 'B';
                int value = -Alpha_Beta(depth + 1, -beta, -alpha, i, j, whoseTurn);
                unMove(i, j);
                if (value > alpha) {
                    if (value >= beta) {
                        return beta;
                    }
                }

                alpha = value;
            }
        }
        return alpha;
    }

    public int[][] predictChessmans = new int[14][14];
    public int[] MCTS(int[][] board){
        int[] AIpos=new int[2];
        int[][] tempMap = new int[14][14];
        int maxReward = 0;
        for(int i=0; i<Main.rows; i++) {
            for (int j = 0; j < Main.rows; j++) {
                System.out.print("  " + -tempMap[i][j]);
                predictChessmans[i][j] = -tempMap[i][j];
                if (-tempMap[i][j] > maxReward) {
                    maxReward = -tempMap[i][j];
                    AIpos[0]=i;
                    AIpos[1]=j;
                }

            }
        }
        return AIpos;

   }

    private void unMove(int i, int j) {
        Main.board[i][j].getChildren().remove(Main.arrayCircle[i][j]);
        Main.board[i][j].whichColor = ' ';
        Main.step--;
    }

}
