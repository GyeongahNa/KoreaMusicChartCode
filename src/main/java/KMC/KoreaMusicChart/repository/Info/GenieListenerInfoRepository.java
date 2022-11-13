package KMC.KoreaMusicChart.repository.Info;

import KMC.KoreaMusicChart.domain.Info.GenieListenerInfo;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenieListenerInfoRepository {

    private final EntityManager em;

    public void save(GenieListenerInfo genieListenerInfo) {
        em.persist(genieListenerInfo);
    }

    public GenieListenerInfo findOne(Long id) {
        return em.find(GenieListenerInfo.class, id);
    }

    public List<GenieListenerInfo> findAll() {
        return em.createQuery("select m from GenieListenerInfo m", GenieListenerInfo.class).getResultList();
    }

    public List<GenieListenerInfo> findByDateTimeAndSong(LocalDateTime dateTime, GenieSong genieSong) {

        return em.createQuery("select m from GenieListenerInfo m where m.dateTime = :dateTime and m.genieSong = :genieSong", GenieListenerInfo.class)
                .setParameter("dateTime", dateTime)
                .setParameter("genieSong", genieSong)
                .getResultList();
    }

    //특정 시간 간격 동안 특정 곡의 청취 정보 가져오기
    public List<GenieListenerInfo> findByDateTimeAndIntervalAndSong(LocalDateTime dateTime, int hourInterval, GenieSong genieSong) {

        LocalDateTime startDateTime = dateTime.minusHours(hourInterval);
        return em.createQuery("select m from GenieListenerInfo m where m.dateTime >= :startDateTime and m.dateTime <= :dateTime and m.genieSong = :genieSong", GenieListenerInfo.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("dateTime", dateTime)
                .setParameter("genieSong", genieSong)
                .getResultList();
    }
}
