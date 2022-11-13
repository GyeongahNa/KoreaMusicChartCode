package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import KMC.KoreaMusicChart.repository.Artist.FloArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FloArtistService {

    private final FloArtistRepository floArtistRepository;

    @Transactional
    public Long save(FloArtist floArtist) {
        validateDuplicateFloArtist(floArtist);
        floArtistRepository.save(floArtist);
        return floArtist.getId();
    }

    private void validateDuplicateFloArtist(FloArtist floArtist) {

        List<FloArtist> floArtists = floArtistRepository.findByName(floArtist.getName());
        if (!floArtists.isEmpty()) {
            throw new IllegalStateException("Error: FloArtist 중복 저장 오류");
        }
    }

    public FloArtist findOne(Long artistId) {
        return floArtistRepository.findOne(artistId);
    }

    public List<FloArtist> findAll() {
        return floArtistRepository.findAll();
    }

    public List<FloArtist> findByName(String name) {
        return floArtistRepository.findByName(name);
    }
}