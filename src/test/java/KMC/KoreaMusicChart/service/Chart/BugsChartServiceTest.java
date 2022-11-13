package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Chart.BugsChart;
import KMC.KoreaMusicChart.domain.Song.BugsSong;
import KMC.KoreaMusicChart.service.Artist.BugsArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.BugsArtistSongService;
import KMC.KoreaMusicChart.service.Song.BugsSongService;
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

import static KMC.KoreaMusicChart.domain.Chart.BugsChart.createBugsChart;
import static KMC.KoreaMusicChart.domain.Song.BugsSong.createBugsSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class BugsChartServiceTest {

    @Autowired
    private BugsChartService bugsChartService;
    @Autowired
    private BugsArtistService bugsArtistService;
    @Autowired
    private BugsSongService bugsSongService;
    @Autowired
    private BugsArtistSongService bugsArtistSongService;

    @Test
    public void 차트정보저장() throws Exception {
        //given
        BugsSong song = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        bugsSongService.save(song);

        //when
        BugsChart bugsChart = createBugsChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song);
        Long saveId = bugsChartService.save(bugsChart);
        BugsChart findBugsChart = bugsChartService.findOne(saveId);

        //then
        assertEquals(bugsChart, findBugsChart);
    }

    @Test(expected = IllegalStateException.class)
    public void 차트_중복_예외() throws Exception {
        //given
        BugsSong song1 = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        BugsSong song2 = createBugsSong((long) 12345, "레옹", "무한도전 앨범", "imageUrlExample");

        bugsSongService.save(song1);
        bugsSongService.save(song2);

        BugsChart bugsChart1 = createBugsChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        BugsChart bugsChart2 = createBugsChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 4, song2);

        //when
        bugsChartService.save(bugsChart1);
        bugsChartService.save(bugsChart2);

        //then
        fail("BugsChart 중복 저장 에러가 발생해야 함");
    }

    @Test
    public void 날짜와_시각으로_차트정보_조회() throws Exception {
        //given
        BugsSong song1 = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        BugsSong song2 = createBugsSong((long) 12346, "레옹", "무한도전 앨범", "imageUrlExample");
        BugsSong song3 = createBugsSong((long) 12347, "바람났어", "무한도전 앨범", "imageUrlExample");
        BugsSong song4 = createBugsSong((long) 12348, "냉면", "무한도전 앨범", "imageUrlExample");

        bugsSongService.save(song1);
        bugsSongService.save(song2);
        bugsSongService.save(song3);
        bugsSongService.save(song4);

        BugsChart bugsChart1 = createBugsChart(LocalDateTime.of(2022, 10, 16, 10, 0), 1, 2, song1);
        BugsChart bugsChart2 = createBugsChart(LocalDateTime.of(2022, 10, 16, 10, 0), 2, 4, song2);
        BugsChart bugsChart3 = createBugsChart(LocalDateTime.of(2022, 10, 16, 10, 0), 3, 3, song3);
        BugsChart bugsChart4 = createBugsChart(LocalDateTime.of(2022, 10, 16, 11, 0), 1, 3, song3);
        BugsChart bugsChart5 = createBugsChart(LocalDateTime.of(2022, 10, 16, 11, 0), 2, 3, song4);

        bugsChartService.save(bugsChart1);
        bugsChartService.save(bugsChart2);
        bugsChartService.save(bugsChart3);
        bugsChartService.save(bugsChart4);
        bugsChartService.save(bugsChart5);

        //when
        List<BugsChart> findBugsChart1 = bugsChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 10, 0));
        List<BugsChart> findBugsChart2 = bugsChartService.findByDateTime(LocalDateTime.of(2022, 10, 16, 11, 0));

        //then
        assertEquals(3, findBugsChart1.size());
        assertEquals(2, findBugsChart2.size());
    }

//    @Test
//    public void 크롤링() throws Exception {
//
//        bugsChartService.scrap();
//
//        //given
//
//        //when
//
//        //then
//    }
}