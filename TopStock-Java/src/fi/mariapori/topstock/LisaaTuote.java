package fi.mariapori.topstock;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class LisaaTuote extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5352383966897326673L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtTuotekoodi;
	private JTextField txtNimike;
	private JTextField txtArvo;
	private JTextField txtSaldo;

	/**
	 * Create the dialog.
	 */
	public LisaaTuote(ConnectionSource connectionSource) {
		setResizable(false);
		setTitle("Lisää tuote");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Tuotekoodi");
			lblNewLabel.setBounds(125, 12, 80, 15);
			contentPanel.add(lblNewLabel);
		}
		{
			txtTuotekoodi = new JTextField();
			txtTuotekoodi.setBounds(210, 10, 191, 19);
			contentPanel.add(txtTuotekoodi);
			txtTuotekoodi.setColumns(10);
		}
		
		JLabel lblNimike = new JLabel("Nimike");
		lblNimike.setBounds(125, 39, 80, 15);
		contentPanel.add(lblNimike);
		
		txtNimike = new JTextField();
		txtNimike.setColumns(10);
		txtNimike.setBounds(210, 37, 191, 19);
		contentPanel.add(txtNimike);
		
		txtArvo = new JTextField();
		txtArvo.setColumns(10);
		txtArvo.setBounds(210, 66, 191, 19);
		contentPanel.add(txtArvo);
		
		JLabel lblArvo = new JLabel("Arvo");
		lblArvo.setBounds(125, 68, 80, 15);
		contentPanel.add(lblArvo);
		{
			txtSaldo = new JTextField();
			txtSaldo.setColumns(10);
			txtSaldo.setBounds(210, 97, 191, 19);
			contentPanel.add(txtSaldo);
		}
		{
			JLabel lblSaldo = new JLabel("Saldo");
			lblSaldo.setBounds(125, 99, 80, 15);
			contentPanel.add(lblSaldo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Lisää");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Tavara uusTavara = new Tavara(txtTuotekoodi.getText(), txtNimike.getText(), Double.parseDouble(txtSaldo.getText()),Double.parseDouble(txtArvo.getText()));
						try {
							Dao<Tavara, String> productDao = DaoManager.createDao(connectionSource, Tavara.class);
							productDao.create(uusTavara);
							productDao.refresh(uusTavara);
							Paaikkuna.PopuloiTuotteet(connectionSource);
							dispose();
						}catch(Exception ex1) {
							
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Peruuta");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
