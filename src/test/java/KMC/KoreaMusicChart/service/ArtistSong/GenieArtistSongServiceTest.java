package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import KMC.KoreaMusicChart.service.Artist.GenieArtistService;
import KMC.KoreaMusicChart.service.Song.GenieSongService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static KMC.KoreaMusicChart.domain.Artist.GenieArtist.createGenieArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong.createGenieArtistSong;
import static KMC.KoreaMusicChart.domain.Song.GenieSong.createGenieSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class GenieArtistSongServiceTest {

    @Autowired
    private GenieArtistSongService genieArtistSongService;
    @Autowired
    private GenieArtistService genieArtistService;
    @Autowired
    private GenieSongService genieSongService;

    @Test
    public void 아티스트곡_등록() throws Exception {
        //given
        GenieArtist genieArtist = createGenieArtist((long) 12345, "유재석", "20220702");
        GenieSong genieSong = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        genieArtistService.save(genieArtist);
        genieSongService.save(genieSong);

        //when
        GenieArtistSong genieArtistSong = createGenieArtistSong(genieArtist, genieSong);
        Long saveId = genieArtistSongService.save(genieArtistSong);
        GenieArtistSong findArtistSong = genieArtistSongService.findOne(saveId);

        //then
        assertEquals(genieArtistSong, findArtistSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트곡_예외() throws Exception {
        //given
        GenieArtist genieArtist = createGenieArtist((long) 12345, "유재석", "20220702");
        GenieSong genieSong = createGenieSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        GenieArtistSong genieArtistSong = createGenieArtistSong(genieArtist, genieSong);

        genieArtistService.save(genieArtist);
        genieSongService.save(genieSong);

        //when
        genieArtistSongService.save(genieArtistSong);
        genieArtistSongService.save(genieArtistSong);

        //then
        fail("GenieArtistSong 중복 예외가 발생해야 함");
    }
}