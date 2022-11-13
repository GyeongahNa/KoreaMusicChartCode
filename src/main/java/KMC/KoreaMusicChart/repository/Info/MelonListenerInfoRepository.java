package KMC.KoreaMusicChart.repository.Info;

import KMC.KoreaMusicChart.domain.Info.MelonListenerInfo;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MelonListenerInfoRepository {

    private final EntityManager em;

    public void save(MelonListenerInfo melonListenerInfo) {
        em.persist(melonListenerInfo);
    }

    public MelonListenerInfo findOne(Long id) {
        return em.find(MelonListenerInfo.class, id);
    }

    public List<MelonListenerInfo> findAll() {
        return em.createQuery("select m from MelonListenerInfo m", MelonListenerInfo.class).getResultList();
    }

    public List<MelonListenerInfo> findByDateTimeAndSong(LocalDateTime dateTime, MelonSong melonSong) {

        return em.createQuery("select m from MelonListenerInfo m where m.dateTime = :dateTime and m.melonSong = :melonSong", MelonListenerInfo.class)
                .setParameter("dateTime", dateTime)
                .setParameter("melonSong", melonSong)
                .getResultList();
    }

    //특정 시간 간격 동안 특정 곡의 청취 정보 가져오기
    public List<MelonListenerInfo> findByDateTimeAndIntervalAndSong(LocalDateTime dateTime, int hourInterval, MelonSong melonSong) {

        LocalDateTime startDateTime = dateTime.minusHours(hourInterval);
        return em.createQuery("select m from MelonListenerInfo m where m.dateTime >= :startDateTime and m.dateTime <= :dateTime and m.melonSong = :melonSong", MelonListenerInfo.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("dateTime", dateTime)
                .setParameter("melonSong", melonSong)
                .getResultList();
    }
}
