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
	private String pk_chofer;
	private String pk_usuario;
	private String origen;
	private String destino;
	private String personas;
	private boolean mascotas;
	private boolean discapacitados;
	private boolean bicicletas;
	private String foto;
	private String referencia;
	


	public TaxiDriver(){}
	
	public TaxiDriver(String name, String lastName, String id,
			String idValidity, String antiquity, int ranking, String placa,
			String taxiModelCar, String sInfraccion, String string,String tiempo,
			String distance,String pk_chofer,String pk_usuario,String origen,String destino,String personas,
			boolean mascotas, boolean discapacitados, boolean bicicletas,String foto,String referencia) {
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
		this.pk_chofer=pk_chofer;
		this.pk_usuario=pk_usuario;
		this.origen= origen;
		this.destino=destino;
		this.personas=personas;
		this.mascotas=mascotas;
		this.discapacitados=discapacitados;
		this.bicicletas=bicicletas;
		this.foto=foto;
		this.referencia=referencia;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getPk_usuario() {
		return pk_usuario;
	}

	public void setPk_usuario(String pk_usuario) {
		this.pk_usuario = pk_usuario;
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
	public String getNumInfracciones() {
		return numInfracciones;
	}

	public void setNumInfracciones(String numInfracciones) {
		this.numInfracciones = numInfracciones;
	}

	public String getPk_chofer() {
		return pk_chofer;
	}

	public void setPk_chofer(String pk_chofer) {
		this.pk_chofer = pk_chofer;
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
	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getPersonas() {
		return personas;
	}

	public void setPersonas(String personas) {
		this.personas = personas;
	}

	public boolean getMascotas() {
		return mascotas;
	}

	public void setMascotas(boolean mascotas) {
		this.mascotas = mascotas;
	}

	public boolean getDiscapacitados() {
		return discapacitados;
	}

	public void setDiscapacitados(boolean discapacitados) {
		this.discapacitados = discapacitados;
	}

	public boolean getBicicletas() {
		return bicicletas;
	}

	public void setBicicletas(boolean bicicletas) {
		this.bicicletas = bicicletas;
	}
	
}