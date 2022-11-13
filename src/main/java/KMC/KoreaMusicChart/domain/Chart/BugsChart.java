package KMC.KoreaMusicChart.domain.Chart;

import KMC.KoreaMusicChart.domain.Song.BugsSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BugsChart {

    @Id
    @GeneratedValue
    @Column(name = "bugs_chart_id")
    private Long id;

    private LocalDateTime dateTime;
    private int curRank;
    private int prevRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bugs_song_id")
    private BugsSong bugsSong;

    //==생성 메서드==//
    public static BugsChart createBugsChart(LocalDateTime dateTime, int curRank, int prevRank, BugsSong bugsSong) {
        BugsChart bugsChart = new BugsChart();
        bugsChart.dateTime = dateTime;
        bugsChart.curRank = curRank;
        bugsChart.prevRank = prevRank;
        bugsChart.bugsSong = bugsSong;
        return bugsChart;
    }
}
