package KMC.KoreaMusicChart.service.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import KMC.KoreaMusicChart.repository.ArtistSong.GenieArtistSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenieArtistSongService {

    private final GenieArtistSongRepository genieArtistSongRepository;

    @Transactional
    public Long save(GenieArtistSong genieArtistSong) {
        validateDuplicateGenieArtistSong(genieArtistSong);
        genieArtistSongRepository.save(genieArtistSong);
        return genieArtistSong.getId();
    }

    private void validateDuplicateGenieArtistSong(GenieArtistSong genieArtistSong) {
        List<GenieArtistSong> findArtistSong = genieArtistSongRepository.findByArtistAndSong(genieArtistSong.getGenieArtist(), genieArtistSong.getGenieSong());
        if (!findArtistSong.isEmpty()) {
            throw new IllegalStateException("Error: GenieArtistSong 중복 에러");
        }
    }

    public GenieArtistSong findOne(Long id) {
        return genieArtistSongRepository.findOne(id);
    }

    public List<GenieArtistSong> findAll() {
        return genieArtistSongRepository.findAll();
    }

    public List<GenieArtistSong> findByArtistAndSong(GenieArtist genieArtist, GenieSong genieSong) {
        return genieArtistSongRepository.findByArtistAndSong(genieArtist, genieSong);
    }
}
