package com.example.test.spring;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(Lumo.class)
@HtmlImport("styles/shared-styles.html")
@Route("")
public class MainView extends VerticalLayout {
	
    private final PersonService service;

	public MainView(PersonService service) {
		this.service = service;
		buildMainLayout();
	}
	
	private void buildMainLayout()
	{
		TextField textField = new TextField("Enter user name");
        Button loginButton = new Button("Log in", event -> login(textField.getValue()));
        Button addUserButton = new Button("Add new user", event -> addUser(textField.getValue()));
        add(textField, loginButton, addUserButton);
	}
	
	private void login(String userName) {
		removeAll();
//		String currentUser = "currrentUser";		
//		String currentTenant = "currentTenant"; // get tenant for current user
		TenantContext.setCurrentTenant(userName);
		
        Grid<Person> grid = new Grid<Person>();
        grid.setColumnReorderingAllowed(true);
        grid.addColumn(Person::getName).setHeader("Name").setResizable(true).setComparator(
        		(item1, item2) -> item1.getName().compareToIgnoreCase(item2.getName()));
        grid.addColumn(Person::getAge).setHeader("Age").setResizable(false).setComparator(
        		(item1, item2) -> item1.getAge().compareTo(item2.getAge()));
        
        DataProvider<Person, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                	Map<String, Boolean> sortOrders = new LinkedHashMap<>();
                    for (QuerySortOrder order : query.getSortOrders()) {
                    	sortOrders.put(order.getSorted(), order.getDirection().equals(SortDirection.ASCENDING));
                    }
                    return service.findAll(query.getOffset(), query.getLimit(), sortOrders).stream();
                },
        		query -> service.count());
        grid.setDataProvider(dataProvider);        
//        Label label = new Label("Not clicked");
//        Button button = new Button("Click me", event -> label.setText("Clicked!"));
        
        add(grid);
	}
	
	@Autowired
	HibernateUtils hibernateUtils;
	
	private void addUser(String userName) {
		try {
			hibernateUtils.createNewSchema(userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}