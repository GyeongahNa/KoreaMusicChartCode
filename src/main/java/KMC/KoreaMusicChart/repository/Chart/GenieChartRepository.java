package KMC.KoreaMusicChart.repository.Chart;

import KMC.KoreaMusicChart.domain.Chart.GenieChart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenieChartRepository {

    private final EntityManager em;

    public void save(GenieChart genieChart) {
        em.persist(genieChart);
    }

    public GenieChart findOne(Long id) {
        return em.find(GenieChart.class, id);
    }

    public List<GenieChart> findAll() {
        return em.createQuery("select m from GenieChart m", GenieChart.class).getResultList();
    }

    public List<GenieChart> findByDateTime(LocalDateTime dateTime) {
        return em.createQuery("select m from GenieChart m where m.dateTime = :dateTime", GenieChart.class)
                .setParameter("dateTime", dateTime)
                .getResultList();
    }

    public List<GenieChart> findByDateTimeAndCurRank(LocalDateTime dateTime, int curRank) {
        return em.createQuery("select m from GenieChart m where m.dateTime = :dateTime and m.curRank = :curRank", GenieChart.class)
                .setParameter("dateTime", dateTime)
                .setParameter("curRank", curRank)
                .getResultList();
    }
}
