package com.feron.RRT;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.feron.Visualisation.*;

public class RrtAlgorithm {

    public static double delta_s=30;
    public static double delta_r=100;
    public static double delta_e=100;

    public static int nb_iter_max=5000;

    public static Point init_rand_point(Environnement env){
        double x=Math.random()*env.get_width();
        double y=Math.random()*env.get_height();
        Point p=new Point(x,y);
        return p;
    }

    public static Point getIntelligentSample(Environnement env,double x_e,double y_e) {
    double rand = Math.random();
    if (rand < 0.10) return new Point(x_e, y_e);

    if (rand < 0.40) {
        Obstacle obs = env.getRandomObstacle();
        Point corner = obs.getRandomCorner(); 
        
        double nx = corner.get_x() + (new Random().nextGaussian() * 15);
        double ny = corner.get_y() + (new Random().nextGaussian() * 15);
        
        Point p = new Point(nx, ny);
        if (env.isPointFree(p)){
            return p; 
        }
    }
    return init_rand_point(env);
}

    public static MyPath rrt(Environnement env,double x_s,double y_s,double x_e,double y_e){
        Tree tree=new Tree(new Node(x_s,y_s));
        int nb_iter=0;
        Node bestGoalNode=null;
        Point end=new Point(x_e,y_e);
        while (nb_iter<nb_iter_max){
            Point v_r;
            v_r = getIntelligentSample(env,x_e,y_e);
                    
            Node v_n=tree.findNearest(v_r);
            double x_n=v_n.get_point().get_x();
            double y_n=v_n.get_point().get_y();
            
            double dist=v_r.dist_sq(v_n.get_point());
            Node v;
            if (delta_s*delta_s>dist){
                v=new Node (v_r.get_x(),v_r.get_y());
            }else{
                double x_new=x_n+(delta_s/Math.sqrt(dist))*(v_r.get_x()-x_n);
                double y_new=y_n+(delta_s/Math.sqrt(dist))*(v_r.get_y()-y_n);
                v=new Node(x_new,y_new);
            }

            if (env.isColisionFree(v.get_point(),v_n.get_point())){
                v.set_parent(v_n);
            
                List<Node> neighbours=tree.neighbour_in_radius(v.get_point(), delta_r);
                for (Node n:neighbours){
                    if (n.get_dist()+Math.sqrt(v.get_point().dist_sq(n.get_point()))<v.get_dist()&& env.isColisionFree(v.get_point(), n.get_point())){
                        v.set_parent(n);
                    }
                }
                tree.insert(v, tree, 0);
                for (Node n:neighbours){
                    double diff=v.get_dist()+Math.sqrt(v.get_point().dist_sq(n.get_point()))-n.get_dist();
                    if (diff<0 && env.isColisionFree(v.get_point(), n.get_point())){
                        if (n.get_parent()!=null){
                            n.get_parent().remove_child(n);
                        }
                        n.set_parent(v);
                        v.add_child(n);
                        n.propagatecost();
                    }
                }
                if (end.dist_sq(v.get_point())<delta_e*delta_e && env.isColisionFree(end, v.get_point())){
                    double currentTotalCost = v.get_dist() + Math.sqrt(v.get_point().dist_sq(end));
                    if (bestGoalNode == null || currentTotalCost < (bestGoalNode.get_dist() + Math.sqrt(bestGoalNode.get_point().dist_sq(end)))) {
                        bestGoalNode = v;
                        System.out.println("best goal updated");
                    } 
            }
            }

            nb_iter++;

        }

        System.out.println("Nombre d'itérations : "+nb_iter);
        if (bestGoalNode != null) {
            MyPath path = tree.path_to_start(bestGoalNode);
            if (!env.isColisionFree(bestGoalNode.get_point(), end)) {
                System.out.println("Attention: Le segment final traverse un obstacle !");
            }
            Collections.reverse(path.get_points()); 
            path.get_points().add(x_e);
            path.get_points().add(y_e);
            System.out.println("le chemin a une longueur initiale avant optimisation de "+path.path_length());
            path.tr_in_improve(env);
            System.out.println("le chemin a une longueur initiale après optimisation de "+path.path_length());
            return path;

        }
        return new MyPath();




}
}
