package com.feron.Visualisation;
public class Point {
    private double x;
    private double y;

    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }

    public Point(){
        this.x=0;
        this.y=0;
    }

    public void set_point(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double get_x(){
        return this.x;
    }

    public double get_y(){
        return this.y;
    }

    public double dist_sq(Point other){
        return (Math.pow((this.get_x()-other.get_x()),2)+Math.pow((this.get_y()-other.get_y()),2));
    }
}
