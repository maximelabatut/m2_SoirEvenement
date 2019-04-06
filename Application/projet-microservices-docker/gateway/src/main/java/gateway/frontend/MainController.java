package gateway.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import gateway.frontend.domain.*;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ui")
public class MainController {

    // Vues
    private static final String CONNEXION = "connexion";
    private static final String MENU_PRINCIPAL = "menuPrincipal";
    private static final String INSCRIPTION = "inscription";
    private static final String VOIR_PROFIL = "voirProfil";
    private static final String MODIFIER_PROFIL = "modifierProfil";
    private static final String RECHECHE_PERSONNE = "recherchePersonne";
    private static final String VOIR_SOIREES = "voirSoirees";
    private static final String VOIR_SOIREE = "voirSoiree";
    private static final String MODIFIER_SOIREE = "modifierSoiree";
    private static final String VOIR_EVENEMENTS = "voirEvenements";
    private static final String MODIFIER_EVENEMENT = "modifierEvenement";
    private static final String VOIR_EVENEMENT_PRIVE = "voirEvenementPrive";
    private static final String VOIR_EVENEMENT_PUBLIQUE = "voirEvenementPublique";

    // Variables
    private static final String PSEUDO_COURANT = "pseudoCourant";
    private static final String ID_COURANT = "idCourant";
    private static final String DOMAIN = "http://localhost:8080";
    // Messages
    private static final String ERROR = "error";
    private static final String AUTHORIZATION = "Authorization";

    private String token;


    /* DEBUT FONCTIONS DE GESTION */

    private String ifConnectedGoTo(HttpServletRequest httpServletRequest, String to, String from){
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null){
            return to;
        }else {
            return from;
        }
    }

    @GetMapping("/")
    public String main(HttpServletRequest httpServletRequest){
        return this.ifConnectedGoTo(httpServletRequest, MENU_PRINCIPAL, CONNEXION);
    }

    @GetMapping("/includes/header")
    public String getHeader() {
        return "/includes/header.html";
    }

    @GetMapping("/includes/headerLogged")
    public String getHeaderLogged() {
        return "/includes/headerLogged.html";
    }

    @GetMapping("/includes/head")
    public String getHead() {
        return "/includes/head.html";
    }

    @GetMapping("/includes/footer")
    public String getFooter() {
        return "/includes/footer.html";
    }

    private HttpResponse httpGet(String url) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader(AUTHORIZATION,this.token);
        return httpClient.execute(request);
    }

    private HttpResponse httpPost(String url, StringEntity params) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader(AUTHORIZATION,this.token);
        request.setEntity(params);
        return httpClient.execute(request);
    }

    private HttpResponse httpPut(String url, StringEntity params) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader(AUTHORIZATION, this.token);
        request.setEntity(params);
        return httpClient.execute(request);
    }

    private HttpResponse httpDelete(String url) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete request = new HttpDelete(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader(AUTHORIZATION, this.token);
        return httpClient.execute(request);
    }

    /* FIN DES FONCTIONS DE GESTION */

    /* DEBUT PAGE PRINCIPALE */

    @GetMapping("/menuPrincipal")
    public String menuPrincipal(
            Model model,
            HttpServletRequest httpServletRequest
    )throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsAmis";
            HttpResponse response = this.httpGet(url);

            String url3 = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsSoirees";
            HttpResponse response3 = this.httpGet(url3);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    InvitationAmi[] notificationsAmis = g.fromJson(json, InvitationAmi[].class);

                    /* Notifications Amis */

                    List<Utilisateur> invitantAmis = new ArrayList<>();
                    List<String> lesNotificationAmisFalse = new ArrayList<>();

                    for(int i=0; i<notificationsAmis.length; i++){
                        if(!notificationsAmis[i].getAccepte()){
                            String url2 = DOMAIN + "/utilisateur/" + notificationsAmis[i].getIdUtilisateurInvitant();
                            HttpResponse response2 = this.httpGet(url2);
                            if(response2.getStatusLine().getStatusCode()==200){
                                String json4 = EntityUtils.toString(response2.getEntity());
                                invitantAmis.add(g.fromJson(json4, Utilisateur.class));
                                lesNotificationAmisFalse.add(notificationsAmis[i].getId());
                            }
                        }
                    }

                    List<String> pseudoNomAmis = new ArrayList<>();
                    for(int i=0; i<invitantAmis.size();i++){
                        pseudoNomAmis.add(invitantAmis.get(i).getPseudo());
                    }

                    HashMap<String,String> infosAmis = new HashMap<>();
                    for(int i=0; i<pseudoNomAmis.size();i++){
                        infosAmis.put(pseudoNomAmis.get(i),lesNotificationAmisFalse.get(i));
                    }

                    /* Notifications Soirées */

                    String json2 = EntityUtils.toString(response3.getEntity());
                    Gson g2 = new Gson();
                    InvitationSoiree[] notificationsSoirees = g2.fromJson(json2, InvitationSoiree[].class);

                    List<Soiree> invitantSoirees = new ArrayList<>();
                    List<String> lesNotificationsSoireesFalse = new ArrayList<>();

                    for(int i=0; i<notificationsSoirees.length; i++){
                        if(!notificationsSoirees[i].getAccepte()){
                            String url4 = DOMAIN + "/soiree/" + notificationsSoirees[i].getIdSoiree();
                            HttpResponse response4 = this.httpGet(url4);
                            if(response4.getStatusLine().getStatusCode()==200){
                                String json4 = EntityUtils.toString(response4.getEntity());
                                invitantSoirees.add(g.fromJson(json4, Soiree.class));
                                lesNotificationsSoireesFalse.add(notificationsSoirees[i].getId());
                            }
                        }
                    }

                    List<String> nomSoirees = new ArrayList<>();
                    for(int i=0; i<invitantSoirees.size();i++){
                        nomSoirees.add(invitantSoirees.get(i).getNom());
                    }

                    HashMap<String,String> infosSoirees = new HashMap<>();
                    for(int i=0; i<nomSoirees.size();i++){
                        infosSoirees.put(nomSoirees.get(i),lesNotificationsSoireesFalse.get(i));
                    }

                    /* Soirées à venir */

                    String url4 = DOMAIN + "/soiree/";

                    HttpResponse response4 = this.httpGet(url4);

                    List<SoireeVenir> listeDesSoireeParticipe = new ArrayList<>();

                    if(response4.getStatusLine().getStatusCode() == 200){
                        String json4 = EntityUtils.toString(response4.getEntity());
                        Gson g4 = new Gson();

                        Soiree[] soirees = g4.fromJson(json4, Soiree[].class);

                        Date actuelle = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String dat = dateFormat.format(actuelle);

                        for(int i=0; i<soirees.length; i++){
                            if(soirees[i].getParticipants().contains(httpServletRequest.getSession().getAttribute(ID_COURANT).toString())){
                                String url5 = DOMAIN + "/utilisateur/" + soirees[i].getUtilisateur();

                                Utilisateur utilisateur = this.returnUtilisateur(url5);

                                if(dat.compareTo(soirees[i].getDateSoiree())<0){
                                    SoireeVenir soireeVenir = new SoireeVenir(soirees[i].getId(), soirees[i].getNom(),
                                            utilisateur.getPseudo(), soirees[i].getDateSoiree(), soirees[i].getHeureDebut(), soirees[i].getParticipants().size(),
                                            soirees[i].getEvenementsOpenAgenda().size()+soirees[i].getEvenements().size());
                                    listeDesSoireeParticipe.add(soireeVenir);
                                }
                            }
                        }
                    }

                    model.addAttribute("infosAmis", infosAmis);
                    model.addAttribute("infosSoirees", infosSoirees);
                    model.addAttribute("soireesParticipe", listeDesSoireeParticipe);
                    return MENU_PRINCIPAL;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return CONNEXION;
            }
        }else{
            return CONNEXION;
        }
    }

    private Utilisateur returnUtilisateur(String url) throws IOException {
        HttpResponse response = this.httpGet(url);

        String json5 = EntityUtils.toString(response.getEntity());
        Gson g5 = new Gson();
        Utilisateur utilisateur = g5.fromJson(json5, Utilisateur.class);

        return utilisateur;
    }

    /* FIN PAGE PRINCIPALE */

    /* DEBUT CONNEXION */

    @GetMapping("/connexion")
    public String menuConnexion(Model model, Principal principal, HttpServletRequest httpServletRequest){
        return this.ifConnectedGoTo(httpServletRequest,MENU_PRINCIPAL,CONNEXION);
    }

    @PostMapping("/connexion")
    public String connexionPost(
            Model model,
            HttpServletRequest httpServletRequest,
            @RequestParam String pseudo,
            @RequestParam String password
    ) throws Exception {

        String url = DOMAIN + "/authentification/connexion";
        StringEntity params = new StringEntity("{\"pseudo\":\"" + pseudo + "\",\"password\":\"" + password + "\"}");
        HttpResponse response = this.httpPost(url, params);

        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                String json = EntityUtils.toString(response.getEntity());
                Gson g = new Gson();
                String idUtilisateur = g.fromJson(json, String.class);
                for (Header header : response.getAllHeaders()) {
                    if (header.getName().equals(AUTHORIZATION)){
                        this.token = header.getValue();
                    }
                }
                httpServletRequest.getSession().setAttribute(PSEUDO_COURANT, pseudo);
                httpServletRequest.getSession().setAttribute(ID_COURANT, idUtilisateur);
                return menuPrincipal(model,httpServletRequest);
            case 404:
                model.addAttribute(ERROR, "Identifiants incorrects.");
                return CONNEXION;
            case 409:
                model.addAttribute(ERROR, "Identifiants incorrects.");
                return CONNEXION;
            default:
                model.addAttribute(ERROR, "Une erreur est survenue.");
                return CONNEXION;
        }
    }

    @GetMapping("/deconnexion")
    public String deconnexion(Model model, Principal principal, HttpServletRequest httpServletRequest) throws IOException {
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null){
            String url = DOMAIN + "/authentification/connexion";
            this.httpDelete(url);
            httpServletRequest.getSession().removeAttribute(PSEUDO_COURANT);
            httpServletRequest.getSession().removeAttribute(ID_COURANT);
        }
        return CONNEXION;
    }

    /* FIN CONNEXION */






    /* DEBUT INSCRIPTION */

    @GetMapping("/inscription")
    public String menuInscription(Model model, Principal principal, HttpServletRequest httpServletRequest){
        return this.ifConnectedGoTo(httpServletRequest,MENU_PRINCIPAL,INSCRIPTION);
    }

    @PostMapping("/inscription")
    public String inscriptionPost(
            Model model,
            HttpServletRequest httpServletRequest,
            @RequestParam String pseudo,
            @RequestParam String password
    ) throws Exception {

        String url = DOMAIN + "/authentification/inscription";
        StringEntity params = new StringEntity("{\"pseudo\":\""+pseudo+"\",\"password\":\"" + password +"\"}");
        HttpResponse response = this.httpPost(url, params);

        switch (response.getStatusLine().getStatusCode()){
            case 201:
                String json = EntityUtils.toString(response.getEntity());
                Gson g = new Gson();
                String idUtilisateur = g.fromJson(json, String.class);
                String url2 = DOMAIN + "/utilisateur/";
                StringEntity params2 = new StringEntity("{\"pseudo\":\""+pseudo+"\"}");
                this.httpPost(url2, params2);
                for (Header header : response.getAllHeaders()) {
                    if (header.getName().equals(AUTHORIZATION)){
                        this.token = header.getValue();
                    }
                }
                httpServletRequest.getSession().setAttribute(PSEUDO_COURANT, pseudo);
                httpServletRequest.getSession().setAttribute(ID_COURANT, idUtilisateur);
                return menuPrincipal(model,httpServletRequest);
            case 409:
                model.addAttribute(ERROR, "Pseudo déjà pris");
                return INSCRIPTION;
            default:
                model.addAttribute(ERROR, "Une erreur est survenue.");
                return INSCRIPTION;
        }
    }

    /* FIN INSCRIPTION */

    /* DEBUT GESTION UTILISATEUR */

    @GetMapping("/profil/{id}")
    public String voirProfil(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String id
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + id;
            HttpResponse response = this.httpGet(url);

            return this.response(model, httpServletRequest, response, id);

        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/modifierProfil/{id}")
    public String modifierProfilGet(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String id
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {
            if (httpServletRequest.getSession().getAttribute(ID_COURANT).equals(id)) {
                String url = DOMAIN + "/utilisateur/" + id;

                HttpResponse response = this.httpGet(url);

                switch (response.getStatusLine().getStatusCode()) {
                    case 200:
                        String json = EntityUtils.toString(response.getEntity());
                        Gson g = new Gson();
                        Utilisateur utilisateur = g.fromJson(json, Utilisateur.class);

                        String url2 = DOMAIN + "/utilisateur/" + id + "/amis";
                        HttpResponse response2 = this.httpGet(url2);

                        Utilisateur[] amis = new Utilisateur[0];

                        if(response.getStatusLine().getStatusCode()==200){
                            String json2 = EntityUtils.toString(response2.getEntity());
                            amis = g.fromJson(json2, Utilisateur[].class);
                        }

                        model.addAttribute("utilisateur",utilisateur);
                        model.addAttribute("amis",amis);
                        return MODIFIER_PROFIL;
                    case 404:
                        model.addAttribute(ERROR, "Utilisateur introuvable");
                        return menuPrincipal(model,httpServletRequest);
                    default:
                        model.addAttribute(ERROR, "Une erreur est survenue.");
                        return menuPrincipal(model,httpServletRequest);
                }

            }else {
                return menuPrincipal(model,httpServletRequest);
            }
        }else {
            return CONNEXION;
        }
    }

    @PostMapping("/modifierProfil")
    public String modifierProfilPost(
            Model model,
            HttpServletRequest httpServletRequest,
            @ModelAttribute Utilisateur utilisateur
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {
            if (httpServletRequest.getSession().getAttribute(ID_COURANT).equals(utilisateur.getId())) {
                String url = DOMAIN + "/utilisateur/" + utilisateur.getId();

                String url2 = DOMAIN + "/utilisateur/" + utilisateur.getId() + "/amis";
                HttpResponse response2 = this.httpGet(url2);
                Utilisateur[] amis = new Utilisateur[0];
                if(response2.getStatusLine().getStatusCode()==200){
                    String json2 = EntityUtils.toString(response2.getEntity());
                    Gson g = new Gson();
                    amis = g.fromJson(json2, Utilisateur[].class);
                }
                utilisateur.setAmis(Arrays.asList(amis));
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(utilisateur);
                StringEntity params = new StringEntity(json);
                HttpResponse response = this.httpPut(url,params);

                switch (response.getStatusLine().getStatusCode()) {
                    case 200:
                        return this.voirProfil(model,httpServletRequest,utilisateur.getId());
                    case 404:
                        model.addAttribute(ERROR, "Utilisateur introuvable");
                        return menuPrincipal(model,httpServletRequest);
                    default:
                        model.addAttribute(ERROR, "Une erreur est survenue.");
                        return menuPrincipal(model,httpServletRequest);
                }
            }else {
                return menuPrincipal(model,httpServletRequest);
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/supprimerAmi/{id}")
    public String voirSoiree(Model model, HttpServletRequest httpServletRequest, @PathVariable("id") Long idAmi) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/amis";
            String idCourant = httpServletRequest.getSession().getAttribute(ID_COURANT).toString();
            StringEntity params = new StringEntity("{\"idPseudoInvitant\":\""+idCourant+"\",\"idPseudoInvite\":\"" + idAmi +"\"}");
            HttpResponse response = this.httpPut(url, params);

            switch (response.getStatusLine().getStatusCode()){
                case 200:
                    return this.voirProfil(model,httpServletRequest, idCourant);
                case 404:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_PROFIL;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_PROFIL;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/utilisateur/{id}/invitationsAmis")
    public String inviterAmis(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idUtilisateur
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + idUtilisateur + "/invitationsAmis";
            StringEntity params = new StringEntity("{\"idUtilisateurInvite\":\""+ idUtilisateur +"\",\"idUtilisateurInvitant\":\"" + httpServletRequest.getSession().getAttribute(ID_COURANT) +"\"}");

            HttpResponse response = this.httpPost(url, params);

            switch (response.getStatusLine().getStatusCode()) {
                case 201:
                    return this.recherchePersonne(model, httpServletRequest);
                case 404:
                    model.addAttribute(ERROR, "Utilisateur introuvable");
                    return MENU_PRINCIPAL;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return MENU_PRINCIPAL;
            }
        }else{
            return CONNEXION;
        }
    }

    @GetMapping("/accepterInvitationAmi/{id}")
    public String accepterInvitationAmis(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idNotification
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsAmis/" + idNotification;
            HttpResponse response = this.httpPut(url, null);

            return this.switchUtilisateur(model, httpServletRequest, response);
        }else{
            return CONNEXION;
        }
    }

    @GetMapping("/refuserInvitationAmi/{id}")
    public String refuserInvitationAmis(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idNotification
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsAmis/" + idNotification;
            HttpResponse response = this.httpDelete(url);

            return this.switchUtilisateur(model, httpServletRequest, response);
        }else{
            return CONNEXION;
        }
    }

    @GetMapping("/accepterInvitationSoirees/{id}")
    public String accepterInvitationSoirees(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idNotification
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsSoirees/";
            HttpResponse response = this.httpGet(url);

            InvitationSoiree[] invitation = this.invitationSoiree(response);

            String url3 = "";
            StringEntity params = null;

            for(int i=0; i<invitation.length; i++){
                if(invitation[i].getId().equals(idNotification)){
                    String url2 = DOMAIN + "/soiree/" + invitation[i].getIdSoiree();
                    HttpResponse response2 = this.httpGet(url2);
                    String json2 = EntityUtils.toString(response2.getEntity());
                    Gson g2 = new Gson();
                    Soiree soiree = g2.fromJson(json2, Soiree.class);

                    soiree.getParticipants().add(httpServletRequest.getSession().getAttribute(ID_COURANT).toString());

                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String json3 = ow.writeValueAsString(soiree);
                    params = new StringEntity(json3);

                    url3 = DOMAIN + "/soiree/" + soiree.getId();
                }
            }

            HttpResponse response3 = this.httpPut(url3, params);
            String json3 = EntityUtils.toString(response3.getEntity());
            Gson g3 = new Gson();
            Soiree soiree = g3.fromJson(json3, Soiree.class);

            String url4 = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsSoirees/" + idNotification;
            this.httpPut(url4, null);

            if(soiree.getParticipants().size() == soiree.getNbPlaces()){
                String url6 = DOMAIN + "/utilisateur/";
                HttpResponse response6 = this.httpGet(url6);
                String json6 = EntityUtils.toString(response6.getEntity());
                Gson g6 = new Gson();
                Utilisateur[] utilisateurs = g6.fromJson(json6, Utilisateur[].class);

                List<InvitationSoiree> listeTemporaire = new ArrayList<>();

                for(int i=0; i<utilisateurs.length; i++){
                    String url7 = DOMAIN + "/utilisateur/" + utilisateurs[i].getId() + "/invitationsSoirees";
                    HttpResponse response7 = this.httpGet(url7);

                    InvitationSoiree[] invitationSoirees = this.invitationSoiree(response7);

                    for(int j=0; j<invitationSoirees.length; j++){
                        listeTemporaire.add(invitationSoirees[j]);
                    }
                }

                if(!listeTemporaire.isEmpty()){
                    for(int i=0; i<listeTemporaire.size(); i++){
                        if(listeTemporaire.get(i).getIdSoiree().equals(soiree.getId())){
                            String url5 = DOMAIN + "/utilisateur/" + listeTemporaire.get(i).getUtilisateurInvite() + "/invitationsSoirees/" + listeTemporaire.get(i).getId();
                            HttpResponse httpResponse = this.httpDelete(url5);

                        }
                    }
                }
            }

            return this.switchUtilisateur(model, httpServletRequest, response);
        }else{
            return CONNEXION;
        }
    }

    @GetMapping("/refuserInvitationSoirees/{id}")
    public String refuserInvitationSoiree(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idNotification
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/invitationsSoirees/" + idNotification;
            HttpResponse response = this.httpDelete(url);

            return this.switchUtilisateur(model, httpServletRequest, response);
        }else{
            return CONNEXION;
        }
    }

    /* FIN GESTION UTILISATEUR */

    /* DEBUT FONCTIONS UTILES UTILISATEURS */

    private String response(Model model, HttpServletRequest httpServletRequest, HttpResponse response, String idUtilisateur) throws Exception {
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                String json = EntityUtils.toString(response.getEntity());
                Gson g = new Gson();
                Utilisateur utilisateur = g.fromJson(json, Utilisateur.class);

                String url2 = DOMAIN + "/utilisateur/" + idUtilisateur + "/amis";
                HttpResponse response2 = this.httpGet(url2);

                Utilisateur[] amis = new Utilisateur[0];

                if(response.getStatusLine().getStatusCode()==200){
                    String json2 = EntityUtils.toString(response2.getEntity());
                    amis = g.fromJson(json2, Utilisateur[].class);
                }

                model.addAttribute("utilisateur",utilisateur);
                model.addAttribute("amis",amis);
                return VOIR_PROFIL;
            case 404:
                model.addAttribute(ERROR, "Utilisateur introuvable");
                return menuPrincipal(model,httpServletRequest);
            default:
                model.addAttribute(ERROR, "Une erreur est survenue.");
                return menuPrincipal(model,httpServletRequest);
        }
    }

    private String switchUtilisateur(Model model, HttpServletRequest httpServletRequest, HttpResponse response) throws Exception {
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                return this.menuPrincipal(model, httpServletRequest);
            case 404:
                model.addAttribute(ERROR, "Utilisateur introuvable");
                return MENU_PRINCIPAL;
            default:
                model.addAttribute(ERROR, "Une erreur est survenue.");
                return MENU_PRINCIPAL;
        }
    }

    private InvitationSoiree[] invitationSoiree(HttpResponse response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        Gson g = new Gson();
        InvitationSoiree[] invitation = g.fromJson(json, InvitationSoiree[].class);
        return invitation;
    }

    /* FIN FONCTIONS UTILES UTILISATEURS */

    /* DEBUT RECHERCHE PERSONNE */

    @GetMapping("/recherchePersonne")
    public String recherchePersonne(
            Model model,
            HttpServletRequest httpServletRequest
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {

            String url = DOMAIN + "/utilisateur/";
            HttpResponse response = this.httpGet(url);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    Utilisateur[] utilisateurs = g.fromJson(json, Utilisateur[].class);

                    String url2 = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/amis";
                    HttpResponse response2 = this.httpGet(url2);

                    List<String> amisUtilisateur = new ArrayList<>();

                    if(response2.getStatusLine().getStatusCode()==200){
                        String json2 = EntityUtils.toString(response2.getEntity());
                        Utilisateur[] amis = g.fromJson(json2, Utilisateur[].class);

                        for(Utilisateur user : amis){
                            amisUtilisateur.add(user.getId());
                        }
                    }

                    model.addAttribute("amis", amisUtilisateur);
                    model.addAttribute("utilisateurs", utilisateurs);
                    return RECHECHE_PERSONNE;
                case 409:
                    model.addAttribute(ERROR, "Personne inexistante");
                    return RECHECHE_PERSONNE;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return RECHECHE_PERSONNE;
            }
        }else {
            return CONNEXION;
        }
    }

    /* FIN RECHERCHE PERSONNE */

    /* DEBUT GESTION DES SOIREES */

    @GetMapping("/voirSoirees")
    public String voirSoiree(Model model, HttpServletRequest httpServletRequest) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/soiree/";

            HttpResponse response = this.httpGet(url);

            String json = EntityUtils.toString(response.getEntity());
            Gson g = new Gson();

            Soiree[] soirees = g.fromJson(json, Soiree[].class);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:

                    String url2 = DOMAIN + "/evenement/";
                    HttpResponse response2 = this.httpGet(url2);
                    Evenement[] evenements = new Evenement[0];

                    if(response2.getStatusLine().getStatusCode()==200){
                        String json2 = EntityUtils.toString(response2.getEntity());
                        evenements = g.fromJson(json2, Evenement[].class);
                    }

                    String url3 = DOMAIN + "/evenementopenagenda/";
                    HttpResponse response3 = this.httpGet(url3);
                    EvenementOpenAgenda[] evenementOpenAgenda = new EvenementOpenAgenda[0];

                    if(response3.getStatusLine().getStatusCode()==200){
                        String json3 = EntityUtils.toString(response3.getEntity());
                        evenementOpenAgenda = g.fromJson(json3, EvenementOpenAgenda[].class);
                    }

                    String url7 = DOMAIN + "/utilisateur/" + httpServletRequest.getSession().getAttribute(ID_COURANT) + "/amis";
                    HttpResponse response7 = this.httpGet(url7);

                    Utilisateur[] amis = new Utilisateur[0];
                    if(response7.getStatusLine().getStatusCode()==200){
                        String json7 = EntityUtils.toString(response7.getEntity());
                        amis = g.fromJson(json7, Utilisateur[].class);
                    }

                    model.addAttribute("soirees", soirees);
                    model.addAttribute("evenements", evenements);
                    model.addAttribute("evenementsOpenAgenda", evenementOpenAgenda);
                    model.addAttribute("amis", amis);
                    return VOIR_SOIREES;
                case 409:
                    model.addAttribute(ERROR, "Aucune Soirée pour le moment");
                    return VOIR_SOIREES;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_SOIREES;
            }
        }else {
            return CONNEXION;
        }
    }

    @PostMapping("/voirSoiree")
    public String voirSoiree(Model model, HttpServletRequest httpServletRequest, @ModelAttribute Soiree soiree) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/soiree/";

            String id = httpServletRequest.getSession().getAttribute(ID_COURANT).toString();
            soiree.setUtilisateur(id);

            List<String> lesParticipants = soiree.getParticipants();

            List<String> leCreateur = new ArrayList<>();
            leCreateur.add(httpServletRequest.getSession().getAttribute(ID_COURANT).toString());

            soiree.setParticipants(leCreateur);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(soiree);
            StringEntity params = new StringEntity(json);

            HttpResponse response = this.httpPost(url, params);

            String json2 = EntityUtils.toString(response.getEntity());
            Gson g = new Gson();
            Soiree maSoiree = g.fromJson(json2, Soiree.class);

            if(lesParticipants!=null){
                for(int i=0; i<lesParticipants.size(); i++){
                    String url3 = DOMAIN + "/utilisateur/" + lesParticipants.get(i);

                    Utilisateur utilisateur = this.returnUtilisateur(url3);

                    String url4 = DOMAIN + "/utilisateur/" + utilisateur.getId() + "/invitationsSoirees";
                    StringEntity params4 = new StringEntity("{\"utilisateurInvite\":\""+utilisateur.getId()+"\",\"idSoiree\":\"" + maSoiree.getId() +"\"}");

                    this.httpPost(url4, params4);
                }
            }

            switch (response.getStatusLine().getStatusCode()){
                case 201:
                    return this.voirSoireeById(model,httpServletRequest, maSoiree.getId());

                case 409:
                    model.addAttribute(ERROR, "Aucune Soirée pour le moment");
                    return VOIR_SOIREES;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_SOIREES;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/voirSoiree/{id}")
    public String voirSoireeById(Model model, HttpServletRequest httpServletRequest, @PathVariable("id") String id) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {
            String url = DOMAIN + "/soiree/" + id;

            HttpResponse response = this.httpGet(url);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    Soiree soiree = g.fromJson(json, Soiree.class);

                    String url2 = DOMAIN + "/soiree/" + id + "/evenements";
                    HttpResponse response2 = this.httpGet(url2);
                    Long[] events = new Long[0];
                    if(response2.getStatusLine().getStatusCode()==200){
                        String json2 = EntityUtils.toString(response2.getEntity());
                        events = g.fromJson(json2, Long[].class);
                    }

                    ArrayList<Evenement> evenements = new ArrayList<>();

                    for(Long idEvenement : events){
                        evenements.add(this.evenementListe(idEvenement));
                    }

                    String url3 = DOMAIN + "/soiree/" + id + "/evenementsOpenAgenda";
                    HttpResponse response3 = this.httpGet(url3);
                    Long[] eventsOpenAgenda = new Long[0];
                    if(response3.getStatusLine().getStatusCode()==200){
                        String json3 = EntityUtils.toString(response3.getEntity());
                        eventsOpenAgenda = g.fromJson(json3, Long[].class);
                    }

                    ArrayList<EvenementOpenAgenda> evenementOpenAgenda = new ArrayList<>();

                    for(Long idEvenement : eventsOpenAgenda){
                        evenementOpenAgenda.add(this.evenementOpenAgendaListe(idEvenement));
                    }

                    ArrayList<String> lesUtilisateurs = new ArrayList<>();

                    for(int i=0; i<soiree.getParticipants().size(); i++){
                        lesUtilisateurs.add(soiree.getParticipants().get(i));
                    }

                    List<Utilisateur> lesParticipants = new ArrayList<>();

                    for(int i=0; i<lesUtilisateurs.size();i++){
                        String url7 = DOMAIN + "/utilisateur/" + lesUtilisateurs.get(i);
                        HttpResponse response7 = this.httpGet(url7);
                        String json7 = EntityUtils.toString(response7.getEntity());
                        lesParticipants.add(g.fromJson(json7, Utilisateur.class));
                    }

                    model.addAttribute("soiree",soiree);
                    model.addAttribute("evenements", evenements);
                    model.addAttribute("evenementsOpenAgenda", evenementOpenAgenda);
                    model.addAttribute("lesParticipants", lesParticipants);
                    return VOIR_SOIREE;
                case 404:
                    model.addAttribute(ERROR, "Soirée introuvable");
                    return menuPrincipal(model,httpServletRequest);
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return menuPrincipal(model,httpServletRequest);
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/modifierSoiree/{id}")
    public String modifierSoireeGet(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idSoiree
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/soiree/" + idSoiree;
            HttpResponse response = this.httpGet(url);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    Soiree soiree = g.fromJson(json, Soiree.class);

                    String url2 = DOMAIN + "/soiree/" + idSoiree + "/evenements";
                    HttpResponse response2 = this.httpGet(url2);
                    Long[] events = new Long[0];
                    if(response2.getStatusLine().getStatusCode()==200){
                        String json2 = EntityUtils.toString(response2.getEntity());
                        events = g.fromJson(json2, Long[].class);
                    }

                    ArrayList<Evenement> evenements = new ArrayList<>();
                    for(Long idEvenement : events){
                        evenements.add(this.evenementListe(idEvenement));
                    }

                    String url3 = DOMAIN + "/soiree/" + idSoiree + "/evenementsOpenAgenda";
                    HttpResponse response3 = this.httpGet(url3);
                    Long[] eventsOpenAgenda = new Long[0];
                    if(response3.getStatusLine().getStatusCode()==200){
                        String json3 = EntityUtils.toString(response3.getEntity());
                        eventsOpenAgenda = g.fromJson(json3, Long[].class);
                    }

                    ArrayList<EvenementOpenAgenda> evenementOpenAgenda = new ArrayList<>();
                    for(Long idEvenement : eventsOpenAgenda){
                        evenementOpenAgenda.add(this.evenementOpenAgendaListe(idEvenement));
                    }

                    model.addAttribute("soiree",soiree);
                    model.addAttribute("evenements", evenements);
                    model.addAttribute("evenementsOpenAgenda", evenementOpenAgenda);
                    return MODIFIER_SOIREE;
                case 404:
                    model.addAttribute(ERROR, "Soiree introuvable");
                    return VOIR_SOIREES;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return menuPrincipal(model,httpServletRequest);
            }
        }else {
            return CONNEXION;
        }
    }

    @PostMapping("/modifierSoiree")
    public String modifierSoiree(
            Model model,
            HttpServletRequest httpServletRequest,
            @ModelAttribute Soiree soiree
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/soiree/" + soiree.getId();

            HttpResponse response = this.httpGet(url);
            String json = EntityUtils.toString(response.getEntity());
            Gson g = new Gson();
            Soiree laSoiree = this.returnSoiree(url);

            laSoiree.setNom(soiree.getNom());
            laSoiree.setHeureDebut(soiree.getHeureDebut());
            laSoiree.setNbPlaces(soiree.getNbPlaces());
            laSoiree.setDateSoiree(soiree.getDateSoiree());

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(laSoiree);
            StringEntity params = new StringEntity(json);

            HttpResponse response2 = this.httpPut(url, params);

            switch (response2.getStatusLine().getStatusCode()) {
                case 200:
                    return this.voirSoireeById(model, httpServletRequest, laSoiree.getId());
                case 404:
                    model.addAttribute(ERROR, "Soiree introuvable");
                    return menuPrincipal(model,httpServletRequest);
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return menuPrincipal(model,httpServletRequest);
            }
        }else {
            return CONNEXION;
        }
    }

    /* FIN GESTION SOIREES */

    /* DEBUT GESTION FONCTION SOIREE */

    private EvenementOpenAgenda evenementOpenAgendaListe(Long idEvenement) throws IOException {
        EvenementOpenAgenda evenementOpenAgenda = null;
        Gson g = new Gson();

        String url6 = DOMAIN + "/evenementopenagenda/" + idEvenement;
        HttpResponse response6 = this.httpGet(url6);
        if(response6.getStatusLine().getStatusCode()==200){
            String json6 = EntityUtils.toString(response6.getEntity());
            evenementOpenAgenda = g.fromJson(json6, EvenementOpenAgenda.class);
        }

        return evenementOpenAgenda;
    }

    private Evenement evenementListe(Long idEvenement) throws IOException {
        Evenement evenement = null;
        Gson g = new Gson();

        String url4 = DOMAIN + "/evenement/" + idEvenement;
        HttpResponse response4 = this.httpGet(url4);
        if(response4.getStatusLine().getStatusCode()==200){
            String json4 = EntityUtils.toString(response4.getEntity());
            evenement = g.fromJson(json4, Evenement.class);
        }

        return evenement;

    }

    /* FIN GESTION FONCTION SORIEE */

    /* GESTION DES EVENEMENTS */

    @PostMapping("/creationEvenement")
    public String voirSoiree(Model model, HttpServletRequest httpServletRequest, @ModelAttribute Evenement evenement) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/evenement/";
            evenement.setIdCreateur(httpServletRequest.getSession().getAttribute(ID_COURANT).toString());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(evenement);
            StringEntity params = new StringEntity(json);

            HttpResponse response = this.httpPost(url, params);
            String json2 = EntityUtils.toString(response.getEntity());
            Gson g = new Gson();
            Evenement monEvenement = g.fromJson(json2, Evenement.class);
            switch (response.getStatusLine().getStatusCode()){
                case 201:
                    return this.voirEvenementPrive(model,httpServletRequest,monEvenement.getId());

                case 409:
                    model.addAttribute(ERROR, "Aucune Soirée pour le moment");
                    return VOIR_EVENEMENTS;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_EVENEMENTS;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/voirEvenements")
    public String voirEvenements(
            Model model,
            HttpServletRequest httpServletRequest
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/evenement/";

            HttpResponse response = this.httpGet(url);

            Evenement[] evenements = new Evenement[0];

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    evenements = g.fromJson(json, Evenement[].class);

                    String url2 = DOMAIN + "/evenementopenagenda/";
                    HttpResponse response2 = this.httpGet(url2);
                    EvenementOpenAgenda[] evenementOpenAgenda = new EvenementOpenAgenda[0];

                    if(response2.getStatusLine().getStatusCode()==200){
                        String json2 = EntityUtils.toString(response2.getEntity());
                        evenementOpenAgenda = g.fromJson(json2, EvenementOpenAgenda[].class);
                    }

                    model.addAttribute("evenements", evenements);
                    model.addAttribute("evenementsOpenAgenda", evenementOpenAgenda);
                    return VOIR_EVENEMENTS;
                case 409:
                    model.addAttribute(ERROR, "Evenement inexistant");
                    return VOIR_EVENEMENTS;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_EVENEMENTS;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/voirEvenementPrive/{id}")
    public String voirEvenementPrive(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idEvenement
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/evenement/" +idEvenement;

            HttpResponse response = this.httpGet(url);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    Evenement evenement = g.fromJson(json, Evenement.class);
                    model.addAttribute("evenement", evenement);
                    return VOIR_EVENEMENT_PRIVE;
                case 409:
                    model.addAttribute(ERROR, "Evenement inexistant");
                    return VOIR_EVENEMENTS;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_EVENEMENTS;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/voirEvenementPublique/{id}")
    public String voirEvenementPublique(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") Long idEvenement
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(PSEUDO_COURANT) != null) {
            String url = DOMAIN + "/evenementopenagenda/"+idEvenement;

            HttpResponse response = this.httpGet(url);

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    EvenementOpenAgenda evenementsOpenAgenda = g.fromJson(json, EvenementOpenAgenda.class);
                    model.addAttribute("evenementsOpenAgenda", evenementsOpenAgenda);
                    return VOIR_EVENEMENT_PUBLIQUE;
                case 409:
                    model.addAttribute(ERROR, "Evenement inexistant");
                    return VOIR_EVENEMENTS;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_EVENEMENTS;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/modifierEvenement/{id}")
    public String modifierEvenementGet(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") String idEvenement
    ) throws Exception{
        if(httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {
            String url = DOMAIN + "/evenement/" + idEvenement;
            HttpResponse response = this.httpGet(url);
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    String json = EntityUtils.toString(response.getEntity());
                    Gson g = new Gson();
                    Evenement evenement = g.fromJson(json, Evenement.class);
                    model.addAttribute("evenement", evenement);
                    return MODIFIER_EVENEMENT;
                case 404:
                    model.addAttribute(ERROR, "Evenement introuvable");
                    return VOIR_EVENEMENTS;
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return VOIR_EVENEMENTS;
            }
        }else {
            return CONNEXION;
        }
    }

    @GetMapping("/suppressionEvenementPrive/{idEvenement}/{idSoiree}")
    public String suppressionEvenementPrive(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("idEvenement") String idEvenement,
            @PathVariable("idSoiree") String idSoiree
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/soiree/" +idSoiree;
            Soiree maSoiree = this.returnSoiree(url);

            for(int i=0; i<maSoiree.getEvenements().size();i++){
                if(maSoiree.getEvenements().get(i).equals(idEvenement)){
                    maSoiree.getEvenements().remove(idEvenement);
                }
            }
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json2 = ow.writeValueAsString(maSoiree);
            StringEntity params = new StringEntity(json2);

            HttpResponse response2 = this.httpPut(url, params);

            return this.switchEvenement(model, httpServletRequest, response2, maSoiree);

        }else{
            return CONNEXION;
        }
    }

    @GetMapping("/suppressionEvenementPublique/{idEvenement}/{idSoiree}")
    public String suppressionEvenementPublique(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("idEvenement") String idEvenement,
            @PathVariable("idSoiree") String idSoiree
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {
            String url = DOMAIN + "/soiree/" +idSoiree;

            Soiree maSoiree = this.returnSoiree(url);

            for(int i=0; i<maSoiree.getEvenementsOpenAgenda().size();i++){
                if(maSoiree.getEvenementsOpenAgenda().get(i).equals(idEvenement)){
                    maSoiree.getEvenementsOpenAgenda().remove(idEvenement);
                }
            }
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json2 = ow.writeValueAsString(maSoiree);
            StringEntity params = new StringEntity(json2);

            HttpResponse response2 = this.httpPut(url, params);

            return this.switchEvenement(model, httpServletRequest, response2, maSoiree);
        }else{
            return CONNEXION;
        }
    }

    @PostMapping("/modifierEvenement")
    public String modifierEvenementPost(
            Model model,
            HttpServletRequest httpServletRequest,
            @ModelAttribute Evenement evenement
    ) throws Exception {
        if (httpServletRequest.getSession().getAttribute(ID_COURANT) != null) {

            String url = DOMAIN + "/evenement/" +evenement.getId();
            HttpResponse response = this.httpGet(url);
            String json = EntityUtils.toString(response.getEntity());
            Gson g = new Gson();
            Evenement monEvenement = g.fromJson(json, Evenement.class);

            monEvenement.setNom(evenement.getNom());
            monEvenement.setLieu(evenement.getLieu());
            monEvenement.setDateEvenement(evenement.getDateEvenement());

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json2 = ow.writeValueAsString(monEvenement);
            StringEntity params = new StringEntity(json2);

            HttpResponse response2 = this.httpPut(url, params);

            switch (response2.getStatusLine().getStatusCode()) {
                case 200:
                    return this.voirEvenementPrive(model, httpServletRequest, monEvenement.getId());
                case 404:
                    model.addAttribute(ERROR, "Evenement introuvable");
                    return menuPrincipal(model,httpServletRequest);
                default:
                    model.addAttribute(ERROR, "Une erreur est survenue.");
                    return menuPrincipal(model,httpServletRequest);
            }
        }else{
            return CONNEXION;
        }
    }

    /* FIN GESTION DES EVENEMENTS */

    /* DEBUT FONCTION GESTION DES EVENEMENTS */

    private String switchEvenement(Model model, HttpServletRequest httpServletRequest, HttpResponse response, Soiree soiree) throws Exception {
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                return this.voirSoireeById(model, httpServletRequest, soiree.getId());
            case 404:
                model.addAttribute(ERROR, "Evenement introuvable");
                return menuPrincipal(model,httpServletRequest);
            default:
                model.addAttribute(ERROR, "Une erreur est survenue.");
                return menuPrincipal(model,httpServletRequest);
        }
    }

    private Soiree returnSoiree(String url) throws IOException {

        HttpResponse response = this.httpGet(url);
        String json = EntityUtils.toString(response.getEntity());
        Gson g = new Gson();

        Soiree soiree = g.fromJson(json, Soiree.class);

        return soiree;
    }

    /* FIN FONCTION GESTION DES EVENEMENTS */
}
