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
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.JMenuBar;

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
		setBounds(100, 100, 450, 321);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnNewButton = new JButton("Uusi tuote");
		menuBar.add(btnNewButton);
		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tuotteet");
		lblNewLabel.setBounds(53, 33, 70, 25);
		contentPane.add(lblNewLabel);
		
		JLabel lblTuotekoodi = new JLabel("");
		lblTuotekoodi.setBounds(277, 32, 135, 15);
		contentPane.add(lblTuotekoodi);
		
		JLabel lblTuotenimi = new JLabel("");
		lblTuotenimi.setBounds(277, 56, 135, 15);
		contentPane.add(lblTuotenimi);
		
		JLabel lblSaldo = new JLabel("");
		lblSaldo.setBounds(277, 77, 135, 15);
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
		list.setBounds(12, 56, 154, 203);
		
		contentPane.add(list);
		
		JSpinner saldoSpinner = new JSpinner();
		saldoSpinner.setFont(new Font("Dialog", Font.BOLD, 24));
		saldoSpinner.setBounds(368, 172, 70, 87);
		contentPane.add(saldoSpinner);
		
		JButton btnLisaavarastoon = new JButton("Lisää varastoon");
		btnLisaavarastoon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				Tavara tavara = tuotteet.get(index);
				tavara.AddSaldo((Double.valueOf((int)saldoSpinner.getValue())));
				try {
					Dao<Tavara, String> productDao = DaoManager.createDao(connectionSource, Tavara.class);
					productDao.update(tavara);
					productDao.refresh(tavara);
					lblSaldo.setText("Saldo: " + tavara.getSaldo());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					tavara.RemoveSaldo(Double.valueOf((int)saldoSpinner.getValue()));
				}
				saldoSpinner.setValue(0);
			}
		});
		btnLisaavarastoon.setBounds(178, 162, 169, 25);
		contentPane.add(btnLisaavarastoon);
		
		JButton btnPoistavarastosta = new JButton("Poista varastosta");
		btnPoistavarastosta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				Tavara tavara = tuotteet.get(index);
				tavara.RemoveSaldo((Double.valueOf((int)saldoSpinner.getValue())));
				try {
					Dao<Tavara, String> productDao = DaoManager.createDao(connectionSource, Tavara.class);
					productDao.update(tavara);
					productDao.refresh(tavara);
					lblSaldo.setText("Saldo: " + tavara.getSaldo());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					tavara.AddSaldo(Double.valueOf((int)saldoSpinner.getValue()));
				}
				saldoSpinner.setValue(0);
			}
		});
		btnPoistavarastosta.setBounds(178, 199, 169, 25);
		contentPane.add(btnPoistavarastosta);
		
		JButton btnNollaa = new JButton("Nollaa");
		btnNollaa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double vanhaSaldo = 0;
				int index = list.getSelectedIndex();
				Tavara tavara = tuotteet.get(index);
				vanhaSaldo = tavara.getSaldo();
				tavara.ClearSaldo();
				try {
					Dao<Tavara, String> productDao = DaoManager.createDao(connectionSource, Tavara.class);
					productDao.update(tavara);
					productDao.refresh(tavara);
					lblSaldo.setText("Saldo: " + tavara.getSaldo());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					tavara.AddSaldo(vanhaSaldo);
				}
				saldoSpinner.setValue(0);
			}
		});
		btnNollaa.setBounds(178, 234, 169, 25);
		contentPane.add(btnNollaa);
		

		
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
