package KMC.KoreaMusicChart.repository.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenieArtistSongRepository {

    private final EntityManager em;

    public void save(GenieArtistSong genieArtistSong) {
        em.persist(genieArtistSong);
    }

    public GenieArtistSong findOne(Long id) {
        return em.find(GenieArtistSong.class, id);
    }

    public List<GenieArtistSong> findAll() {
        return em.createQuery("select a from GenieArtistSong a", GenieArtistSong.class).getResultList();
    }

    public List<GenieArtistSong> findByArtistAndSong(GenieArtist genieArtist, GenieSong genieSong) {
        return em.createQuery("select a from GenieArtistSong a where a.genieArtist = :genieArtist and a.genieSong = :genieSong", GenieArtistSong.class)
                .setParameter("genieArtist", genieArtist)
                .setParameter("genieSong", genieSong)
                .getResultList();
    }
}