package os.pagereplacement.gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class SwingUtil {
	
	private SwingUtil() {
	}
	
	@SuppressWarnings("serial")
	public static void setupTable(JTable jTable, TableCellRenderer tableCellRenderer, String[][] content) {
		
		jTable.setModel(new DefaultTableModel(content, content[0]) {
			@Override
			public boolean isCellEditable(int i, int j) {
				return false;
			}
		});
		
		for (int j = 0; j < jTable.getColumnModel().getColumnCount(); j++) {
			jTable.getColumnModel().getColumn(j).setCellRenderer(tableCellRenderer);
//			jTable.getColumnModel().getColumn(j).sizeWidthToFit();
		}
		jTable.doLayout();
	}
}
