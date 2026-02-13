package com.feron.Visualisation;
import java.util.ArrayList;


public class MyPath {
    private ArrayList<Double> points;

    public MyPath(ArrayList<Double> points){
        this.points=points;
    }

    public MyPath(){
        this.points=new ArrayList<Double>();
    }

    public ArrayList<Double> get_points(){
        return this.points;
    }

    public double path_length(){
        if (this.points==null){
            return 0.0;
        }else{
            double l=0;
            for (int i=0; i<this.points.size()-3;i+=2){
                double x1=this.points.get(i);
                double y1=this.points.get(i+1);
                double x2=this.points.get(i+2);
                double y2=this.points.get(i+3);

                l+=Math.sqrt(Math.pow((y2-y1),2)+Math.pow((x2-x1),2));

            }

            return l;
        }
    }

    //Amélioration proposé dans la partie RRT, d'inégalité triangulaire.
    public MyPath tr_in_improve(Environnement env){
        int n=this.points.size();
        if (n<6){
            return this;
        }else{
            int i=0;
            while (i<this.points.size()-5){
                double x1=this.points.get(i);
                double y1=this.points.get(i+1);
                double x2=this.points.get(i+4);
                double y2=this.points.get(i+5);
                Point a=new Point(x1,y1);
                Point b=new Point(x2,y2);
                if (env.isColisionFree(a,b)){
                    this.points.remove(i+2);
                    this.points.remove(i+2);
                }else{
                    i+=2;
                }
            }
            return this;

        }
    }
}
