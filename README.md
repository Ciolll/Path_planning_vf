#  [Algorithms for Path planning]


## Fonctionnalités

* **Visualisation graphique :** Affichage de l'environnement, des robots et des obstacles en temps réel.
* **Chargement de scénarios :** Lecture de fichiers de configuration (.txt) définissant la taille du terrain et les positions.
* **Algorithmique :** Implémentation d'un algorithme PSO (Particle Swarm Optimization) et RRT ainsi que de plusieurs heuristiques.
* **Multi-Robots :** Gestion de 2 robots simultanés.

## Prérequis

Pour lancer ce projet, vous avez besoin de :

* **JDK 21** (ou version supérieure).
* **Maven** (3.8+).
* Une connexion internet (pour le premier téléchargement des dépendances JavaFX).

*Note : Grâce à Maven, vous n'avez pas besoin d'installer JavaFX manuellement.*

## Installation et Lancement

Ce projet est configuré pour être lancé en une seule ligne de commande, quel que soit votre système d'exploitation (Windows, Linux, macOS).

1.  **Cloner le dépôt :**
    ```bash
    git clone [https://github.com/](https://github.com/)[TonPseudo]/[NomDuRepo].git
    cd [NomDuRepo]
    ```

2.  **Lancer l'application :**
    ```bash
    mvn clean javafx:run
    ```

## Structure du Projet

L'architecture respecte le standard Maven :

```text
├── src/
│   ├── main/
│   │   ├── java/com/feron/       # Code source (Logique & UI)
│   │   └── resources/com/feron/  # Vues FXML, CSS, et Scénarios de test
├── target/                       # Dossier de compilation (généré par Maven)
└── pom.xml                       # Configuration des dépendances
