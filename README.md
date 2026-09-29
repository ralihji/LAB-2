# SEG3502 – Lab 2 : Calculatrice Spring MVC + Thymeleaf

Réimplémentation de la calculatrice du Lab 1 (Angular) sous forme d'application **Spring MVC** avec des vues **Thymeleaf**, écrite en **Kotlin** et construite avec **Gradle**.

**Équipe**

| Nom | Numéro étudiant | Rôle principal |
|---|---|---|
| Hjiyej Andaloussi Elghali | 300379897 | Logique (contrôleur, calculs) |
| Brayan Adou | 300433616 | Interface (vue Thymeleaf, CSS) |

## Fonctionnalités

- Deux champs de saisie (premier et second nombre)
- Quatre opérations : addition, soustraction, multiplication, division
- Le calcul est fait **côté serveur** par le contrôleur Kotlin ; le résultat est placé dans le `Model` puis affiché par Thymeleaf
- Messages d'erreur : division par zéro, champ vide, valeur non numérique, opération inconnue
- Même design que la calculatrice Angular du Lab 1 (écran LCD, touches en relief)

## Structure

```
src/main/kotlin/com/example/calculatrice/
├── CalculatriceApplication.kt   point d'entrée (@SpringBootApplication)
├── CalculatorController.kt      routes GET / et POST /calculer, logique des 4 opérations
├── CalculatorForm.kt            objet lié au formulaire (nombres, opération, résultat, erreur)
└── WebConfig.kt                 conversion texte <-> nombre pour les champs
src/main/resources/
├── templates/calculator.html    vue Thymeleaf
├── static/css/style.css         feuille de style
└── application.properties
src/test/kotlin/com/example/calculatrice/
└── CalculatorControllerTest.kt  tests MockMvc
```

## Prérequis

- **JDK 17 ou plus récent** (testé avec JDK 21). Vérifier avec `java -version`.
- Aucune installation de Gradle n'est nécessaire : le projet contient le *Gradle Wrapper* (`gradlew` / `gradlew.bat`), qui télécharge la bonne version au premier lancement (connexion Internet requise la première fois).

## Installer et exécuter

```bash
# 1. Cloner le dépôt
git clone https://github.com/ralihji/LAB-2.git
cd LAB-2

# 2. Lancer les tests
./gradlew test          # Windows : .\gradlew.bat test

# 3. Démarrer l'application
./gradlew bootRun       # Windows : .\gradlew.bat bootRun
```

Ouvrir ensuite **http://localhost:8080/** dans un navigateur.
La console reste bloquée vers « 80 % EXECUTING » : c'est normal, le serveur tourne. `Ctrl + C` pour l'arrêter.

**Avec IntelliJ IDEA** : *File → Open*, choisir le dossier du projet (IntelliJ détecte `build.gradle.kts`), attendre la synchronisation Gradle, puis lancer `CalculatriceApplication.kt` avec le bouton ▶.

## Fonctionnement (parcours d'une requête)

1. Le navigateur envoie le formulaire : `POST /calculer` avec `premierNombre`, `secondNombre` et `operation` (valeur du bouton cliqué : `+`, `-`, `*` ou `/`).
2. Spring remplit un objet `CalculatorForm` (`@ModelAttribute`) ; une saisie non numérique est captée dans le `BindingResult`.
3. `CalculatorController` calcule le résultat (ou détecte l'erreur) et ajoute `calculatorForm` et `affichage` au `Model`.
4. Le contrôleur retourne le nom de vue `calculator` ; Thymeleaf génère le HTML renvoyé au navigateur.

## Dépannage

- **Port 8080 déjà utilisé** : arrêter l'autre application, ou lancer avec `./gradlew bootRun --args='--server.port=8081'`.
- **`Permission denied` sur `./gradlew` (macOS/Linux)** : `chmod +x gradlew`.
- **Mauvaise version de Java** : `java -version` doit afficher 17 ou plus.
