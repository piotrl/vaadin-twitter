package net.piotrl.imports;

import com.vaadin.ui.*;

public class UploadInfoWindow extends Window implements
        Upload.StartedListener, Upload.ProgressListener, Upload.SucceededListener,
        Upload.FinishedListener {
    private final Label state = new Label();
    private final Label fileName = new Label();

    private final ProgressBar progressBar = new ProgressBar();

    public UploadInfoWindow(final Upload upload) {
        super("Status");

        setWidth(350, Unit.PIXELS);

        addStyleName("upload-info");

        setResizable(false);
        setDraggable(false);

        final FormLayout form = new FormLayout();
        setContent(form);
        form.setMargin(true);

        final HorizontalLayout stateLayout = new HorizontalLayout();
        stateLayout.setSpacing(true);
        stateLayout.addComponent(state);

        stateLayout.setCaption("Current state");
        state.setValue("Idle");
        form.addComponent(stateLayout);

        fileName.setCaption("File name");
        form.addComponent(fileName);

        progressBar.setCaption("Progress");
        progressBar.setVisible(false);
        form.addComponent(progressBar);

        upload.addStartedListener(this);
        upload.addProgressListener(this);
        upload.addSucceededListener(this);
    }

    @Override
    public void uploadFinished(final Upload.FinishedEvent event) {
        state.setValue("Finished");
        progressBar.setVisible(false);
    }

    @Override
    public void uploadStarted(final Upload.StartedEvent event) {
        // this method gets called immediately after upload is started
        progressBar.setValue(0f);
        progressBar.setVisible(true);
        UI.getCurrent().setPollInterval(500);
        // updates to client
        state.setValue("Uploading");
        fileName.setValue(event.getFilename());
    }

    @Override
    public void updateProgress(final long readBytes,
                               final long contentLength) {
        // this method gets called several times during the update
        progressBar.setValue(new Float(readBytes / (float) contentLength));
    }

    @Override
    public void uploadSucceeded(final Upload.SucceededEvent event) {

    }
}