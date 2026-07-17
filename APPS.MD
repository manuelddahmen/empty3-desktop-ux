# Documentation des Applications

Ce fichier documente comment construire et empaqueter les applications de ce projet.

## Application: FaceDetect3

L'application `FaceDetect3` fournit une interface utilisateur pour la détection et la manipulation de points de repère sur des visages.

### Comment créer l'installeur Windows (.msi)

Un script `jpackage.bat` est fourni pour automatiser la création d'un installeur Windows natif.

#### Prérequis

1.  **JDK 21 ou supérieur** : Vous devez avoir un JDK (Java Development Kit) version 21 ou plus récent installé sur votre machine.
2.  **Variable d'environnement `JAVA_HOME`** : La variable d'environnement `JAVA_HOME` doit être configurée et pointer vers le répertoire d'installation de votre JDK.
    - *Exemple*: `C:\Program Files\Java\jdk-21`
3.  **WiX Toolset** (optionnel, mais recommandé) : `jpackage` utilise WiX Toolset en arrière-plan pour créer les installateurs MSI. Si `jpackage` ne le trouve pas, il vous demandera de le télécharger. Vous pouvez l'installer en amont depuis [https://wixtoolset.org/](https://wixtoolset.org/).

#### Étapes de création

1.  **Ouvrez une invite de commande** (Command Prompt, PowerShell, ou Terminal Windows) à la racine de ce projet.
2.  **Exécutez le script `jpackage.bat`** en tapant la commande suivante :
    ```bash
    .\jpackage.bat
    ```
3.  **Le script va effectuer les actions suivantes :**
    - Lancer une tâche Gradle (`prepareJPackageFiles`) pour compiler l'application et rassembler tous les fichiers JAR nécessaires dans le répertoire `build/app-image`.
    - Lancer `jpackage` pour prendre les fichiers préparés et créer un installeur `.msi`.

#### Résultat

À la fin du processus, vous trouverez l'installeur `FaceDetect3-1.0.msi` (ou une version similaire) dans le répertoire `build\jpackage`.

Vous pouvez ensuite exécuter cet installeur sur n'importe quelle machine Windows pour installer l'application.
