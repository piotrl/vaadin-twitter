package net.piotrl;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import net.piotrl.analyser.summary.TweetSummaryService;
import net.piotrl.dao.Party;
import net.piotrl.dao.PartyRepository;
import net.piotrl.dao.TweetsRepository;
import net.piotrl.imports.SummaryPreview;
import net.piotrl.imports.TweetsImporter;
import net.piotrl.imports.TweetsPreview;
import net.piotrl.imports.UploadInfoWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@SpringUI
@Theme("valo")
public class VaadinInitializer extends UI {

    private final TweetsRepository tweetsRepository;
    private final PartyRepository partyRepository;
    private final TweetSummaryService tweetSummaryService;
    private final TweetsPreview tweetsPreview;
    private final SummaryPreview summaryPreview;

    private final Grid grid = new Grid();
    private final TextField filter = new TextField();
    private final Upload uploadButton;
    public UploadInfoWindow uploadInfoWindow;

    @Autowired
    public VaadinInitializer(PartyRepository partyRepository,
                             TweetsRepository tweetsRepository,
                             TweetSummaryService tweetSummaryService,
                             TweetsPreview tweetsPreview,
                             SummaryPreview summaryPreview) {
        this.tweetsRepository = tweetsRepository;
        this.partyRepository = partyRepository;
        this.tweetSummaryService = tweetSummaryService;
        this.tweetsPreview = tweetsPreview;
        this.summaryPreview = summaryPreview;
        this.uploadButton = uploadButton();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout layout = buildLayout();
        setContent(layout);

        filter.setInputPrompt("Filter by party name");
        filter.addTextChangeListener(e -> refreshPartiesGrid(e.getText()));

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("name");
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                tweetsPreview.setVisible(false);
                summaryPreview.setVisible(false);
            } else {
                Party selectedParty = (Party) grid.getSelectedRow();
                tweetsPreview.preview(selectedParty.getName());
                summaryPreview.preview(selectedParty.getId());
            }
        });

        // Initialize listing
        refreshPartiesGrid(null);
    }

    private HorizontalLayout buildLayout() {
        VerticalLayout columnRight = new VerticalLayout(summaryPreview, tweetsPreview);
        HorizontalLayout actions = new HorizontalLayout(filter, uploadButton, columnRight);
        VerticalLayout columnLeft = new VerticalLayout(actions, grid);

        actions.setSpacing(true);
        columnLeft.setMargin(true);
        columnLeft.setSpacing(true);
        columnRight.setMargin(true);
        columnRight.setSpacing(true);

        return new HorizontalLayout(columnLeft, columnRight);
    }

    public void refreshPartiesGrid(String partyName) {
        List<Party> parties;
        if (StringUtils.isEmpty(partyName)) {
            parties = partyRepository.findAll();
        } else {
            parties = partyRepository.findByNameStartsWithIgnoreCase(partyName);
        }

        if (partyName == null && parties.isEmpty()) {
            return;
        }

        grid.setContainerDataSource(
                new BeanItemContainer(Party.class, parties));
    }

    private Upload uploadButton() {
        TweetsImporter fileUploader = new TweetsImporter(partyRepository, tweetsRepository, tweetSummaryService, this);
        Upload upload = new Upload();
        upload.setImmediate(true);
        upload.setButtonCaption("Upload File");
        upload.setErrorHandler(errorEvent -> {});
        upload.addStartedListener((Upload.StartedListener) event -> {
            uploadInfoWindow.uploadStarted(event);
            if (uploadInfoWindow.getParent() == null) {
                UI.getCurrent().addWindow(uploadInfoWindow);
            }
            uploadInfoWindow.setClosable(false);
        });
        upload.addSucceededListener(event -> {
            fileUploader.uploadSucceeded(event);
            refreshPartiesGrid(null);
        });

        upload.setReceiver(fileUploader);
        uploadInfoWindow = new UploadInfoWindow(upload);
        return upload;
    }
}
