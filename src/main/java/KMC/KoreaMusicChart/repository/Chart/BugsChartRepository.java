package KMC.KoreaMusicChart.repository.Chart;

import KMC.KoreaMusicChart.domain.Chart.BugsChart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BugsChartRepository {

    private final EntityManager em;

    public void save(BugsChart bugsChart) {
        em.persist(bugsChart);
    }

    public BugsChart findOne(Long id) {
        return em.find(BugsChart.class, id);
    }

    public List<BugsChart> findAll() {
        return em.createQuery("select m from BugsChart m", BugsChart.class).getResultList();
    }

    public List<BugsChart> findByDateTime(LocalDateTime dateTime) {
        return em.createQuery("select m from BugsChart m where m.dateTime = :dateTime", BugsChart.class)
                .setParameter("dateTime", dateTime)
                .getResultList();
    }

    public List<BugsChart> findByDateTimeAndCurRank(LocalDateTime dateTime, int curRank) {
        return em.createQuery("select m from BugsChart m where m.dateTime = :dateTime and m.curRank = :curRank", BugsChart.class)
                .setParameter("dateTime", dateTime)
                .setParameter("curRank", curRank)
                .getResultList();
    }
}
