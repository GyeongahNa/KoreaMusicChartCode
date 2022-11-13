package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Chart.MelonChart;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import KMC.KoreaMusicChart.service.Artist.MelonArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.MelonArtistSongService;
import KMC.KoreaMusicChart.service.Song.MelonSongService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static KMC.KoreaMusicChart.domain.Chart.MelonChart.createMelonChart;
import static KMC.KoreaMusicChart.domain.Song.MelonSong.createMelonSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class MelonChartServiceTest {

    @Autowired
    private MelonChartService melonChartService;
    @Autowired
    private MelonArtistService melonArtistService;
    @Autowired
    private MelonSongService melonSongService;
    @Autowired
    private MelonArtistSongService melonArtistSongService;

    @Test
    public void 차트정보저장() throws Exception {
        //given
        MelonSong song = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        melonSongService.save(song);

        //when
        MelonChart melonChart = createMelonChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song);
        Long saveId = melonChartService.save(melonChart);
        MelonChart findMelonChart = melonChartService.findOne(saveId);

        //then
        assertEquals(melonChart, findMelonChart);
    }

    @Test(expected = IllegalStateException.class)
    public void 차트_중복_예외() throws Exception {
        //given
        MelonSong song1 = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        MelonSong song2 = createMelonSong((long) 12345, "레옹", "무한도전 앨범", "imageUrlExample");

        melonSongService.save(song1);
        melonSongService.save(song2);

        MelonChart melonChart1 = createMelonChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        MelonChart melonChart2 = createMelonChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 4, song2);

        //when
        melonChartService.save(melonChart1);
        melonChartService.save(melonChart2);

        //then
        fail("MelonChart 중복 저장 에러가 발생해야 함");
    }

    @Test
    public void 날짜와_시각으로_차트정보_조회() throws Exception {
        //given
        MelonSong song1 = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        MelonSong song2 = createMelonSong((long) 12346, "레옹", "무한도전 앨범", "imageUrlExample");
        MelonSong song3 = createMelonSong((long) 12347, "바람났어", "무한도전 앨범", "imageUrlExample");
        MelonSong song4 = createMelonSong((long) 12348, "냉면", "무한도전 앨범", "imageUrlExample");

        melonSongService.save(song1);
        melonSongService.save(song2);
        melonSongService.save(song3);
        melonSongService.save(song4);

        MelonChart melonChart1 = createMelonChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        MelonChart melonChart2 = createMelonChart(LocalDateTime.of(2022, 10, 16, 10, 0), 2, 4, song2);
        MelonChart melonChart3 = createMelonChart(LocalDateTime.of(2022, 10, 16, 10, 0), 3, 3, song3);
        MelonChart melonChart4 = createMelonChart(LocalDateTime.of(2022, 10, 16, 11, 0), 1, 3, song3);
        MelonChart melonChart5 = createMelonChart(LocalDateTime.of(2022, 10, 16, 11, 0), 2, 3, song4);

        melonChartService.save(melonChart1);
        melonChartService.save(melonChart2);
        melonChartService.save(melonChart3);
        melonChartService.save(melonChart4);
        melonChartService.save(melonChart5);

        //when
        List<MelonChart> findMelonChart1 = melonChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 10, 0));
        List<MelonChart> findMelonChart2 = melonChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 11, 0));

        //then
        assertEquals(3, findMelonChart1.size());
        assertEquals(2, findMelonChart2.size());
    }

//    @Test
//    public void 크롤링() throws Exception {
//
//        melonChartService.scrap();
//
//        //given
//
//        //when
//
//        //then
//    }
}