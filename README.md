# Authentication-Service-JWT
Java, Spring [Boot, Security, Data, Web], JWT, Rest Repositories , MySQL

Objectif : Implémentation d’un service d’authentification Stateless, basée sur Spring Security et JWT (JSON Web Token), pour les API REST.

On gère le problème de révocation des tokens, avec l'utilisation de deux tokens : access-token (AT) et refresh-token (RT).

La solution consiste à attribuer un timeout court pour l’AT afin d'obliger le client d’interroger le serveur, pour vérifier s’il y a des mises à jour dans le contexte de l’application.

L'utilisation d'un RT (de timeout long), sert à renouveler l’AT, consiste à ne pas demander à chaque fois aux utilisateurs de s'authentifier lorsque l'AT est expiré. Dans ce cas, il faut envoyer une requête GET : http://localhost:8080/refreshToken, avec un RT dans le header, afin de récupérer en réponse de la requête un AT renouveler.

Pour signer les JWT et la vérification de la signature, on utilise une clé symétrique avec l’algorithme HMAC256.

L’autorisation d’accès aux différentes ressources est gérée selon le rôle des utilisateurs. 
  
