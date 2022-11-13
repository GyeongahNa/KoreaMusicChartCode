package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.BugsSong;
import KMC.KoreaMusicChart.repository.Song.BugsSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BugsSongService {

    private final BugsSongRepository bugsSongRepository;

    @Transactional
    public Long save(BugsSong song) {
        validateDuplicateSong(song);
        bugsSongRepository.save(song);
        return song.getId();
    }

    private void validateDuplicateSong(BugsSong song) {
        List<BugsSong> songs = bugsSongRepository.findByNameAndAlbum(song.getName(), song.getAlbum());
        if (!songs.isEmpty()) {
            throw new IllegalStateException("Error: BugsSong 중복 저장");
        }
    }

    public BugsSong findOne(Long songId) {
        return bugsSongRepository.findOne(songId);
    }

    public List<BugsSong> findAll() {
        return bugsSongRepository.findAll();
    }

    public List<BugsSong> findByNameAndAlbum(String name, String album) {
        return bugsSongRepository.findByNameAndAlbum(name, album);
    }
}
