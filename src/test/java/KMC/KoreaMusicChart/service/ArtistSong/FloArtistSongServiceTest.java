package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong;
import KMC.KoreaMusicChart.domain.Song.FloSong;
import KMC.KoreaMusicChart.service.Artist.FloArtistService;
import KMC.KoreaMusicChart.service.Song.FloSongService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static KMC.KoreaMusicChart.domain.Artist.FloArtist.createFloArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong.createFloArtistSong;
import static KMC.KoreaMusicChart.domain.Song.FloSong.createFloSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class FloArtistSongServiceTest {

    @Autowired
    private FloArtistSongService floArtistSongService;
    @Autowired
    private FloArtistService floArtistService;
    @Autowired
    private FloSongService floSongService;

    @Test
    public void 아티스트곡_등록() throws Exception {
        //given
        FloArtist floArtist = createFloArtist((long) 12345, "유재석");
        FloSong floSong = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        floArtistService.save(floArtist);
        floSongService.save(floSong);

        //when
        FloArtistSong floArtistSong = createFloArtistSong(floArtist, floSong);
        Long saveId = floArtistSongService.save(floArtistSong);
        FloArtistSong findArtistSong = floArtistSongService.findOne(saveId);

        //then
        assertEquals(floArtistSong, findArtistSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트곡_예외() throws Exception {
        //given
        FloArtist floArtist = createFloArtist((long) 12345, "유재석");
        FloSong floSong = createFloSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        FloArtistSong floArtistSong = createFloArtistSong(floArtist, floSong);

        floArtistService.save(floArtist);
        floSongService.save(floSong);

        //when
        floArtistSongService.save(floArtistSong);
        floArtistSongService.save(floArtistSong);

        //then
        fail("FloArtistSong 중복 예외가 발생해야 함");
    }
}