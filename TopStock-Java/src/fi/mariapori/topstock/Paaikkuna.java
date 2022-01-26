package fi.mariapori.topstock;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Paaikkuna extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6756547575820618963L;
	private JPanel contentPane;
	static DefaultListModel<String> listaTuotteet = new DefaultListModel<String>();
	static List<Tavara> tuotteet;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String connectionString = "jdbc:sqlite:kanta.db";
					ConnectionSource connectionSource = new JdbcConnectionSource(connectionString);
					try {
					TableUtils.createTable(connectionSource, Tavara.class);
					}catch(SQLException e1) {
						System.out.print(e1.getMessage());
					}
					PopuloiTuotteet(connectionSource);
					Paaikkuna frame = new Paaikkuna(connectionSource);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Paaikkuna(ConnectionSource connectionSource) {
		setTitle("TopStock");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tuotteet");
		lblNewLabel.setBounds(48, 12, 70, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblTuotekoodi = new JLabel("");
		lblTuotekoodi.setBounds(277, 32, 70, 15);
		contentPane.add(lblTuotekoodi);
		
		JLabel lblTuotenimi = new JLabel("");
		lblTuotenimi.setBounds(277, 56, 70, 15);
		contentPane.add(lblTuotenimi);
		
		JLabel lblSaldo = new JLabel("");
		lblSaldo.setBounds(277, 77, 70, 15);
		contentPane.add(lblSaldo);
		
		JList<String> list = new JList<String>(listaTuotteet);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int index = list.getSelectedIndex();
				Tavara tavara = tuotteet.get(index);
				lblTuotekoodi.setText(tavara.getProductCode());
				lblTuotenimi.setText(tavara.getProductName());
				lblSaldo.setText("Saldo: " + tavara.getSaldo());
			}
			});
		list.setVisibleRowCount(-1);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(12, 33, 154, 226);
		contentPane.add(list);
		
	}
	
	public static void PopuloiTuotteet(ConnectionSource connectionSource) {
		try {
			Dao<Tavara, String> productDao = DaoManager.createDao(connectionSource, Tavara.class);
			tuotteet = productDao.queryForAll();
			tuotteet.forEach((tavara) -> listaTuotteet.addElement(tavara.getProductName()));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
