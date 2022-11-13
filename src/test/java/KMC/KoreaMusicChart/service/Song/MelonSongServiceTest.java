package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.MelonSong;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Song.MelonSong.createMelonSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MelonSongServiceTest {

    @Autowired
    private MelonSongService melonSongService;

    @Test
    public void 곡등록() throws Exception {
        //given
        MelonSong melonSong = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        Long saveId = melonSongService.save(melonSong);
        MelonSong findMelonSong = melonSongService.findOne(saveId);

        //then
        assertEquals(melonSong, findMelonSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_곡_예외() throws Exception {
        //given
        MelonSong melonSong = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        melonSongService.save(melonSong);
        melonSongService.save(melonSong);

        //then
        fail("MelonSong 예외가 발생해야 함");
    }

    @Test
    public void 이름과앨범으로찾기() throws Exception {
        //given
        MelonSong melonSong1 = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample1");
        MelonSong melonSong2 = createMelonSong((long) 54321, "냉면", "무한도전 앨범", "imageUrlExample2");

        melonSongService.save(melonSong1);
        melonSongService.save(melonSong2);

        //when
        List<MelonSong> findSongs = melonSongService.findByNameAndAlbum("말하는대로", "무한도전 앨범");

        //then
        assertEquals(1, findSongs.size());
        assertEquals(melonSong1, findSongs.get(0));
    }
}