package com.example.pefpr.kahaniyonkashehar.modalclasses;

/**
 * Created by PEF-2 on 27/10/2015.
 */
public class GroupList {
    public String groupId;
    public String groupName;

    public GroupList(String id, String name) {
        groupId = id;
        groupName = name;
    }


    @Override
    public String toString() {
        return this.groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }
}
