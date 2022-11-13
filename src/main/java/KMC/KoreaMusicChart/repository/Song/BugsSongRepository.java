package KMC.KoreaMusicChart.repository.Song;

import KMC.KoreaMusicChart.domain.Song.BugsSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BugsSongRepository {

    private final EntityManager em;

    public void save(BugsSong bugsSong) {
        em.persist(bugsSong);
    }

    public BugsSong findOne(Long id) {
        return em.find(BugsSong.class, id);
    }

    public List<BugsSong> findAll() {
        return em.createQuery("select s from BugsSong s", BugsSong.class).getResultList();
    }

    public List<BugsSong> findByNameAndAlbum(String name, String album) {
        return em.createQuery("select s from BugsSong s where s.name = :name and s.album = :album", BugsSong.class)
                .setParameter("name", name)
                .setParameter("album", album)
                .getResultList();
    }
}