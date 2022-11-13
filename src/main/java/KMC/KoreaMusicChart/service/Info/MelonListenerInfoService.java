//package KMC.KoreaMusicChart.service.Info;
//
//import KMC.KoreaMusicChart.domain.Chart.MelonChart;
//import KMC.KoreaMusicChart.domain.Info.MelonListenerInfo;
//import KMC.KoreaMusicChart.domain.Song.MelonSong;
//import KMC.KoreaMusicChart.repository.Info.MelonListenerInfoRepository;
//import KMC.KoreaMusicChart.service.Chart.MelonChartService;
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
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static KMC.KoreaMusicChart.domain.Info.MelonListenerInfo.createMelonListenerInfo;
//import static java.lang.Integer.parseInt;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class MelonListenerInfoService {
//
//    private final MelonListenerInfoRepository melonListenerInfoRepository;
//    private final MelonChartService melonChartService;
//
//    @Transactional
//    public Long save(MelonListenerInfo melonListenerInfo) {
//        validateDuplicateMelonListenerInfo(melonListenerInfo);
//        melonListenerInfoRepository.save(melonListenerInfo);
//        return melonListenerInfo.getId();
//    }
//
//    private void validateDuplicateMelonListenerInfo(MelonListenerInfo melonListenerInfo) {
//        List<MelonListenerInfo> findMelonListenerInfos = melonListenerInfoRepository.findByDateTimeAndSong(melonListenerInfo.getDateTime(), melonListenerInfo.getMelonSong());
//        if (!findMelonListenerInfos.isEmpty()) {
//            throw new IllegalStateException("Error: MelonListenerInfo 중복 에러");
//        }
//    }
//
//    public MelonListenerInfo findOne(Long id) {
//        return melonListenerInfoRepository.findOne(id);
//    }
//
//    public List<MelonListenerInfo> findAll() {
//        return melonListenerInfoRepository.findAll();
//    }
//
//    public List<MelonListenerInfo> findByDateTimeAndSong(LocalDateTime dateTime, MelonSong melonSong) {
//        return melonListenerInfoRepository.findByDateTimeAndSong(dateTime, melonSong);
//    }
//
//    public List<MelonListenerInfo> findByDateTimeAndIntervalAndSong(LocalDateTime dateTime, int hourInterval, MelonSong melonSong) {
//        return melonListenerInfoRepository.findByDateTimeAndIntervalAndSong(dateTime, hourInterval, melonSong);
//    }
//
//    //== MelonSong 재생 정보 크롤링 ==//
//    public void scrap(LocalDateTime dateTime) throws IOException, JSONException {
//
//        //dateTime 시각에 차트에 있는 곡들에 대하여 실시간 감상자 수 크롤링
//        List<MelonChart> melonCharts = melonChartService.findByDateAndTime(dateTime.toLocalDate(), dateTime.toLocalTime());
//
//        for (MelonChart melonChart : melonCharts) {
//
//            MelonSong melonSong = melonChart.getMelonSong();
//
//            System.out.println("melonSong.getName() = " + melonSong.getName());
//
//
//            //차트리포트에서 실시간 감상자수 가져오기
//            String chartReportUrl = "https://m2.melon.com/m6/chart/song/chartReport.json?songId=" + melonSong.getMelonId().toString();
//
//            System.out.println("chartReportUrl = " + chartReportUrl);
//
//            JSONObject charReport = getJSONObject(chartReportUrl);
//            JSONObject response = (JSONObject) charReport.get("response");
//            JSONObject listenerChart = (JSONObject) response.get("LISTENERCHART");
//            JSONObject contents = (JSONObject) listenerChart.get("TITLE");
//            int realTimeListenerCnt = parseInt(contents.get("VALUE").toString().replaceAll(",", ""));
//
//            System.out.println("realTimeListenerCnt = " + realTimeListenerCnt);
//
//            //이전 시간 대비 증감량 구하기
//            int RTLCIncrement = 0;
//            LocalDateTime oneHourBefore = dateTime.minusHours(1);
//            List<MelonListenerInfo> melonListenerInfos = melonListenerInfoRepository.findByDateTimeAndSong(oneHourBefore, melonSong);
//            if (!melonListenerInfos.isEmpty())
//                RTLCIncrement = realTimeListenerCnt - melonListenerInfos.get(0).getRealTimeListenerCnt();
//
//            MelonListenerInfo melonListenerInfo = createMelonListenerInfo(dateTime, melonSong, realTimeListenerCnt, RTLCIncrement);
//            this.save(melonListenerInfo);
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
