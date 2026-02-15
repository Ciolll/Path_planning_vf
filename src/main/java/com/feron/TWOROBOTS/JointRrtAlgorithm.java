package com.feron.TWOROBOTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.feron.Visualisation.*;

public class JointRrtAlgorithm {

    public static double delta_s = 30;
    public static double delta_r = 100;
    public static double delta_e = 70;

    public static int nb_iter_max = 10000;

    public static Node4D init_rand_point_4d(Environnement env, Node4D end){
        if (Math.random() < 0.15) {
            return new Node4D(end.get_x1(), end.get_y1(), end.get_x2(), end.get_y2());
        }
        
        double x1 = Math.random() * env.get_width();
        double y1 = Math.random() * env.get_height();
        double x2 = Math.random() * env.get_width();
        double y2 = Math.random() * env.get_height();
        return new Node4D(x1, y1, x2, y2);
    }

    public static List<MyPath> rrtJoint(Environnement env){
        Robot r1 = env.get_robots().get(0);
        Robot r2 = env.get_robots().get(1);
        double R_safe = r1.get_R();

        double x1_s = r1.get_x_center(), y1_s = r1.get_y_center();
        double x2_s = r2.get_x_center(), y2_s = r2.get_y_center();
        double x1_e = r1.get_x_obj(), y1_e = r1.get_y_obj();
        double x2_e = r2.get_x_obj(), y2_e = r2.get_y_obj();

        List<Node4D> treeList = new ArrayList<>();
        treeList.add(new Node4D(x1_s, y1_s, x2_s, y2_s));

        int nb_iter = 0;
        Node4D bestGoalNode = null;
        Node4D end = new Node4D(x1_e, y1_e, x2_e, y2_e);

        while (nb_iter < nb_iter_max){
            Node4D v_r = init_rand_point_4d(env,end);
                    
            Node4D v_n = findNearest(treeList, v_r);
            
            Node4D v = steer(v_n, v_r,end);

            if (isValid4D(env, v_n, v, R_safe,end)){
                
                v.set_parent(v_n);
            
                List<Node4D> neighbours = neighbour_in_radius(treeList, v, delta_r);
                
                for (Node4D n : neighbours){
                    if (n.get_dist() + Math.sqrt(v.dist_sq(n)) < v.get_dist() && isValid4D(env, n, v, R_safe,end)){
                        v.set_parent(n);
                    }
                }
                
                treeList.add(v);
                
                for (Node4D n : neighbours){
                    double diff = v.get_dist() + Math.sqrt(v.dist_sq(n)) - n.get_dist();
                    if (diff < 0 && isValid4D(env, v, n, R_safe,end)){
                        n.set_parent(v);
                        n.propagatecost();
                    }
                }
                
                double d1 = Math.hypot(end.get_x1() - v.get_x1(), end.get_y1() - v.get_y1());
                double d2 = Math.hypot(end.get_x2() - v.get_x2(), end.get_y2() - v.get_y2());

                // On vérifie que les DEUX robots sont individuellement dans leur zone d'arrivée
                if (d1 <= delta_e && d2 <= delta_e && isValid4D(env, v, end, R_safe, end)){
                    double currentTotalCost = v.get_dist() + Math.sqrt(v.dist_sq(end));
                if (bestGoalNode == null || currentTotalCost < (bestGoalNode.get_dist() + Math.sqrt(bestGoalNode.dist_sq(end)))) {
                    bestGoalNode = v;
                    System.out.println("best goal updated (Joint RRT*)");
                } 
                }
            }
            nb_iter++;
        }

        System.out.println("Nombre d'itérations : " + nb_iter);
        if (bestGoalNode != null) {
            return path_to_start_4d(bestGoalNode, end);
        }
        
        List<MyPath> fail = new ArrayList<>();
        fail.add(new MyPath()); fail.add(new MyPath());
        System.out.println("fail");
        return fail;
    }





    private static Node4D findNearest(List<Node4D> treeList, Node4D target) {
        Node4D best = treeList.get(0);
        double minDist = target.dist_sq(best);
        for (Node4D node : treeList) {
            double d = target.dist_sq(node);
            if (d < minDist) {
                minDist = d;
                best = node;
            }
        }
        return best;
    }

    private static List<Node4D> neighbour_in_radius(List<Node4D> treeList, Node4D target, double radius) {
        List<Node4D> neighbours = new ArrayList<>();
        double radiusSq = radius * radius;
        for (Node4D node : treeList) {
            if (target.dist_sq(node) < radiusSq) {
                neighbours.add(node);
            }
        }
        return neighbours;
    }

//place le noeud v_r
    private static Node4D steer(Node4D v_n, Node4D v_r, Node4D end) {
        double dist1 = Math.hypot(v_r.get_x1() - v_n.get_x1(), v_r.get_y1() - v_n.get_y1());
        double dist2 = Math.hypot(v_r.get_x2() - v_n.get_x2(), v_r.get_y2() - v_n.get_y2());

        double distToGoal1 = Math.hypot(end.get_x1() - v_n.get_x1(), end.get_y1() - v_n.get_y1());
        double distToGoal2 = Math.hypot(end.get_x2() - v_n.get_x2(), end.get_y2() - v_n.get_y2());

        double x1_new = v_n.get_x1(), y1_new = v_n.get_y1();
        double x2_new = v_n.get_x2(), y2_new = v_n.get_y2();

        if (distToGoal1 > delta_e) {
            if (dist1 > 0) { 
                x1_new = v_n.get_x1() + (delta_s / dist1) * (v_r.get_x1() - v_n.get_x1());
                y1_new = v_n.get_y1() + (delta_s / dist1) * (v_r.get_y1() - v_n.get_y1());
            }
        }
        
        if (distToGoal2 > delta_e) {
            if (dist2 > 0) {
                x2_new = v_n.get_x2() + (delta_s / dist2) * (v_r.get_x2() - v_n.get_x2());
                y2_new = v_n.get_y2() + (delta_s / dist2) * (v_r.get_y2() - v_n.get_y2());
            }
        }

        return new Node4D(x1_new, y1_new, x2_new, y2_new);
    }

    // vérifie si le chemin entre v et v_n a un obstacle et si les deux points tirés ne sont pas trop proches. 
    private static boolean isValid4D(Environnement env, Node4D from, Node4D to, double R_safe, Node4D end) {
        Point p1_from = new Point(from.get_x1(), from.get_y1());
        Point p1_to = new Point(to.get_x1(), to.get_y1());
        Point p2_from = new Point(from.get_x2(), from.get_y2());
        Point p2_to = new Point(to.get_x2(), to.get_y2());

        if (!env.isColisionFree(p1_from, p1_to)) return false;
        if (!env.isColisionFree(p2_from, p2_to)) return false;

        int steps = 10;
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double cx1 = from.get_x1() + t * (to.get_x1() - from.get_x1());
            double cy1 = from.get_y1() + t * (to.get_y1() - from.get_y1());
            double cx2 = from.get_x2() + t * (to.get_x2() - from.get_x2());
            double cy2 = from.get_y2() + t * (to.get_y2() - from.get_y2());

            boolean r1_is_parked = Math.hypot(cx1 - end.get_x1(), cy1 - end.get_y1()) <= delta_e;
            boolean r2_is_parked = Math.hypot(cx2 - end.get_x2(), cy2 - end.get_y2()) <= delta_e;

            // Si AUCUN des deux n'est garé, on applique la stricte règle de distance
            if (!r1_is_parked && !r2_is_parked) {
                if (Math.hypot(cx1 - cx2, cy1 - cy2) <= R_safe) {
                    return false;
                }
            }
        }
        return true;
    }

    
//recrée le chemin à partir de l'arbre construit.
    private static List<MyPath> path_to_start_4d(Node4D node, Node4D end) {
        List<Node4D> rawPath = new ArrayList<>();
        Node4D current = node;
        while (current != null) {
            rawPath.add(current);
            current = current.get_parent();
        }
        Collections.reverse(rawPath);

        MyPath path1 = new MyPath();
        MyPath path2 = new MyPath();

        for (Node4D n : rawPath) {
            path1.get_points().add(n.get_x1());
            path1.get_points().add(n.get_y1());
            
            path2.get_points().add(n.get_x2());
            path2.get_points().add(n.get_y2());
        }

        path1.get_points().add(end.get_x1()); path1.get_points().add(end.get_y1());
        path2.get_points().add(end.get_x2()); path2.get_points().add(end.get_y2());

        List<MyPath> res = new ArrayList<>();
        res.add(path1);
        res.add(path2);
        return res;
    }
}