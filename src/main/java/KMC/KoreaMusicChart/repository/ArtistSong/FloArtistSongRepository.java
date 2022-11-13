package KMC.KoreaMusicChart.repository.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong;
import KMC.KoreaMusicChart.domain.Song.FloSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FloArtistSongRepository {

    private final EntityManager em;

    public void save(FloArtistSong floArtistSong) {
        em.persist(floArtistSong);
    }

    public FloArtistSong findOne(Long id) {
        return em.find(FloArtistSong.class, id);
    }

    public List<FloArtistSong> findAll() {
        return em.createQuery("select a from FloArtistSong a", FloArtistSong.class).getResultList();
    }

    public List<FloArtistSong> findByArtistAndSong(FloArtist floArtist, FloSong floSong) {
        return em.createQuery("select a from FloArtistSong a where a.floArtist = :floArtist and a.floSong = :floSong", FloArtistSong.class)
                .setParameter("floArtist", floArtist)
                .setParameter("floSong", floSong)
                .getResultList();
    }
}