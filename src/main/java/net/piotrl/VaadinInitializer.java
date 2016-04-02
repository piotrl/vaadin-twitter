package net.piotrl;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import net.piotrl.imports.Customer;
import net.piotrl.imports.CustomerRepository;
import net.piotrl.imports.UploadInfoWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

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
        TextField filter = new TextField();
        filter.setInputPrompt("Filter by last name");
        filter.addTextChangeListener(e -> listCustomers(e.getText()));
        VerticalLayout mainLayout = new VerticalLayout(filter, grid);
        setContent(mainLayout);
    }

    private void listCustomers(String text) {
        List<Customer> customers;
        if (StringUtils.isEmpty(text)) {
            customers = repo.findAll();
        } else {
            customers = repo.findByLastNameStartsWithIgnoreCase(text);
        }

        grid.setContainerDataSource(
                new BeanItemContainer(Customer.class, customers));
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
