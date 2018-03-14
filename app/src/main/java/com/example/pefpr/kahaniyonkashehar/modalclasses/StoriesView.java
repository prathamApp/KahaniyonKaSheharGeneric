package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by Ameya on 28-Nov-17.
 */

public class StoriesView {

    public String storyId;
    public String storyName;
    public String storyThumbnail;
    public String storyData;

    public StoriesView(String storyName, String storyId, String storyThumbnail, String storyData) {
        this.storyName = storyName;
        this.storyId = storyId;
        this.storyThumbnail = storyThumbnail;
        this.storyData = storyData;
    }


    public StoriesView() {

    }
}
