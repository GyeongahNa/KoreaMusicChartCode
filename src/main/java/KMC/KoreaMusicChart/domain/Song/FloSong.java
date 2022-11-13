package KMC.KoreaMusicChart.domain.Song;

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
public class FloSong {

    @Id
    @GeneratedValue
    @Column(name = "flo_song_id")
    private Long id;

    private Long floId;
    private String name;
    private String album;
    private String imageUrl;

    @OneToMany(mappedBy = "floSong")
    private final List<FloArtistSong> floArtistSongs = new ArrayList<>();

    //==생성 메서드==//
    public static FloSong createFloSong(Long floId, String name, String album, String imageUrl) {
        FloSong floSong = new FloSong();
        floSong.floId = floId;
        floSong.name = name;
        floSong.album = album;
        floSong.imageUrl = imageUrl;
        return floSong;
    }

    public void updateFloId(Long floId) {
        this.floId = floId;
    }
}
