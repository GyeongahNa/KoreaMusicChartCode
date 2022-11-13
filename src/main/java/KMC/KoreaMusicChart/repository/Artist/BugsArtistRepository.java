package KMC.KoreaMusicChart.repository.Artist;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BugsArtistRepository {

    private final EntityManager em;

    public void save(BugsArtist bugsArtist) {
        em.persist(bugsArtist);
    }

    public BugsArtist findOne(Long id) {
        return em.find(BugsArtist.class, id);
    }

    public List<BugsArtist> findAll() {
        return em.createQuery("select m from BugsArtist m", BugsArtist.class).getResultList();
    }

    public List<BugsArtist> findByNameAndDebutDate(String name, String debutDate) {
        return em.createQuery("select m from BugsArtist m where m.name = :name and m.debutDate = :debutDate", BugsArtist.class)
                .setParameter("name", name)
                .setParameter("debutDate", debutDate)
                .getResultList();
    }
}