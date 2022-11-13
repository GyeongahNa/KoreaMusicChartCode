package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.FloSong;
import KMC.KoreaMusicChart.repository.Song.FloSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FloSongService {

    private final FloSongRepository floSongRepository;

    @Transactional
    public Long save(FloSong floSong) {
        validateDuplicateFloSong(floSong);
        floSongRepository.save(floSong);
        return floSong.getId();
    }

    private void validateDuplicateFloSong(FloSong floSong) {
        List<FloSong> floSongs = floSongRepository.findByNameAndAlbum(floSong.getName(), floSong.getAlbum());
        if (!floSongs.isEmpty()) {
            throw new IllegalStateException("Error: FloSong 중복 저장");
        }
    }

    public FloSong findOne(Long songId) {
        return floSongRepository.findOne(songId);
    }

    public List<FloSong> findAll() {
        return floSongRepository.findAll();
    }

    public List<FloSong> findByNameAndAlbum(String name, String album) {
        return floSongRepository.findByNameAndAlbum(name, album);
    }
}
