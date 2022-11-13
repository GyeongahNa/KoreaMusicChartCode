package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import KMC.KoreaMusicChart.repository.Artist.GenieArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenieArtistService {

    private final GenieArtistRepository genieArtistRepository;

    @Transactional
    public Long save(GenieArtist artist) {
        validateDuplicateGenieArtist(artist);
        genieArtistRepository.save(artist);
        return artist.getId();
    }

    private void validateDuplicateGenieArtist(GenieArtist artist) {

        List<GenieArtist> artists = genieArtistRepository.findByNameAndDebutDate(artist.getName(), artist.getDebutDate());
        if (!artists.isEmpty()) {
            throw new IllegalStateException("Error: GenieArtist 중복 저장 오류");
        }
    }

    public GenieArtist findOne(Long artistId) {
        return genieArtistRepository.findOne(artistId);
    }

    public List<GenieArtist> findAll() {
        return genieArtistRepository.findAll();
    }

    public List<GenieArtist> findByNameAndDebutDate(String name, String debutDate) {
        return genieArtistRepository.findByNameAndDebutDate(name, debutDate);
    }
}