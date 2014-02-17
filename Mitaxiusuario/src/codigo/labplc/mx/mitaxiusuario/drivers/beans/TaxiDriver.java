package codigo.labplc.mx.mitaxiusuario.drivers.beans;

import java.io.Serializable;

public class TaxiDriver implements Serializable {
	/**
	 * ID taxidriver serialization
	 */
	private static final long serialVersionUID = -1098245708702892529L;
	private String name;
	private String lastName;
	private String id;
	private String idValidity;
	private String antiquity;
	private int ranking;
	private String placa;
	private String taxiModelCar;
	private String numInfracciones;
	private String distance;
	private String tiempo;
	private String taxiTypeId;
	
	public TaxiDriver(){}
	
	public TaxiDriver(String name, String lastName, String id,
			String idValidity, String antiquity, int ranking, String placa,
			String taxiModelCar, String sInfraccion, String string,String tiempo,String distance) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.id = id;
		this.idValidity = idValidity;
		this.antiquity = antiquity;
		this.ranking = ranking;
		this.placa = placa;
		this.taxiModelCar = taxiModelCar;
		this.distance= distance;
		this.tiempo=tiempo;
		this.numInfracciones = sInfraccion;
		this.taxiTypeId = string;
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
	public String getNumInfrac() {
		return numInfracciones;
	}
	public void setNumInfrac(String numInfrac) {
		this.numInfracciones = numInfrac;
	}
	public String getTaxiTypeId() {
		return taxiTypeId;
	}
	public void setTaxiTypeId(String taxiTypeId) {
		this.taxiTypeId = taxiTypeId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	
}