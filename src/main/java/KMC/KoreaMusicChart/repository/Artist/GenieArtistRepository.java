package KMC.KoreaMusicChart.repository.Artist;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenieArtistRepository {

    private final EntityManager em;

    public void save(GenieArtist genieArtist) {
        em.persist(genieArtist);
    }

    public GenieArtist findOne(Long id) {
        return em.find(GenieArtist.class, id);
    }

    public List<GenieArtist> findAll() {
        return em.createQuery("select m from GenieArtist m", GenieArtist.class).getResultList();
    }

    public List<GenieArtist> findByNameAndDebutDate(String name, String debutDate) {
        return em.createQuery("select m from GenieArtist m where m.name = :name and m.debutDate = :debutDate", GenieArtist.class)
                .setParameter("name", name)
                .setParameter("debutDate", debutDate)
                .getResultList();
    }
}