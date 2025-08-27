Structure du Projet :
src/main/java/com/example/agents/
├── controller/
│ └── AgentController.java
├── model/
│ ├── Agent.java
│ ├── Role.java
│ ├── UserInfo.java
│ └── UserAddress.java
├── repository/
│ └── AgentRepository.java
├── service/
│ └── AgentService.java
├── util/
│ └── UserIdGenerator.java
└── AgentManagementApplication.java

Les dépendances qui sont utilisé :
Spring Web
Spring Data JPA
PostgreSQL Driver
Lombok
Validation
Spring Boot DevTools

Diagramme de class
![diagrame de class](images/classe.png)

Capture Postman
collection of tested api
![imagee](images/collections.png)

Ajoute un nouvel agent
![imagee](images/add.png)
Récupérer tous les agents

![imagee](images/getAll.png)

Récupérer un agent par ID
![imagee](images/getAgent.png)
met a jour un agent
![imagee](images/update.png)
suprimer un agent
![imagee](images/delete.png)
Récupère les agents actifs
![imagee](images/getActive.png)

Capture Postgres
base de donne de agent
![imagee](images/agentdb.png)

exemple role
![imagee](images/roledb.png)
