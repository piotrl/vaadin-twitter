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
    private Label SizeLabel = new Label();

    @Autowired
    public SummaryPreview(PartyRepository repository) {
        this.repository = repository;
        addComponents(AngerLabel,
                DisgustLabel,
                HappinessLabel,
                FearLabel,
                SadnessLabel,
                SizeLabel
        );
        setVisible(false);
    }

    public final void preview(Long partyId) {
        Party party = repository.findOne(partyId);
        double size = party.getTweetsSize();
        AngerLabel.setValue("Anger: " + Math.floor((party.getANGER() / size) * 100) + "%");
        DisgustLabel.setValue("Disgust: " + Math.floor((party.getDISGUST() / size) * 100) + "%");
        HappinessLabel.setValue("Happiness: " + Math.floor((party.getHAPPINESS() / size) * 100) + "%");
        FearLabel.setValue("Fear: " + Math.floor((party.getFEAR() / size) * 100) + "%");
        SadnessLabel.setValue("Sadness: " + Math.floor((party.getSADNESS() / size) * 100) + "%");
        SizeLabel.setValue("Tweets: " + party.getTweetsSize());

        setVisible(true);
    }
}
