package pl.lodz.p.it.zzpj.spotify.model;

import java.util.List;

public class PlaylistList {
    private List<Playlist> playlists;

    public PlaylistList() {
    }

    public PlaylistList(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
