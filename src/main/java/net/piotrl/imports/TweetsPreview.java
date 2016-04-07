package net.piotrl.imports;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import net.piotrl.dao.Tweet;
import net.piotrl.dao.TweetsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class TweetsPreview extends VerticalLayout {

    private final TweetsRepository repository;
    private final Grid grid;

    @Autowired
    public TweetsPreview(TweetsRepository repository) {
        this.repository = repository;
        this.grid = new Grid();
        addComponents(grid);

        setVisible(false);
    }

    public final void preview(String partyName) {
        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("partyName", "day", "tweet");

        List<Tweet> tweets = repository.findByPartyNameStartsWithIgnoreCase(partyName);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
//        BeanFieldGroup.bindFieldsUnbuffered(customer, this);

        setVisible(true);

        // Select all text in firstName field automatically
        grid.setContainerDataSource(
                new BeanItemContainer(Tweet.class, tweets));
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
    }

    public interface ChangeHandler {

        void onChange();
    }
}