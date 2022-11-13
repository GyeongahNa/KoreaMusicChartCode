package KMC.KoreaMusicChart.repository.Song;

import KMC.KoreaMusicChart.domain.Song.MelonSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MelonSongRepository {

    private final EntityManager em;

    public void save(MelonSong melonSong) {
        em.persist(melonSong);
    }

    public MelonSong findOne(Long id) {
        return em.find(MelonSong.class, id);
    }

    public List<MelonSong> findAll() {
        return em.createQuery("select s from MelonSong s", MelonSong.class).getResultList();
    }

    public List<MelonSong> findByNameAndAlbum(String name, String album) {
        return em.createQuery("select s from MelonSong s where s.name = :name and s.album = :album", MelonSong.class)
                .setParameter("name", name)
                .setParameter("album", album)
                .getResultList();
    }
}