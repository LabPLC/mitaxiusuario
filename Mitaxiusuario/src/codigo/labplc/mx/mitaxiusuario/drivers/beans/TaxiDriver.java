package codigo.labplc.mx.mitaxiusuario.drivers.beans;

import java.io.Serializable;

public class TaxiDriver implements Serializable {
	/**
	 * ID taxidriver serialization
	 */
	private static final long serialVersionUID = -1098245708702892529L;
	
	public TaxiDriver(){}
	
	public TaxiDriver(String name, String lastName, String id,
			String idValidity, String antiquity, int ranking, String placa,
			String taxiModelCar, int numInfrac, int taxiTypeId) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.id = id;
		this.idValidity = idValidity;
		this.antiquity = antiquity;
		this.ranking = ranking;
		this.placa = placa;
		this.taxiModelCar = taxiModelCar;
		this.numInfrac = numInfrac;
		this.taxiTypeId = taxiTypeId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdValidity() {
		return idValidity;
	}
	public void setIdValidity(String idValidity) {
		this.idValidity = idValidity;
	}
	public String getAntiquity() {
		return antiquity;
	}
	public void setAntiquity(String antiquity) {
		this.antiquity = antiquity;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getTaxiModelCar() {
		return taxiModelCar;
	}
	public void setTaxiModelCar(String taxiModelCar) {
		this.taxiModelCar = taxiModelCar;
	}
	public int getNumInfrac() {
		return numInfrac;
	}
	public void setNumInfrac(int numInfrac) {
		this.numInfrac = numInfrac;
	}
	public int getTaxiTypeId() {
		return taxiTypeId;
	}
	public void setTaxiTypeId(int taxiTypeId) {
		this.taxiTypeId = taxiTypeId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String name;
	private String lastName;
	private String id;
	private String idValidity;
	private String antiquity;
	private int ranking;
	private String placa;
	private String taxiModelCar;
	private int numInfrac;
	private int taxiTypeId;
}