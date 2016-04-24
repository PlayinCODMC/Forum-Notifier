package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.model.ForumAccount;
import com.cldfire.xenforonotifier.util.notifications.EnumImageType;
import com.cldfire.xenforonotifier.util.notifications.Notification;
import com.cldfire.xenforonotifier.util.Settings;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StatViewController {
    @FXML
    private Label newMessagesField;
    @FXML
    private Label newAlertsField;
    @FXML
    private Label ratingField;
    @FXML
    private Label postCountField;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @FXML
    private void initialize() {
        checkEverythingAtFixedRate();
    }

    private String getXenToken(String url, Set<Cookie> cookies) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlInput token;

        cookies.forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage(url);
            token = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[2]/form/div/input[2]");

            webClient.close();
            return token.getValueAttribute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        return null;
    }

    private Map<String, String> getEverything(String url, Set<Cookie> cookies) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlSpan messages;
        final HtmlSpan alerts;
        final HtmlSpan ratings;
        final HtmlDefinitionDescription posts;

        // feed the WebClient so that it does what we want it to
        cookies.forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage(url);
            messages = page.getFirstByXPath("//*[@id='uix_ConversationsMenu_Counter']/span");
            alerts = page.getFirstByXPath("//*[@id='uix_AlertsMenu_Counter']/span");
            ratings = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[1]/div/div/div/dl/dd/span");
            posts = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[1]/div/div/div/div/dl/dd");

            Map<String, String> values = new HashMap<>();
            values.put("messages", messages.asText());
            values.put("alerts", alerts.asText());
            values.put("ratings", ratings.asText());
            values.put("posts", posts.asText());

            webClient.close();
            return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        return null;
    }

    private void checkEverythingAtFixedRate() { // TODO: I'm very aware this is not going to thread properly atm, will fix in the future
        Runnable getEverythingRunnable = () -> {
            LoginViewController.websiteList.forEach(u -> {
                System.out.println("Website list had something");
                ArrayList<ForumAccount> siteAccounts = new ArrayList<>(LoginViewController.accounts.get(u));

                siteAccounts.forEach(a -> {
                    System.out.println("There were accounts for that site");
                    System.out.println(a.getName());
                    // TODO: Verify that returnedValues isn't null
                    final Map<String, String> returnedValues = new HashMap<>(getEverything("https://" + a.getForumUrl(), a.getCookies()));
                    final Integer newMessagesCount = Integer.parseInt(returnedValues.get("messages"));
                    final Integer newAlertsCount = Integer.parseInt(returnedValues.get("alerts"));

                    if (newMessagesCount > a.getMessageCount()) { // TODO: Get notifications to work when both a message and alert notification needs to be created
                        if (newMessagesCount - a.getMessageCount() == 1) {
                            new Notification("XenForo Notifier", "You have a new message", EnumImageType.ALERT).send();
                        } else {
                            new Notification("XenForo Notifier", "You have " + (newMessagesCount - a.getMessageCount()) + " new messages", EnumImageType.ALERT).send();
                        }
                    }

                    if (newAlertsCount > a.getAlertCount()) {
                        if (newAlertsCount - a.getAlertCount() == 1) {
                            new Notification("XenForo Notifier", "You have a new alert", EnumImageType.ALERT).send();
                        } else {
                            new Notification("XenForo Notifier", "You have " + (newAlertsCount - a.getAlertCount()) + " new alerts", EnumImageType.ALERT).send();
                        }
                    }

                    a.setMessageCount(newMessagesCount);
                    a.setAlertCount(newMessagesCount);

                    Platform.runLater(() -> { // TODO: Get UI to support multiple accounts
                        newMessagesField.setText(returnedValues.get("messages"));
                        newAlertsField.setText(returnedValues.get("alerts"));
                        ratingField.setText(returnedValues.get("ratings"));
                        postCountField.setText(returnedValues.get("posts"));
                    });
                });
            });
            System.out.println("Ran checker");
        };
        // TODO: Provide way to cancel this other than task manager / force quit / whatever *nix does
        scheduler.scheduleAtFixedRate(getEverythingRunnable, 0, 15, SECONDS);
    }
}
