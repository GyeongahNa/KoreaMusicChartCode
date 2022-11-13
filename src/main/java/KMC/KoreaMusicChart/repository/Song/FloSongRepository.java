package KMC.KoreaMusicChart.repository.Song;

import KMC.KoreaMusicChart.domain.Song.FloSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FloSongRepository {

    private final EntityManager em;

    public void save(FloSong floSong) {
        em.persist(floSong);
    }

    public FloSong findOne(Long id) {
        return em.find(FloSong.class, id);
    }

    public List<FloSong> findAll() {
        return em.createQuery("select s from FloSong s", FloSong.class).getResultList();
    }

    public List<FloSong> findByNameAndAlbum(String name, String album) {
        return em.createQuery("select s from FloSong s where s.name = :name and s.album = :album", FloSong.class)
                .setParameter("name", name)
                .setParameter("album", album)
                .getResultList();
    }
}