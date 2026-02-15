package com.feron.Visualisation;
import java.util.ArrayList;

public class Environnement {
    private double xmax;
    private double ymax;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Robot> robots;

    public Environnement(double xmax,double ymax){
        this.xmax=xmax;
        this.ymax=ymax;
        this.obstacles= new ArrayList<Obstacle>();
        this.robots=new ArrayList<Robot> ();
    }
    
    public Environnement(double xmax,double ymax,ArrayList<Obstacle> obstacles, ArrayList<Robot> robots){
        this.xmax=xmax;
        this.ymax=ymax;
        this.obstacles= obstacles;
        this.robots=robots;
    }
    
    public double get_width(){
        return this.xmax;
    }

    public double get_height(){
        return this.ymax;
    }
    
    public ArrayList<Obstacle> get_obstacles(){
        return this.obstacles;
    }
    public ArrayList<Robot> get_robots(){
        return this.robots;
    }
    public void set_width(double x){
        this.xmax=x;
    }
    public void set_height(double y){
        this.ymax=y;
    }
    public void add_obstacle(Obstacle o){
        this.obstacles.add(o);
    }
    public void add_robot(Robot r){
        this.robots.add(r);
    }
    public Obstacle getRandomObstacle(){
        int n=this.obstacles.size();
        int i=(int)(Math.random()*n);
        return this.obstacles.get(i);
    }


    
    public boolean isPointFree(Point p){
        for (Obstacle o:this.obstacles){
            double obs_x_min = o.get_x();
            double obs_x_max = o.get_x() + o.get_width();
            double obs_y_min = o.get_y();
            double obs_y_max = o.get_y() + o.get_height();

            double x1 = p.get_x();
            double y1 = p.get_y();
            boolean p1_inside = (x1 >= obs_x_min && x1 <= obs_x_max && y1 >= obs_y_min && y1 <= obs_y_max);
            if (!p1_inside){
                return false;
            }
        }
        return true;
    }

    
    public boolean isColisionFree(Point a,Point b){
        for (Obstacle o:this.obstacles){
            double obs_x_min = o.get_x();
            double obs_x_max = o.get_x() + o.get_width();
            double obs_y_min = o.get_y();
            double obs_y_max = o.get_y() + o.get_height();

            double x1 = a.get_x();
            double y1 = a.get_y();
            double x2 = b.get_x();
            double y2 = b.get_y();

            boolean p1_inside = (x1 >= obs_x_min && x1 <= obs_x_max && y1 >= obs_y_min && y1 <= obs_y_max);
            boolean p2_inside = (x2 >= obs_x_min && x2 <= obs_x_max && y2 >= obs_y_min && y2 <= obs_y_max);

            if (p1_inside || p2_inside) {
                return false;
            }

            double dx = x2 - x1;
            double dy = y2 - y1;

            double t_min = 0.0;
            double t_max = 1.0;

            double[] p = {-dx, dx, -dy, dy};
            double[] q = {x1 - obs_x_min, obs_x_max - x1, y1 - obs_y_min, obs_y_max - y1};

            boolean intersect = true;
            for (int k = 0; k < 4; k++) {
                if (p[k] == 0) { 
                    if (q[k] < 0) {
                        intersect = false; 
                        break;
                    }
                } else {
                    double t = q[k] / p[k];
                    if (p[k] < 0) {
                        if (t > t_max) { 
                            intersect = false; 
                            break; 
                        }
                        if (t > t_min) t_min = t;
                    } else {
                        if (t < t_min) { 
                            intersect = false; 
                            break; 
                        }
                        if (t < t_max) t_max = t;
                    }
                }
            }

            if (intersect && (t_max-t_min>1e-7)) {
                return false;
            }
            
        }
        return true;
    }
}
