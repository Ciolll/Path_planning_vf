package com.feron.Visualisation;
public class Obstacle {
    private double xo;
    private double yo;
    private double lx;
    private double ly;

    public Obstacle(double xo,double yo,double lx,double ly){
        this.xo=xo;
        this.yo=yo;
        this.lx=lx;
        this.ly=ly;
    }

    public double get_x(){
        return this.xo;
    }
    public double get_y(){
        return this.yo;
    }
    public double get_width(){
        return this.lx;
    }
    public double get_height(){
        return this.ly;
    }
    public Point getRandomCorner(){
        int i=(int)(Math.random()*4);
        if(i==0){
            return new Point(xo,yo);
        }
        if(i==1){
            return new Point(xo+lx,yo);
        }
        if(i==2){
            return new Point(xo,yo+ly);
        }
        if(i==3){
            return new Point(xo+lx,yo+ly);
        }
        return new Point(0,0);
    }
}
