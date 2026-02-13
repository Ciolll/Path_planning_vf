package com.feron.PSO;
import com.feron.Visualisation.*;
import java.util.ArrayList;

public class Particle {
    public final static int intermedaire_steps=5;

    private ArrayList<Double> position=new ArrayList<Double>(2*intermedaire_steps);
    private ArrayList<Double> velocity=new ArrayList<Double>(2*intermedaire_steps);
    private double x_start;
    private double y_start;
    private double x_end;
    private double y_end;

    public Particle(double x_s,double y_s,double x_e,double y_e){
        this.x_start=x_s;
        this.y_start=y_s;
        this.x_end=x_e;
        this.y_end=y_e;
    }

    public ArrayList<Double> get_position(){
        return this.position;
    }
    public ArrayList<Double> get_velocity(){
        return this.velocity;
    }

    public double get_x_s(){
        return this.x_start;
    }
    public double get_y_s(){
        return this.y_start;
    }

    public double get_x_e(){
        return this.x_end;
    }

    public double get_y_e(){
        return this.y_end;
    }


    public void set_start(double x,double y){
        this.x_start=x;
        this.y_start=y;
    }
    public void set_end(double x,double y){
        this.x_end=x;
        this.y_end=y;
    }

    public void set_position(ArrayList<Double> position){
        if (position.size()!=2*intermedaire_steps){
            System.err.println("la taille de la liste de position ne correspond pas");
        }else {
            this.position=position;
        }
    }
    public void set_velocity(ArrayList<Double> velocity){
        if (velocity.size()!=2*intermedaire_steps){
            System.err.println("la taille de la liste de vitesse ne correspond pas");
        }else {
            this.velocity=velocity;
        }
    }

    public MyPath particle_to_path(){
        ArrayList<Double> pointsCopy = new ArrayList<>(this.position);
        MyPath path=new MyPath(pointsCopy);
        path.get_points().add(x_end);
        path.get_points().add(y_end);
        path.get_points().add(0,y_start);
        path.get_points().add(0,x_start);
        return path;
    }
}
