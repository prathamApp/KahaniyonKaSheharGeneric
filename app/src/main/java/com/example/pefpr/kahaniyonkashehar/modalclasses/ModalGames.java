
package com.example.pefpr.kahaniyonkashehar.modalclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModalGames {

    @SerializedName("nodeId")
    @Expose
    private String nodeId;
    @SerializedName("nodeType")
    @Expose
    private String nodeType;
    @SerializedName("nodeTitle")
    @Expose
    private String nodeTitle;
    @SerializedName("nodeImage")
    @Expose
    private String nodeImage;
    @SerializedName("nodePhase")
    @Expose
    private String nodePhase;
    @SerializedName("nodeAge")
    @Expose
    private String nodeAge;
    @SerializedName("nodeDesc")
    @Expose
    private String nodeDesc;
    @SerializedName("nodeKeywords")
    @Expose
    private String nodeKeywords;
    @SerializedName("sameCode")
    @Expose
    private String sameCode;
    @SerializedName("resourceId")
    @Expose
    private String resourceId;
    @SerializedName("resourceAudio")
    @Expose
    private String resourceAudio;
    @SerializedName("resourceBg")
    @Expose
    private String resourceBg;
    @SerializedName("resourceDesc")
    @Expose
    private String resourceDesc;
    @SerializedName("resourceType")
    @Expose
    private String resourceType;
    @SerializedName("resourcePath")
    @Expose
    private String resourcePath;
    @SerializedName("resourceFrom")
    @Expose
    private String resourceFrom;
    @SerializedName("resourceTo")
    @Expose
    private String resourceTo;
    @SerializedName("resourceDuration")
    @Expose
    private String resourceDuration;
    @SerializedName("nodelist")
    @Expose
    private List<ModalGames> nodelist = null;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getNodeImage() {
        return nodeImage;
    }

    public void setNodeImage(String nodeImage) {
        this.nodeImage = nodeImage;
    }

    public String getNodePhase() {
        return nodePhase;
    }

    public void setNodePhase(String nodePhase) {
        this.nodePhase = nodePhase;
    }

    public String getNodeAge() {
        return nodeAge;
    }

    public void setNodeAge(String nodeAge) {
        this.nodeAge = nodeAge;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getNodeKeywords() {
        return nodeKeywords;
    }

    public void setNodeKeywords(String nodeKeywords) {
        this.nodeKeywords = nodeKeywords;
    }

    public String getSameCode() {
        return sameCode;
    }

    public void setSameCode(String sameCode) {
        this.sameCode = sameCode;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceAudio() {
        return resourceAudio;
    }

    public void setResourceAudio(String resourceAudio) {
        this.resourceAudio = resourceAudio;
    }

    public String getResourceBg() {
        return resourceBg;
    }

    public void setResourceBg(String resourceBg) {
        this.resourceBg = resourceBg;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourceFrom() {
        return resourceFrom;
    }

    public void setResourceFrom(String resourceFrom) {
        this.resourceFrom = resourceFrom;
    }

    public String getResourceTo() {
        return resourceTo;
    }

    public void setResourceTo(String resourceTo) {
        this.resourceTo = resourceTo;
    }

    public String getResourceDuration() {
        return resourceDuration;
    }

    public void setResourceDuration(String resourceDuration) { this.resourceDuration = resourceDuration; }

    public List<ModalGames> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<ModalGames> nodelist) {
        this.nodelist = nodelist;
    }

}
