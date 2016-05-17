package net.yazsoft.frame.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.yazsoft.frame.controller.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple example of how to access the Google Analytics API using a service
 * account.
 */
@Named
@ViewScoped
public class GoogleAnalytics {


    private static final Logger log = Logger.getLogger(GoogleAnalytics.class);
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String APPLICATION_NAME = "Yazframe Analytics";
    private static final File KEY_FILE = new File(GoogleAnalytics.class.getResource("/yazframe.p12").getFile());
    private static final String SERVICE_ACCOUNT_EMAIL = "yazframeser@yazframe.iam.gserviceaccount.com";
    private static final String PROFILE_ID= "121840535";
    private static Analytics analytics;

    private static String monthSessions;
    private static String monthUsers;
    private static String monthPageviews;

    private static List<List<String>> graphicSessions;
    private static List<List<String>> graphicCities;
    private static List<String> cityNames;


    private static void initializeAnalytics() throws Exception {
        // Initializes an authorized analytics service object.

        // Construct a GoogleCredential object with the service account email
        // and p12 file downloaded from the developer console.
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        File testFile;
        log.info("KEY_FILE : " + KEY_FILE.toString());
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(KEY_FILE)
                .setServiceAccountScopes(AnalyticsScopes.all())
                .build();

        // Construct the Analytics service object.
        analytics = new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }


    private static String getFirstProfileId(Analytics analytics) throws IOException {
        // Get the first view (profile) ID for the authorized user.
        String profileId = null;

        // Query for the list of all accounts associated with the service account.
        Accounts accounts = analytics.management().accounts().list().execute();

        if (accounts.getItems().isEmpty()) {
            System.err.println("No accounts found");
        } else {
            String firstAccountId = accounts.getItems().get(0).getId();

            // Query for the list of properties associated with the first account.
            Webproperties properties = analytics.management().webproperties()
                    .list(firstAccountId).execute();

            if (properties.getItems().isEmpty()) {
                System.err.println("No Webproperties found");
            } else {
                String firstWebpropertyId = properties.getItems().get(0).getId();

                // Query for the list views (profiles) associated with the property.
                Profiles profiles = analytics.management().profiles()
                        .list(firstAccountId, firstWebpropertyId).execute();

                if (profiles.getItems().isEmpty()) {
                    System.err.println("No views (profiles) found");
                } else {
                    // Return the first (view) profile associated with the property.
                    profileId = profiles.getItems().get(0).getId();
                }
            }
        }
        return profileId;
    }

    public static String findSessions() {
        try {
            if (analytics == null) initializeAnalytics();
            GaData dataSessions = analytics.data().ga()
                    .get("ga:" + PROFILE_ID, "30daysAgo", "yesterday", "ga:sessions")
                    .execute();
            if (dataSessions != null && !dataSessions.getRows().isEmpty()) {
                monthSessions = dataSessions.getRows().get(0).get(0);
                System.out.println("Month Sessions: " + monthSessions);
            } else {
                monthSessions = " ";
                System.out.println("No results found");
            }
            return monthSessions;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public static List<List<String>> findGraphicSessions() {
        try {
            if (analytics == null) initializeAnalytics();
            GaData dataSessions = analytics.data().ga()
                    .get("ga:" + PROFILE_ID, "30daysAgo", "yesterday", "ga:sessions").setDimensions("ga:day")
                    .execute();
            if (dataSessions != null && !dataSessions.getRows().isEmpty()) {
                System.out.println("Month Graphic: " + dataSessions.getRows());
                graphicSessions=dataSessions.getRows();
                return graphicSessions;
            } else {
                return null;
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public static List<List<String>> findGraphicCities() {
        cityNames=new ArrayList<>();
        try {
            if (analytics == null) initializeAnalytics();
            GaData dataSessions = analytics.data().ga()
                    .get("ga:" + PROFILE_ID, "30daysAgo", "yesterday", "ga:sessions")
                    .setDimensions("ga:city").setMaxResults(10).setSort("-ga:sessions")
                    .execute();
            if (dataSessions != null && !dataSessions.getRows().isEmpty()) {
                System.out.println("City Graphic: " + dataSessions.getRows());
                graphicCities=dataSessions.getRows();
                for (int i=0; i<graphicCities.size(); i++) {
                    List listi=graphicCities.get(i);
                    if (listi.get(0).equals("(not set)")) {
                        listi.set(0,"'Belirtilmemiş'" );
                    } else {
                        listi.set(0, "'" + graphicCities.get(i).get(0) + "'");
                    }
                    cityNames.add((String)listi.get(0));
                    graphicCities.set(i, listi);
                    System.out.println(graphicCities.get(i).get(0));

                }
                return graphicCities;
            } else {
                return null;
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public static String findUsers() {
        try {
            if (analytics == null) initializeAnalytics();
            GaData dataUsers=analytics.data().ga()
                    .get("ga:" + PROFILE_ID, "30daysAgo", "yesterday", "ga:users")
                    .execute();
            if (dataUsers != null && !dataUsers.getRows().isEmpty()) {
                monthUsers=dataUsers.getRows().get(0).get(0);
                System.out.println("Month Users: " +monthUsers );
            } else {
                monthUsers=" ";
                System.out.println("No results found");
            }
            return monthUsers;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public static String findPageviews() {
        try {
            if (analytics == null) initializeAnalytics();
            GaData dataPageviews=analytics.data().ga()
                    .get("ga:" + PROFILE_ID, "30daysAgo", "yesterday", "ga:pageviews")
                    .execute();
            if (dataPageviews != null && !dataPageviews.getRows().isEmpty()) {
                monthPageviews=dataPageviews.getRows().get(0).get(0);
                System.out.println("Month Pageviews: " +monthPageviews );
            } else {
                monthPageviews="";
                System.out.println("No results found");
            }
            return monthPageviews;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public static String getMonthSessions() {
        return monthSessions;
    }

    public static void setMonthSessions(String monthSessions) {
        GoogleAnalytics.monthSessions = monthSessions;
    }

    public static String getMonthUsers() {
        return monthUsers;
    }

    public static void setMonthUsers(String monthUsers) {
        GoogleAnalytics.monthUsers = monthUsers;
    }

    public static String getMonthPageviews() {
        return monthPageviews;
    }

    public static void setMonthPageviews(String monthPageviews) {
        GoogleAnalytics.monthPageviews = monthPageviews;
    }

    public static List<List<String>> getGraphicSessions() {
        return graphicSessions;
    }

    public static void setGraphicSessions(List<List<String>> graphicSessions) {
        GoogleAnalytics.graphicSessions = graphicSessions;
    }

    public static List<List<String>> getGraphicCities() {
        return graphicCities;
    }

    public static void setGraphicCities(List<List<String>> graphicCities) {
        GoogleAnalytics.graphicCities = graphicCities;
    }

    public static List<String> getCityNames() {
        return cityNames;
    }

    public static void setCityNames(List<String> cityNames) {
        GoogleAnalytics.cityNames = cityNames;
    }
}
