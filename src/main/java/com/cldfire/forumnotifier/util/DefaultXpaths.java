package com.cldfire.forumnotifier.util;

import com.cldfire.forumnotifier.model.Forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultXpaths {

    private final Map<String, Object> defaultXpathMaps;

    public DefaultXpaths(Forum.ForumType forumType, String url) {

        defaultXpathMaps = new HashMap<>();

        switch (forumType) {
            case XENFORO: {

                final List<String> twoFactorloginForm = new ArrayList<>();
                twoFactorloginForm.add("//*[@id='content']/div/div/div[2]/form"); // Spigot
                twoFactorloginForm.add("//*[@id='content']/div/div/div[1]/form"); // Hypixel

                final List<String> messages = new ArrayList<>();
                messages.add("//*[@id='uix_ConversationsMenu_Counter']/span"); // Spigot
                messages.add("//*[@id='ConversationsMenu_Counter']/span[1]"); // Hypixel

                final List<String> alerts = new ArrayList<>();
                alerts.add("//*[@id='uix_AlertsMenu_Counter']/span"); // Spigot
                alerts.add("//*[@id='AlertsMenu_Counter']/span[1]"); // Hypixel

                final List<String> ratings = new ArrayList<>();
                ratings.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[6]/dd"); // Spigot
                ratings.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[2]/div/dl[6]/dd"); // Hypixel

                final List<String> posts = new ArrayList<>();
                posts.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[3]/dd"); // Spigot
                posts.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[2]/div/dl[3]/dd"); // Hypixel

                final List<String> accountUrl = new ArrayList<>();
                accountUrl.add("//*[@id='AccountMenu']/div[1]/ul/li/a"); // Spigot, Hypixel

                final List<String> accountPic = new ArrayList<>();
                accountPic.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[1]/a/img"); // Spigot
                accountPic.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[1]/a/img"); // Hypixel

                final List<String> accountName = new ArrayList<>();
                accountName.add("//*[@id='userBar']/div/div/div/div/ul[2]/li[1]/a/strong[1]"); // Spigot
                accountName.add("//*[@id='userBar']/div/div/div/div/ul/li[1]/a/strong[1]"); // Spigot
                accountName.add("//*[@id='userBar']/div/div/div/ul/li[1]/a/strong[1]"); // Hypixel

                defaultXpathMaps.put("twoFactorLoginFormPaths", twoFactorloginForm);
                defaultXpathMaps.put("pageUrl", url);
                defaultXpathMaps.put("messagePaths", messages);
                defaultXpathMaps.put("alertPaths", alerts);
                defaultXpathMaps.put("ratingPaths", ratings);
                defaultXpathMaps.put("postPaths", posts);
                defaultXpathMaps.put("accountUrlPaths", accountUrl);
                defaultXpathMaps.put("accountPicPaths", accountPic);
                defaultXpathMaps.put("accountNamePaths", accountName);


                break;
            }
        }
    }
    public Map<String, Object> get() {
        return defaultXpathMaps;
    }
}
