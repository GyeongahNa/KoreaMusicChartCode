package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong;
import KMC.KoreaMusicChart.domain.Chart.FloChart;
import KMC.KoreaMusicChart.domain.Song.FloSong;
import KMC.KoreaMusicChart.repository.Chart.FloChartRepository;
import KMC.KoreaMusicChart.service.Artist.FloArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.FloArtistSongService;
import KMC.KoreaMusicChart.service.Song.FloSongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.FloArtist.createFloArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong.createFloArtistSong;
import static KMC.KoreaMusicChart.domain.Chart.FloChart.createFloChart;
import static KMC.KoreaMusicChart.domain.Song.FloSong.createFloSong;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FloChartService {

    private final FloChartRepository floChartRepository;
    private final FloArtistService floArtistService;
    private final FloSongService floSongService;
    private final FloArtistSongService floArtistSongService;

    @Transactional
    public Long save(FloChart floChart) {
        validateDuplicateFloChart(floChart); //같은 날, 같은 시각 데이터 중복 확인
        floChartRepository.save(floChart);
        return floChart.getId();
    }

    public void validateDuplicateFloChart(FloChart floChart) {
        List<FloChart> floCharts = floChartRepository.findByDateTimeAndCurRank(floChart.getDateTime(), floChart.getCurRank());
        if (!floCharts.isEmpty()) {
            throw new IllegalStateException("Error: FloChart 중복 에러");
        }
    }

    public FloChart findOne(Long id) {
        return floChartRepository.findOne(id);
    }

    public List<FloChart> findAll() {
        return floChartRepository.findAll();
    }

    public List<FloChart> findByDateTime(LocalDateTime dateTime) {
        return floChartRepository.findByDateTime(dateTime);
    }

    //== FloChart 실시간 크롤링 ==//
    @Scheduled(cron = "0 2 0-23 * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scrap() throws JSONException, IOException {

        log.info("Flo Chart Scraping started.");

        //현재 날짜(시각)
        Clock system = Clock.system(ZoneId.of("Asia/Seoul"));

        LocalDate localDate = LocalDate.now(system);
        LocalTime localTime = LocalTime.now(system);
        int hour = localTime.getHour();
        localTime = LocalTime.of(hour, 0);

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        //실시간 floChart 정보를 Json으로 요청
        JSONObject floChartJson = getJSONObject("https://api.music-flo.com/display/v1/browser/chart/1/list");
        JSONObject response = (JSONObject) floChartJson.get("data");
        JSONArray chartList = (JSONArray) response.get("trackList");

        for (int i = 0; i < chartList.length(); i++) {
            //i+1번째 곡 정보 가져오기
            JSONObject songInfo = (JSONObject) chartList.get(i);
            FloSong floSong = getFloSong(songInfo);

            //복수의 아티스트 처리
            JSONArray artistList = (JSONArray) songInfo.get("artistList");
            for (int j = 0; j < artistList.length(); j++) {
                //아티스트 정보 가져오기
                JSONObject artistInfo = (JSONObject) artistList.get(j);
                FloArtist floArtist = getFloArtist(artistInfo);

                //아티스트 - 곡 연결 정보 저장
                List<FloArtistSong> floArtistSongs = floArtistSongService.findByArtistAndSong(floArtist, floSong);

                if (floArtistSongs.isEmpty()) {
                    FloArtistSong floArtistSong = createFloArtistSong(floArtist, floSong);
                    floArtistSongService.save(floArtistSong);
                }
            }

            //차트 정보 저장
            JSONObject rankInfo = (JSONObject) songInfo.get("rank");
            int curRank = i + 1;
            int prevRank = curRank + parseInt(rankInfo.get("rankBadge").toString());
            FloChart floChart = createFloChart(localDateTime, curRank, prevRank, floSong);
            this.save(floChart);
        }

        log.info("Flo Chart Scraping completed.");
    }

    private JSONObject getJSONObject(String address) throws IOException, JSONException {

        //URL connection
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-type", "application/json");
        con.setDoInput(true);
        con.setDoOutput(false);

        //데이터를 입력 스트림에 담기
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        con.disconnect();

        //가져온 데이터를 JSON으로 파싱하여 필요한 데이터 추출
        return new JSONObject(sb.toString());
    }

    private FloSong getFloSong(JSONObject songInfo) throws JSONException {

        //곡 정보 가져오기
        Long songId = parseLong(songInfo.get("id").toString());
        String songName = songInfo.get("name").toString();

        JSONObject albumInfo = (JSONObject) songInfo.get("album");
        String albumName = albumInfo.get("title").toString();

        JSONArray imgArray = (JSONArray) albumInfo.get("imgList");
        JSONObject imgInfo = (JSONObject) imgArray.get(4); //size500 이미지
        String albumImg = imgInfo.get("url").toString();

        List<FloSong> floSongs = floSongService.findByNameAndAlbum(songName, albumName);

        FloSong floSong = null;
        if (!floSongs.isEmpty()) {
            //이미 곡 정보가 있으면 floId만 수정
            floSong = floSongs.get(0);
            floSong.updateFloId(songId);
        } else {
            //새로운 곡 저장
            floSong = createFloSong(songId, songName, albumName, albumImg);
            floSongService.save(floSong);
        }
        return floSong;
    }

    private FloArtist getFloArtist(JSONObject artistInfo) throws JSONException, IOException {

        //아티스트 정보 가져오기
        Long artistId = parseLong(artistInfo.get("id").toString());
        String artistName = artistInfo.get("name").toString();

        //FLO에서는 아티스트 상세 정보를 제공하지 않으므로 이름으로 중복 검사
        List<FloArtist> floArtists = floArtistService.findByName(artistName);

        FloArtist floArtist = null;
        if (!floArtists.isEmpty()) {
            //이미 아티스트정보가 있으면 floId만 수정
            floArtist = floArtists.get(0);
            floArtist.updateFloId(artistId);
        } else {
            //새로운 아티스트 저장
            floArtist = createFloArtist(artistId, artistName);
            floArtistService.save(floArtist);
        }
        return floArtist;
    }
}
