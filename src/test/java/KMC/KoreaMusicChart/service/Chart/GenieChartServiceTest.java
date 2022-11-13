package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Chart.GenieChart;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import KMC.KoreaMusicChart.service.Artist.GenieArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.GenieArtistSongService;
import KMC.KoreaMusicChart.service.Song.GenieSongService;
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

import static KMC.KoreaMusicChart.domain.Chart.GenieChart.createGenieChart;
import static KMC.KoreaMusicChart.domain.Song.GenieSong.createGenieSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class GenieChartServiceTest {

    @Autowired
    private GenieChartService genieChartService;
    @Autowired
    private GenieArtistService genieArtistService;
    @Autowired
    private GenieSongService genieSongService;
    @Autowired
    private GenieArtistSongService genieArtistSongService;

    @Test
    public void 차트정보저장() throws Exception {
        //given
        GenieSong genieSong = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        genieSongService.save(genieSong);

        //when
        GenieChart genieChart = createGenieChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, genieSong);
        Long saveId = genieChartService.save(genieChart);
        GenieChart findGenieChart = genieChartService.findOne(saveId);

        //then
        assertEquals(genieChart, findGenieChart);
    }

    @Test(expected = IllegalStateException.class)
    public void 차트_중복_예외() throws Exception {
        //given
        GenieSong genieSong1 = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        GenieSong genieSong2 = createGenieSong((long) 12345, "레옹", "무한도전 앨범", "imageUrlExample");

        genieSongService.save(genieSong1);
        genieSongService.save(genieSong2);

        GenieChart genieChart1 = createGenieChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, genieSong1);
        GenieChart genieChart2 = createGenieChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 4, genieSong2);

        //when
        genieChartService.save(genieChart1);
        genieChartService.save(genieChart2);

        //then
        fail("MelonChart 중복 저장 에러가 발생해야 함");
    }

    @Test
    public void 날짜와_시각으로_차트정보_조회() throws Exception {
        //given
        GenieSong song1 = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        GenieSong song2 = createGenieSong((long) 12346, "레옹", "무한도전 앨범", "imageUrlExample");
        GenieSong song3 = createGenieSong((long) 12347, "바람났어", "무한도전 앨범", "imageUrlExample");
        GenieSong song4 = createGenieSong((long) 12348, "냉면", "무한도전 앨범", "imageUrlExample");

        genieSongService.save(song1);
        genieSongService.save(song2);
        genieSongService.save(song3);
        genieSongService.save(song4);

        GenieChart genieChart1 = createGenieChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        GenieChart genieChart2 = createGenieChart(LocalDateTime.of(2022, 10, 16, 10, 0), 2, 4, song2);
        GenieChart genieChart3 = createGenieChart(LocalDateTime.of(2022, 10, 16, 10, 0), 3, 3, song3);
        GenieChart genieChart4 = createGenieChart(LocalDateTime.of(2022, 10, 16, 11, 0), 1, 3, song3);
        GenieChart genieChart5 = createGenieChart(LocalDateTime.of(2022, 10, 16, 11, 0), 2, 3, song4);

        genieChartService.save(genieChart1);
        genieChartService.save(genieChart2);
        genieChartService.save(genieChart3);
        genieChartService.save(genieChart4);
        genieChartService.save(genieChart5);

        //when
        List<GenieChart> findGenieChart1 = genieChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 10, 0));
        List<GenieChart> findGenieChart2 = genieChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 11, 0));

        //then
        assertEquals(3, findGenieChart1.size());
        assertEquals(2, findGenieChart2.size());
    }

//    @Test
//    public void 크롤링() throws Exception {
//
//        genieChartService.scrap();
//
//        //given
//
//        //when
//
//        //then
//    }
}