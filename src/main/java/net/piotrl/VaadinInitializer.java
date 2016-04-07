package net.piotrl;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import net.piotrl.analyser.scrapper.Tweet;
import net.piotrl.imports.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@SpringUI
@Theme("valo")
public class VaadinInitializer extends UI {

    private final TweetsRepository repo;
    private final CustomerEditor editor;

    private final Grid grid;
    private final TextField filter;
    private final Upload addNewBtn;
    private UploadInfoWindow uploadInfoWindow;

    @Autowired
    public VaadinInitializer(TweetsRepository tweetsRepository, CustomerEditor editor) {
        this.repo = tweetsRepository;
        this.editor = editor;
        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = uploadButton();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
        setContent(mainLayout);

        // Configure layouts and components
        actions.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("partyName", "day", "tweet");

        filter.setInputPrompt("Filter by party name");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.addTextChangeListener(e -> listTweets(e.getText()));

        // Connect selected Customer to editor or hide if none is selected
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            }
            else {
                editor.editCustomer((Customer) grid.getSelectedRow());
            }
        });


        // Instantiate and edit new Customer the new button is clicked
//        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listTweets(filter.getValue());
        });

        // Initialize listing
        listTweets(null);
    }

    private void listTweets(String partyName) {
        List<Tweet> tweets;
        if (StringUtils.isEmpty(partyName)) {
            tweets = repo.findAll();
        } else {
            tweets = repo.findByPartyNameStartsWithIgnoreCase(partyName);
        }

        grid.setContainerDataSource(
                new BeanItemContainer(Tweet.class, tweets));
    }

    private Upload uploadButton() {
        Upload upload = new Upload();
        upload.setImmediate(false);
        upload.setButtonCaption("Upload File");
        TweetsImporter fileUploader = new TweetsImporter(repo);
        upload.setReceiver(fileUploader);
        upload.addStartedListener((Upload.StartedListener) event -> {
            if (uploadInfoWindow.getParent() == null) {
                UI.getCurrent().addWindow(uploadInfoWindow);
            }
            uploadInfoWindow.setClosable(false);
        });
        upload.addSucceededListener(fileUploader::uploadSucceeded);
        upload.addFinishedListener((Upload.FinishedListener) event ->
                uploadInfoWindow.setClosable(true)
        );

        uploadInfoWindow = new UploadInfoWindow(upload);
        return upload;
    }
}
