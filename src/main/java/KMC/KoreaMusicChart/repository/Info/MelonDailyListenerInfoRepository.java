package KMC.KoreaMusicChart.repository.Info;

import KMC.KoreaMusicChart.domain.Info.MelonDailyListenerInfo;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MelonDailyListenerInfoRepository {

    private final EntityManager em;

    public void save(MelonDailyListenerInfo melonDailyListenerInfo) {
        em.persist(melonDailyListenerInfo);
    }

    public MelonDailyListenerInfo findOne(Long id) {
        return em.find(MelonDailyListenerInfo.class, id);
    }

    public List<MelonDailyListenerInfo> findAll() {
        return em.createQuery("select m from MelonDailyListenerInfo m", MelonDailyListenerInfo.class).getResultList();
    }

    public List<MelonDailyListenerInfo> findByDateAndSong(LocalDate date, MelonSong melonSong) {

        return em.createQuery("select m from MelonDailyListenerInfo m where m.date = :date and m.melonSong = :melonSong", MelonDailyListenerInfo.class)
                .setParameter("date", date)
                .setParameter("melonSong", melonSong)
                .getResultList();
    }

    //특정 일자 간격 동안 특정 곡의 청취 정보 가져오기
    public List<MelonDailyListenerInfo> findByDateAndIntervalAndSong(LocalDate date, int dayInterval, MelonSong melonSong) {

        LocalDate startDate = date.minusDays(dayInterval);
        return em.createQuery("select m from MelonDailyListenerInfo m where m.date >= :startDate and m.date <= :date and m.melonSong = :melonSong", MelonDailyListenerInfo.class)
                .setParameter("startDate", startDate)
                .setParameter("date", date)
                .setParameter("melonSong", melonSong)
                .getResultList();
    }
}
