package com.feron.PSO;

import java.util.ArrayList;
import com.feron.Visualisation.*;

public class FitnessFunction {

    public static int check_collision(Obstacle obstacle, MyPath path) {
        ArrayList<Double> points = path.get_points();
        int N = points.size();
        int nb_cross = 0;

        double obs_x_min = obstacle.get_x();
        double obs_x_max = obstacle.get_x() + obstacle.get_width();
        double obs_y_min = obstacle.get_y();
        double obs_y_max = obstacle.get_y() + obstacle.get_height();

        for (int i = 0; i < N - 3; i += 2) {
            double x1 = points.get(i);
            double y1 = points.get(i + 1);
            double x2 = points.get(i + 2);
            double y2 = points.get(i + 3);

            boolean p1_inside = (x1 >= obs_x_min && x1 <= obs_x_max && y1 >= obs_y_min && y1 <= obs_y_max);
            boolean p2_inside = (x2 >= obs_x_min && x2 <= obs_x_max && y2 >= obs_y_min && y2 <= obs_y_max);

            if (p1_inside || p2_inside) {
                nb_cross++;
                continue; 
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

            if (intersect && t_min <= t_max) {
                nb_cross++;
            }
        }
        return nb_cross;
    }

    public static int check_collisions(ArrayList<Obstacle> obstacles, MyPath path) {
        if (obstacles == null) {
            return 0;
        }
        int nb = 0;
        for (Obstacle o : obstacles) {
            nb += check_collision(o, path);
        }
        return nb;
    }

    public static double fitness_function(Particle particle, Environnement env) {
        MyPath path = particle.particle_to_path();
        double length = path.path_length();
        return length + check_collisions(env.get_obstacles(), path) * 20000.0;
    }
}