package KMC.KoreaMusicChart.service.Song;

import KMC.KoreaMusicChart.domain.Song.MelonSong;
import KMC.KoreaMusicChart.repository.Song.MelonSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MelonSongService {

    private final MelonSongRepository melonSongRepository;

    @Transactional
    public Long save(MelonSong song) {
        validateDuplicateSong(song);
        melonSongRepository.save(song);
        return song.getId();
    }

    private void validateDuplicateSong(MelonSong song) {
        List<MelonSong> songs = melonSongRepository.findByNameAndAlbum(song.getName(), song.getAlbum());
        if (!songs.isEmpty()) {
            throw new IllegalStateException("Error: MelonSong 중복 저장");
        }
    }

    public MelonSong findOne(Long songId) {
        return melonSongRepository.findOne(songId);
    }

    public List<MelonSong> findAll() {
        return melonSongRepository.findAll();
    }

    public List<MelonSong> findByNameAndAlbum(String name, String album) {
        return melonSongRepository.findByNameAndAlbum(name, album);
    }
}
