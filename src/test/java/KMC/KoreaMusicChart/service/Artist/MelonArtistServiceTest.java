package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Artist.MelonArtist.createMelonArtist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class MelonArtistServiceTest {

    @Autowired
    private MelonArtistService melonArtistService;

    @Test
    public void 아티스트등록() throws Exception {
        //given
        MelonArtist melonArtist = createMelonArtist((long) 12345, "유재석", "20220722");

        //when
        Long saveId = melonArtistService.save(melonArtist);
        MelonArtist findArtist = melonArtistService.findOne(saveId);

        //then
        assertEquals(melonArtist, findArtist);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트_예외() throws Exception {
        //given
        MelonArtist melonArtist1 = createMelonArtist((long) 12345, "박명수", "20220722");
        MelonArtist melonArtist2 = createMelonArtist((long) 12346, "박명수", "20220722");

        //when
        melonArtistService.save(melonArtist1);
        melonArtistService.save(melonArtist2);

        //then
        fail("MelonArtist 중복 예외가 발생해야 함");
    }


    @Test
    public void 이름과데뷔일로찾기() throws Exception {
        //given
        MelonArtist melonArtist1 = createMelonArtist((long) 12345, "유재석", "20220702");
        MelonArtist melonArtist2 = createMelonArtist((long) 12345, "박명수", "20220702");
        MelonArtist melonArtist3 = createMelonArtist((long) 12345, "유재석", "20210502");

        melonArtistService.save(melonArtist1);
        melonArtistService.save(melonArtist2);
        melonArtistService.save(melonArtist3);

        //when
        List<MelonArtist> findArtists = melonArtistService.findByNameAndDebutDate("유재석", "20220702");

        //then
        assertEquals(1, findArtists.size());
        assertEquals(melonArtist1, findArtists.get(0));
    }
}