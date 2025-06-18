package com.example.lab6;

public class Linea1 {

    private String idTarjeta;
    private String fecha;
    private String estacionEntrada;
    private String estacionSalida;
    private String tiempoViaje;

    public Linea1() {

    }

    public String getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(String idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstacionEntrada() {
        return estacionEntrada;
    }

    public void setEstacionEntrada(String estacionEntrada) {
        this.estacionEntrada = estacionEntrada;
    }

    public String getEstacionSalida() {
        return estacionSalida;
    }

    public void setEstacionSalida(String estacionSalida) {
        this.estacionSalida = estacionSalida;
    }

    public String getTiempoViaje() {
        return tiempoViaje;
    }

    public void setTiempoViaje(String tiempoViaje) {
        this.tiempoViaje = tiempoViaje;
    }

    public Linea1(String idTarjeta, String fecha, String estacionEntrada, String estacionSalida, String tiempoViaje) {
        this.idTarjeta = idTarjeta;
        this.fecha = fecha;
        this.estacionEntrada = estacionEntrada;
        this.estacionSalida = estacionSalida;
        this.tiempoViaje = tiempoViaje;
    }

    private String id;  // iddel documento Firestore

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
