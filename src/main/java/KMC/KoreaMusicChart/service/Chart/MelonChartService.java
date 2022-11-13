package KMC.KoreaMusicChart.service.Chart;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong;
import KMC.KoreaMusicChart.domain.Chart.MelonChart;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import KMC.KoreaMusicChart.repository.Chart.MelonChartRepository;
import KMC.KoreaMusicChart.service.Artist.MelonArtistService;
import KMC.KoreaMusicChart.service.ArtistSong.MelonArtistSongService;
import KMC.KoreaMusicChart.service.Song.MelonSongService;
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

import static KMC.KoreaMusicChart.domain.Artist.MelonArtist.createMelonArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong.createMelonArtistSong;
import static KMC.KoreaMusicChart.domain.Chart.MelonChart.createMelonChart;
import static KMC.KoreaMusicChart.domain.Song.MelonSong.createMelonSong;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MelonChartService {

    private final MelonChartRepository melonChartRepository;
    private final MelonArtistService melonArtistService;
    private final MelonSongService melonSongService;
    private final MelonArtistSongService melonArtistSongService;

    @Transactional
    public Long save(MelonChart melonChart) {
        validateDuplicateMelonChart(melonChart); //같은 날, 같은 시각 데이터 중복 확인
        melonChartRepository.save(melonChart);
        return melonChart.getId();
    }

    public void validateDuplicateMelonChart(MelonChart melonChart) {
        List<MelonChart> melonCharts = melonChartRepository.findByDateTimeAndCurRank(melonChart.getDateTime(), melonChart.getCurRank());
        if (!melonCharts.isEmpty()) {
            throw new IllegalStateException("Error: MelonChart 중복 에러");
        }
    }

    public MelonChart findOne(Long id) {
        return melonChartRepository.findOne(id);
    }

    public List<MelonChart> findAll() {
        return melonChartRepository.findAll();
    }

    public List<MelonChart> findByDateTime(LocalDateTime dateTime) {
        return melonChartRepository.findByDateTime(dateTime);
    }

    //== MelonChart 실시간 크롤링 ==//
    @Scheduled(cron = "0 2 0-23 * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scrap() throws JSONException, IOException {

        log.info("Melon Chart Scraping started.");

        //현재 시각
        Clock system = Clock.system(ZoneId.of("Asia/Seoul"));

        LocalDate localDate = LocalDate.now(system);
        LocalTime localTime = LocalTime.now(system);
        int hour = localTime.getHour();
        localTime = LocalTime.of(hour, 0);

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        //실시간 melonChart 정보를 Json으로 요청
        JSONObject melonChartJson = getJSONObject("https://mac.melon.com/chart/top100/list.json?appVer=1.2.0");
        JSONObject response = (JSONObject) melonChartJson.get("response");
        JSONArray chartList = (JSONArray) response.get("CHARTLIST");

        for (int i = 0; i < chartList.length(); i++) {
            //곡 정보 가져오기
            JSONObject songInfo = (JSONObject) chartList.get(i);
            MelonSong melonSong = getMelonSong(songInfo);

            //복수의 아티스트 처리
            JSONArray artistList = (JSONArray) songInfo.get("ARTISTLIST");

            for (int j = 0; j < artistList.length(); j++) {
                //아티스트 정보 가져오기
                JSONObject artistInfo = (JSONObject) artistList.get(j);
                MelonArtist melonArtist = getMelonArtist(artistInfo);

                //아티스트 - 곡 연결 정보 저장
                List<MelonArtistSong> melonArtistSongs = melonArtistSongService.findByArtistAndSong(melonArtist, melonSong);

                if (melonArtistSongs.isEmpty()) {
                    MelonArtistSong melonArtistSong = createMelonArtistSong(melonArtist, melonSong);
                    melonArtistSongService.save(melonArtistSong);
                }
            }

            //차트 정보 저장
            int curRank = parseInt(songInfo.get("CURRANK").toString());
            int prevRank = parseInt(songInfo.get("PASTRANK").toString()); //0이면 NEW chartIn
            MelonChart melonChart = createMelonChart(localDateTime, curRank, prevRank, melonSong);
            this.save(melonChart);
        }

        log.info("Melon Chart Scraping completed.");
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

    private MelonSong getMelonSong(JSONObject songInfo) throws JSONException {

        //곡 정보 가져오기
        Long songId = parseLong(songInfo.get("SONGID").toString());
        String songName = songInfo.get("SONGNAME").toString();
        String albumName = songInfo.get("ALBUMNAME").toString();
        String albumImg = songInfo.get("ALBUMIMGLARGE").toString();

        List<MelonSong> melonSongs = melonSongService.findByNameAndAlbum(songName, albumName);

        MelonSong melonSong = null;
        if (!melonSongs.isEmpty()) {
            //이미 곡 정보가 있으면 melonId만 수정
            melonSong = melonSongs.get(0);
            melonSong.updateMelonId(songId);
        } else {
            //새로운 곡 저장
            melonSong = createMelonSong(songId, songName, albumName, albumImg);
            melonSongService.save(melonSong);
        }
        return melonSong;
    }

    private MelonArtist getMelonArtist(JSONObject artistInfo) throws JSONException, IOException {

        //아티스트 정보 가져오기
        Long artistId = parseLong(artistInfo.get("ARTISTID").toString());
        String artistName = artistInfo.get("ARTISTNAME").toString();

        //아티스트 상세 페이지에서 데뷔일 찾기
        JSONObject artistDetailJSON = getJSONObject("https://m2.melon.com/m6/v1/artist/detail/info.json?artistId=" + artistId);
        JSONObject response = (JSONObject) artistDetailJSON.get("response");
        String debut = response.get("DEBUTDATE").toString();

        List<MelonArtist> melonArtists = melonArtistService.findByNameAndDebutDate(artistName, debut);

        MelonArtist melonArtist = null;
        if (!melonArtists.isEmpty()) {
            //이미 아티스트정보가 있으면 melonId만 수정
            melonArtist = melonArtists.get(0);
            melonArtist.updateMelonId(artistId);
        } else {
            //새로운 아티스트 저장
            melonArtist = createMelonArtist(artistId, artistName, debut);
            melonArtistService.save(melonArtist);
        }
        return melonArtist;
    }
}