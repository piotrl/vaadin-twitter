package net.piotrl.imports;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import net.piotrl.dao.Party;
import net.piotrl.dao.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class SummaryPreview extends HorizontalLayout {
    private final PartyRepository repository;

    @Autowired
    public SummaryPreview(PartyRepository repository) {
        this.repository = repository;
        setVisible(false);
    }

    public final void preview(Long partyId) {
        Party party = repository.findOne(partyId);
        addComponents(
                new Label("Anger: " + party.getANGER()),
                new Label("Disgust: " + party.getDISGUST()),
                new Label("Happiness: " + party.getHAPPINESS()),
                new Label("Fear: " + party.getFEAR()),
                new Label("Sadness: " + party.getSADNESS())
        );
        setVisible(true);
    }
}
