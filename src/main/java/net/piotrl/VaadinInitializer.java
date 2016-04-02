package net.piotrl;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import net.piotrl.imports.UploadInfoWindow;

@SpringUI
@Theme("valo")
public class VaadinInitializer extends UI {

    UploadInfoWindow uploadInfoWindow;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Upload upload = getUploadButton();
        setContent(upload);
    }

    private Upload getUploadButton() {
        Upload upload = new Upload();
        upload.setImmediate(false);
        upload.setButtonCaption("Upload File");
        upload.addStartedListener((Upload.StartedListener) event -> {
            if (uploadInfoWindow.getParent() == null) {
                UI.getCurrent().addWindow(uploadInfoWindow);
            }
            uploadInfoWindow.setClosable(false);
        });
        upload.addFinishedListener((Upload.FinishedListener) event ->
                uploadInfoWindow.setClosable(true)
        );

        uploadInfoWindow = new UploadInfoWindow(upload);
        return upload;
    }
}
