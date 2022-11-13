package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import KMC.KoreaMusicChart.service.Artist.MelonArtistService;
import KMC.KoreaMusicChart.service.Song.MelonSongService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static KMC.KoreaMusicChart.domain.Artist.MelonArtist.createMelonArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong.createMelonArtistSong;
import static KMC.KoreaMusicChart.domain.Song.MelonSong.createMelonSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class MelonArtistSongServiceTest {

    @Autowired
    private MelonArtistSongService melonArtistSongService;
    @Autowired
    private MelonArtistService melonArtistService;
    @Autowired
    private MelonSongService melonSongService;

    @Test
    public void 아티스트곡_등록() throws Exception {
        //given
        MelonArtist melonArtist = createMelonArtist((long) 12345, "유재석", "20220702");
        MelonSong melonSong = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        melonArtistService.save(melonArtist);
        melonSongService.save(melonSong);

        //when
        MelonArtistSong melonArtistSong = createMelonArtistSong(melonArtist, melonSong);
        Long saveId = melonArtistSongService.save(melonArtistSong);
        MelonArtistSong findArtistSong = melonArtistSongService.findOne(saveId);

        //then
        assertEquals(melonArtistSong, findArtistSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트곡_예외() throws Exception {
        //given
        MelonArtist melonArtist = createMelonArtist((long) 12345, "유재석", "20220702");
        MelonSong melonSong = createMelonSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        MelonArtistSong melonArtistSong = createMelonArtistSong(melonArtist, melonSong);

        melonArtistService.save(melonArtist);
        melonSongService.save(melonSong);

        //when
        melonArtistSongService.save(melonArtistSong);
        melonArtistSongService.save(melonArtistSong);

        //then
        fail("MelonArtistSong 중복 예외가 발생해야 함");
    }
}