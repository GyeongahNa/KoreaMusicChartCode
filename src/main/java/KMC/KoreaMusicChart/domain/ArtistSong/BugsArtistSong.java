package KMC.KoreaMusicChart.domain.ArtistSong;

import KMC.KoreaMusicChart.domain.Artist.BugsArtist;
import KMC.KoreaMusicChart.domain.Song.BugsSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BugsArtistSong {

    @Id
    @GeneratedValue
    @Column(name = "bugs_artist_song_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bugs_artist_id")
    private BugsArtist bugsArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bugs_song_id")
    private BugsSong bugsSong;

    //==생성 메서드==//
    public static BugsArtistSong createBugsArtistSong(BugsArtist bugsArtist, BugsSong bugsSong) {
        BugsArtistSong bugsArtistSong = new BugsArtistSong();
        bugsArtistSong.setBugsArtist(bugsArtist);
        bugsArtistSong.setBugsSong(bugsSong);
        return bugsArtistSong;
    }

    //==연관관계 메서드==//
    public void setBugsArtist(BugsArtist bugsArtist) {
        bugsArtist.getBugsArtistSongs().add(this);
        this.bugsArtist = bugsArtist;
    }

    public void setBugsSong(BugsSong bugsSong) {
        bugsSong.getBugsArtistSongs().add(this);
        this.bugsSong = bugsSong;
    }
}
