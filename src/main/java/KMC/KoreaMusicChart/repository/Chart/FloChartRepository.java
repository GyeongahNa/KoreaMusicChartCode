package KMC.KoreaMusicChart.repository.Chart;

import KMC.KoreaMusicChart.domain.Chart.FloChart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FloChartRepository {

    private final EntityManager em;

    public void save(FloChart floChart) {
        em.persist(floChart);
    }

    public FloChart findOne(Long id) {
        return em.find(FloChart.class, id);
    }

    public List<FloChart> findAll() {
        return em.createQuery("select m from FloChart m", FloChart.class).getResultList();
    }

    public List<FloChart> findByDateTime(LocalDateTime dateTime) {
        return em.createQuery("select m from FloChart m where m.dateTime = :dateTime", FloChart.class)
                .setParameter("dateTime", dateTime)
                .getResultList();
    }

    public List<FloChart> findByDateTimeAndCurRank(LocalDateTime dateTime, int curRank) {
        return em.createQuery("select m from FloChart m where m.dateTime = :dateTime and m.curRank = :curRank", FloChart.class)
                .setParameter("dateTime", dateTime)
                .setParameter("curRank", curRank)
                .getResultList();
    }
}
