/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vistas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "propietarios_morosos")
public class PropietariosMorosos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_medidor")
    private Integer idMedidor;
    @Size(max = 2147483647)
    @Column(name = "porp_cedula")
    private String porpCedula;
    @Size(max = 2147483647)
    @Column(name = "prop_nombre")
    private String propNombre;
    @Size(max = 2147483647)
    @Column(name = "prop_apellido")
    private String propApellido;
    @Size(max = 2147483647)
    @Column(name = "med_numero")
    private String medNumero;
    @Column(name = "fecha_ultimo_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimoPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "dias_mora")
    private BigDecimal diasMora;

    public PropietariosMorosos() {
    }

    public Integer getIdMedidor() {
        return idMedidor;
    }

    public void setIdMedidor(Integer idMedidor) {
        this.idMedidor = idMedidor;
    }

    public String getPorpCedula() {
        return porpCedula;
    }

    public void setPorpCedula(String porpCedula) {
        this.porpCedula = porpCedula;
    }

    public String getPropNombre() {
        return propNombre;
    }

    public void setPropNombre(String propNombre) {
        this.propNombre = propNombre;
    }

    public String getPropApellido() {
        return propApellido;
    }

    public void setPropApellido(String propApellido) {
        this.propApellido = propApellido;
    }

    public String getMedNumero() {
        return medNumero;
    }

    public void setMedNumero(String medNumero) {
        this.medNumero = medNumero;
    }

    public Date getFechaUltimoPago() {
        return fechaUltimoPago;
    }

    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

    public BigDecimal getDiasMora() {
        return diasMora;
    }

    public void setDiasMora(BigDecimal diasMora) {
        this.diasMora = diasMora;
    }

   
    
}
