package com.feron.Visualisation;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;

import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javafx.scene.SnapshotParameters;

import com.feron.PSO.*;
import com.feron.RRT.*;

public class Workplace extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        Environnement testEnvironnement= EnvironnementLoader.load_test("scenario3.txt");
        double scale =0.5;
        // Obstacle testObstacle=new Obstacle(0, 450, 20,50);
        // testEnvironnement.add_obstacle(testObstacle);
        // Robot testRobot=new Robot(2,2);
        // testRobot.set_R(-10);
        // testEnvironnement.add_robot(testRobot);
        
        Pane canvas = new Pane();
        canvas.setStyle("-fx-background-color: white;");
        canvas.setPrefSize(testEnvironnement.get_width()*scale*1.1,testEnvironnement.get_height()*scale*1.1);

        Rectangle work_env= new Rectangle (0.05*scale*testEnvironnement.get_width(),0.05*scale*testEnvironnement.get_height(),scale*testEnvironnement.get_width(),scale*testEnvironnement.get_height());
        work_env.setFill(Color.WHITE);
        work_env.setStroke(Color.BLACK);
        canvas.getChildren().add(work_env);

        double x_s=testEnvironnement.get_robots().get(0).get_x_center();
        double y_s=testEnvironnement.get_robots().get(0).get_y_center();
        double x_e=testEnvironnement.get_robots().get(0).get_x_obj();
        double y_e=testEnvironnement.get_robots().get(0).get_y_obj();

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long startCpuTime = bean.getCurrentThreadCpuTime();
        MyPath path=Algorithm.basic_pso(testEnvironnement,x_s,y_s,x_e,y_e); // changer en fonction de l'algo utilisé soit Algorithm.basic_pso soit RRt.rrt
        long endCpuTime = bean.getCurrentThreadCpuTime();
        double cpuTimeMs = (endCpuTime - startCpuTime) / 1_000_000.0;
        System.out.println("Temps CPU effectif : " + cpuTimeMs + " ms");

        view_path(canvas, testEnvironnement, path, scale);

        for (Obstacle obs:testEnvironnement.get_obstacles()){
            Point p_obs=mod_coord(obs.get_x(), obs.get_y(), testEnvironnement);
            Rectangle viewObs= new Rectangle (p_obs.get_x()*scale,scale*(p_obs.get_y()-obs.get_height()),scale*obs.get_width(),scale*obs.get_height());
            viewObs.setFill(Color.BLACK);
            canvas.getChildren().add(viewObs);
            // System.out.println(p_obs.get_x());
            // System.out.println(p_obs.get_y());
        }
        for (Robot r:testEnvironnement.get_robots()){
            double R_current=r.get_R();
            Point p_rob=mod_coord(r.get_x_center(), r.get_y_center(), testEnvironnement);
            Circle viewRob;
            if (R_current>0){
                viewRob=new Circle (p_rob.get_x()*scale,p_rob.get_y()*scale,R_current*scale);
                viewRob.setFill(Color.RED);
            }else{
                viewRob=new Circle (p_rob.get_x()*scale,p_rob.get_y()*scale,10*scale);
                viewRob.setFill(Color.PINK);
            }
            canvas.getChildren().add(viewRob);
        }

        
        String filename = "resultat_" + System.currentTimeMillis() + ".png";
        canvas.layout(); 
        save_canvas(canvas, filename);
        

        Scene scene = new Scene(canvas);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation Robotique");
        primaryStage.show();
        }catch (Exception e){
            System.err.println("le fichier n'a pas pu être ouvert");
            e.printStackTrace();
        }
    
    }

    public Point mod_coord(double x,double y,Environnement env){
        double xmax=env.get_width();
        double ymax=env.get_height();

        double x_new;
        double y_new;
        x_new=x+0.05*xmax;
        y_new=1.05*ymax-y;
        Point p=new Point(x_new,y_new);
        return p;
    }

    public void view_path(Pane canvas, Environnement env, MyPath path,double scale){
        ArrayList<Double> points =path.get_points();
        if (points==null){
            return;
        }

        for (int i=0; i<points.size()-3;i+=2){
            double x_a=points.get(i);
            double y_a=points.get(i+1);
            double x_b=points.get(i+2);
            double y_b=points.get(i+3);

            Point p_a=mod_coord(x_a,y_a, env);
            Point p_b=mod_coord(x_b,y_b,env);

            Line l=new Line(p_a.get_x()*scale,p_a.get_y()*scale,p_b.get_x()*scale,p_b.get_y()*scale);
            l.setStroke(Color.RED);
            canvas.getChildren().add(l);
        }
    }

    public void save_canvas(Pane canvas, String filename) {
    WritableImage image = new WritableImage((int)canvas.getPrefWidth(), (int)canvas.getPrefHeight());
    
    canvas.snapshot(new SnapshotParameters(), image);
    
    File file = new File(filename);
    try {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        System.out.println("Environnement sauvegardé sous : " + file.getAbsolutePath());
    } catch (IOException e) {
        System.err.println("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}