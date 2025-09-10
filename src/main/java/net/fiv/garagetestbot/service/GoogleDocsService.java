package net.fiv.garagetestbot.service;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import net.fiv.garagetestbot.dto.FeedbackJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class GoogleDocsService {

    private final Docs docsService;

    @Value("${google.api.document_id}")
    private String documentId;

    public GoogleDocsService(@Value("${google.api.credentials_file_path}") final String credentialsFilePath) throws Exception {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsFilePath))
                .createScoped(Collections.singleton(DocsScopes.DOCUMENTS));

        this.docsService = new Docs.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("FeedbackBot").build();
    }

    public void appendFeedback(String feedback, FeedbackJson feedback_analysis) throws IOException {
        List<Request> requests = new ArrayList<>();
        Document document = this.docsService.documents().get(documentId).execute();
        int endIndex = document.getBody().getContent()
                .getLast()
                .getEndIndex();

        String text = String.format(
                """
                        📌 Відгук: %s
                        Сентимент: %s
                        Критичність: %s
                        Рішення: %s
                        Telegram ID автора: %s
                        """,
                feedback,
                feedback_analysis.getSentiment(),
                feedback_analysis.getCriticality(),
                feedback_analysis.getSolution(),
                feedback_analysis.getUser().getTelegramUserId()
        );

        requests.add(new Request().setInsertText(new InsertTextRequest()
                        .setLocation(new Location().setIndex(endIndex-1))
                .setText(text)
        ));

        docsService.documents().batchUpdate(documentId, new BatchUpdateDocumentRequest()
                .setRequests(requests)).execute();
    }

}
