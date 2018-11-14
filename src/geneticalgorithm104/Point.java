/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm104;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author German
 */
public class Point {
    
    private DoubleProperty X;
    private DoubleProperty Y;
    private IntegerProperty index;
    
    
    //Конструктор
    /**
     * Создание точки без параметров
     */
    public Point() {
    }
    /**
     * Создание точки в известных координатах.
     * @param X - координата Х.
     * @param Y - координата Y.
     * @param index - индекс точки внутри листа (ObservableList) точек в контроллере.
     */
    public Point( double X, double Y, int index ) {
        this.X = new SimpleDoubleProperty(X);
        this.Y = new SimpleDoubleProperty(Y);
        this.index = new SimpleIntegerProperty(index);
    }
    
    //Методы
    public double getX() {
        return X.doubleValue();
    }
    
    public void setX(double X) {
        this.X.set(X);
    }
    
    public double getY() {
        return Y.doubleValue();
    }
    
    public void setY(double Y) {
        this.Y.set(Y);
    }
    
    public int getIndex() {
        return index.intValue();
    }
    
    public void setIndex(int index) {
        this.index.set(index);
    }
}
