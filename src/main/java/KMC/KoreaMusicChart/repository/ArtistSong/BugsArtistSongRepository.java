package KMC.KoreaMusicChart.repository.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong;
import KMC.KoreaMusicChart.domain.Song.BugsSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BugsArtistSongRepository {

    private final EntityManager em;

    public void save(BugsArtistSong bugsArtistSong) {
        em.persist(bugsArtistSong);
    }

    public BugsArtistSong findOne(Long id) {
        return em.find(BugsArtistSong.class, id);
    }

    public List<BugsArtistSong> findAll() {
        return em.createQuery("select a from BugsArtistSong a", BugsArtistSong.class).getResultList();
    }

    public List<BugsArtistSong> findByArtistAndSong(BugsArtist bugsArtist, BugsSong bugsSong) {
        return em.createQuery("select a from BugsArtistSong a where a.bugsArtist = :bugsArtist and a.bugsSong = :bugsSong", BugsArtistSong.class)
                .setParameter("bugsArtist", bugsArtist)
                .setParameter("bugsSong", bugsSong)
                .getResultList();
    }
}