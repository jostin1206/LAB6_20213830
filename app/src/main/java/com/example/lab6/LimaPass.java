package com.example.lab6;

public class LimaPass {

    private String idTarjeta;
    private String fecha;
    private String paraderoEntrada;
    private String paraderoSalida;

    // ese campo no se guarda en Firestore, pero lo usaremos localmente
    private String id;

    public LimaPass() {
    }

    public LimaPass(String idTarjeta, String fecha, String paraderoEntrada, String paraderoSalida) {
        this.idTarjeta = idTarjeta;
        this.fecha = fecha;
        this.paraderoEntrada = paraderoEntrada;
        this.paraderoSalida = paraderoSalida;
    }

    // Getters y Setters
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

    public String getParaderoEntrada() {
        return paraderoEntrada;
    }

    public void setParaderoEntrada(String paraderoEntrada) {
        this.paraderoEntrada = paraderoEntrada;
    }

    public String getParaderoSalida() {
        return paraderoSalida;
    }

    public void setParaderoSalida(String paraderoSalida) {
        this.paraderoSalida = paraderoSalida;
    }

    // cmpo para guardar el ID del documento Firestore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
