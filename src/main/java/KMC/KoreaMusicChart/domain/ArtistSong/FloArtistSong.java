package KMC.KoreaMusicChart.domain.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.FloArtist;
import KMC.KoreaMusicChart.domain.Song.FloSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FloArtistSong {

    @Id
    @GeneratedValue
    @Column(name = "flo_artist_song_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flo_artist_id")
    private FloArtist floArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flo_song_id")
    private FloSong floSong;

    //==생성 메서드==//
    public static FloArtistSong createFloArtistSong(FloArtist floArtist, FloSong floSong) {
        FloArtistSong floArtistSong = new FloArtistSong();
        floArtistSong.setFloArtist(floArtist);
        floArtistSong.setFloSong(floSong);
        return floArtistSong;
    }

    //==연관관계 메서드==//
    public void setFloArtist(FloArtist floArtist) {
        floArtist.getFloArtistSongs().add(this);
        this.floArtist = floArtist;
    }

    public void setFloSong(FloSong floSong) {
        floSong.getFloArtistSongs().add(this);
        this.floSong = floSong;
    }
}
