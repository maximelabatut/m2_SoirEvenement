# Déploiement
Voici un guide pas-à-pas pour le déploiement de l'application.
## Informations
* Maven est requis pour pouvoir build le projet.
* Docker est requis pour pouvoir lancer les micro-services dans des conteneurs.

## Build
Rendez-vous dans le répertoire `projet-microservices-docker` et exécutez la commande `mvn clean
package -Dmaven.test.skip=true` afin de générer les fichiers jar des différents micro-services.

```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] projetMicroServices 1.0-SNAPSHOT ................... SUCCESS [  0.378 s]
[INFO] gateway 1.0-SNAPSHOT ............................... SUCCESS [  5.625 s]
[INFO] soiree 1.0-SNAPSHOT ................................ SUCCESS [  1.974 s]
[INFO] utilisateur 1.0-SNAPSHOT ........................... SUCCESS [  1.848 s]
[INFO] evenement 1.0-SNAPSHOT ............................. SUCCESS [  1.012 s]
[INFO] authentification 1.0-SNAPSHOT ...................... SUCCESS [  1.028 s]
[INFO] evenementOpenAgenda 1.0-SNAPSHOT ................... SUCCESS [  0.981 s]
[INFO] eureka-server 1.0.0-SNAPSHOT ....................... SUCCESS [  0.508 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  14.180 s
[INFO] Finished at: 2019-03-30T18:10:56+01:00
[INFO] ------------------------------------------------------------------------
```

## Lancer les conteneurs Docker

Tout d'abord vous devez build les images. Pour cela, exécutez la commande `docker-compose build`. Ainsi les images seront et installées.

Un exemple d'image correctement built :
```
Building eurekaserver
Step 1/5 : FROM java:8-jre
 ---> e44d62cf8862
Step 2/5 : ENV PORT=8761
 ---> Using cache
 ---> 928840405912
Step 3/5 : ADD target/eurekaServer.jar eurekaServer.jar
 ---> 17857f1aa86b
Step 4/5 : ENTRYPOINT ["java", "-jar", "/eurekaServer.jar"]
 ---> Running in a48723a15baf
Removing intermediate container a48723a15baf
 ---> ae94258a0d3a
Step 5/5 : EXPOSE 8761
 ---> Running in 1c4559de40be
Removing intermediate container 1c4559de40be
 ---> 967a7c130f61
Successfully built 967a7c130f61
Successfully tagged projet-microservices_eurekaserver:latest
```

Une fois l'installation terminée, vous pouvez vérifier que les images sont correctement installées avec la commande `docker images`.

```
REPOSITORY                                  TAG                 IMAGE ID            CREATED             SIZE
projet-microservices_evenement              latest              53971128f6fe        4 seconds ago       375MB
projet-microservices_evenement_openagenda   latest              53971128f6fe        4 seconds ago       375MB
projet-microservices_authentification       latest              4c254734a7b6        7 seconds ago       364MB
projet-microservices_utilisateur            latest              1d8cd1062421        10 seconds ago      375MB
projet-microservices_gateway                latest              78232ca1737a        14 seconds ago      357MB
projet-microservices_eurekaserver           latest              3af78c940b1d        17 seconds ago      356MB
mysql                                       5.7                 98455b9624a9        3 days ago          372MB
java                                        8-jre               e44d62cf8862        2 years ago         311MB
```

Vous pouvez maintenant démarrer les conteneurs avec la commande `docker-compose up -d`. Le paramètre `-d` signifie que ceux-ci s'exécutent en tâche de fond.

```
Creating network "projet-microservices_default" with the default driver
Creating utilisateur_db          ... done
Creating evenement_db            ... done
Creating soiree_db               ... done
Creating evenement_openagenda_db ... done
Creating authentification_db     ... done
Creating eurekaserver            ... done
Creating gateway                 ... done
Creating utilisateur             ... done
Creating evenement               ... done
Creating soiree                  ... done
Creating evenement_openagenda    ... done
Creating authentification        ... done
```

La commande `docker ps -a` permet d'afficher tous les conteneurs (en cours ou terminés) ce qui peut être intéressant.

Si vous souhaitez accéder aux logs d'un conteneur en particulier, vous pouvez utiliser la commande `docker logs [nom-du-conteneur]`. Les noms des conteneurs peuvent être retrouvés grâce à la commandé précédemment citée : `docker ps -a`

Vous pouvez accéder à :
* L'application : http://localhost:8080/ui/
* Le panneau de contrôle Eureka : http://localhost:8761/

Vous pouvez arrêter tous les conteneurs avec la commande `docker-compose down` :
```
Stopping authentification        ... done
Stopping evenement               ... done
Stopping utilisateur             ... done
Stopping soiree                  ... done
Stopping gateway                 ... done
Stopping evenement_db            ... done
Stopping eurekaserver            ... done
Stopping soiree_db               ... done
Stopping authentification_db     ... done
Stopping utilisateur_db          ... done
Stopping evenement_openagenda_db ... done
Removing evenement_openagenda    ... done
Removing authentification        ... done
Removing evenement               ... done
Removing utilisateur             ... done
Removing soiree                  ... done
Removing gateway                 ... done
Removing evenement_db            ... done
Removing eurekaserver            ... done
Removing soiree_db               ... done
Removing authentification_db     ... done
Removing utilisateur_db          ... done
Removing evenement_openagenda_db ... done
Removing network projet-microservices_default
```