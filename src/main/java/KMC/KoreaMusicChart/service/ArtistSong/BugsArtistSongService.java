package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong;
import KMC.KoreaMusicChart.domain.Song.BugsSong;
import KMC.KoreaMusicChart.repository.ArtistSong.BugsArtistSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BugsArtistSongService {

    private final BugsArtistSongRepository bugsArtistSongRepository;

    @Transactional
    public Long save(BugsArtistSong bugsArtistSong) {
        validateDuplicateBugsArtistSong(bugsArtistSong);
        bugsArtistSongRepository.save(bugsArtistSong);
        return bugsArtistSong.getId();
    }

    private void validateDuplicateBugsArtistSong(BugsArtistSong bugsArtistSong) {
        List<BugsArtistSong> findArtistSong = bugsArtistSongRepository.findByArtistAndSong(bugsArtistSong.getBugsArtist(), bugsArtistSong.getBugsSong());
        if (!findArtistSong.isEmpty()) {
            throw new IllegalStateException("Error: BugsArtistSong 중복 에러");
        }
    }

    public BugsArtistSong findOne(Long id) {
        return bugsArtistSongRepository.findOne(id);
    }

    public List<BugsArtistSong> findAll() {
        return bugsArtistSongRepository.findAll();
    }

    public List<BugsArtistSong> findByArtistAndSong(BugsArtist artist, BugsSong song) {
        return bugsArtistSongRepository.findByArtistAndSong(artist, song);
    }
}
