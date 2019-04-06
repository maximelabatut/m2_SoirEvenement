package evenementOpenAgenda.backend.modele.entities;

import lombok.Data;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@ToString(exclude = "participants")
public class EvenementOpenAgenda implements Serializable {

    @Id
    private Long id;
    private String latlon;
    private String lang;
    private String title;
    private String uid;
    private String placename;
    private String pricing_info;
    private String image;
    private String date_start;
    private String updated_at;
    private String space_time_info;
    private String department;
    private String city;
    private String link;
    @Size(max = 5000)
    private String free_text;
    private String address;
    @Size(max = 5000)
    private String timetable;
    private String image_thumb;
    private String region;
    private String date_end;
    private String tags;
    private String description;

    @ElementCollection
    @JsonIgnore
    private List<Long> participants;

    public EvenementOpenAgenda(Long id, String latlon, String lang, String title, String uid, String placename, String pricing_info, String image, String date_start, String updated_at, String space_time_info, String department, String city, String link, @Size(max = 5000) String free_text, String address, @Size(max = 5000) String timetable, String image_thumb, String region, String date_end, String tags, String description) {
        this.id = id;
        this.latlon = latlon;
        this.lang = lang;
        this.title = title;
        this.uid = uid;
        this.placename = placename;
        this.pricing_info = pricing_info;
        this.image = image;
        this.date_start = date_start;
        this.updated_at = updated_at;
        this.space_time_info = space_time_info;
        this.department = department;
        this.city = city;
        this.link = link;
        this.free_text = free_text;
        this.address = address;
        this.timetable = timetable;
        this.image_thumb = image_thumb;
        this.region = region;
        this.date_end = date_end;
        this.tags = tags;
        this.description = description;
        this.participants = new ArrayList<>();
    }

    public void ajouterParticipant(Long idUtilisateur){
        if(!participants.contains(idUtilisateur)) {
            this.participants.add(idUtilisateur);
        }
    }
}
