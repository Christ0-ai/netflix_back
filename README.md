
# Netflix API

## Technos

- Java 25
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Swagger / OpenAPI
- JUnit 5 & Mockito


## Fonctionnalités

- Consulter la liste des films
- Consulter le détail d’un film
- Gestion des données via PostgreSQL
- Documentation de l’API avec Swagger


---
# Prise en main

## Prérequis

Avant d'exécuter l'application, assurez-vous d'avoir :

- Docker installed and running
- IntelliJ IDEA (Community or Ultimate)
- Java 25
- Maven

L'application utilise une base de données PostgreSQL gérée via Docker Compose. Suivez les étapes ci-dessous pour configurer Docker, démarrer les conteneurs nécessaires et exécuter l'application en local.
## Configuration des variables d'environnement

Créez un fichier `.env` à la racine du projet et ajoutez les variables d'environnement requises :
```env

DATABASE_USERNAME=user  
DATABASE_PASSWORD=password  
DATABASE_URL=jdbc:postgresql://localhost:5432/moviesdb  
PGADMIN_DEFAULT_EMAIL=admin@admin.com  
PGADMIN_DEFAULT_PASSWORD=admin

```

## Configuration de Docker dans IntelliJ

1. Ouvrir **Settings** → **Build, Execution, Deployment** → **Docker**.
2. Cliquer sur **+** puis ajouter une connexion Docker :
   - Docker Desktop (Windows/Mac)
   - Unix Socket (`unix:///var/run/docker.sock`) for Linux
1. Cliquer sur **Test Connection** pour vérifier que Docker est accessible.


Si vous utilisez **Ubuntu/WSL**, vérifier que Docker est en cours d'exécution
```bash  
docker ps
```  

## Démarrage des conteneurs

Depuis la racine du projet :

```bash  
docker compose up -d
```  
## Arrêt des conteneurs

```bash  
docker compose down
```  

Arrêter et supprimer les conteneurs ainsi que les volumes associés :

```shell
docker compose down -v
```

## Accès à PgAdmin

Accéder à PgAdmin à l'adresse suivante :

http://localhost:5000

Créer un nouveau serveur PostgreSQL en utilisant les valeurs définies dans le fichier `.env`.

Exemple :

- Host : `postgres`
- Port : `5432`
- Database : `netflix`
- Username : `postgres`
- Password : `password`

## Migrations liquibase

Le projet utilise **Liquibase** pour créer automatiquement le schéma de la base de données et charger les données de démonstration.

Pour créer les tables et charger les données :
`./mvnw liquibase:update`

Afficher les migrations restantes à exécuter : `./mvnw liquibase:status`

*Réinitialiser la base de données (développement):*
```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```

Puis réappliquer les migrations : `./mvnw liquibase:update`