package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Model;
import model.Point;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {
    public ColorPicker colorPicker;
    public Slider slider;
    public Canvas canvas;
    public GraphicsContext gr;
    public Button brush;
    public Button eraser;
    public Button down;
    public Button save;
    public Button back;
    public Button clear;
    Model model;
    public Image image;
    public Image image1;
    public Image image2;
    public Image image3;
    public Image image4;
    public Image image5;
    public Image image6;
    public double x;
    public double y;
    public double w;
    public double h;
    public Window window;
    String flag;

    public void initialize(){
        model = new Model();
        gr = canvas.getGraphicsContext2D();
        sliderTol();
        image1 = new Image(getClass().getResourceAsStream("/resourse//кисть.jpg"));
        brush.graphicProperty().setValue(new ImageView(image1));
        image2 = new Image(getClass().getResourceAsStream("/resourse//ластик.jpg"));
        eraser.graphicProperty().setValue(new ImageView(image2));
        image3 = new Image(getClass().getResourceAsStream("/resourse//открыть.jpg"));
        down.graphicProperty().setValue(new ImageView(image3));
        image4 = new Image(getClass().getResourceAsStream("/resourse//сохранить.png"));
        save.graphicProperty().setValue(new ImageView(image4));
        image5 = new Image(getClass().getResourceAsStream("/resourse//вернуться.png"));
        back.graphicProperty().setValue(new ImageView(image5));
        image6 = new Image(getClass().getResourceAsStream("/resourse//очистить.jpg"));
        clear.graphicProperty().setValue(new ImageView(image6));
    }

    private void sliderTol() {
        slider.setMin(1);
        slider.setMax(10);
        slider.setValue(1);
        colorPicker.setValue(Color.BLACK);
        flag = brush.getId();
    }

    public void on_Download(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображениe...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Изображение", "*.png"), new FileChooser.ExtensionFilter("Изображение", "*.bmp"));
        File loadImageFile = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (loadImageFile != null) {
            System.out.println("Процесс открытия файла");
            initDraw(gr, loadImageFile);
            update(model);
        }
    }

    private void update(Model model) {
        gr.clearRect(0, 0, gr.getCanvas().getWidth(), gr.getCanvas().getHeight());
        for (int i = 0; i < model.getPointCount(); i++) {
            gr.setFill(model.getPoint(i).getColor());
            gr.fillOval(model.getPoint(i).getX(),model.getPoint(i).getY(),model.getPoint(i).getwP() ,model.getPoint(i).gethP());
        }
    }

    private void initDraw(GraphicsContext gc, File file) {
        String str=file.getPath();
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        image = new Image(file.toURI().toString());
        x = canvasWidth/2;
        y = canvasHeight/2 ;
        gc.drawImage(image, x, y, w, h);
        PixelReader pixelReader = image.getPixelReader();
        double yr=canvas.getHeight()/image.getHeight();
        double xr=canvas.getWidth()/image.getWidth();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                Point point =new Point(x,y);
                point.setColor(color);
                point.setSizePoint(xr,yr);
                model.addPoint(point);
            }
        }
    }


    public void on_Save(ActionEvent actionEvent) throws IOException {
        WritableImage writableImage = canvas.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите директорию...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", "*.png"));
        File file = fileChooser.showSaveDialog(window);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void on_Clear(ActionEvent actionEvent) {
        gr.clearRect(0, 0, gr.getCanvas().getWidth(), gr.getCanvas().getHeight());
    }

    public void on_Draw(MouseEvent mouseEvent) {
        Point point = new Point((int) mouseEvent.getX(), (int) mouseEvent.getY());
        if (flag == brush.getId()) {
            point.setColor(colorPicker.getValue());
            point.setSizePoint(slider.getValue(), slider.getValue());
            model.addPoint(point);
        } else if(flag == eraser.getId()) {
            model.removePoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
        }
        update(model);
    }

    public void on_Return(ActionEvent actionEvent) {
        for (int i = 0; i < model.getPointCount(); i++) {
            gr.setFill(model.getPoint(i).getColor());
            gr.fillOval(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
            model.getPoint(i).setColor(colorPicker.getValue());
        }
        update(model);
    }

    public void on_Brush(ActionEvent actionEvent) {
        flag = brush.getId();
    }

    public void on_Eraser(ActionEvent actionEvent) {
        flag = eraser.getId();
    }
}
