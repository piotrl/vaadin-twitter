package net.piotrl.imports;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.piotrl.dao.Party;
import net.piotrl.dao.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class SummaryPreview extends VerticalLayout {
    private final PartyRepository repository;
    private Label AngerLabel = new Label();
    private Label DisgustLabel = new Label();
    private Label HappinessLabel = new Label();
    private Label FearLabel = new Label();
    private Label SadnessLabel = new Label();

    @Autowired
    public SummaryPreview(PartyRepository repository) {
        this.repository = repository;
        addComponents(AngerLabel,
                DisgustLabel,
                HappinessLabel,
                FearLabel,
                SadnessLabel);
        setVisible(false);
    }

    public final void preview(Long partyId) {
        Party party = repository.findOne(partyId);
        AngerLabel.setValue("Anger: " + party.getANGER());
        DisgustLabel.setValue("Disgust: " + party.getDISGUST());
        HappinessLabel.setValue("Happiness: " + party.getHAPPINESS());
        FearLabel.setValue("Fear: " + party.getFEAR());
        SadnessLabel.setValue("Sadness: " + party.getSADNESS());

        setVisible(true);
    }
}
