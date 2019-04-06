package authentification.backend.facade;

import authentification.backend.modele.entities.User;
import authentification.backend.modele.exception.IdentifiantsIncorrectsException;
import authentification.backend.modele.exception.PseudoDejaPrisException;
import authentification.backend.modele.exception.UserIntrouvableException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthentificationServiceTest {

    @Autowired
    private AuthentificationService service;

    @Test
    public void connexionOK() {
        this.service.inscription("michel","Michel123");
        this.service.connexion("michel", "Michel123");
        Assert.assertTrue(this.service.getUtilisateurByPseudo("michel").getActive());
    }

    @Test(expected = UserIntrouvableException.class)
    public void connexionUserIntrouvableExceptionKO() {
        this.service.connexion("bobby","Bobby123");
    }

    @Test(expected = IdentifiantsIncorrectsException.class)
    public void connexionIdentifiantsIncorrectsExceptionKO() {
        this.service.inscription("mike","Mike123");
        this.service.connexion("mike","Mike12");
        this.service.connexion("mickey","Mike123");
        this.service.connexion("mickey","Mike12");
    }

    @Test
    public void deconnexionOK() {
        this.service.inscription("jules","Jules123");
        this.service.connexion("jules","Jules123");
        this.service.deconnexion("jules");
        Assert.assertFalse(this.service.getUtilisateurByPseudo("jules").getActive());
    }

    @Test(expected = UserIntrouvableException.class)
    public void deconnexionUserIntrouvableExceptionKO() {
        this.service.deconnexion("fabrice");
    }

    @Test
    public void inscriptionOK() {
        User user = this.service.inscription("sharleyne","Sharleyne123");
        Assert.assertNotNull(user);
        Assert.assertEquals("sharleyne",user.getUsername());
        Assert.assertEquals("Sharleyne123",user.getPassword());
        Assert.assertEquals(this.service.getUtilisateurByPseudo(user.getUsername()), user);
    }

    @Test(expected = PseudoDejaPrisException.class)
    public void inscriptionKO() {
        this.service.inscription("maxime", "Maxime123");
        this.service.inscription("maxime", "Maxime123");
    }

    @Test
    public void getUtilisateurByPseudoOK() {
        User user = this.service.inscription("garry","Garry123");
        User user2 = this.service.getUtilisateurByPseudo("garry");
        Assert.assertNotNull(user2);
        Assert.assertEquals(user,user2);
    }

    @Test(expected = UserIntrouvableException.class)
    public void getUtilisateurByPseudoUserIntrouvableExceptionKO() {
        this.service.getUtilisateurByPseudo("belinda");
    }

    @Test(expected = UserIntrouvableException.class)
    public void deleteUtilisateurOK() {
        User user = this.service.inscription("levent","Daniel123");
        this.service.deleteUtilisateur(user.getUsername());
        Assert.assertNull(this.service.getUtilisateurByPseudo("levent"));
    }

    @Test(expected = UserIntrouvableException.class)
    public void deleteUtilisateurUserIntrouvableExceptionKO() {
        this.service.deleteUtilisateur("doug");
    }

    @Test
    public void updateUserOK() {
        User user = this.service.inscription("marie","Marie123");
        user.setUsername("marc");
        user.setPassword("Marc123");
        user.setActive(true);
        user.setRole("ADMIN");
        this.service.updateUser(user);
        User user1 = this.service.getUtilisateurByPseudo("marc");
        Assert.assertNotNull(user1);
        Assert.assertEquals("marc",user1.getUsername());
        Assert.assertEquals("Marc123",user1.getPassword());
        Assert.assertTrue("marc",user1.getActive());
        Assert.assertEquals("ADMIN",user1.getRole());
    }
}
