package KMC.KoreaMusicChart.controller;

import KMC.KoreaMusicChart.domain.Chart.*;
import KMC.KoreaMusicChart.service.Chart.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.*;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MelonChartService melonChartService;
    private final GenieChartService genieChartService;
    private final FloChartService floChartService;
    private final BugsChartService bugsChartService;

    @GetMapping("/")
    public String home(Model model) {

        //현재 시각
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.toLocalTime().getMinute() < 6) {
            //아직 크롤링 되지 않은 경우 한시간 전 차트 렌더링
            localDateTime = localDateTime.minusHours(1);
        }

        LocalDate nowDate = localDateTime.toLocalDate();
        int hour = localDateTime.getHour();
        LocalTime nowTime = LocalTime.of(hour, 0);
        localDateTime = LocalDateTime.of(nowDate, nowTime);

        model.addAttribute("nowDate", nowDate);
        model.addAttribute("nowTime", nowTime);

        //최근 차트 가져오기
        List<MelonChart> melonCharts = melonChartService.findByDateTime(localDateTime);
        List<GenieChart> genieCharts = genieChartService.findByDateTime(localDateTime);
        List<FloChart> floCharts = floChartService.findByDateTime(localDateTime);
        List<BugsChart> bugsCharts = bugsChartService.findByDateTime(localDateTime);

        model.addAttribute("melonCharts", melonCharts);
        model.addAttribute("genieCharts", genieCharts);
        model.addAttribute("floCharts", floCharts);
        model.addAttribute("bugsCharts", bugsCharts);
        return "home";
    }
}