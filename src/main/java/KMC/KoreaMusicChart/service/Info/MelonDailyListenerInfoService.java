//package KMC.KoreaMusicChart.service.Info;
//
//import KMC.KoreaMusicChart.domain.Info.MelonDailyListenerInfo;
//import KMC.KoreaMusicChart.domain.Song.MelonSong;
//import KMC.KoreaMusicChart.repository.Info.MelonDailyListenerInfoRepository;
//import KMC.KoreaMusicChart.service.Song.MelonSongService;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.util.List;
//
//import static KMC.KoreaMusicChart.domain.Info.MelonDailyListenerInfo.createMelonDailyListenerInfo;
//import static java.lang.Integer.parseInt;
//import static java.util.Arrays.asList;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class MelonDailyListenerInfoService {
//
//    private final MelonDailyListenerInfoRepository melonDailyListenerInfoRepository;
//    private final MelonSongService melonSongService;
//
//    @Transactional
//    public Long save(MelonDailyListenerInfo melonDailyListenerInfo) {
//        validateDuplicateMelonDailyListenerInfo(melonDailyListenerInfo);
//        melonDailyListenerInfoRepository.save(melonDailyListenerInfo);
//        return melonDailyListenerInfo.getId();
//    }
//
//    private void validateDuplicateMelonDailyListenerInfo(MelonDailyListenerInfo melonDailyListenerInfo) {
//        List<MelonDailyListenerInfo> melonDailyListenerInfos = melonDailyListenerInfoRepository.findByDateAndSong(melonDailyListenerInfo.getDate(), melonDailyListenerInfo.getMelonSong());
//        if (!melonDailyListenerInfos.isEmpty()) {
//            throw new IllegalStateException("Error: MelonDailyListenerInfo 중복 에러");
//        }
//    }
//
//    public MelonDailyListenerInfo findOne(Long id) {
//        return melonDailyListenerInfoRepository.findOne(id);
//    }
//
//    public List<MelonDailyListenerInfo> findAll() {
//        return melonDailyListenerInfoRepository.findAll();
//    }
//
//    public List<MelonDailyListenerInfo> findByDateAndSong(LocalDate date, MelonSong melonSong) {
//        return melonDailyListenerInfoRepository.findByDateAndSong(date, melonSong);
//    }
//
//    public List<MelonDailyListenerInfo> findByDateAndIntervalAndSong(LocalDate date, int dayInterval, MelonSong melonSong) {
//        return melonDailyListenerInfoRepository.findByDateAndIntervalAndSong(date, dayInterval, melonSong);
//    }
//
//    //== MelonSong 데일리 감상자 수 크롤링 ==//
//    public void scrap(LocalDate date) throws JSONException, IOException {
//
//        //MelonSong 에 있는 모든 곡들에 대하여 데일리 감상자 수 크롤링
//        List<MelonSong> melonSongs = melonSongService.findAll();
//
//        for (MelonSong melonSong : melonSongs) {
//
//            //곡 상세정보에서 데일리 감상자 수 가져오기
//            String songInfoUrl = "https://m2.melon.com/m6/v2/song/info.json?songId=" + melonSong.getMelonId().toString();
//            JSONObject songInfo = getJSONObject(songInfoUrl);
//            JSONObject response = (JSONObject) songInfo.get("response");
//            JSONObject dailyListenInfo = (JSONObject) response.get("DAILYLISTENINFO");
//
//            //데일리 감상자 수
//            int dailyListenerCnt = parseInt(dailyListenInfo.get("LISTNERCNT").toString().replaceAll(",", ""));
//
//            //명시된 날짜
//            String notedDate = dailyListenInfo.get("DATE").toString().replaceAll("\\.", " ");
//
//            System.out.println("melonSong = " + melonSong);
//            System.out.println("songInfoUrl = " + songInfoUrl);
//            System.out.println("notedDate = " + notedDate);
//            System.out.println("dailyListenerCnt = " + dailyListenerCnt);
//
//            int year = parseInt(asList(notedDate.trim().split(" ")).get(0));
//            int month = parseInt(asList(notedDate.trim().split(" ")).get(1));
//            int day = parseInt(asList(notedDate.trim().split(" ")).get(2));
//            LocalDate noted = LocalDate.of(year, month, day);
//
//            //이전 날짜 대비 증감량 구하기
//            LocalDate oneDayBefore = noted.minusDays(1);
//            List<MelonDailyListenerInfo> melonDailyListenerInfos = melonDailyListenerInfoRepository.findByDateAndSong(oneDayBefore, melonSong);
//            int DLCIncrement = 0;
//            if (!melonDailyListenerInfos.isEmpty())
//                DLCIncrement = dailyListenerCnt - melonDailyListenerInfos.get(0).getDailyListenerCnt();
//
//            MelonDailyListenerInfo melonDailyListenerInfo = createMelonDailyListenerInfo(noted, melonSong, dailyListenerCnt, DLCIncrement);
//            this.save(melonDailyListenerInfo);
//        }
//    }
//
//    private JSONObject getJSONObject(String address) throws IOException, JSONException {
//
//        //URL connection
//        URL url = new URL(address);
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Content-type", "application/json");
//        con.setDoInput(true);
//        con.setDoOutput(false);
//
//        //데이터를 입력 스트림에 담기
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
//        String line;
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        br.close();
//        con.disconnect();
//
//        //가져온 데이터를 JSON으로 파싱하여 필요한 데이터 추출
//        return new JSONObject(sb.toString());
//    }
//
//}
