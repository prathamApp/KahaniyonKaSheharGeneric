package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by Ameya on 28-Nov-17.
 */

public class GamesView {

    public String gameId;
    public String gameName;
    public String gameThumbnail;
    public String gamePath;
    public String nodeType;
    public String nodelist;

    public GamesView(String gameName, String gameId, String gameThumbnail, String gamePath, String nodeType, String nodelist) {
        this.gameName = gameName;
        this.gameId = gameId;
        this.gameThumbnail = gameThumbnail;
        this.gamePath = gamePath;
        this.nodeType = nodeType;
        this.nodelist = nodelist;
    }


    public GamesView() {

    }
}
