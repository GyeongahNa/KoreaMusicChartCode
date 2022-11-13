package KMC.KoreaMusicChart.domain.Chart;

import KMC.KoreaMusicChart.domain.Song.GenieSong;
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
public class GenieChart {

    @Id
    @GeneratedValue
    @Column(name = "genie_chart_id")
    private Long id;

    private LocalDateTime dateTime;
    private int curRank;
    private int prevRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genie_song_id")
    private GenieSong genieSong;

    //==생성 메서드==//
    public static GenieChart createGenieChart(LocalDateTime dateTime, int curRank, int prevRank, GenieSong genieSong) {
        GenieChart genieChart = new GenieChart();
        genieChart.dateTime = dateTime;
        genieChart.curRank = curRank;
        genieChart.prevRank = prevRank;
        genieChart.genieSong = genieSong;
        return genieChart;
    }
}
