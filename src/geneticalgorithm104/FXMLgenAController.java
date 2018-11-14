/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm104;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 *
 * @author German
 */
public class FXMLgenAController implements Initializable {
    
    /**
        ***Поля***
    */
    
    @FXML
    private Canvas canvasPoints;
    
    //не FXML
    int i_point = 0;
    int N_points = -1;
    int N_routes = -1;
    private ObservableList<Point> pointsList = FXCollections.observableArrayList();
    private ObservableList<Route> routesList = FXCollections.observableArrayList();
    private double[][] incidencyMatrix;
    boolean disableLever = false;
    private Comparator<Route> comparatorRouteLength = Comparator.comparingDouble(Route::getLength);
    
    //test
    Route testR;
    
    
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TextField textfieldNumPoints;
    @FXML
    private TextArea textareaStatus;
    @FXML
    private Label labelNumPortsNotSet;
    @FXML
    private Button buttonRandomDisposition;
    @FXML
    private TableView<Point> tableXY;
    @FXML
    private Button buttonCircleDisposition;
    @FXML
    private TableColumn<Point, Integer> tableXYcolumn0;
    @FXML
    private TableColumn<Point, Double> tableXYcolumnX;
    @FXML
    private TableColumn<Point, Double> tableXYcolumnY;
    @FXML
    private SplitPane splitPane0;
    @FXML
    private SplitPane splitPane1;
    @FXML
    private SplitPane splitPane2;
    @FXML
    private SplitPane splitPane3;
    @FXML
    private Button buttonDropTableXYSelection;
    @FXML
    private TextField textfieldNumRoutes;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonPause;
    @FXML
    private Button buttonStop;
    
    
    /**
        ***Инициализация***
    */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Заливка области построения
        fillCanvasWhite(canvasPoints);
        //Приветствие в строке состояния
        textareaStatus.appendText("Оптимизация длины маршрута кругового рейса методом генетических химер. Версия 1.04 (22.N.17)" + "\n");
        textareaStatus.appendText("Разработано: ФГБОУ ВО \"ГУМРФ имени адмирала С. О. Макарова\", Кафедра Портов и грузовых терминалов," + "\n");
        textareaStatus.appendText("А. Л. Кузнецов, Г. Б. Попов." + "\n");
        textareaStatus.appendText("Введите количество портов." + "\n");
        //Запрет сортировки таблицы
        tableXYcolumn0.setSortable(false);
        tableXYcolumnX.setSortable(false);
        tableXYcolumnY.setSortable(false);
    }    
    
    /**
        ***Методы***
    */
    
    //Щелчок мышки по области построения
    @FXML
    private void canvasPointsMouseClicked(MouseEvent event) {
        //Проверка включено управление или нет?
        if ( !disableLever ) {
            //Проверка задано количество точек или нет?
            if (N_points == -1) {
                labelNumPortsNotSet.setVisible(true);
                textareaStatus.appendText("Невозможно задать дислокацию портов. Сначала задайте количество портов." + "\n");
            }
            else {
                labelNumPortsNotSet.setVisible(false);
                GraphicsContext gc = canvasPoints.getGraphicsContext2D();
                /*
                *   Проверка не выделено ли значение в таблице
                */
                //Ничего не выделено
                if ( tableXY.getSelectionModel().isEmpty() ) {
                    //Постановка первой точки
                    if (this.i_point == 0) {
                        fillCanvasWhite(canvasPoints);
                        //Постановка точки и установка шрифта
                        gc.setFill(Color.ROYALBLUE);
                        gc.fillOval(event.getX(), event.getY(), 4, 4);
                        gc.setStroke(Color.BLACK);
                        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
                        //Передача Х и У в лист точек
                        pointsList.get(i_point).setX(event.getX());
                        pointsList.get(i_point).setY(event.getY());
                        //Обновление таблицы с Х и У
                        tableXYcolumnX.setVisible(false);
                        tableXYcolumnX.setVisible(true);
                        //
                        gc.strokeText(String.valueOf(this.i_point + 1), event.getX() + 3, event.getY() - 3);
                        this.i_point++;
                    }
                    //Постановка остальных точек до максимальной
                    else {
                        //Постановка точки и установка шрифта
                        gc.setFill(Color.ROYALBLUE);
                        gc.fillOval(event.getX(), event.getY(), 4, 4);
                        gc.setStroke(Color.BLACK);
                        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
                        //Передача Х и У в лист точек
                        pointsList.get(i_point).setX(event.getX());
                        pointsList.get(i_point).setY(event.getY());
                        //Обновление таблицы с Х и У
                        tableXYcolumnX.setVisible(false);
                        tableXYcolumnX.setVisible(true);
                        //
                        gc.strokeText(String.valueOf(this.i_point + 1), event.getX() + 3, event.getY() - 3);
                        this.i_point++;
                        if (this.i_point == N_points) {
                            textareaStatus.appendText("Задана пользовательская дислокация портов." + "\n");
                            this.i_point = 0;
                        }
                    }
                }
                //Выделена точка, которую хотят сместить
                else {
                    i_point = tableXY.getSelectionModel().getSelectedIndex();
                    //Прорисовка всех точек по новой
                    redrawCanvasExcept(gc, i_point);
                    //Постановка точки и установка шрифта
                    gc.setFill(Color.ROYALBLUE);
                    gc.fillOval(event.getX(), event.getY(), 4, 4);
                    gc.setStroke(Color.BLACK);
                    gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
                    //Передача Х и У в лист точек
                    pointsList.get(i_point).setX(event.getX());
                    pointsList.get(i_point).setY(event.getY());
                    //Обновление таблицы с Х и У
                    tableXYcolumnX.setVisible(false);
                    tableXYcolumnX.setVisible(true);
                    //
                    gc.strokeText(String.valueOf(this.i_point + 1), event.getX() + 3, event.getY() - 3);
                    //Вывод информации в строку состояния
                    textareaStatus.appendText("Порт " + (i_point + 1) + " перемещён." + "\n");
                    //возврат к начальному значению i_point
                    i_point = 0;
                }
            }
        }
    }
    
    /*
    *   Мои кастомные функции
    */
    
    //Заливка области рисования белым
    private void fillCanvasWhite(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    //Тупое округление до десятых долей
    private double roundToDec(double a) {
        a = Math.round(a * 10 );
        a =  a / 10;
        return a;
    }
    
    //Прорисовка всех точек, кроме одной
    private void redrawCanvasExcept (GraphicsContext gc, int numOfPointExcepted) {
        fillCanvasWhite(canvasPoints);
        for (int i = 0; i < N_points; i++) {
            if (i != numOfPointExcepted) {
                //Постановка точки и установка шрифта
                gc.setFill(Color.ROYALBLUE);
                gc.fillOval(pointsList.get(i).getX(), pointsList.get(i).getY(), 4, 4);
                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
                gc.strokeText(String.valueOf(i + 1), pointsList.get(i).getX() + 3, pointsList.get(i).getY() - 3);
            }
        }
    }
    
    //Прорисовка линий соединения
    private void drawConnections (Route routeToDraw) {
        GraphicsContext gc = canvasPoints.getGraphicsContext2D();
        fillCanvasWhite(canvasPoints);
        for (int i = 0; i < pointsList.size(); i++ ) {
            gc.setFill(Color.ROYALBLUE);
            gc.fillOval(pointsList.get(i).getX(), pointsList.get(i).getY(), 4, 4);
            gc.setStroke(Color.BLACK);
            gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
            gc.strokeText(String.valueOf(i + 1), pointsList.get(i).getX() + 3, pointsList.get(i).getY() - 3);
            gc.setStroke(Color.ROYALBLUE);
            if ( i == pointsList.size() - 1 ) {
                gc.strokeLine( routeToDraw.getPoint(i).getX(), routeToDraw.getPoint(i).getY(), routeToDraw.getPoint(0).getX(), routeToDraw.getPoint(0).getY() );
            }
            else {
                gc.strokeLine( routeToDraw.getPoint(i).getX(), routeToDraw.getPoint(i).getY(), routeToDraw.getPoint(i + 1).getX(), routeToDraw.getPoint(i + 1).getY() );
            }
        }
    }
    //Расчет матрицы инциденций
    private void calculateIncidencyMatrix() {
        incidencyMatrix = new double[N_points][N_points];
        for (int i = 0; i < incidencyMatrix.length; i++) {
            for (int j = 0; j < incidencyMatrix.length; j++) {
                if ( i != j ) {
                    incidencyMatrix[i][j] = Math.sqrt( Math.pow( ( pointsList.get(i).getX() - pointsList.get(j).getX() ) , 2) + Math.pow( ( pointsList.get(i).getY() - pointsList.get(j).getY() ) , 2) );
                }
                else {
                    incidencyMatrix[i][j] = 0;
                }
            }
        }
    }
    
    //Блокировка и снятие блокировки элементов управления
    private void setBlockControls(boolean block) {
        if (block){
            buttonCircleDisposition.setDisable(true);
            buttonDropTableXYSelection.setDisable(true);
            buttonRandomDisposition.setDisable(true);
            textfieldNumPoints.setDisable(true);
            textfieldNumRoutes.setDisable(true);
            disableLever = true;
        }
        else {
            buttonCircleDisposition.setDisable(false);
            buttonDropTableXYSelection.setDisable(false);
            buttonRandomDisposition.setDisable(false);
            textfieldNumPoints.setDisable(false);
            textfieldNumRoutes.setDisable(false);
            disableLever = false;
        }
    }
    
    
    /*
    *******Генетический Алгоритм**********
    */
    private void stepAlgorithm(){
        //Половины листа с маршрутами
        
        int firstHalfList = (int) ( routesList.size() / 2 );
        Route[] survivors = new Route[firstHalfList];
        
        
        int otherHalfList = routesList.size() - firstHalfList;
        int firstHalOfRoute = (int) ( pointsList.size() / 2 );
        int otherHalOfRoute = pointsList.size() - firstHalOfRoute;
        
        //Вычисление лучшего маршрута
        Route theBest = new Route( routesList.get(0) );
        double minL = routesList.get(0).returnLength(incidencyMatrix);
        int minL_index = 0;
        for (Route r : routesList) {
            if ( minL >= r.returnLength(incidencyMatrix) ) {
                minL = r.returnLength(incidencyMatrix);
                minL_index = routesList.indexOf(r);
                theBest = r;
            }
        }
        drawConnections( theBest );
        
        //TEST
        textareaStatus.appendText( "//Размер популяции после вычисления лучшего = " + routesList.size() + "\n" );
        textareaStatus.appendText( "//Длины: " + routesList.size() + "\n" );
        for (Route r : routesList) {
            textareaStatus.appendText( routesList.indexOf(r) + ")        " + r.returnLength(incidencyMatrix) + "\n" );
        }
        textareaStatus.appendText( "//Лучший: " + routesList.indexOf(theBest) + "     Его длина: " + theBest.returnLength(incidencyMatrix) + "\n" );
        
        //Comparator
        routesList.sort(comparatorRouteLength);
        for (int i = 0; i < firstHalfList; i++) {
            routesList.set(i + firstHalfList,  routesList.get(i));
        }
        
        
        
        /*
        //Вычисление половины худших и убивание их
        double maxL = 0;
        int maxL_index = 0;
        
        for ( int k = 0; k < firstHalfList; k++) {
            for (Route r : routesList) {
                if ( maxL <= r.returnLength(incidencyMatrix) ) {
                    maxL = r.returnLength(incidencyMatrix);
                    maxL_index = routesList.indexOf(r);
                }
            }
            routesList.remove(maxL_index);
        }
        */
        
        //TEST
        textareaStatus.appendText( "//Размер популяции после убивания худших = " + routesList.size() + "\n" );
//        
//        //Заполнение половины новыми особями и обновление старых
//        for ( int k = 0; k < firstHalfList; k++) {
//            
//            Route r = new Route( routesList.get(k) );
//            for (int i = 0; i < (int) (N_points / 2) - 1; i++ ) {
//                r.shuffleHead();
//            }
//            routesList.add( r );
//        }
//        
        //TEST
        textareaStatus.appendText( "//Размер популяции после замены старых на новых = " + routesList.size() + "\n");
        
        /*
        //Повторный поиск худшего для убивания и замены лучшим предыдущей серии
        for (Route r : routesList) {
            if ( maxL < r.returnLength(incidencyMatrix) ) {
                maxL = r.returnLength(incidencyMatrix);
                maxL_index = routesList.indexOf(r);
            }
        }
        routesList.set(maxL_index, theBest);
        
        //TEST
        textareaStatus.appendText( "//Размер популяции в конце = " + routesList.size() + "\n" );
        */
    }
    
    private void runAlgorithm(){
        for (int i = 0; i < 10; i++) {
            stepAlgorithm();
        }
    }
    
    /*
    * Мои кастомные функции чисто для ОТЛАДКИ
    * TEST
    */
    
    //Тест рандомного шафла точек
    private void testRandomShuffle(ObservableList<Point> points){
        int[] i_sequence = new int[points.size()];
        boolean[] usage_check = new boolean[points.size()];
        //
        GraphicsContext gc = canvasPoints.getGraphicsContext2D();
        fillCanvasWhite(canvasPoints);
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
        //
        //**Шафл начинается тут**
        //Присвоение рандомной 1-й точки
        i_sequence[0] = (int) ( Math.random() * points.size() );
        usage_check[ i_sequence[0] ] = true;
        gc.strokeText( String.valueOf( i_sequence[0] ), 10, 20);
        //Присвоение рандомных остальных точек
        for (int i = 1; i < points.size(); i++) {
            while (usage_check[ i_sequence[i] ]) {
                i_sequence[i] = (int) ( Math.random() * points.size() );
            }
            usage_check[ i_sequence[i] ] = true;
            gc.strokeText( String.valueOf( i_sequence[i] ), 10 + i * 20, 20);
        }
        //**
    }
    
    //Тест конструктора маршрутов и длины маршрута
    
    private Route testRouteConstructor() {
        Route r = new Route(pointsList, -1);
        //
        GraphicsContext gc = canvasPoints.getGraphicsContext2D();
        fillCanvasWhite(canvasPoints);
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
        //
        gc.strokeText( r.returnSolutionSequence() + "     Длина: " + String.valueOf( r.returnLength(incidencyMatrix) ), 10, 20);
        return r;
    }
    
    //Shuffle head Route
    //НИХУЯ НЕ ПАШЕТ
    private void testRouteHeadShuffle(Route r) {
        r.shuffleHead();
        //
        GraphicsContext gc = canvasPoints.getGraphicsContext2D();
        fillCanvasWhite(canvasPoints);
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
        //
        gc.strokeText( r.returnSolutionSequence() + "     Длина: " + String.valueOf( r.returnLength(incidencyMatrix) ), 10, 20);
        /*int[] returnedInt = r.shuffleHead(pointsList);
        //
        GraphicsContext gc = canvasPoints.getGraphicsContext2D();
        fillCanvasWhite(canvasPoints);
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
        //
        for (int i = 0; i < returnedInt.length; i++) {
            gc.strokeText(String.valueOf(returnedInt[i] + 1), 10 + 15 * i, 20);
        }
        //gc.strokeText( r.returnSolutionSequence() + "     Длина: " + String.valueOf( r.returnLength(incidencyMatrix) ), 10, 20);
        */
    }
    
    private Route testRoutesListCreation(int i) {
        Route r = new Route(pointsList, -1);
        double l = r.returnLength(incidencyMatrix);
        //
        GraphicsContext gc = canvasPoints.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
        //
        gc.strokeText( r.returnSolutionSequence() + "     Длина: " + String.valueOf(l), 10, i * 20);
        return r;
    }
    
    /*
    *   FXML-сгенеренные функции продолжение
    */
    
    //Поле введения количества точек
    @FXML
    private void inputNumPoints(ActionEvent event) {
        
        N_points = Integer.valueOf( textfieldNumPoints.getText() );
        textareaStatus.appendText("Задано количество портов: " + N_points + "\n");
        //Создание Обзервабл листа точек
        pointsList.clear();
        tableXY.setItems(pointsList);
        for (int i = 0; i < N_points; i++) {
            pointsList.add(new Point( 0, 0, (i + 1) ) );
            //Обновление таблицы
            tableXYcolumn0.setCellValueFactory(new PropertyValueFactory<>("index"));
            tableXYcolumnX.setCellValueFactory(new PropertyValueFactory<>("X"));
            tableXYcolumnY.setCellValueFactory(new PropertyValueFactory<>("Y"));
        }
    }
    
    //Кнопка случайных точек
    @FXML
    private void handleRandomDisposition(ActionEvent event) {
        if (N_points == -1) {
            labelNumPortsNotSet.setVisible(true);
            textareaStatus.appendText("Невозможно задать случайную дислокацию портов. Сначала задайте количество портов." + "\n");
        }
        else {
            fillCanvasWhite(canvasPoints);
            labelNumPortsNotSet.setVisible(false);
            GraphicsContext gc = canvasPoints.getGraphicsContext2D();
            for (int i = 0; i < N_points; i++) {
                //Расчет и передача Х и У в лист точек
                pointsList.get(i).setX( roundToDec( 5 + Math.random() * ( canvasPoints.getWidth() - 5 ) ) );
                pointsList.get(i).setY( roundToDec( 5 + Math.random() * ( canvasPoints.getHeight() - 5 ) ) );
                //Обновление таблицы с Х и У
                tableXYcolumnX.setVisible(false);
                tableXYcolumnX.setVisible(true);
                //Постановка точки и установка шрифта
                gc.setFill(Color.ROYALBLUE);
                gc.fillOval(pointsList.get(i).getX(), pointsList.get(i).getY(), 4, 4);
                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
                gc.strokeText(String.valueOf(i + 1), pointsList.get(i).getX() + 3, pointsList.get(i).getY() - 3);
            }
            textareaStatus.appendText("Задана случайная дислокация портов." + "\n");
        }
    }
    
    
    //Кнопка точек по кругу
    @FXML
    private void handleCircleDisposition(ActionEvent event) {
        if (N_points == -1) {
            labelNumPortsNotSet.setVisible(true);
            textareaStatus.appendText("Невозможно задать круговую дислокацию портов. Сначала задайте количество портов." + "\n");
        }
        else {
            int radius = 150;
            fillCanvasWhite(canvasPoints);
            labelNumPortsNotSet.setVisible(false);
            GraphicsContext gc = canvasPoints.getGraphicsContext2D();
            for (int i = 0; i < N_points; i++) {
                //Расчет и передача Х и У в лист точек
                pointsList.get(i).setX( roundToDec( canvasPoints.getWidth() / 2 + Math.cos( i * 2 * Math.PI / N_points ) * radius ) );
                pointsList.get(i).setY( roundToDec( canvasPoints.getHeight() / 2 + Math.sin( i * 2 * Math.PI / N_points ) * radius ) );
                //Обновление таблицы с Х и У
                tableXYcolumnX.setVisible(false);
                tableXYcolumnX.setVisible(true);
                //Постановка точки и установка шрифта
                gc.setFill(Color.ROYALBLUE);
                gc.fillOval(pointsList.get(i).getX(), pointsList.get(i).getY(), 4, 4);
                gc.setStroke(Color.BLACK);
                gc.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 10));
                gc.strokeText(String.valueOf(i + 1), pointsList.get(i).getX() + 3, pointsList.get(i).getY() - 3);
            }
            textareaStatus.appendText("Задана круговая дислокация портов." + "\n");
        }
    }
    
    //Кнопка очистки таблицы с Х и У
    @FXML
    private void handleDropTableXYSelection(ActionEvent event) {
        tableXY.getSelectionModel().clearSelection();
    }
    
    //Поле введения количества маршрутов в популяции
    @FXML
    private void inputNumRoutes(ActionEvent event) {
        N_routes = Integer.valueOf( textfieldNumRoutes.getText() );
        textareaStatus.appendText("Задан размер популяции решений: " + N_routes + "\n");
        //Обновление листа маршрутов
        routesList.clear();
        for (int i = 0; i < N_routes; i++) {
            routesList.add( new Route(pointsList, i ) );
        }
        calculateIncidencyMatrix();
    }

    @FXML
    private void handleStart(ActionEvent event) {
        //Не задано количество решений в популяции
        if (N_routes == -1) {
            labelNumPortsNotSet.setText("Не задан размер популяции решений!");
            labelNumPortsNotSet.setVisible(true);
            textareaStatus.appendText("Невозможно запустить алгоритм. Сначала задайте размер популяции решений." + "\n");
        }
        else {
            //
            
            runAlgorithm();
            
        }
    }

    @FXML
    private void handlePause(ActionEvent event) {
    }

    @FXML
    private void handleStop(ActionEvent event) {
    }
}
