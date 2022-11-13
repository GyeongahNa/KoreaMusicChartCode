package KMC.KoreaMusicChart.domain.Chart;

import KMC.KoreaMusicChart.domain.Song.MelonSong;
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
public class MelonChart {

    @Id
    @GeneratedValue
    @Column(name = "melon_chart_id")
    private Long id;

    private LocalDateTime dateTime;
    private int curRank;
    private int prevRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "melon_song_id")
    private MelonSong melonSong;

    //==생성 메서드==//
    public static MelonChart createMelonChart(LocalDateTime dateTime, int curRank, int prevRank, MelonSong melonSong) {
        MelonChart melonChart = new MelonChart();
        melonChart.dateTime = dateTime;
        melonChart.curRank = curRank;
        melonChart.prevRank = prevRank;
        melonChart.melonSong = melonSong;
        return melonChart;
    }
}
