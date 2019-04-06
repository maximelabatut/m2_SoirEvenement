package evenementOpenAgenda.backend.facade;

import evenementOpenAgenda.backend.modele.entities.EvenementOpenAgenda;
import evenementOpenAgenda.backend.modele.exception.EvenementOpenAgendaIntrouvableException;
import evenementOpenAgenda.backend.repository.EvenementOpenAgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
public class EvenementOpenAgendaServiceImpl implements EvenementOpenAgendaService {

    @Autowired
    private RestTemplate restTemplate;

    private EvenementOpenAgendaRepository evenementOpenAgendaRepository;

    public EvenementOpenAgendaServiceImpl(EvenementOpenAgendaRepository evenementOpenAgendaRepository) {
        this.evenementOpenAgendaRepository = evenementOpenAgendaRepository;
        restTemplate = new RestTemplate();
    }

    /**
     * Recuperation de tous les evenements
     *
     * @return les evenements
     */
    @Override
    public Iterable<EvenementOpenAgenda> getAllEvenement() {
        this.init();
        return this.evenementOpenAgendaRepository.findAll();
    }

    /**
     * Recuperation d'un évenement
     *
     * @param uid numéro de l'évenement
     * @return un évenement
     */
    @Override
    public EvenementOpenAgenda getEvenementById(String uid) {
        this.init();
        EvenementOpenAgenda evenement = this.evenementOpenAgendaRepository.findEvenementOpenAgendaByUid(uid);
        if (evenement == null){
            throw new EvenementOpenAgendaIntrouvableException();
        }
        return evenement;
    }

    /**
     * Recuperation des participants
     *
     * @param uid numéro de l'évenement
     * @return un évenement
     */

    @Override
    @Transactional(readOnly = true)
    public Collection<Long> getUtilisateurByEvenement(String uid){
        return this.evenementOpenAgendaRepository.findEvenementOpenAgendaByUid(uid).getParticipants();
    }

    private void init(){
        HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>> map = restTemplate.getForObject(
                "https://data.orleans-metropole.fr/api/records/1.0/search/?dataset=evenements-publics-openagenda&facet=tags&facet=placename&facet=department&facet=region&facet=city&facet=date_start&facet=date_end&facet=pricing_info&facet=updated_at&facet=city_district",
                HashMap.class);

        ArrayList<HashMap<String, HashMap<String, String>>> tableau = map.get("records");

        for(int i=0; i<tableau.size(); i++){

            HashMap<String, HashMap<String, String>> records = tableau.get(i);

            HashMap<String, String> fields = records.get("fields");

            String latlon = "";
            //String latlon = (String) fields.get("latlon").toString();
            String lang = fields.get("lang");
            String title = fields.get("title");
            String uid = fields.get("uid");
            String placename = fields.get("placename");
            String pricing_info = fields.get("pricing_info");
            String image = fields.get("image");
            String date_start = fields.get("date_start");
            String updated_at = fields.get("updated_at");
            String space_time_info = fields.get("space_time_info");
            String department = fields.get("department");
            String city = fields.get("city");
            String link = fields.get("link");
            String free_text = fields.get("free_text");
            String address = fields.get("address");
            String timetable = fields.get("timetable");
            String image_thumb = fields.get("image_thumb");
            String region = fields.get("region");
            String date_end = fields.get("date_end");
            String tags = fields.get("tags");
            String description = fields.get("description");

            EvenementOpenAgenda evenementOpenAgenda = new EvenementOpenAgenda(Long.valueOf(i), latlon, lang, title, uid, placename,
                    pricing_info, image, date_start, updated_at, space_time_info, department, city, link,
                    free_text, address, timetable, image_thumb, region, date_end, tags, description);

            this.evenementOpenAgendaRepository.save(evenementOpenAgenda);
        }
    }

    @Override
    public EvenementOpenAgenda updateEvenement(EvenementOpenAgenda evenement) {
        return this.evenementOpenAgendaRepository.save(evenement);
    }


}

