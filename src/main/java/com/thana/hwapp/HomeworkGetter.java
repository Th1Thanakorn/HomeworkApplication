package com.thana.hwapp;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomeworkGetter {

    public static final HashMap<String, List<String>> DATE_AND_HOMEWORKS = new HashMap<>();
    private static final String APPLICATION_NAME = "HomeworkAPI";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    // I know this kind of ridiculous, but in fact of storing to these variables might increase the performance.
    private static Credential credential;
    private static JsonObject apiJson;

    public static boolean finished = false;

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        InputStream in = HomeworkGetter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void tryGet() throws Exception {
        final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        if (credential == null) credential = getCredentials(transport);
        Drive service = new Drive.Builder(transport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
        List<File> files = result.getFiles();
        if (files != null) {
            for (File file : files) {
                String fileId = file.getId();
                URL getterURL = new URL("https://drive.google.com/uc?export=download&id=" + fileId);
                InputStream stream = getterURL.openConnection().getInputStream();
                String json = StreamUtil.read(stream);
                apiJson = JsonParser.parseString(json).getAsJsonObject();
            }
        }
    }

    public static void getHomeworkList() {
        JsonObject homeworks = apiJson.getAsJsonObject("homeworks");
        for (String date : homeworks.keySet()) {
            List<String> list = new ArrayList<>();
            homeworks.getAsJsonArray(date).forEach((o) -> list.add(o.getAsString()));
            DATE_AND_HOMEWORKS.put(date, list);
        }
        finished = true;
    }
}
