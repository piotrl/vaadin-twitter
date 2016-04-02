package net.piotrl.imports;

import com.vaadin.ui.*;

public class UploadInfoWindow extends Window implements
        Upload.StartedListener, Upload.ProgressListener, Upload.SucceededListener,
        Upload.FinishedListener {
    private final Label state = new Label();
    private final Label fileName = new Label();
    private final Label textualProgress = new Label();

    private final ProgressBar progressBar = new ProgressBar();
    private final Button cancelButton;

    public UploadInfoWindow(final Upload upload) {
        super("Status");

        setWidth(350, Unit.PIXELS);

        addStyleName("upload-info");

        setResizable(false);
        setDraggable(false);

        final FormLayout l = new FormLayout();
        setContent(l);
        l.setMargin(true);

        final HorizontalLayout stateLayout = new HorizontalLayout();
        stateLayout.setSpacing(true);
        stateLayout.addComponent(state);

        cancelButton = new Button("Cancel");
        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelButton.setVisible(false);
        cancelButton.setStyleName("small");
        stateLayout.addComponent(cancelButton);

        stateLayout.setCaption("Current state");
        state.setValue("Idle");
        l.addComponent(stateLayout);

        fileName.setCaption("File name");
        l.addComponent(fileName);

        progressBar.setCaption("Progress");
        progressBar.setVisible(false);
        l.addComponent(progressBar);

        textualProgress.setVisible(false);
        l.addComponent(textualProgress);

        upload.addStartedListener(this);
        upload.addProgressListener(this);
        upload.addSucceededListener(this);
        upload.addFinishedListener(this);

    }

    @Override
    public void uploadFinished(final Upload.FinishedEvent event) {
        state.setValue("Idle");
        progressBar.setVisible(false);
        textualProgress.setVisible(false);
        cancelButton.setVisible(false);
    }

    @Override
    public void uploadStarted(final Upload.StartedEvent event) {
        // this method gets called immediately after upload is started
        progressBar.setValue(0f);
        progressBar.setVisible(true);
        UI.getCurrent().setPollInterval(500);
        textualProgress.setVisible(true);
        // updates to client
        state.setValue("Uploading");
        fileName.setValue(event.getFilename());

        cancelButton.setVisible(true);
    }

    @Override
    public void updateProgress(final long readBytes,
                               final long contentLength) {
        // this method gets called several times during the update
        progressBar.setValue(new Float(readBytes / (float) contentLength));
        textualProgress.setValue("Processed " + readBytes + " bytes of "
                + contentLength);
    }

    @Override
    public void uploadSucceeded(final Upload.SucceededEvent event) {

    }
}