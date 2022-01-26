package fi.mariapori.topstock;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Tavarat")
public class Tavara {
	
	@DatabaseField(id = true)
	private String Tuotekoodi;
	
	@DatabaseField
	private String Nimike;
	
	@DatabaseField
	private double Saldo;
	
	public Tavara() {
		
	}
	
	public Tavara(String Tuotekoodi, String Nimike, double Saldo) {
		this.Tuotekoodi = Tuotekoodi;
		this.Nimike = Nimike;
		this.Saldo = Saldo;
	}
	
	public String getProductCode() {
		return Tuotekoodi;
	}
	
	public String getProductName() {
		return Nimike;
	}
	
	public double getSaldo() {
		return Saldo;
	}
	
	public void AddSaldo(double Amount) {
		this.Saldo += Amount;
	}
	
	public void RemoveSaldo(double Amount) {
		this.Saldo -= Amount;
	}
	
	public void ClearSaldo() {
		this.Saldo = 0;
	}
	
}
