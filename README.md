# Agent Management Application - Documentation

## Dépendances utilisées

- **Spring Web** : Framework web pour les APIs REST
- **Spring Data JPA** : Couche d'accès aux données
- **PostgreSQL Driver** : Driver pour la base de données PostgreSQL
- **Lombok** : Réduction du code boilerplate
- **Validation** : Validation des données d'entrée
- **Spring Boot DevTools** : Outils de développement

## Structure du Projet

```text
src/main/java/com/example/agents/
├── controller/
│   └── AgentController.java
├── model/
│   ├── Agent.java
│   ├── Role.java
│   ├── UserInfo.java
│   └── UserAddress.java
├── repository/
│   └── AgentRepository.java
├── service/
│   └── AgentService.java
├── util/
│   └── UserIdGenerator.java
└── AgentManagementApplication.java
```

## Diagramme de classes

![Diagramme de classes](images/classe.png)

- Un Agent peut avoir plusieurs rôles.
- Chaque Agent possède une seule fiche d’informations (UserInfo).
- Chaque Agent possède une seule adresse (UserAddress).

### Tests avec Postman

#### Collection des APIs testées

![Collection Postman](images/collections.png)

APIs REST - Endpoints :

| Méthode  | Endpoint                 | Description                            |
| -------- | ------------------------ | -------------------------------------- |
| `GET`    | `/api/v1/agents`         | Récupère la liste de tous les agents   |
| `POST`   | `/api/v1/agent`          | Ajoute un nouvel agent                 |
| `GET`    | `/api/v1/agent/{userId}` | Récupère un agent par son ID           |
| `PUT`    | `/api/v1/agent/{userId}` | Met à jour les informations d'un agent |
| `DELETE` | `/api/v1/agent/{userId}` | Supprime un agent                      |
| `GET`    | `/api/v1/agents/active`  | Récupère les agents actifs             |

#### Ajouter un nouvel agent

![Ajout agent](images/add.png)

#### Récupérer tous les agents

![Liste agents](images/getAll.png)

#### Récupérer un agent par ID

![Agent par ID](images/getAgent.png)

#### Mettre à jour un agent

![Mise à jour agent](images/update.png)

#### Supprimer un agent

![Suppression agent](images/delete.png)

#### Récupérer les agents actifs

![Agents actifs](images/getActive.png)

### Base de données PostgreSQL

#### Table des agents

![Base de données agents](images/agentdb.png)

#### Table des rôles

![Base de données rôles](images/roledb.png)

#### Table des adresses utilisateurs

![Base de données adresses utilisateurs](images/user_adressdb.png)

#### Table des informations utilisateurs

![Base de données informations utilisateurs](images/user_infodb.png)
