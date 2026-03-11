package com.feron.PSO;

import java.util.ArrayList;

import com.feron.Visualisation.*;

public class PSOAlgorithm {
    public static int S=200;
    public static double w=0.9;
    public static double c1=1.8;
    public static double c2=1.0;

    public static double alpha=0.25;

    public static double beta=0.99;
    public static double T0=100;

    public static double noise_amplitude=300;

    public static int max_iter=10000;
    public static int max_iter_wo_imp=150;

    public static int max_iter_local=50;


    //Initialisation intelligente
    public static ArrayList<Particle> init_heuristic(Environnement env,double x_s,double y_s,double x_e,double y_e){
        ArrayList<Particle> particles=new ArrayList<Particle>(S);
        for (int i=0;i<S;++i){
            Particle curr=new Particle (x_s,y_s,x_e,y_e);
            ArrayList<Double> velo=new ArrayList<Double>(2*Particle.intermedaire_steps);
            for (int k=0;k<2*Particle.intermedaire_steps;++k){
                velo.add(0.0);
            }
            curr.set_velocity(velo);
            for (int k = 0; k < Particle.intermedaire_steps; k++) {
                
                double t = (double) (k + 1) / (Particle.intermedaire_steps + 1);

                double ideal_x = x_s + t * (x_e - x_s);
                double ideal_y = y_s + t * (y_e - y_s);

                double noise_x = (Math.random() - 0.5) * 2 * noise_amplitude;
                double noise_y = (Math.random() - 0.5) * 2 * noise_amplitude;

                double final_x = ideal_x + noise_x;
                double final_y = ideal_y + noise_y;

                final_x = Math.max(0, Math.min(env.get_width(), final_x));
                final_y = Math.max(0, Math.min(env.get_height(), final_y));

                curr.get_position().add(final_x);
                curr.get_position().add(final_y);
                }
            particles.add(curr);
        }
        return particles;
    }


    public static ArrayList<Particle> init_config(Environnement env,double x_s,double y_s,double x_e,double y_e){
        ArrayList<Particle> particles=new ArrayList<Particle>(S);
        for (int i=0;i<S;++i){
            Particle curr=new Particle (x_s,y_s,x_e,y_e);
            ArrayList<Double> velo=new ArrayList<Double>(2*Particle.intermedaire_steps);
            for (int k=0;k<2*Particle.intermedaire_steps;++k){
                velo.add(0.0);
            }
            curr.set_velocity(velo);
            for (int j=0;j<Particle.intermedaire_steps;j++){
                double x=Math.random()*env.get_width();
                double y=Math.random()*env.get_height();
                curr.get_position().add(x);
                curr.get_position().add(y);
            }
            particles.add(curr);
        }
        return particles;
    }


    public static MyPath basic_pso(Environnement env,double x_s,double y_s,double x_e,double y_e){
        ArrayList<Particle> particles=init_heuristic(env, x_s, y_s, x_e, y_e);
        
        double[] iter_last_up=new double[S];
        
        ArrayList<Particle> best_local_particles=new ArrayList<Particle>();
        ArrayList<Double> best_local_part_len=new ArrayList<Double>(particles.size());
        
        int ind_best=0;
        double min_length=Double.POSITIVE_INFINITY;

        for (int i=0;i<S;++i){      // Initialisation d'un tableau contenant à chaque instant la meilleure itération de chaque particule.
            double part_l= FitnessFunction.fitness_function(particles.get(i),env);
            best_local_part_len.add(part_l);

            Particle copy=new Particle(x_s,y_s,x_e,y_e);

            copy.set_position(new ArrayList<>(particles.get(i).get_position()));
            copy.set_velocity(new ArrayList<>(particles.get(i).get_velocity()));
            best_local_particles.add(copy);
            if (part_l<min_length){
                min_length=part_l;
                ind_best=i;
            }
        }
        Particle best_particle=particles.get(ind_best);

        int nb_iteration=0;
        int nb_iter_wo_imp=0;
        
        double vmax=Math.min(env.get_height(),env.get_width())*alpha;
        

        while(nb_iteration<max_iter){
            double current_min_l=min_length;
            for (int i=0;i<S;++i){
                Particle best_local_particle=best_local_particles.get(i);
                
                Particle current_part=particles.get(i);
                ArrayList<Double> new_position=new ArrayList<Double>();
                ArrayList<Double> new_velocity=new ArrayList<Double>();
                
                Particle new_particle=new Particle(current_part.get_x_s(),current_part.get_y_s(),current_part.get_x_e(),current_part.get_y_e());
                
                for (int j=0;j<2*Particle.intermedaire_steps;++j){
                    double r1=Math.random();
                    double r2=Math.random();
                    
                    double v_i_j=w*current_part.get_velocity().get(j)+
                    c1*r1*(best_local_particle.get_position().get(j)-current_part.get_position().get(j))+
                    c2*r2*(best_particle.get_position().get(j)-current_part.get_position().get(j));

                    v_i_j = Math.max(-vmax, Math.min(vmax, v_i_j));
                    new_velocity.add(v_i_j);
                    double updated_pos = v_i_j + current_part.get_position().get(j);
                    
                    if (j % 2 == 0) { 
                        updated_pos = Math.max(0, Math.min(env.get_width(), updated_pos));
                    } else { 
                        updated_pos = Math.max(0, Math.min(env.get_height(), updated_pos));
                    }
                    new_position.add(updated_pos);
                }

                new_particle.set_position(new_position);
                new_particle.set_velocity(new_velocity);
                
                double new_len=FitnessFunction.fitness_function(new_particle,env);
                
                // HEURISTIQUE QUESTION 10
                double T=T0*Math.pow(beta,nb_iteration);
                if (T>0.000){
                    double p=Math.min(1,Math.exp(-(new_len-min_length)/T));
                    double r=Math.random();
                    if (r<p){
                        best_particle=new_particle;
                        min_length=new_len;
                        //System.out.println("changement de best solution");
                    }
                }

                if (new_len<best_local_part_len.get(i)){
                    best_local_part_len.set(i,new_len);
                    best_local_particles.get(i).set_position(new ArrayList<>(new_particle.get_position()));
                    best_local_particles.get(i).set_velocity(new ArrayList<>(new_particle.get_velocity()));
                    iter_last_up[i]=0;
                }else {
                    iter_last_up[i]++;
                }
                //FIN DE l'HEURISTIQUE

                //HEURISTIQUE QUESTION 11 
                // if (iter_last_up[i]>max_iter_local){
                //     ArrayList<Double> pi = new ArrayList<>(best_local_particles.get(i).get_position());
                //     double currentBestlen = best_local_part_len.get(i);

                //     for (int j = 0; j < 2 * Particle.intermedaire_steps; j++) {
                //         double originalValue = pi.get(j);
                        
                //         pi.set(j, best_particle.get_position().get(j));
                        
                //         Particle testParticle = new Particle(x_s, y_s, x_e, y_e);
                //         testParticle.set_position(pi);
                //         double testlen = FitnessFunction.fitness_function(testParticle, env);
                        
                //         if (testlen < currentBestlen) {
                //             currentBestlen = testlen;
                //             best_local_part_len.set(i, testlen);
                //             best_local_particles.get(i).set_position(new ArrayList<>(pi));
                //         } else {
                //             pi.set(j, originalValue);
                //         }
                //     }
                // } //Fin de l'heuristique


                if(new_len<min_length){
                    best_particle=new_particle;
                    min_length=new_len;
                }

                particles.set(i,new_particle);
            }

            nb_iteration++;

            if (min_length<current_min_l){
                nb_iter_wo_imp=0;
            }else{
                nb_iter_wo_imp++;
            }
            // Le bloc suivant est à décommenter si l'on ne veut pas utiliser d'heuristique du tout.
            /*
            if (nb_iter_wo_imp>max_iter_wo_imp){
                break;                              
            }
                */
            //HEURISTIQUE QUESTION 9
            if (nb_iter_wo_imp>max_iter_wo_imp){
                //System.out.println("Stagnation détectée à l'itération " + nb_iteration + ". Restart partiel...");
        
                for (int i = S/2; i < S; i++) {
                    Particle curr=new Particle (x_s,y_s,x_e,y_e);
                    ArrayList<Double> velo=new ArrayList<Double>(2*Particle.intermedaire_steps);
                    for (int k=0;k<2*Particle.intermedaire_steps;++k){
                        velo.add(0.0);
                    }
                    curr.set_velocity(velo);
                    for (int j=0;j<Particle.intermedaire_steps;j++){
                    double x=Math.random()*env.get_width();
                    double y=Math.random()*env.get_height();
                    curr.get_position().add(x);
                    curr.get_position().add(y);
                    }
                    particles.set(i,curr);
        
                    best_local_part_len.set(i, Double.POSITIVE_INFINITY);
                }
                nb_iter_wo_imp=0;
            } //FIN DE L'HEURISTIQUE

        }
        System.out.println("l'algorithme a effectué "+nb_iteration+" étapes");
        System.out.println("La longueur du chemin calculé est "+min_length);
        return best_particle.particle_to_path();
    }

}
