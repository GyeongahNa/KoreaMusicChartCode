package KMC.KoreaMusicChart.repository.Chart;

import KMC.KoreaMusicChart.domain.Chart.MelonChart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MelonChartRepository {

    private final EntityManager em;

    public void save(MelonChart melonChart) {
        em.persist(melonChart);
    }

    public MelonChart findOne(Long id) {
        return em.find(MelonChart.class, id);
    }

    public List<MelonChart> findAll() {
        return em.createQuery("select m from MelonChart m", MelonChart.class).getResultList();
    }

    public List<MelonChart> findByDateTime(LocalDateTime dateTime) {
        return em.createQuery("select m from MelonChart m where m.dateTime = :dateTime", MelonChart.class)
                .setParameter("dateTime", dateTime)
                .getResultList();
    }

    public List<MelonChart> findByDateTimeAndCurRank(LocalDateTime dateTime, int curRank) {
        return em.createQuery("select m from MelonChart m where m.dateTime = :dateTime and m.curRank = :curRank", MelonChart.class)
                .setParameter("dateTime", dateTime)
                .setParameter("curRank", curRank)
                .getResultList();
    }
}
