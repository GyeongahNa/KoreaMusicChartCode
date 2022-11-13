package KMC.KoreaMusicChart.domain.Artist;

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
public class BugsArtist {

    @Id
    @GeneratedValue
    @Column(name = "bugs_artist_id")
    private Long id;

    private Long bugsId;
    private String name;

    @OneToMany(mappedBy = "bugsArtist")
    private final List<BugsArtistSong> bugsArtistSongs = new ArrayList<>();

    private String debutDate;

    //==생성 메서드==//
    public static BugsArtist createBugsArtist(Long bugsId, String name, String debutDate) {
        BugsArtist bugsArtist = new BugsArtist();
        bugsArtist.bugsId = bugsId;
        bugsArtist.name = name;
        bugsArtist.debutDate = debutDate;
        return bugsArtist;
    }

    public void updateBugsId(Long bugsId) {
        this.bugsId = bugsId;
    }
}