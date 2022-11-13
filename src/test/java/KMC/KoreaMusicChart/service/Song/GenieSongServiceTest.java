package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.GenieSong;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Song.GenieSong.createGenieSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GenieSongServiceTest {

    @Autowired
    private GenieSongService genieSongService;

    @Test
    public void 곡등록() throws Exception {
        //given
        GenieSong genieSong = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        Long saveId = genieSongService.save(genieSong);
        GenieSong findGenieSong = genieSongService.findOne(saveId);

        //then
        assertEquals(genieSong, findGenieSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_곡_예외() throws Exception {
        //given
        GenieSong genieSong = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        genieSongService.save(genieSong);
        genieSongService.save(genieSong);

        //then
        fail("GenieSong 중복 예외가 발생해야 함");
    }

    @Test
    public void 이름과앨범으로찾기() throws Exception {
        //given
        GenieSong genieSong1 = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample1");
        GenieSong genieSong2 = createGenieSong((long) 54321, "냉면", "무한도전 앨범", "imageUrlExample2");

        genieSongService.save(genieSong1);
        genieSongService.save(genieSong2);

        //when
        List<GenieSong> findSongs = genieSongService.findByNameAndAlbum("말하는대로", "무한도전 앨범");

        //then
        assertEquals(1, findSongs.size());
        assertEquals(genieSong1, findSongs.get(0));
    }
}