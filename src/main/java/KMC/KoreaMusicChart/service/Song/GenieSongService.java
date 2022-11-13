package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.GenieSong;
import KMC.KoreaMusicChart.repository.Song.GenieSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenieSongService {

    private final GenieSongRepository genieSongRepository;

    @Transactional
    public Long save(GenieSong genieSong) {
        validateDuplicateGenieSong(genieSong);
        genieSongRepository.save(genieSong);
        return genieSong.getId();
    }

    private void validateDuplicateGenieSong(GenieSong genieSong) {
        List<GenieSong> genieSongs = genieSongRepository.findByNameAndAlbum(genieSong.getName(), genieSong.getAlbum());
        if (!genieSongs.isEmpty()) {
            throw new IllegalStateException("Error: GenieSong 중복 저장");
        }
    }

    public GenieSong findOne(Long songId) {
        return genieSongRepository.findOne(songId);
    }

    public List<GenieSong> findAll() {
        return genieSongRepository.findAll();
    }

    public List<GenieSong> findByNameAndAlbum(String name, String album) {
        return genieSongRepository.findByNameAndAlbum(name, album);
    }
}
