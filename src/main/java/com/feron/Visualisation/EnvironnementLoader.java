package com.feron.Visualisation;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class EnvironnementLoader {


    public static Environnement load_test(String nomFichier) {
        

        String cheminRessource = "/com/feron/tests/" + nomFichier;

        InputStream is = EnvironnementLoader.class.getResourceAsStream(cheminRessource);

        if (is == null) {
            System.err.println("ERREUR FATALE : Le fichier est introuvable dans les ressources !");
            System.err.println("Chemin cherché : " + cheminRessource);
            System.err.println("Vérifie que le fichier est bien dans 'src/main/resources/com/feron/tests/'");
            return null;
        }

        try (Scanner scanner = new Scanner(is)) {
            scanner.useLocale(Locale.US); 
            
            if (!scanner.hasNextDouble()) {
                System.err.println("Erreur format : Largeur (Ligne 1) manquante."); return null;
            }
            double width = scanner.nextDouble();
            
            if (!scanner.hasNextDouble()) {
                System.err.println("Erreur format : Hauteur (Ligne 2) manquante."); return null;
            }
            double height = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("US1_X (Ligne 3)");
            double us1_x = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("US1_Y (Ligne 4)");
            double us1_y = scanner.nextDouble();           

            if (!scanner.hasNextDouble()) return erreurLecture("UD1_X (Ligne 5)");
            double ud1_x = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("UD1_Y (Ligne 6)");
            double ud1_y = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("US2_X (Ligne 7)");
            double us2_x = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("US2_Y (Ligne 8)");
            double us2_y = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("UD2_X (Ligne 9)");
            double ud2_x = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("UD2_Y (Ligne 10)");
            double ud2_y = scanner.nextDouble();

            if (!scanner.hasNextDouble()) return erreurLecture("Rayon R (Ligne 11)");
            double R = scanner.nextDouble();

            Environnement env = new Environnement(width, height);
            
            Robot r1 = new Robot(us1_x, us1_y, ud1_x, ud1_y);
            Robot r2 = new Robot(us2_x, us2_y, ud2_x, ud2_y);
            
            r1.set_R(R);
            r2.set_R(R);
            
            env.get_robots().add(r1);
            env.get_robots().add(r2);

            while (scanner.hasNext()) { 
                if (scanner.hasNextDouble()) {
                    double x = scanner.nextDouble();
                    double y = scanner.nextDouble();
                    double lx = scanner.nextDouble();
                    double ly = scanner.nextDouble();
                    env.add_obstacle(new Obstacle(x, y, lx, ly));
                } else {
                    scanner.next(); 
                }
            }

            return env;
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la lecture du fichier : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static Environnement erreurLecture(String champ) {
        System.err.println("Impossible de lire le champ : " + champ);
        return null;
    }
}