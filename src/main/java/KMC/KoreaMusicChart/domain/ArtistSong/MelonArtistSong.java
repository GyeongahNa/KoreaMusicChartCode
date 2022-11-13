package KMC.KoreaMusicChart.domain.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.MelonArtist;
import KMC.KoreaMusicChart.domain.Song.MelonSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MelonArtistSong {

    @Id
    @GeneratedValue
    @Column(name = "melon_artist_song_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "melon_artist_id")
    private MelonArtist melonArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "melon_song_id")
    private MelonSong melonSong;

    //==생성 메서드==//
    public static MelonArtistSong createMelonArtistSong(MelonArtist melonArtist, MelonSong melonSong) {
        MelonArtistSong melonArtistSong = new MelonArtistSong();
        melonArtistSong.setMelonArtist(melonArtist);
        melonArtistSong.setMelonSong(melonSong);
        return melonArtistSong;
    }

    //==연관관계 메서드==//
    public void setMelonArtist(MelonArtist melonArtist) {
        melonArtist.getMelonArtistSongs().add(this);
        this.melonArtist = melonArtist;
    }

    public void setMelonSong(MelonSong melonSong) {
        melonSong.getMelonArtistSongs().add(this);
        this.melonSong = melonSong;
    }
}
