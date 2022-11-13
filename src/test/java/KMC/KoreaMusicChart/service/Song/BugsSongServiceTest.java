package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.BugsSong;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KMC.KoreaMusicChart.domain.Song.BugsSong.createBugsSong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BugsSongServiceTest {

    @Autowired
    private BugsSongService bugsSongService;

    @Test
    public void 곡등록() throws Exception {
        //given
        BugsSong bugsSong = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        Long saveId = bugsSongService.save(bugsSong);
        BugsSong findBugsSong = bugsSongService.findOne(saveId);

        //then
        assertEquals(bugsSong, findBugsSong);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_곡_예외() throws Exception {
        //given
        BugsSong bugsSong = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample");

        //when
        bugsSongService.save(bugsSong);
        bugsSongService.save(bugsSong);

        //then
        fail("BugsSong 예외가 발생해야 함");
    }

    @Test
    public void 이름과앨범으로찾기() throws Exception {
        //given
        BugsSong bugsSong1 = createBugsSong((long) 12345, "말하는대로", "무한도전 앨범", "imageUrlExample1");
        BugsSong bugsSong2 = createBugsSong((long) 54321, "냉면", "무한도전 앨범", "imageUrlExample2");

        bugsSongService.save(bugsSong1);
        bugsSongService.save(bugsSong2);

        //when
        List<BugsSong> findSongs = bugsSongService.findByNameAndAlbum("말하는대로", "무한도전 앨범");

        //then
        assertEquals(1, findSongs.size());
        assertEquals(bugsSong1, findSongs.get(0));
    }
}