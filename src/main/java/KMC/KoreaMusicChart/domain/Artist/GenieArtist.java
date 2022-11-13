package KMC.KoreaMusicChart.domain.Artist;

import KMC.KoreaMusicChart.domain.ArtistSong.GenieArtistSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenieArtist {

    @Id
    @GeneratedValue
    @Column(name = "genie_artist_id")
    private Long id;

    private Long genieId;
    private String name;

    @OneToMany(mappedBy = "genieArtist")
    private final List<GenieArtistSong> genieArtistSongs = new ArrayList<>();

    private String debutDate;

    //==생성 메서드==//
    public static GenieArtist createGenieArtist(Long genieId, String name, String debutDate) {
        GenieArtist genieArtist = new GenieArtist();
        genieArtist.genieId = genieId;
        genieArtist.name = name;
        genieArtist.debutDate = debutDate;
        return genieArtist;
    }

    public void updateGenieId(Long genieId) {
        this.genieId = genieId;
    }
}