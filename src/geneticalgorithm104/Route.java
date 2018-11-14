/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm104;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author German
 */
public class Route {
    
    /*
    * Поля
    */
    
    private ObservableList<Point> includedPoints = FXCollections.observableArrayList();
    private double length;
    private int index;
    private int numOfPoints;
    
    /*
    * Конструкторы
    */
    
    //Пустой
    public Route() {
        
    }
    
    //Генерация случайного маршрута сразу
    public Route(ObservableList<Point> points, int index) {
        int[] shuffledOrder = randomShuffledSequence(points.size());
        for (int i = 0; i < points.size(); i++) {
            includedPoints.add(points.get(shuffledOrder[i]));
            includedPoints.get(i).setIndex( points.get(shuffledOrder[i]).getIndex() );
        }
        this.index = index;
        this.numOfPoints = points.size();
    }
    
    //Копирование существующего маршрута
    public Route(Route copiedRoute){
        this.includedPoints = copiedRoute.includedPoints;
        this.length = copiedRoute.length;
        this.index = copiedRoute.index;
        this.numOfPoints = copiedRoute.numOfPoints;
    }
    
    /*
    * Методы
    */
    
    //Индекс маршрута в популяции

    /**
     *
     * @param index
     */
    public void setIndex (int index) {
        this.index = index;
    }
    
    /**
     *
     * @return
     */
    public int getIndex() {
        return this.index;
    }
    
    public double getLength() {
        return this.length;
    }
    
    //Расчет и возврат длины маршрута
    public double returnLength(double[][] incidencyMatrix){
        length = 0;
        for (int i = 0; i < incidencyMatrix.length - 1; i++) {
            length += incidencyMatrix[includedPoints.get(i).getIndex() - 1][includedPoints.get(i + 1).getIndex() - 1];
        }
        return length;
    }
    
    //Возврат решения в виде последовательности точек
    public String returnSolutionSequence() {
        String solSeq = ( includedPoints.get(0).getIndex() ) + "  -  ";
        for (int i = 1; i <= includedPoints.size(); i++) {
            if ( i == includedPoints.size() ) {
                solSeq += ( includedPoints.get(0).getIndex() ) + ".";
            }
            else {
                solSeq += ( includedPoints.get(i).getIndex() ) + "  -  ";
            }
        }
        return solSeq;
    }
    
    
    //Cлучайная расстановка целых чисел от 0 до sequenceSize
    private int[] randomShuffledSequence( int sequenceSize ) {
        int[] i_sequence = new int[sequenceSize];
        boolean[] usage_check = new boolean[sequenceSize];
        //**Шафл начинается тут**
        //Присвоение рандомной 1-й точки
        i_sequence[0] = (int) ( Math.random() * sequenceSize );
        usage_check[ i_sequence[0] ] = true;
        //Присвоение рандомных остальных точек
        for (int i = 1; i < sequenceSize; i++) {
            while (usage_check[ i_sequence[i] ]) {
                i_sequence[i] = (int) ( Math.random() * sequenceSize );
            }
            usage_check[ i_sequence[i] ] = true;
        }
        //**
        return i_sequence;
    }
    
    //Cлучайная перестановка двух целых чисел в массиве
    public void shuffleTwoIntegersInFirstHalf( int[] sequence ) {
        int i1_index = (int) (Math.random() * sequence.length / 2);
        int i1 = sequence[i1_index];
        int i2_index = (int) (Math.random() * sequence.length / 2);
        sequence[i1_index] = sequence[i2_index];
        sequence[i2_index] = i1;
    }
    
    //Cлучайная перестановка двух точек в ГОЛОВЕ маршрута
    public void shuffleHead() {
        int i1_index = (int) (Math.random() * includedPoints.size() / 2);
        Point i1 = includedPoints.get(i1_index);
        int i2_index = (int) (Math.random() * includedPoints.size() / 2);
        Point i2 = includedPoints.get(i2_index);
        includedPoints.set(i1_index, i2);
        includedPoints.set(i2_index, i1);
    }
    
    //Cлучайная перестановка двух точек в ХВОСТЕ маршрута
    public void shuffleTail() {
        int i1_index = (int) (Math.random() * includedPoints.size() / 2 + (int) (includedPoints.size() / 2) );
        Point i1 = includedPoints.get(i1_index);
        int i2_index = (int) (Math.random() * includedPoints.size() / 2 + (int) (includedPoints.size() / 2) );
        Point i2 = includedPoints.get(i2_index);
        includedPoints.set(i1_index, i2);
        includedPoints.set(i2_index, i1);
    }
    
    //Предоставление точки под указанным индексом в маршруте
    public Point getPoint(int i){
        return includedPoints.get(i);
    }
}
