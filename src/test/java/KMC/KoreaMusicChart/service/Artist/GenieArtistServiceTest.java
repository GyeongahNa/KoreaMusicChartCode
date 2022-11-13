package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.GenieArtist.createGenieArtist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class GenieArtistServiceTest {

    @Autowired
    private GenieArtistService genieArtistService;

    @Test
    public void 아티스트등록() throws Exception {
        //given
        GenieArtist genieArtist = createGenieArtist((long) 12345, "유재석", "20220722");

        //when
        Long saveId = genieArtistService.save(genieArtist);
        GenieArtist findArtist = genieArtistService.findOne(saveId);

        //then
        assertEquals(genieArtist, findArtist);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트_예외() throws Exception {
        //given
        GenieArtist genieArtist1 = createGenieArtist((long) 12345, "박명수", "20220722");
        GenieArtist genieArtist2 = createGenieArtist((long) 12346, "박명수", "20220722");

        //when
        genieArtistService.save(genieArtist1);
        genieArtistService.save(genieArtist2);

        //then
        fail("GenieArtist 중복 예외가 발생해야 함");
    }


    @Test
    public void 이름과데뷔일로찾기() throws Exception {
        //given
        GenieArtist genieArtist1 = createGenieArtist((long) 12345, "유재석", "20220702");
        GenieArtist genieArtist2 = createGenieArtist((long) 12345, "박명수", "20220702");
        GenieArtist genieArtist3 = createGenieArtist((long) 12345, "유재석", "20210502");

        genieArtistService.save(genieArtist1);
        genieArtistService.save(genieArtist2);
        genieArtistService.save(genieArtist3);

        //when
        List<GenieArtist> findArtists = genieArtistService.findByNameAndDebutDate("유재석", "20220702");

        //then
        assertEquals(1, findArtists.size());
        assertEquals(genieArtist1, findArtists.get(0));
    }
}