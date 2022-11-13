package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong;
import KMC.KoreaMusicChart.domain.Chart.BugsChart;
import KMC.KoreaMusicChart.domain.Song.BugsSong;
import KMC.KoreaMusicChart.repository.Chart.BugsChartRepository;
import KMC.KoreaMusicChart.service.Artist.BugsArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.BugsArtistSongService;
import KMC.KoreaMusicChart.service.Song.BugsSongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.*;
import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.BugsArtist.createBugsArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong.createBugsArtistSong;
import static KMC.KoreaMusicChart.domain.Chart.BugsChart.createBugsChart;
import static KMC.KoreaMusicChart.domain.Song.BugsSong.createBugsSong;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BugsChartService {

    private final BugsChartRepository bugsChartRepository;
    private final BugsArtistService bugsArtistService;
    private final BugsSongService bugsSongService;
    private final BugsArtistSongService bugsArtistSongService;

    @Transactional
    public Long save(BugsChart bugsChart) {
        validateDuplicateBugsChart(bugsChart); //같은 날, 같은 시각 데이터 중복 확인
        bugsChartRepository.save(bugsChart);
        return bugsChart.getId();
    }

    public void validateDuplicateBugsChart(BugsChart bugsChart) {
        List<BugsChart> bugsCharts = bugsChartRepository.findByDateTimeAndCurRank(bugsChart.getDateTime(), bugsChart.getCurRank());
        if (!bugsCharts.isEmpty()) {
            throw new IllegalStateException("Error: BugsChart 중복 에러");
        }
    }

    public BugsChart findOne(Long id) {
        return bugsChartRepository.findOne(id);
    }

    public List<BugsChart> findAll() {
        return bugsChartRepository.findAll();
    }

    public List<BugsChart> findByDateTime(LocalDateTime dateTime) {
        return bugsChartRepository.findByDateTime(dateTime);
    }

    //== BugsChart 실시간 크롤링 ==//
    @Scheduled(cron = "0 2 0-23 * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scrap() throws IOException {

        log.info("Bugs Chart Scraping started.");

        //현재 날짜(시각)
        Clock system = Clock.system(ZoneId.of("Asia/Seoul"));

        LocalDate localDate = LocalDate.now(system);
        LocalTime localTime = LocalTime.now(system);
        int hour = localTime.getHour();
        localTime = LocalTime.of(hour, 0);

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        //Jsoup으로 HTML Document 객체 가져오기
        Connection con = Jsoup.connect("https://music.bugs.co.kr/chart");
        Document document = con.get();

        //차트 리스트 가져오기
        Elements chartList = document.select("table.byChart tbody tr");
        for (Element chart : chartList) {

            //곡 정보 가져오기
            BugsSong bugsSong = getBugsSong(chart);

            //곡 상세정보 페이지에서 아티스트 정보 가져오기
            String songDetailUrl = chart.select("a.trackInfo").attr("href");
            Connection songDetailCon = Jsoup.connect(songDetailUrl);
            Document songDetailDoc = songDetailCon.get();
            Elements artistList = songDetailDoc.select("div.basicInfo table.info tbody tr:nth-child(1) td a");

            //복수의 아티스트 처리
            for (Element artistElem : artistList) {

                //아티스트 상세 페이지에서 아티스트 정보 가져오기
                String artistDetailUrl = artistElem.attr("href");
                BugsArtist bugsArtist = getBugsArtist(artistDetailUrl);

                //아티스트 - 곡 연결 정보 저장
                List<BugsArtistSong> bugsArtistSongs = bugsArtistSongService.findByArtistAndSong(bugsArtist, bugsSong);
                if (bugsArtistSongs.isEmpty()) {
                    BugsArtistSong bugsArtistSong = createBugsArtistSong(bugsArtist, bugsSong);
                    bugsArtistSongService.save(bugsArtistSong);
                }
            }

            //차트 정보 저장
            int curRank = parseInt(chart.select("div.ranking strong").text());
            String change = chart.select("div.ranking p").attr("class");
            int prevRank = curRank;
            if (change.equals("change up")) prevRank += parseInt(chart.select("div.ranking p em").text());
            else if (change.equals("change down")) prevRank -= parseInt(chart.select("div.ranking p em").text());

            BugsChart bugsChart = createBugsChart(localDateTime, curRank, prevRank, bugsSong);
            this.save(bugsChart);
        }

        log.info("Bugs Chart Scraping completed.");
    }

    private BugsSong getBugsSong(Element chart) {

        //곡 정보 가져오기
        Long trackId = parseLong(chart.attr("trackid"));
        String songName = chart.select("p.title a").attr("title");
        String albumName = chart.select("a.album").attr("title");
        String albumImg = chart.select("a.thumbnail img").attr("src");

        List<BugsSong> bugsSongs = bugsSongService.findByNameAndAlbum(songName, albumName);

        BugsSong bugsSong = null;
        if (!bugsSongs.isEmpty()) {
            //이미 곡 정보가 있으면 bugsId만 수정
            bugsSong = bugsSongs.get(0);
            bugsSong.updateBugsId(trackId);
        } else {
            //새로운 곡 저장
            bugsSong = createBugsSong(trackId, songName, albumName, albumImg);
            bugsSongService.save(bugsSong);
        }
        return bugsSong;
    }

    private BugsArtist getBugsArtist(String artistDetailUrl) throws IOException {

        Connection artistDetailCon = Jsoup.connect(artistDetailUrl);
        Document artistDetailDoc = artistDetailCon.get();

        //아티스트 정보 가져오기
        String numbers = artistDetailUrl.replaceAll("[^0-9]+", " ");
        Long artistId = parseLong(asList(numbers.trim().split(" ")).get(0));
        String artistName = artistDetailDoc.select("article.mnArtist header.pgTitle h1").text();

        String debut = "";
        Elements artistInfoBox = artistDetailDoc.select("div.basicInfo table.info tr");
        for (Element artistInfo : artistInfoBox) {
            //아티스트 정보 중 "데뷔" 정보가 있으면 debut에 저장
            String infoTitle = artistInfo.select("th").text();
            if (infoTitle.equals("데뷔")) debut = artistInfo.select("td").text();
        }

        List<BugsArtist> bugsArtists = bugsArtistService.findByNameAndDebutDate(artistName, debut);

        BugsArtist bugsArtist = null;
        if (!bugsArtists.isEmpty()) {
            //이미 아티스트정보가 있으면 bugsId만 수정
            bugsArtist = bugsArtists.get(0);
            bugsArtist.updateBugsId(artistId);
        } else {
            //새로운 아티스트 저장
            bugsArtist = createBugsArtist(artistId, artistName, debut);
            bugsArtistService.save(bugsArtist);
        }
        return bugsArtist;
    }
}
