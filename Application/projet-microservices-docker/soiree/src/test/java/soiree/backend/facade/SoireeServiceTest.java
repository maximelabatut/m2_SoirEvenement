package soiree.backend.facade;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import soiree.backend.modele.entities.Soiree;
import soiree.backend.modele.exceptions.SoireeNotFoundException;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SoireeServiceTest {

    @Autowired
    private SoireeService service;

    @Test
    public void creerSoireeOK() {
        Soiree soiree = service.creerSoiree(new Soiree("soiree projet", 1L, 100L,  LocalDate.of(2019,3,21), "12"));
        Assert.assertNotNull(soiree);
        Assert.assertEquals("soiree projet", soiree.getNom());
    }

    @Test
    public void updateSoireeOK() {
        Soiree soiree = service.creerSoiree(new Soiree("soiree projet", 1L, 100L,  LocalDate.of(2019,3,21), "13"));
        Assert.assertNotNull(soiree);
        soiree.setNom("ma deuxieme soiree");
        Soiree soireeUpdated = service.updateSoiree(soiree);
        Assert.assertEquals("ma deuxieme soiree", soireeUpdated.getNom());
    }

    @Test(expected = SoireeNotFoundException.class)
    public void getSoireeByIdKO(){
        this.service.getSoireeById(999999999L);
    }

    @Test
    public void ajouterEvenementOK(){
        Soiree soiree = service.creerSoiree(new Soiree("soiree projet", 1L, 100L,  LocalDate.of(2019,3,21), "14"));
        this.service.ajouterEvenement(soiree.getId(),"5");
        this.service.ajouterEvenement(soiree.getId(),"7");
        this.service.updateSoiree(soiree);
        soiree = this.service.getSoireeById(soiree.getId());
        Assert.assertNotNull(soiree.getEvenements());
        Assert.assertEquals(2,soiree.getEvenements().size());
        Assert.assertEquals(String.valueOf(5),soiree.getEvenements().get(0));
        Assert.assertEquals(String.valueOf(7),soiree.getEvenements().get(1));
    }

    @Test
    public void ajouterEvenementOpenAgendaOK(){
        Soiree soiree = service.creerSoiree(new Soiree("soiree projet", 1L, 100L,  LocalDate.of(2019,3,21), "15"));
        this.service.ajouterEvenementOpenAgenda(soiree.getId(),"3");
        this.service.ajouterEvenementOpenAgenda(soiree.getId(),"6");
        this.service.updateSoiree(soiree);
        soiree = this.service.getSoireeById(soiree.getId());
        Assert.assertNotNull(soiree.getEvenementsOpenAgenda());
        Assert.assertEquals(2,soiree.getEvenementsOpenAgenda().size());
        Assert.assertEquals(String.valueOf(3),soiree.getEvenementsOpenAgenda().get(0));
        Assert.assertEquals(String.valueOf(6),soiree.getEvenementsOpenAgenda().get(1));
    }

}
