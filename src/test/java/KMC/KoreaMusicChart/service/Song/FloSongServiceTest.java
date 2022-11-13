package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.FloSong;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Song.FloSong.createFloSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FloSongServiceTest {

    @Autowired
    private FloSongService floSongService;

    @Test
    public void 곡등록() throws Exception {
        //given
        FloSong floSong = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        Long saveId = floSongService.save(floSong);
        FloSong findFloSong = floSongService.findOne(saveId);

        //then
        assertEquals(floSong, findFloSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_곡_예외() throws Exception {
        //given
        FloSong floSong = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        floSongService.save(floSong);
        floSongService.save(floSong);

        //then
        fail("FloSong 중복 예외가 발생해야 함");
    }

    @Test
    public void 이름과앨범으로찾기() throws Exception {
        //given
        FloSong floSong1 = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample1");
        FloSong floSong2 = createFloSong((long) 54321, "냉면", "무한도전 앨범", "imageUrlExample2");

        floSongService.save(floSong1);
        floSongService.save(floSong2);

        //when
        List<FloSong> findSongs = floSongService.findByNameAndAlbum("말하는대로", "무한도전 앨범");

        //then
        assertEquals(1, findSongs.size());
        assertEquals(floSong1, findSongs.get(0));
    }
}