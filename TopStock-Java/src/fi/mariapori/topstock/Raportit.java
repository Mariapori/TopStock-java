package fi.mariapori.topstock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class Raportit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6265165710333319738L;
	private String[] raportit = new String[] { "Varastosaldot" };
	private JTable table;
	private String path;
	/**
	 * Create the dialog.
	 */
	public Raportit(ConnectionSource connectionSource) {
		
		String home = System.getProperty("user.home");
		path = home + "/topstock/";
		
		setResizable(false);
		setTitle("Raportit");
		
		setBounds(100, 100, 450, 331);
		getContentPane().setLayout(null);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(12, 12, 125, 247);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setBounds(149, 14, 289, 245);
		getContentPane().add(table);
		
		JList<String> reportList = new JList<String>();
		reportList.setListData(raportit);
		reportList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String item = reportList.getSelectedValue();
				
				switch(item) {
				case "Varastosaldot":  
			        String[] columns = new String[] {
			            "Tuotekoodi", "Tuote", "Saldo", "Arvo"
			        };
			         
			        
			        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, columns);

					try {
						Dao<Tavara, String> productDao = DaoManager.createDao(connectionSource, Tavara.class);
						productDao.queryForAll().forEach((tuote -> {
					        model.addRow(new Object[] {tuote.getProductCode(),tuote.getProductName(), tuote.getSaldo(), tuote.getArvo()});
						}));
					} catch (SQLException e1) {

					}
					
			        
			        
			        table.setModel(model);
			        model.fireTableDataChanged();
					break;
				}
			}
		});
		scrollPane.setViewportView(reportList);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnNewButton = new JButton("Vie exceliin");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				   String item = reportList.getSelectedValue();
		           JFileChooser fchoose = new JFileChooser(path);
		           fchoose.setDialogTitle("Vie Exceliin");
		           FileFilter filter = new FileNameExtensionFilter("Excel | .xlsx", "xlsx");
		           fchoose.setFileFilter(filter);
		           fchoose.setAcceptAllFileFilterUsed(false);
		           int option = fchoose.showSaveDialog(Raportit.this);
		           if(option == JFileChooser.APPROVE_OPTION){
		             String name = fchoose.getSelectedFile().getName(); 
		             String path = fchoose.getSelectedFile().getParentFile().getPath();
		             String file = path + "//" + name + ".xlsx"; 
		             export(table, new File(file),item);
			}
		           }
		});
		menuBar.add(btnNewButton);
	
		
	}
	public void export(JTable table, File file,String raportti){
	    try
	    {
	      TableModel m = table.getModel();
	      FileWriter fw = new FileWriter(file);
	      Date date = new Date();
	      SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	      fw.write(raportti + " | " + formatter.format(date) + "\n");
	      for(int i = 0; i < m.getColumnCount(); i++){
	        fw.write(m.getColumnName(i) + "\t");
	      }
	      fw.write("\n");
	      for(int i=0; i < m.getRowCount(); i++) {
	        for(int j=0; j < m.getColumnCount(); j++) {
	          fw.write(m.getValueAt(i,j).toString()+"\t");
	        }
	        fw.write("\n");
	      }
	      fw.close();
	      JOptionPane.showMessageDialog(null, "Tiedosto luotu onnistuneesti.", "Excel vienti",JOptionPane.INFORMATION_MESSAGE);
	    }
	    catch(IOException e){ JOptionPane.showMessageDialog(null, "Virhe: " + e.getMessage(), "Excel vienti",JOptionPane.ERROR_MESSAGE); }
	  }
}
