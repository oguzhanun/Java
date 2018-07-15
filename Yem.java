package yilancik;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class Yem extends Rectangle{

    int randomBoundX,randomBoundY,rectangleX,rectangleY;
    Random random;
    Color color; 
    int width = 10;
    int heigth = 10;
    int yemSayaci= 0;
    boolean yenmedi = true;
    int count;
    
    public Yem(int a) {

        random = new Random();
        color = Color.rgb(random.nextInt(255), random.nextInt(255), 
            random.nextInt(255));
        randomBoundX = (int) Yilancik.ekranGenisligi;
        randomBoundY = (int) Yilancik.ekranYuksekligi;
        rectangleX = random.nextInt(randomBoundX);
        rectangleY = random.nextInt(randomBoundY);
        this.count = count+1;
        setX(rectangleX);
        setY(rectangleY);
        setWidth(width);
        setHeight(heigth);
        setFill(color);
        
    }
    
//    public void setWidth(int width) {
//        this.width = width;
//    }
//
//    public int getRectangleX() {
//        return rectangleX;
//    }
//
//    public int getRectangleY() {
//        return rectangleY;
//    }
//
//    public Color getColor() {
//        return color;
//    }
//
//    public int getHeigth() {
//        return heigth;
//    }
//
//    public void setHeigth(int heigth) {
//        this.heigth = heigth;
//    }
    
    
}
