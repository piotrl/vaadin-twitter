package net.piotrl;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import net.piotrl.imports.Customer;
import net.piotrl.imports.CustomerRepository;
import net.piotrl.imports.UploadInfoWindow;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("valo")
public class VaadinInitializer extends UI {

    UploadInfoWindow uploadInfoWindow;
    Grid grid;
    CustomerRepository repo;

    @Autowired
    public VaadinInitializer(CustomerRepository repo) {
        this.repo = repo;
        this.grid = new Grid();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(grid);
        listCustomers();
    }

    private void listCustomers() {
        grid.setContainerDataSource(
                new BeanItemContainer(Customer.class, repo.findAll()));
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
