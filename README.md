# Application de Gestion des Utilisateurs

Cette application permet de gérer les utilisateurs d'une manière sécurisée, avec des fonctionnalités modernes telles que l'inscription avec confirmation par email, la connexion sécurisée via JWT, la réinitialisation du mot de passe, et bien plus.

## Fonctionnalités
- **Inscription avec confirmation par email**
- **Connexion sécurisée avec JWT**
- **Réinitialisation de mot de passe via email**
- **Mise à jour du mot de passe actuel**
- **Désactivation du compte utilisateur**
- **API sécurisée avec tests**
- **Interface utilisateur avec Thymeleaf**


## Technologies
- **Backend** : Spring Boot 3, Spring Security, Spring Data JPA
- **Frontend** : Thymeleaf, HTML, CSS
- **Base de données** :  MySQL/PostgreSQL
- **Sécurité** : JWT (JSON Web Token), Spring Security
- **Tests** : JUnit 5, Mockito
- **Email** : Spring Mail
- **Environnement** : Java 17, Gradle
## Installation

### Prérequis
- Java 17 ou supérieur
- Maven ou Gradle pour la gestion des dépendances
- Base de données configurée (H2 pour le développement, MySQL/PostgreSQL pour la production)


## Fonctionnalités

### 1. Inscription et Confirmation par Email
Lorsqu'un utilisateur s'inscrit, un email avec un lien de confirmation est envoyé. Ce lien contient un token unique qui expire après un certain délai. Le processus est implémenté dans `UserRegistrationService`.

### 2. Connexion sécurisée avec JWT
L'utilisateur peut se connecter en envoyant un `POST` à `/login` avec ses informations d'identification. En cas de succès, un token JWT est généré et retourné à l'utilisateur. Ce token doit être utilisé pour toutes les requêtes API suivantes. Le mécanisme de génération du token se trouve dans `JWTTokenProvider`.

### 3. Réinitialisation du Mot de Passe
L'utilisateur peut réinitialiser son mot de passe en demandant un email avec un lien de réinitialisation. Le lien contient un token de réinitialisation. Ce processus est géré par `PasswordResetService`.

### 4. Mise à Jour du Mot de Passe
Après la réinitialisation du mot de passe, l'utilisateur peut mettre à jour son mot de passe via une API sécurisée en envoyant un `PUT` à `/user/update-password`.

### 5. Désactivation du Compte
Un utilisateur peut désactiver son compte via une requête API. Cette fonctionnalité est gérée dans `UserService`.

## Architecture
L'application suit une architecture de type **MVC** avec une séparation claire entre le frontend (Thymeleaf) et le backend (Spring Boot).

Le schéma suivant montre l'architecture globale de l'application :
```plaintext
+----------------+          +------------------+
|  Utilisateur   |  <-->    |  Frontend (UI)   |
+----------------+          +------------------+
         |                         |
+----------------+          +------------------+
| Backend (API)  |  <-->    | Base de données  |
+----------------+          +------------------+

```


## Contribution

Si vous souhaitez contribuer à ce projet, veuillez suivre ces étapes :
1. Fork ce repository
2. Créez une nouvelle branche pour votre fonctionnalité (`git checkout -b feature/ma-fonctionnalité`)
3. Committez vos modifications (`git commit -am 'Ajoute ma fonctionnalité'`)
4. Poussez votre branche (`git push origin feature/ma-fonctionnalité`)
5. Créez une Pull Request

## Support

Si vous avez des questions ou rencontrez des problèmes, veuillez ouvrir un problème sur GitHub ou contacter l'auteur du projet à `samuelangan77@gmail.com`.



