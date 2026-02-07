package com.feron.Visualisation;
public class Robot {
    private double x_center;
    private double y_center;
    private double R=0;
    private double x_obj;
    private double y_obj;

    public Robot(double x, double y){
        this.x_center= x;
        this.y_center=y;
    }
    
    public Robot (double x, double y, double x_goal, double y_goal){
        this.x_center=x;
        this.y_center=y;
        this.x_obj=x_goal;
        this.y_obj=y_goal;
    }

    public double get_x_center(){
        return this.x_center;
    }

    public double get_y_center(){
        return this.y_center;
    }
    public double get_x_obj(){
        return x_obj;
    }
    public double get_y_obj(){
        return y_obj;
    }
    
    public double get_R(){
        return this.R;
    }

    public void set_R(double R){
        if (R<0){
            this.R=0;
        }
        else{
            this.R=R;
        }
    }
}

