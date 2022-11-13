package KMC.KoreaMusicChart.service.Artist;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import KMC.KoreaMusicChart.repository.Artist.BugsArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BugsArtistService {

    private final BugsArtistRepository bugsArtistRepository;

    @Transactional
    public Long save(BugsArtist artist) {
        validateDuplicateBugsArtist(artist);
        bugsArtistRepository.save(artist);
        return artist.getId();
    }

    private void validateDuplicateBugsArtist(BugsArtist artist) {

        List<BugsArtist> artists = bugsArtistRepository.findByNameAndDebutDate(artist.getName(), artist.getDebutDate());
        if (!artists.isEmpty()) {
            throw new IllegalStateException("Error: BugsArtist 중복 저장 오류");
        }
    }

    public BugsArtist findOne(Long artistId) {
        return bugsArtistRepository.findOne(artistId);
    }

    public List<BugsArtist> findAll() {
        return bugsArtistRepository.findAll();
    }

    public List<BugsArtist> findByNameAndDebutDate(String name, String debutDate) {
        return bugsArtistRepository.findByNameAndDebutDate(name, debutDate);
    }
}