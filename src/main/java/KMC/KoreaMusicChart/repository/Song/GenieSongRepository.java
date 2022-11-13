package KMC.KoreaMusicChart.repository.Song;

import KMC.KoreaMusicChart.domain.Song.GenieSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenieSongRepository {

    private final EntityManager em;

    public void save(GenieSong genieSong) {
        em.persist(genieSong);
    }

    public GenieSong findOne(Long id) {
        return em.find(GenieSong.class, id);
    }

    public List<GenieSong> findAll() {
        return em.createQuery("select s from GenieSong s", GenieSong.class).getResultList();
    }

    public List<GenieSong> findByNameAndAlbum(String name, String album) {
        return em.createQuery("select s from GenieSong s where s.name = :name and s.album = :album", GenieSong.class)
                .setParameter("name", name)
                .setParameter("album", album)
                .getResultList();
    }
}