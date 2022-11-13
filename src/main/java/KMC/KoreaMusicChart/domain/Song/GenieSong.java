package KMC.KoreaMusicChart.domain.Song;

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
public class GenieSong {

    @Id
    @GeneratedValue
    @Column(name = "genie_song_id")
    private Long id;

    private Long genieId;
    private String name;
    private String album;
    private String imageUrl;

    @OneToMany(mappedBy = "genieSong")
    private final List<GenieArtistSong> genieArtistSongs = new ArrayList<>();

    //==생성 메서드==//
    public static GenieSong createGenieSong(Long genieId, String name, String album, String imageUrl) {
        GenieSong genieSong = new GenieSong();
        genieSong.genieId = genieId;
        genieSong.name = name;
        genieSong.album = album;
        genieSong.imageUrl = imageUrl;
        return genieSong;
    }

    public void updateGenieId(Long genieId) {
        this.genieId = genieId;
    }
}
