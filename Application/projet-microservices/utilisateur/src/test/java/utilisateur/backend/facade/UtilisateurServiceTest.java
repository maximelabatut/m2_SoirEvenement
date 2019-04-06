package utilisateur.backend.facade;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import utilisateur.backend.modele.entities.InvitationAmi;
import utilisateur.backend.modele.entities.InvitationSoiree;
import utilisateur.backend.modele.exceptions.PseudoDejaPrisException;
import utilisateur.backend.modele.exceptions.UtilisateurNotFoundException;
import utilisateur.backend.modele.entities.Utilisateur;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UtilisateurServiceTest {

    @Autowired
    private UtilisateurService service;

    @Test
    public void inscriptionTestOK() {
        Utilisateur utilisateur = service.inscription("Max");
        Assert.assertNotNull(utilisateur);
        Assert.assertEquals("Max", utilisateur.getPseudo());
    }

    @Test(expected = PseudoDejaPrisException.class)
    public void inscriptionTestKO() {
        service.inscription("Marc");
        service.inscription("Marc");
    }

    @Test(expected = UtilisateurNotFoundException.class)
    public void deleteUtilisateurTestOK() {
        Utilisateur utilisateur = service.inscription("Matt");
        Assert.assertNotNull(utilisateur);
        service.deleteUtilisateur(utilisateur.getId());
        service.getUtilisateurById(utilisateur.getId());
    }

    @Test
    public void updateUtilisateurTestOK() {
        Utilisateur utilisateur = service.inscription("Michel");
        utilisateur.setAdresse("123 rue des bidules");
        utilisateur.setVille("Orleans");
        utilisateur.setCodePostal("45000");
        utilisateur.setDateNaiss(LocalDate.of(1995,2,12));
        utilisateur.setEmail("michel@gmail.com");
        utilisateur.setNom("Berger");
        utilisateur.setPrenom("Michel");

        Utilisateur utilisateurUpdated = service.updateUtilisateur(utilisateur);

        Assert.assertEquals("123 rue des bidules", utilisateurUpdated.getAdresse());
        Assert.assertEquals("Orleans", utilisateurUpdated.getVille());
        Assert.assertEquals("45000", utilisateurUpdated.getCodePostal());
        Assert.assertEquals(LocalDate.of(1995,2,12), utilisateurUpdated.getDateNaiss());
        Assert.assertEquals("michel@gmail.com", utilisateurUpdated.getEmail());
        Assert.assertEquals("Berger", utilisateurUpdated.getNom());
        Assert.assertEquals("Michel", utilisateurUpdated.getPrenom());
        Assert.assertEquals(LocalDate.now(), utilisateurUpdated.getDateInscription());
    }

    @Test
    public void ajouterAmiOK() {
        Utilisateur utilisateur1 = service.inscription("Joe");
        Utilisateur utilisateur2 = service.inscription("Francis");

        Assert.assertNotNull(utilisateur1);
        Assert.assertNotNull(utilisateur2);
        Assert.assertEquals(0,utilisateur1.getAmis().size());
        Assert.assertEquals(0,utilisateur2.getAmis().size());

        service.ajouterAmi(utilisateur1.getId(),utilisateur2.getId());

        utilisateur1 = service.getUtilisateurById(utilisateur1.getId());
        utilisateur2 = service.getUtilisateurById(utilisateur2.getId());

        Assert.assertEquals(1, utilisateur1.getAmis().size());
        Assert.assertEquals(1, utilisateur2.getAmis().size());
        Assert.assertTrue(service.areFriends(utilisateur1,utilisateur2));
    }

    @Test(expected = UtilisateurNotFoundException.class)
    public void ajouterAmiUtilisateurIntrouvableKO() {
        Utilisateur utilisateur1 = service.inscription("Bill");
        Assert.assertNotNull(utilisateur1);
        service.ajouterAmi(utilisateur1.getId(),99999999L);
    }

    @Test
    public void enleverAmiOK() {
        Utilisateur utilisateur1 = service.inscription("Mike");
        Utilisateur utilisateur2 = service.inscription("Etienne");

        service.ajouterAmi(utilisateur1.getId(),utilisateur2.getId());

        utilisateur1 = service.getUtilisateurById(utilisateur1.getId());
        utilisateur2 = service.getUtilisateurById(utilisateur2.getId());

        Assert.assertEquals(1, utilisateur1.getAmis().size());
        Assert.assertEquals(1, utilisateur2.getAmis().size());

        service.enleverAmi(utilisateur1.getId(),utilisateur2.getId());

        utilisateur1 = service.getUtilisateurById(utilisateur1.getId());
        utilisateur2 = service.getUtilisateurById(utilisateur2.getId());

        Assert.assertEquals(0, utilisateur1.getAmis().size());
        Assert.assertEquals(0, utilisateur2.getAmis().size());
        Assert.assertFalse(service.areFriends(utilisateur1,utilisateur2));
    }

    @Test(expected = UtilisateurNotFoundException.class)
    public void enleverAmiUtilisateurIntrouvableKO() {
        Utilisateur utilisateur1 = service.inscription("Thierry");
        Assert.assertNotNull(utilisateur1);
        service.enleverAmi(utilisateur1.getId(),9999999999L);
    }
    @Test
    public void enleverAmiNonAmiKO() {
        Utilisateur utilisateur1 = service.inscription("Ben");
        Utilisateur utilisateur2 = service.inscription("Jude");
        Assert.assertNotNull(utilisateur1);
        service.enleverAmi(utilisateur1.getId(),utilisateur2.getId());
    }

    @Test
    public void ajouterInvitationAmiOK(){
        Utilisateur utilisateur1 = service.inscription("Laqueuille");
        Utilisateur utilisateur2 = service.inscription("Saguaro");
        this.service.ajouterInvitationAmi(utilisateur1.getId(),utilisateur2.getId());
        utilisateur1 = this.service.getUtilisateurById(utilisateur1.getId());
        Assert.assertEquals(1,utilisateur1.getInvitationsAmis().size());
        Assert.assertEquals(utilisateur2.getId(),utilisateur1.getInvitationsAmis().get(0).getIdUtilisateurInvitant());
        Assert.assertEquals(utilisateur1.getId(),utilisateur1.getInvitationsAmis().get(0).getIdUtilisateurInvite());
    }

    @Test
    public void ajouterInvitationSoireeOK(){
        Utilisateur utilisateur1 = service.inscription("Evian");
        this.service.ajouterInvitationSoiree(utilisateur1.getId(),1L);
        utilisateur1 = this.service.getUtilisateurById(utilisateur1.getId());
        Assert.assertEquals(1,utilisateur1.getInvitationsSoirees().size());
        Assert.assertEquals(Long.valueOf(1),utilisateur1.getInvitationsSoirees().get(0).getIdSoiree());
        Assert.assertEquals(utilisateur1.getId(),utilisateur1.getInvitationsSoirees().get(0).getUtilisateurInvite());
    }

    @Test
    public void accepterInvitationAmiOK(){
        Utilisateur utilisateur1 = service.inscription("Christaline");
        Utilisateur utilisateur2 = service.inscription("Volvic");

        this.service.ajouterInvitationAmi(utilisateur1.getId(),utilisateur2.getId());
        Assert.assertNotNull(this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsAmis());

        InvitationAmi invitationAmi = this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsAmis().get(0);
        Assert.assertFalse(invitationAmi.getIsAccepte());

        this.service.accepterInvitationAmi(utilisateur1.getId(),invitationAmi.getId());

        Assert.assertTrue(utilisateur1.getInvitationsAmis().size() == 0);
    }

    @Test
    public void supprimerInvitationAmiOK(){
        Utilisateur utilisateur1 = service.inscription("Dylan");
        Utilisateur utilisateur2 = service.inscription("Mickael");
        Utilisateur utilisateur3 = service.inscription("Elvis");
        this.service.ajouterInvitationAmi(utilisateur1.getId(),utilisateur2.getId());
        this.service.ajouterInvitationAmi(utilisateur1.getId(),utilisateur3.getId());
        Assert.assertNotNull(this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsAmis());
        List<InvitationAmi> invitationAmi = this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsAmis();
        Assert.assertEquals(2,invitationAmi.size());
        this.service.supprimerInvitationAmi(utilisateur1.getId(),invitationAmi.get(0).getId());
        invitationAmi = this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsAmis();
        Assert.assertEquals(1,invitationAmi.size());
    }

    @Test
    public void accepterInvitationSoireeOK(){
        Utilisateur utilisateur1 = service.inscription("Badoit");
        Utilisateur utilisateur2 = service.inscription("Pelegrino");

        this.service.ajouterInvitationSoiree(utilisateur1.getId(),utilisateur2.getId());
        Assert.assertNotNull(this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsSoirees());

        InvitationSoiree invitationSoiree = this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsSoirees().get(0);
        Assert.assertFalse(invitationSoiree.getIsAccepte());

        this.service.accepterInvitationSoiree(utilisateur1.getId(),invitationSoiree.getId());

        Assert.assertTrue(utilisateur1.getInvitationsSoirees().size() == 0);
    }

    @Test
    public void supprimerInvitationSoireeOK(){
        Utilisateur utilisateur1 = service.inscription("Stan");
        Utilisateur utilisateur2 = service.inscription("Eric");
        Utilisateur utilisateur3 = service.inscription("Kyle");

        this.service.ajouterInvitationSoiree(utilisateur1.getId(),utilisateur2.getId());
        this.service.ajouterInvitationSoiree(utilisateur1.getId(),utilisateur3.getId());

        Assert.assertNotNull(this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsAmis());
        List<InvitationSoiree> invitationSoirees = this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsSoirees();

        Assert.assertEquals(2,invitationSoirees.size());

        this.service.supprimerInvitationSoiree(utilisateur1.getId(),invitationSoirees.get(0).getId());
        invitationSoirees = this.service.getUtilisateurById(utilisateur1.getId()).getInvitationsSoirees();

        Assert.assertEquals(1,invitationSoirees.size());
    }

}
