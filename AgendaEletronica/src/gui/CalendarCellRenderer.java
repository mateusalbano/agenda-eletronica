package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import backend.CalendarDate;

public class CalendarCellRenderer extends JLabel implements TableCellRenderer {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
		        table, value, isSelected, hasFocus, row, column);
		    ((JLabel) renderer).setOpaque(true);
		    Color foreground, background;
		    Color light_blue = new Color(200, 225, 245);
		    Color light_yellow = new Color(250, 250, 175);
		    Color black = Color.black;
		    CalendarDate calendarDate = (CalendarDate) value;
		    if (calendarDate != null) {
		    	if(calendarDate.isImportant()) {
		    		foreground = black;
		    		background = light_yellow;
		    		
		    	} else {
		    		foreground = black;
		    		background = Color.white;
		    	}
		    } else {
	    		foreground = black;
	    		background = Color.white;
		    }
		    
		    if (isSelected) {
	    		foreground = black;
	    		background = light_blue;
	    	}
		    
		    renderer.setForeground(foreground);
		    renderer.setBackground(background);
		    return renderer;
	}

}
