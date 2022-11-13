package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import KMC.KoreaMusicChart.repository.Artist.MelonArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MelonArtistService {

    private final MelonArtistRepository melonArtistRepository;

    @Transactional
    public Long save(MelonArtist artist) {
        validateDuplicateMelonArtist(artist);
        melonArtistRepository.save(artist);
        return artist.getId();
    }

    private void validateDuplicateMelonArtist(MelonArtist artist) {

        List<MelonArtist> artists = melonArtistRepository.findByNameAndDebutDate(artist.getName(), artist.getDebutDate());
        if (!artists.isEmpty()) {
            throw new IllegalStateException("Error: MelonArtist 중복 저장 오류");
        }
    }

    public MelonArtist findOne(Long artistId) {
        return melonArtistRepository.findOne(artistId);
    }

    public List<MelonArtist> findAll() {
        return melonArtistRepository.findAll();
    }

    public List<MelonArtist> findByNameAndDebutDate(String name, String debutDate) {
        return melonArtistRepository.findByNameAndDebutDate(name, debutDate);
    }
}