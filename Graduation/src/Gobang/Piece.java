package Gobang;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by DELL on 2019/2/14.
 */
public class Piece extends Circle{

    Piece(int radius, Color color){
        //设置半径 颜色 以及阴影效果
        super.setRadius(radius);
        super.setStroke(color);
        super.setFill(color);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(3.0);
        super.setEffect(ds);
    }

}
