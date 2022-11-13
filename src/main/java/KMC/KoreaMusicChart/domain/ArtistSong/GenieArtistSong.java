package KMC.KoreaMusicChart.domain.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.GenieArtist;
import KMC.KoreaMusicChart.domain.Song.GenieSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenieArtistSong {

    @Id
    @GeneratedValue
    @Column(name = "genie_artist_song_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genie_artist_id")
    private GenieArtist genieArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genie_song_id")
    private GenieSong genieSong;

    //==생성 메서드==//
    public static GenieArtistSong createGenieArtistSong(GenieArtist genieArtist, GenieSong genieSong) {
        GenieArtistSong genieArtistSong = new GenieArtistSong();
        genieArtistSong.setGenieArtist(genieArtist);
        genieArtistSong.setGenieSong(genieSong);
        return genieArtistSong;
    }

    //==연관관계 메서드==//
    public void setGenieArtist(GenieArtist genieArtist) {
        genieArtist.getGenieArtistSongs().add(this);
        this.genieArtist = genieArtist;
    }

    public void setGenieSong(GenieSong genieSong) {
        genieSong.getGenieArtistSongs().add(this);
        this.genieSong = genieSong;
    }
}
