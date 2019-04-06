package evenement.backend.facade;

import evenement.backend.modele.entities.Evenement;
import evenement.backend.modele.exception.EvenementIntrouvableException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EvenementServiceTest {

    @Autowired
    private EvenementService service;

    @Test
    public void creerEvenementOK() {
        Evenement evenement = service.creerEvenement("monEvent","1",LocalDate.of(2019,3,21),"Olivet");
        Assert.assertNotNull(evenement);
        Assert.assertEquals("monEvent", evenement.getNom());
        Assert.assertEquals(LocalDate.of(2019,3,21), evenement.getDateEvenement());
        Assert.assertEquals("Olivet", evenement.getLieu());
    }

    @Test
    public void updateEvenementOK() {
        Evenement evenement = service.creerEvenement("monEvent","1",LocalDate.of(2019,3,21),"Olivet");
        Assert.assertNotNull(evenement);

        evenement.setNom("monEvent2");
        evenement.setDateEvenement(LocalDate.of(2019,4,21));
        evenement.setLieu("Orléans");

        Evenement evenementUpdated = service.updateEvenement(evenement);

        Assert.assertEquals("monEvent2", evenementUpdated.getNom());
        Assert.assertEquals(LocalDate.of(2019,4,21), evenementUpdated.getDateEvenement());
        Assert.assertEquals("Orléans", evenementUpdated.getLieu());
    }


    @Test(expected = EvenementIntrouvableException.class)
    public void deleteEvenementOK() {
        Evenement evenement = service.creerEvenement("monEvent","1",LocalDate.of(2019,3,21),"Olivet");
        Assert.assertNotNull(evenement);
        service.deleteEvenement(evenement.getId());
        service.getEvenementById(evenement.getId());
    }

    @Test
    public void getUtilisateurByEvenementOK() {
        Evenement evenement = service.creerEvenement("monEvent","1",LocalDate.of(2019,3,21),"Olivet");
        Assert.assertNotNull(evenement);
        evenement.ajouterParticipant(1L);
        evenement.ajouterParticipant(2L);
        this.service.updateEvenement(evenement);
        List<Long> participants = this.service.getUtilisateurByEvenement(evenement.getId());
        Assert.assertEquals(2, participants.size());
        Assert.assertEquals(Long.valueOf(1L), participants.get(0));
        Assert.assertEquals(Long.valueOf(2L), participants.get(1));
        Assert.assertEquals(this.service.getEvenementById(evenement.getId()).getParticipants().size(),participants.size());
    }

}
