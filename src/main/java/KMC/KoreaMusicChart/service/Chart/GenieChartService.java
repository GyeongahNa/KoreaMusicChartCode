package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong;
import KMC.KoreaMusicChart.domain.Chart.GenieChart;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import KMC.KoreaMusicChart.repository.Chart.GenieChartRepository;
import KMC.KoreaMusicChart.service.Artist.GenieArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.GenieArtistSongService;
import KMC.KoreaMusicChart.service.Song.GenieSongService;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.GenieArtist.createGenieArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong.createGenieArtistSong;
import static KMC.KoreaMusicChart.domain.Chart.GenieChart.createGenieChart;
import static KMC.KoreaMusicChart.domain.Song.GenieSong.createGenieSong;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.net.URLDecoder.decode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GenieChartService {

    private final GenieChartRepository genieChartRepository;
    private final GenieArtistService genieArtistService;
    private final GenieSongService genieSongService;
    private final GenieArtistSongService genieArtistSongService;

    @Transactional
    public Long save(GenieChart genieChart) {
        validateDuplicateGenieChart(genieChart); //같은 날, 같은 시각 데이터 중복 확인
        genieChartRepository.save(genieChart);
        return genieChart.getId();
    }

    public void validateDuplicateGenieChart(GenieChart genieChart) {
        List<GenieChart> genieCharts = genieChartRepository.findByDateTimeAndCurRank(genieChart.getDateTime(), genieChart.getCurRank());
        if (!genieCharts.isEmpty()) {
            throw new IllegalStateException("Error: GenieChart 중복 에러");
        }
    }

    public GenieChart findOne(Long id) {
        return genieChartRepository.findOne(id);
    }

    public List<GenieChart> findAll() {
        return genieChartRepository.findAll();
    }

    public List<GenieChart> findByDateTime(LocalDateTime dateTime) {
        return genieChartRepository.findByDateTime(dateTime);
    }

    //== GenieChart 실시간 크롤링 ==//
    @Scheduled(cron = "0 2 0-23 * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scrap() throws JSONException, IOException {

        log.info("Genie Chart Scraping started.");

        //현재 날짜(시각)
        Clock system = Clock.system(ZoneId.of("Asia/Seoul"));

        LocalDate localDate = LocalDate.now(system);
        LocalTime localTime = LocalTime.now(system);
        int hour = localTime.getHour();
        localTime = LocalTime.of(hour, 0);

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        for (int pg = 1; pg < 5; pg++) { //top100까지만 수집

            //실시간 genieChart 정보를 Json으로 요청
            JSONObject genieChartJson = getJSONObject("https://app.genie.co.kr/chart/j_RealTimeRankSongList.json?pg=" + pg);
            JSONObject dataSet = (JSONObject) genieChartJson.get("DataSet");
            JSONArray chartList = (JSONArray) dataSet.get("DATA");

            for (int i = 0; i < chartList.length(); i++) {
                //곡 정보 가져오기
                JSONObject songInfo = (JSONObject) chartList.get(i);
                GenieSong genieSong = getGenieSong(songInfo);

                //지니는 아티스트를 묶어서 처리하므로 하나의 아티스트만 저장
                String artistId = songInfo.get("ARTIST_ID").toString();
                JSONObject response = getJSONObject("https://info.genie.co.kr/info/artist?xxnm=" + artistId);
                JSONObject artistInfo = (JSONObject) response.get("artist_info");
                GenieArtist genieArtist = getGenieArtist(artistInfo);

                //아티스트 - 곡 연결 정보 저장
                List<GenieArtistSong> genieArtistSongs = genieArtistSongService.findByArtistAndSong(genieArtist, genieSong);

                if (genieArtistSongs.isEmpty()) {
                    GenieArtistSong genieArtistSong = createGenieArtistSong(genieArtist, genieSong);
                    genieArtistSongService.save(genieArtistSong);
                }

                //차트 정보 저장
                int curRank = parseInt(songInfo.get("RANK_NO").toString());
                int prevRank = parseInt(songInfo.get("PRE_RANK_NO").toString()); //0이면 NEW chartIn
                GenieChart genieChart = createGenieChart(localDateTime, curRank, prevRank, genieSong);
                this.save(genieChart);
            }
        }

        log.info("Genie Chart Scraping completed.");
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

    private GenieSong getGenieSong(JSONObject songInfo) throws JSONException, UnsupportedEncodingException {

        //곡 정보 가져오기
        Long songId = parseLong(decode(songInfo.get("SONG_ID").toString(), StandardCharsets.UTF_8));
        String songName = decode(songInfo.get("SONG_NAME").toString(), StandardCharsets.UTF_8); //인코딩
        String albumName = decode(songInfo.get("ALBUM_NAME").toString(), StandardCharsets.UTF_8);
        String albumImg = decode(songInfo.get("ALBUM_IMG_PATH").toString(), StandardCharsets.UTF_8);
        List<GenieSong> genieSongs = genieSongService.findByNameAndAlbum(songName, albumName);

        GenieSong genieSong = null;
        if (!genieSongs.isEmpty()) {
            //이미 곡 정보가 있으면 genieId만 수정
            genieSong = genieSongs.get(0);
            genieSong.updateGenieId(songId);
        } else {
            //새로운 곡 저장
            genieSong = createGenieSong(songId, songName, albumName, albumImg);
            genieSongService.save(genieSong);
        }
        return genieSong;
    }

    private GenieArtist getGenieArtist(JSONObject artistInfo) throws JSONException, IOException {

        //아티스트 정보 가져오기
        Long artistId = parseLong(decode(artistInfo.get("artist_id").toString(), StandardCharsets.UTF_8));
        String artistName = decode(artistInfo.get("artist_name").toString(), StandardCharsets.UTF_8);
        String debut = decode(artistInfo.get("artist_debut_dt").toString(), StandardCharsets.UTF_8);
        List<GenieArtist> genieArtists = genieArtistService.findByNameAndDebutDate(artistName, debut);

        GenieArtist genieArtist = null;
        if (!genieArtists.isEmpty()) {
            //이미 아티스트정보가 있으면 genieId만 수정
            genieArtist = genieArtists.get(0);
            genieArtist.updateGenieId(artistId);
        } else {
            //새로운 아티스트 저장
            genieArtist = createGenieArtist(artistId, artistName, debut);
            genieArtistService.save(genieArtist);
        }
        return genieArtist;
    }
}
