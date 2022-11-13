package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong;
import KMC.KoreaMusicChart.domain.Song.FloSong;
import KMC.KoreaMusicChart.repository.ArtistSong.FloArtistSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FloArtistSongService {

    private final FloArtistSongRepository floArtistSongRepository;

    @Transactional
    public Long save(FloArtistSong floArtistSong) {
        validateDuplicateFloArtistSong(floArtistSong);
        floArtistSongRepository.save(floArtistSong);
        return floArtistSong.getId();
    }

    private void validateDuplicateFloArtistSong(FloArtistSong floArtistSong) {
        List<FloArtistSong> floArtistSongs = floArtistSongRepository.findByArtistAndSong(floArtistSong.getFloArtist(), floArtistSong.getFloSong());
        if (!floArtistSongs.isEmpty()) {
            throw new IllegalStateException("Error: FloArtistSong 중복 에러");
        }
    }

    public FloArtistSong findOne(Long id) {
        return floArtistSongRepository.findOne(id);
    }

    public List<FloArtistSong> findAll() {
        return floArtistSongRepository.findAll();
    }

    public List<FloArtistSong> findByArtistAndSong(FloArtist artist, FloSong song) {
        return floArtistSongRepository.findByArtistAndSong(artist, song);
    }
}
