package KMC.KoreaMusicChart.domain.Artist;

import KMC.KoreaMusicChart.domain.ArtistSong.FloArtistSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FloArtist {

    @Id
    @GeneratedValue
    @Column(name = "flo_artist_id")
    private Long id;

    private Long floId;
    private String name;

    @OneToMany(mappedBy = "floArtist")
    private final List<FloArtistSong> floArtistSongs = new ArrayList<>();

    //==생성 메서드==//
    public static FloArtist createFloArtist(Long floId, String name) {
        FloArtist floArtist = new FloArtist();
        floArtist.floId = floId;
        floArtist.name = name;
        return floArtist;
    }

    public void updateFloId(Long floId) {
        this.floId = floId;
    }
}