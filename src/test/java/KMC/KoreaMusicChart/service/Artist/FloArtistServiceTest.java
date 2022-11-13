package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.FloArtist.createFloArtist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class FloArtistServiceTest {

    @Autowired
    private FloArtistService floArtistService;

    @Test
    public void 아티스트등록() throws Exception {
        //given
        FloArtist floArtist = createFloArtist((long) 12345, "유재석");

        //when
        Long saveId = floArtistService.save(floArtist);
        FloArtist findArtist = floArtistService.findOne(saveId);

        //then
        assertEquals(floArtist, findArtist);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트_예외() throws Exception {
        //given
        FloArtist floArtist1 = createFloArtist((long) 12345, "박명수");
        FloArtist floArtist2 = createFloArtist((long) 12346, "박명수");

        //when
        floArtistService.save(floArtist1);
        floArtistService.save(floArtist2);

        //then
        fail("FloArtist 중복 예외가 발생해야 함");
    }


    @Test
    public void 이름으로_찾기() throws Exception {
        //given
        FloArtist floArtist1 = createFloArtist((long) 12345, "유재석");
        FloArtist floArtist2 = createFloArtist((long) 12346, "박명수");

        floArtistService.save(floArtist1);
        floArtistService.save(floArtist2);

        //when
        List<FloArtist> findArtists = floArtistService.findByName("유재석");

        //then
        assertEquals(1, findArtists.size());
        assertEquals(floArtist1, findArtists.get(0));
    }
}