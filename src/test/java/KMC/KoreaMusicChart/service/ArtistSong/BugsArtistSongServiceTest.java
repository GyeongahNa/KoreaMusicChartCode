package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong;
import KMC.KoreaMusicChart.domain.Song.BugsSong;
import KMC.KoreaMusicChart.service.Artist.BugsArtistService;
import KMC.KoreaMusicChart.service.Song.BugsSongService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static KMC.KoreaMusicChart.domain.Artist.BugsArtist.createBugsArtist;
import static KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong.createBugsArtistSong;
import static KMC.KoreaMusicChart.domain.Song.BugsSong.createBugsSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
//@Rollback(false)
public class BugsArtistSongServiceTest {

    @Autowired
    private BugsArtistSongService bugsArtistSongService;
    @Autowired
    private BugsArtistService bugsArtistService;
    @Autowired
    private BugsSongService bugsSongService;

    @Test
    public void 아티스트곡_등록() throws Exception {
        //given
        BugsArtist bugsArtist = createBugsArtist((long) 12345, "유재석", "20220702");
        BugsSong bugsSong = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        bugsArtistService.save(bugsArtist);
        bugsSongService.save(bugsSong);

        //when
        BugsArtistSong bugsArtistSong = createBugsArtistSong(bugsArtist, bugsSong);
        Long saveId = bugsArtistSongService.save(bugsArtistSong);
        BugsArtistSong findArtistSong = bugsArtistSongService.findOne(saveId);

        //then
        assertEquals(bugsArtistSong, findArtistSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_아티스트곡_예외() throws Exception {
        //given
        BugsArtist bugsArtist = createBugsArtist((long) 12345, "유재석", "20220702");
        BugsSong bugsSong = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");
        BugsArtistSong bugsArtistSong = createBugsArtistSong(bugsArtist, bugsSong);

        bugsArtistService.save(bugsArtist);
        bugsSongService.save(bugsSong);

        //when
        bugsArtistSongService.save(bugsArtistSong);
        bugsArtistSongService.save(bugsArtistSong);

        //then
        fail("BugsArtistSong 중복 예외가 발생해야 함");
    }
}