package org.example;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.HorizontalLayout;
import org.example.backend.Product;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.example.backend.service.ProductFacade;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 *
 * @author Mortoza Khan
 */
@CDIView
public class ProductView extends MVerticalLayout implements View {

    @Inject
    ProductFacade facade1;
    @Inject
    ProductForm form;

    Button newButton;
    Button delButton;
   
    MTable<Product> table = new MTable<>(Product.class);    //.withProperties("id","name");

    //Panel for Form
    Panel panel = new Panel("Product");
  
    HorizontalLayout buttons1;
    MHorizontalLayout horizontal;
    
    @PostConstruct
    public void initComponent() {
        //buttons
        buttons1 = new HorizontalLayout();
        horizontal=new MHorizontalLayout();
        
        addTableButtons();
        buttons1.addComponent(newButton);
        buttons1.addComponent(delButton);
        
        form.setResetHandler(this::reset);
        form.setSavedHandler(this::save);

        table.setWidth("400px");
        //table.setHeight("400px");
        table.setColumnCollapsingAllowed(true);
        table.addMValueChangeListener(e -> {
            form.setEntity(e.getValue());
        });

        //panel.addStyleName("mypanelexample");
        panel.setSizeUndefined();

        // Create the content
        //form.addStyleName("mypanelcontent");
        form.setSizeUndefined();
        panel.setContent(form);

        listEntities();
        
        horizontal.addComponent(table);
        horizontal.addComponent(panel);
        addComponents(new Header("Product listing"),
                buttons1,                
                horizontal
        );
    }

    private void listEntities() {
        table.setBeans(facade1.findAll());
    }

 private void addTableButtons(){
        newButton = new Button("New", event -> {
            try {
                form.setEntity(new Product());
            } catch (Exception e) {
                return;
            }
        });  
        
        delButton = new Button("Delete", event -> {
            try {
                facade1.remove(table.getValue());
                table.setBeans(facade1.findAll());
                Notification.show("Deleted...");
            } catch (Exception e) {
                return;
            }
        });  
    }

   
    public void save(Product entity) {
        if (form.getEntity().getId() == null) {
            facade1.create(entity);
        } else {
            facade1.edit(entity);
        }
        form.setEntity(null);        
        listEntities();
        Notification.show("Saved!");
    }

    public void reset(Product entity) {
        // just hide the form
        form.setEntity(null);
        listEntities();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}