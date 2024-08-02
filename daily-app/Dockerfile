# Utiliser une image de base Java
FROM openjdk:17-jdk-slim

# Installer Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le reste des fichiers du projet
COPY src ./src

# Compiler le projet
RUN mvn clean package -DskipTests

# Exposer le port de l'application
EXPOSE 5000

# Démarrer l'application
CMD ["java", "-jar", "target/daily-app-0.0.1-SNAPSHOT.jar"]