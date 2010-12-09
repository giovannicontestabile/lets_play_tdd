package com.jamesshore.finances.ui;

import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.junit.*;
import com.jamesshore.finances.domain.*;

public class _ApplicationFrameTest {

	private ApplicationFrame frame;
	
	@Before
	public void setup() {
		frame = new ApplicationFrame(new ApplicationModel());
	}
	
	@Test
	public void shouldExitApplicationWhenWindowClosed() {
		assertEquals("should exit on close", WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
	}
	
	@Test
	public void shouldHaveTitle() {
		assertEquals("title", ApplicationFrame.TITLE, frame.getTitle());
	}
	
	@Test
	public void shouldHaveHardcodedPositionAndSize() {
		assertEquals("position", ApplicationFrame.INITIAL_POSITION, frame.getLocation());
		assertEquals("size", ApplicationFrame.INITIAL_SIZE, frame.getSize());
	}
	
	@Test
	public void shouldLayoutProperly() {
		assertEquals("layout", BorderLayout.class, frame.getContentPane().getLayout().getClass());
		
		Component[] components = frame.getContentPane().getComponents();
		
		assertEquals("# of components", 2, components.length);
		assertEquals("component #0", JScrollPane.class, components[0].getClass());
		assertEquals("component #0 should contain table", ForecastTable.class, ((JScrollPane)components[0]).getViewport().getView().getClass());
		assertEquals("component #1", JTextField.class, components[1].getClass());
	}
	
	@Test
	public void forecastTableShouldContainCorrectModel() {
		JScrollPane scrollPane = (JScrollPane)frame.getContentPane().getComponent(0);
		TableModel model = ((ForecastTable)scrollPane.getViewport().getView()).getModel();
		assertEquals("forecast table model class", StockMarketTableModel.class, model.getClass());
		assertEquals("# of rows in model", 41, model.getRowCount());
	}
	
	@Test
	public void startingBalanceFieldShouldUpdateApplicationModel() {
		class MockApplicationModel extends ApplicationModel {
			public Dollars setStartingBalanceCalledWith;

			@Override
			public void setStartingBalance(Dollars startingBalance) {
				setStartingBalanceCalledWith = startingBalance;
			}
		}
		MockApplicationModel mockModel = new MockApplicationModel();
		frame = new ApplicationFrame(mockModel);
		
		JTextField field = frame.startingBalanceField();
		field.setText("668");
		ActionListener[] listeners = field.getActionListeners();
		listeners[0].actionPerformed(new ActionEvent(field, 0, ""));
		
		assertEquals("text field should have action listener", 1, listeners.length);
		assertEquals("applicationModel should be updated", new Dollars(668), mockModel.setStartingBalanceCalledWith);  

		
		field.setText("670");
		FocusListener[] focusListeners = field.getFocusListeners();
		focusListeners[0].focusLost(new FocusEvent(field, 0));
		
		assertEquals("text field should have focus listener", 1, focusListeners.length);
//		assertEquals("applicationModel should be updated", new Dollars(670), mockModel.setStartingBalanceCalledWith);  
	}
	
}
