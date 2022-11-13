package KMC.KoreaMusicChart.domain.Song;

import KMC.KoreaMusicChart.domain.ArtistSong.BugsArtistSong;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BugsSong {

    @Id
    @GeneratedValue
    @Column(name = "bugs_song_id")
    private Long id;

    private Long bugsId;
    private String name;
    private String album;
    private String imageUrl;

    @OneToMany(mappedBy = "bugsSong")
    private final List<BugsArtistSong> bugsArtistSongs = new ArrayList<>();

    //==생성 메서드==//
    public static BugsSong createBugsSong(Long bugsId, String name, String album, String imageUrl) {
        BugsSong bugsSong = new BugsSong();
        bugsSong.bugsId = bugsId;
        bugsSong.name = name;
        bugsSong.album = album;
        bugsSong.imageUrl = imageUrl;
        return bugsSong;
    }

    public void updateBugsId(Long bugsId) {
        this.bugsId = bugsId;
    }
}
