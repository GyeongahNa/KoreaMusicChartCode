package KMC.KoreaMusicChart.domain.Artist;

import KMC.KoreaMusicChart.domain.ArtistSong.MelonArtistSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MelonArtist {

    @Id
    @GeneratedValue
    @Column(name = "melon_artist_id")
    private Long id;

    private Long melonId;
    private String name;

    @OneToMany(mappedBy = "melonArtist")
    private final List<MelonArtistSong> melonArtistSongs = new ArrayList<>();
    private String debutDate;

    //==생성 메서드==//
    public static MelonArtist createMelonArtist(Long melonId, String name, String debutDate) {
        MelonArtist melonArtist = new MelonArtist();
        melonArtist.melonId = melonId;
        melonArtist.name = name;
        melonArtist.debutDate = debutDate;
        return melonArtist;
    }

    public void updateMelonId(Long melonId) {
        this.melonId = melonId;
    }
}