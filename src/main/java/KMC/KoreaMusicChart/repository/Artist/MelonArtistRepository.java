package KMC.KoreaMusicChart.repository.Artist;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MelonArtistRepository {

    private final EntityManager em;

    public void save(MelonArtist melonArtist) {
        em.persist(melonArtist);
    }

    public MelonArtist findOne(Long id) {
        return em.find(MelonArtist.class, id);
    }

    public List<MelonArtist> findAll() {
        return em.createQuery("select m from MelonArtist m", MelonArtist.class).getResultList();
    }

    public List<MelonArtist> findByNameAndDebutDate(String name, String debutDate) {
        return em.createQuery("select m from MelonArtist m where m.name = :name and m.debutDate = :debutDate", MelonArtist.class)
                .setParameter("name", name)
                .setParameter("debutDate", debutDate)
                .getResultList();
    }
}