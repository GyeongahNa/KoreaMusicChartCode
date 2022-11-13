package KMC.KoreaMusicChart.repository.Artist;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FloArtistRepository {

    private final EntityManager em;

    public void save(FloArtist floArtist) {
        em.persist(floArtist);
    }

    public FloArtist findOne(Long id) {
        return em.find(FloArtist.class, id);
    }

    public List<FloArtist> findAll() {
        return em.createQuery("select m from FloArtist m", FloArtist.class).getResultList();
    }

    public List<FloArtist> findByName(String name) {
        return em.createQuery("select m from FloArtist m where m.name = :name", FloArtist.class)
                .setParameter("name", name)
                .getResultList();
    }
}