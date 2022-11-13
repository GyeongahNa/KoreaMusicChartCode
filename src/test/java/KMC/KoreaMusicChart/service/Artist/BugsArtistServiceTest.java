package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.BugsArtist.createBugsArtist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class BugsArtistServiceTest {

    @Autowired
    private BugsArtistService bugsArtistService;

    @Test
    public void 아티스트등록() throws Exception {
        //given
        BugsArtist bugsArtist = createBugsArtist((long) 12345, "유재석", "20220722");

        //when
        Long saveId = bugsArtistService.save(bugsArtist);
        BugsArtist findArtist = bugsArtistService.findOne(saveId);

        //then
        assertEquals(bugsArtist, findArtist);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트_예외() throws Exception {
        //given
        BugsArtist bugsArtist1 = createBugsArtist((long) 12345, "박명수", "20220722");
        BugsArtist bugsArtist2 = createBugsArtist((long) 12346, "박명수", "20220722");

        //when
        bugsArtistService.save(bugsArtist1);
        bugsArtistService.save(bugsArtist2);

        //then
        fail("BugsArtist 중복 예외가 발생해야 함");
    }


    @Test
    public void 이름과데뷔일로찾기() throws Exception {
        //given
        BugsArtist bugsArtist1 = createBugsArtist((long) 12345, "유재석", "20220702");
        BugsArtist bugsArtist2 = createBugsArtist((long) 12345, "박명수", "20220702");
        BugsArtist bugsArtist3 = createBugsArtist((long) 12345, "유재석", "20210502");

        bugsArtistService.save(bugsArtist1);
        bugsArtistService.save(bugsArtist2);
        bugsArtistService.save(bugsArtist3);

        //when
        List<BugsArtist> findArtists = bugsArtistService.findByNameAndDebutDate("유재석", "20220702");

        //then
        assertEquals(1, findArtists.size());
        assertEquals(bugsArtist1, findArtists.get(0));
    }
}