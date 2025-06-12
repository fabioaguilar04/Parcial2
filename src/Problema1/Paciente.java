package Problema1;


public class Paciente {
    // Atributos privados (encapsulamiento)
    private String nombre;
    private String identificacion;
    private int edad;
    private String prioridad;

    public Paciente(String nombre, String identificacion, int edad, String prioridad) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.edad = edad;
        this.prioridad = prioridad != null ? prioridad : "Normal";
    }


    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public int getEdad() {
        return edad;
    }

    public String getPrioridad() {
        return prioridad;
    }

    // === MÉTODOS SETTER (para modificar valores) ===
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setEdad(int edad) {
        if (edad > 0 && edad < 150) {
            this.edad = edad;
        }
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s, Edad: %d, Prioridad: %s)",
                nombre, identificacion, edad, prioridad);
    }

    public boolean esPrioridadAlta() {
        return prioridad.equalsIgnoreCase("Urgente") ||
                prioridad.equalsIgnoreCase("Emergencia");
    }
}