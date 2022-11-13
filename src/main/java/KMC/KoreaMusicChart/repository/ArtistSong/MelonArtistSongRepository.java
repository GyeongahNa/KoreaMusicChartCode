package KMC.KoreaMusicChart.repository.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MelonArtistSongRepository {

    private final EntityManager em;

    public void save(MelonArtistSong melonArtistSong) {
        em.persist(melonArtistSong);
    }

    public MelonArtistSong findOne(Long id) {
        return em.find(MelonArtistSong.class, id);
    }

    public List<MelonArtistSong> findAll() {
        return em.createQuery("select a from MelonArtistSong a", MelonArtistSong.class).getResultList();
    }

    public List<MelonArtistSong> findByArtistAndSong(MelonArtist melonArtist, MelonSong melonSong) {
        return em.createQuery("select a from MelonArtistSong a where a.melonArtist = :melonArtist and a.melonSong = :melonSong", MelonArtistSong.class)
                .setParameter("melonArtist", melonArtist)
                .setParameter("melonSong", melonSong)
                .getResultList();
    }
}