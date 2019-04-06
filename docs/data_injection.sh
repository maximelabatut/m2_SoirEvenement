#!/bin/sh

API="http://localhost:8080";
JSON="Content-Type:application/json";

printf "\n--  INSCRIPTIONS  --\n";
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"bob","password":"Bob123"}' -X POST $API/authentification/inscription;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"bob"}' -X POST $API/utilisateur/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"alice","password":"Alice123"}' -X POST $API/authentification/inscription;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"alice"}' -X POST $API/utilisateur/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"charlie","password":"Charlie123"}' -X POST $API/authentification/inscription;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"charlie"}' -X POST $API/utilisateur/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"daniel","password":"Daniel123"}' -X POST $API/authentification/inscription;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"daniel"}' -X POST $API/utilisateur/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"emilie","password":"Emilie123"}' -X POST $API/authentification/inscription;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"emilie"}' -X POST $API/utilisateur/;
sleep 1

printf "\n--  CONNEXIONS  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"charlie","password":"Charlie123"}' -X POST $API/authentification/connexion;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"daniel","password":"Daniel123"}' -X POST $API/authentification/connexion;
sleep 1
$curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo":"emilie","password":"Emilie123"}' -X POST $API/authentification/connexion;

printf "\n--  MODIFICATION D'UTILISATEURS  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo": "bob","nom": "Martin","prenom": "Bob","adresse": "123 rue des trucs","codePostal": "45000","email": "bob.martin@gmail.com","dateNaiss": "1980-01-01"}' --header "Authorization: 123" -X PUT $API/utilisateur/1;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo": "alice","nom": "Nevers","prenom": "Alice","adresse": "54 rue des bidules","codePostal": "45160","email": "alice.nevers@gmail.com","dateNaiss": "1980-03-11"}' --header "Authorization: 123" -X PUT $API/utilisateur/2;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"pseudo": "charlie","nom": "Chocolat","prenom": "Charlie","adresse": "27 rue des machins","codePostal": "45800","email": "charlie.chocolat@gmail.com","dateNaiss": "1980-10-03"}' --header "Authorization: 123" -X PUT $API/utilisateur/3;
sleep 1

printf "\n--  AJOUTER DES AMIS  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' --header "Authorization: 123" -X PUT $API/utilisateur/1/amis/2;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' --header "Authorization: 123" -X PUT $API/utilisateur/1/amis/3;
sleep 1

printf "\n--  CREATION D'EVENEMENTS  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -X GET $API/evenementopenagenda/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Construction en lego","idCreateur":"2","dateEvenement":"2019-11-01","lieu":"Paris"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Lancer de nains","idCreateur":"2","dateEvenement":"2019-10-02","lieu":"Olivet"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Apprenez a coder en CSS","idCreateur":"3","dateEvenement":"2019-09-03","lieu":"Orleans"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Jargeau Plage","idCreateur":"1","dateEvenement":"2019-08-04","lieu":"Jargeau"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"TP note impossible","idCreateur":"1","dateEvenement":"2019-07-05","lieu":"Lyon"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Prank a la cantine","idCreateur":"1","dateEvenement":"2019-06-06","lieu":"Calais"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Promenade  dos de lama","idCreateur":"3","dateEvenement":"2019-05-07","lieu":"PaysImmaginaire"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Chasse aux clowns","idCreateur":"2","dateEvenement":"2019-04-08","lieu":"Nice"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Faites du Cassoulet","idCreateur":"4","dateEvenement":"2013-03-09","lieu":"Rouen"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Adoptez un clodo","idCreateur":"2","dateEvenement":"2019-02-10","lieu":"Bruxelles"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Rencontre Jean-Michel Jarre","idCreateur":"3","dateEvenement":"2019-01-11","lieu":"Londres"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Course a dos de penguin","idCreateur":"1","dateEvenement":"2019-01-01","lieu":"Moscou"}' --header "Authorization: 123" -X POST $API/evenement/;
sleep 1

printf "\n--  CREATION DE SOIREES  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Soiree vroum vroum","utilisateur":"1","nbPlaces":"20","dateSoiree":"2019-11-04","heureDebut":"9"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Ne buvez pas trop d eau","utilisateur":"2","nbPlaces":"4","dateSoiree":"2019-11-04","heureDebut":"10"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Degutation de petit pois","utilisateur":"3","nbPlaces":"5","dateSoiree":"2019-11-04","heureDebut":"11"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Anniversaire de Laom","utilisateur":"4","nbPlaces":"50","dateSoiree":"2019-11-04","heureDebut":"12"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Soiree deguise Fort Boyard","utilisateur":"5","nbPlaces":"7","dateSoiree":"2019-11-04","heureDebut":"13"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Combat de crabe","utilisateur":"1","nbPlaces":"2","dateSoiree":"2019-11-04","heureDebut":"14"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Balance ton eboueur","utilisateur":"1","nbPlaces":"3","dateSoiree":"2019-11-04","heureDebut":"15"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Meet up with the holy GARRY","utilisateur":"2","nbPlaces":"4","dateSoiree":"2019-11-04","heureDebut":"16"}' --header "Authorization: 123" -X POST $API/soiree/;
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -d '{"nom":"Soiree cornichon","utilisateur":"1","nbPlaces":"1","dateSoiree":"2019-11-04","heureDebut":"17"}' --header "Authorization: 123" -X POST $API/soiree/;
sleep 1

printf "\n--  AJOUTER DES PARTICIPANTS  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' --header "Authorization: 123" -X PUT $API/soiree/1/utilisateur/1;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' --header "Authorization: 123" -X PUT $API/soiree/1/utilisateur/2;
sleep 1

printf "\n--  DECONNEXIONS  --\n";
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -X DELETE $API/authentification/connexion?pseudo=charlie;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -X DELETE $API/authentification/connexion?pseudo=daniel;
sleep 1
curl -H $JSON -w '\nRESPONSE:%{response_code}\n' -X DELETE $API/authentification/connexion?pseudo=emilie;


sleep 10000;
