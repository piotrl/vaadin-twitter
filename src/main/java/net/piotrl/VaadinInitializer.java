package net.piotrl;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SpringUI
@Theme("valo")
public class VaadinInitializer extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Button("Say hello", e -> Notification.show("Hello World spring and vaadin")));
    }

}
