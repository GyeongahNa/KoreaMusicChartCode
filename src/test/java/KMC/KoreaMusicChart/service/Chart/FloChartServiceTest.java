package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Chart.FloChart;
import KMC.KoreaMusicChart.domain.Song.FloSong;
import KMC.KoreaMusicChart.service.Artist.FloArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.FloArtistSongService;
import KMC.KoreaMusicChart.service.Song.FloSongService;
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

import static KMC.KoreaMusicChart.domain.Chart.FloChart.createFloChart;
import static KMC.KoreaMusicChart.domain.Song.FloSong.createFloSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class FloChartServiceTest {

    @Autowired
    private FloChartService floChartService;
    @Autowired
    private FloArtistService floArtistService;
    @Autowired
    private FloSongService floSongService;
    @Autowired
    private FloArtistSongService floArtistSongService;

    @Test
    public void 차트정보저장() throws Exception {
        //given
        FloSong song = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        floSongService.save(song);

        //when
        FloChart floChart = createFloChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song);
        Long saveId = floChartService.save(floChart);
        FloChart findFloChart = floChartService.findOne(saveId);

        //then
        assertEquals(floChart, findFloChart);
    }

    @Test(expected = IllegalStateException.class)
    public void 차트_중복_예외() throws Exception {
        //given
        FloSong song1 = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        FloSong song2 = createFloSong((long) 12345, "레옹", "무한도전 앨범", "imageUrlExample");

        floSongService.save(song1);
        floSongService.save(song2);

        FloChart floChart1 = createFloChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        FloChart floChart2 = createFloChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 4, song2);

        //when
        floChartService.save(floChart1);
        floChartService.save(floChart2);

        //then
        fail("FloChart 중복 저장 에러가 발생해야 함");
    }

    @Test
    public void 날짜와_시각으로_차트정보_조회() throws Exception {
        //given
        FloSong song1 = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        FloSong song2 = createFloSong((long) 12346, "레옹", "무한도전 앨범", "imageUrlExample");
        FloSong song3 = createFloSong((long) 12347, "바람났어", "무한도전 앨범", "imageUrlExample");
        FloSong song4 = createFloSong((long) 12348, "냉면", "무한도전 앨범", "imageUrlExample");

        floSongService.save(song1);
        floSongService.save(song2);
        floSongService.save(song3);
        floSongService.save(song4);

        FloChart floChart1 = createFloChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        FloChart floChart2 = createFloChart(LocalDateTime.of(2022, 10, 16, 10, 0), 2, 4, song2);
        FloChart floChart3 = createFloChart(LocalDateTime.of(2022, 10, 16, 10, 0), 3, 3, song3);
        FloChart floChart4 = createFloChart(LocalDateTime.of(2022, 10, 16, 11, 0), 1, 3, song3);
        FloChart floChart5 = createFloChart(LocalDateTime.of(2022, 10, 16, 11, 0), 2, 3, song4);

        floChartService.save(floChart1);
        floChartService.save(floChart2);
        floChartService.save(floChart3);
        floChartService.save(floChart4);
        floChartService.save(floChart5);

        //when
        List<FloChart> findFloChart1 = floChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 10, 0));
        List<FloChart> findFloChart2 = floChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 11, 0));

        //then
        assertEquals(3, findFloChart1.size());
        assertEquals(2, findFloChart2.size());
    }

//    @Test
//    public void 크롤링() throws Exception {
//
//        floChartService.scrap();
//
//        //given
//
//        //when
//
//        //then
//    }
}