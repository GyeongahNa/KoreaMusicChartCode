package KMC.KoreaMusicChart.domain.Song;

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
public class MelonSong {

    @Id
    @GeneratedValue
    @Column(name = "melon_song_id")
    private Long id;

    private Long melonId;
    private String name;
    private String album;
    private String imageUrl;

    @OneToMany(mappedBy = "melonSong")
    private final List<MelonArtistSong> melonArtistSongs = new ArrayList<>();

    //==생성 메서드==//
    public static MelonSong createMelonSong(Long melonId, String name, String album, String imageUrl) {
        MelonSong melonSong = new MelonSong();
        melonSong.melonId = melonId;
        melonSong.name = name;
        melonSong.album = album;
        melonSong.imageUrl = imageUrl;
        return melonSong;
    }

    public void updateMelonId(Long melonId) {
        this.melonId = melonId;
    }
}
