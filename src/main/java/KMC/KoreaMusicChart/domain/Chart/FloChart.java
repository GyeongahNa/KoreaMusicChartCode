package KMC.KoreaMusicChart.domain.Chart;

import KMC.KoreaMusicChart.domain.Song.FloSong;
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
public class FloChart {

    @Id
    @GeneratedValue
    @Column(name = "flo_chart_id")
    private Long id;

    private LocalDateTime dateTime;
    private int curRank;
    private int prevRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flo_song_id")
    private FloSong floSong;

    //==생성 메서드==//
    public static FloChart createFloChart(LocalDateTime dateTime, int curRank, int prevRank, FloSong floSong) {
        FloChart floChart = new FloChart();
        floChart.dateTime = dateTime;
        floChart.curRank = curRank;
        floChart.prevRank = prevRank;
        floChart.floSong = floSong;
        return floChart;
    }
}
