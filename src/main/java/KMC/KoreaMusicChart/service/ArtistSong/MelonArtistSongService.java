package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import KMC.KoreaMusicChart.repository.ArtistSong.MelonArtistSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MelonArtistSongService {

    private final MelonArtistSongRepository melonArtistSongRepository;

    @Transactional
    public Long save(MelonArtistSong melonArtistSong) {
        validateDuplicateMelonArtistSong(melonArtistSong);
        melonArtistSongRepository.save(melonArtistSong);
        return melonArtistSong.getId();
    }

    private void validateDuplicateMelonArtistSong(MelonArtistSong melonArtistSong) {
        List<MelonArtistSong> findArtistSong = melonArtistSongRepository.findByArtistAndSong(melonArtistSong.getMelonArtist(), melonArtistSong.getMelonSong());
        if (!findArtistSong.isEmpty()) {
            throw new IllegalStateException("Error: MelonArtistSong 중복 에러");
        }
    }

    public MelonArtistSong findOne(Long id) {
        return melonArtistSongRepository.findOne(id);
    }

    public List<MelonArtistSong> findAll() {
        return melonArtistSongRepository.findAll();
    }

    public List<MelonArtistSong> findByArtistAndSong(MelonArtist artist, MelonSong song) {
        return melonArtistSongRepository.findByArtistAndSong(artist, song);
    }
}
