//package KMC.KoreaMusicChart.service.Info;
//
//import KMC.KoreaMusicChart.domain.Info.GenieListenerInfo;
//import KMC.KoreaMusicChart.domain.Song.GenieSong;
//import KMC.KoreaMusicChart.repository.Info.GenieListenerInfoRepository;
//import KMC.KoreaMusicChart.service.Song.GenieSongService;
//import lombok.RequiredArgsConstructor;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static KMC.KoreaMusicChart.domain.Info.GenieListenerInfo.createGenieListenerInfo;
//import static java.lang.Integer.parseInt;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class GenieListenerInfoService {
//
//    private final GenieListenerInfoRepository genieListenerInfoRepository;
//    private final GenieSongService genieSongService;
//
//    @Transactional
//    public Long save(GenieListenerInfo genieListenerInfo) {
//        validateDuplicateGenieListenerInfo(genieListenerInfo);
//        genieListenerInfoRepository.save(genieListenerInfo);
//        return genieListenerInfo.getId();
//    }
//
//    private void validateDuplicateGenieListenerInfo(GenieListenerInfo genieListenerInfo) {
//        List<GenieListenerInfo> findGenieListenerInfos = genieListenerInfoRepository.findByDateTimeAndSong(genieListenerInfo.getDateTime(), genieListenerInfo.getGenieSong());
//        if (!findGenieListenerInfos.isEmpty()) {
//            throw new IllegalStateException("Error: GenieListenerInfo 중복 에러");
//        }
//    }
//
//    public GenieListenerInfo findOne(Long id) {
//        return genieListenerInfoRepository.findOne(id);
//    }
//
//    public List<GenieListenerInfo> findAll() {
//        return genieListenerInfoRepository.findAll();
//    }
//
//    public List<GenieListenerInfo> findByDateTimeAndSong(LocalDateTime dateTime, GenieSong genieSong) {
//        return genieListenerInfoRepository.findByDateTimeAndSong(dateTime, genieSong);
//    }
//
//    public List<GenieListenerInfo> findByDateTimeAndIntervalAndSong(LocalDateTime dateTime, int hourInterval, GenieSong genieSong) {
//        return genieListenerInfoRepository.findByDateTimeAndIntervalAndSong(dateTime, hourInterval, genieSong);
//    }
//
//    //== GenieSong 재생 정보 크롤링 ==//
//    public void scrap(LocalDateTime dateTime) throws IOException {
//
//        //저장된 모든 GenieSong에 대하여 전체 청취자수/전체 재생수 크롤링
//        List<GenieSong> genieSongs = genieSongService.findAll();
//
//        for (GenieSong genieSong : genieSongs) {
//
//            //곡 상세페이지 접속
//            String songDetailUrl = "https://www.genie.co.kr/detail/songInfo?xgnm=" + genieSong.getGenieId();
//            Connection con = Jsoup.connect(songDetailUrl);
//            Document document = con.get();
//
//            //전체 청취자수/재생수 가져오기
//            int totalListenerCnt = parseInt(document.select("div.total div:nth-child(1) p").text().replaceAll(",", ""));
//            int totalPlayCnt = parseInt(document.select("div.total div:nth-child(2) p").text().replaceAll(",", ""));
//
//            //한시간 전 데이터를 찾아 증감 계산
//            LocalDateTime oneHourBefore = dateTime.minusHours(1);
//            List<GenieListenerInfo> genieListenerInfos = genieListenerInfoRepository.findByDateTimeAndSong(oneHourBefore, genieSong);
//
//            int TLCIncrement = 0;
//            int TPCIncrement = 0;
//            if (!genieListenerInfos.isEmpty()) {
//                TLCIncrement = totalListenerCnt - genieListenerInfos.get(0).getTotalListenerCnt();
//                TPCIncrement = totalPlayCnt - genieListenerInfos.get(0).getTotalPlayCnt();
//            }
//
//            GenieListenerInfo genieListenerInfo = createGenieListenerInfo(dateTime, genieSong, totalListenerCnt, totalPlayCnt, TLCIncrement, TPCIncrement);
//            this.save(genieListenerInfo);
//        }
//    }
//}
