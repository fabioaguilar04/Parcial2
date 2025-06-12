package Problema1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Clase que representa un turno médico
 * Cada turno tiene un número único y está asociado a un paciente
 */
public class Turno {
    // Atributos privados
    private String numeroTurno;
    private LocalDateTime horaEntrada;
    private int duracionEstimada; // en minutos
    private Paciente paciente;
    private LocalDateTime horaAtencion; // cuando fue atendido

    // Contador estático para generar números únicos
    private static int contador = 1;

    /**
     * Constructor para crear un nuevo turno
     * @param paciente El paciente asociado al turno
     * @param duracionEstimada Tiempo estimado de atención en minutos
     */
    public Turno(Paciente paciente, int duracionEstimada) {
        this.numeroTurno = String.format("T%03d", contador++);
        this.horaEntrada = LocalDateTime.now();
        this.paciente = paciente;
        this.duracionEstimada = duracionEstimada > 0 ? duracionEstimada : 15; // mínimo 15 min
        this.horaAtencion = null; // se asigna cuando se atiende
    }

    // === MÉTODOS GETTER ===
    public String getNumeroTurno() {
        return numeroTurno;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getHoraAtencion() {
        return horaAtencion;
    }

    // === MÉTODOS SETTER ===
    public void setDuracionEstimada(int duracionEstimada) {
        if (duracionEstimada > 0) {
            this.duracionEstimada = duracionEstimada;
        }
    }

    public void setHoraAtencion(LocalDateTime horaAtencion) {
        this.horaAtencion = horaAtencion;
    }

    /**
     * Genera un resumen del turno en una línea
     * Formato: "T001 - Juan Pérez - 10:32 AM"
     * @return String con el resumen
     */
    public String getTurnoResumenLinea() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("%s - %s - %s",
                numeroTurno,
                paciente.getNombre(),
                horaEntrada.format(formatter));
    }

    /**
     * Calcula el tiempo de espera en minutos
     * @return minutos de espera (0 si aún no ha sido atendido)
     */
    public long calcularTiempoEspera() {
        if (horaAtencion == null) {
            return ChronoUnit.MINUTES.between(horaEntrada, LocalDateTime.now());
        } else {
            return ChronoUnit.MINUTES.between(horaEntrada, horaAtencion);
        }
    }

    /**
     * Marca el turno como atendido
     */
    public void marcarComoAtendido() {
        this.horaAtencion = LocalDateTime.now();
    }

    /**
     * Verifica si el turno ya fue atendido
     * @return true si ya fue atendido
     */
    public boolean fueAtendido() {
        return horaAtencion != null;
    }

    /**
     * Genera información detallada del turno
     * @return String con información completa
     */
    public String getInformacionDetallada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder info = new StringBuilder();

        info.append("Turno: ").append(numeroTurno).append("\n");
        info.append("Paciente: ").append(paciente.toString()).append("\n");
        info.append("Hora de entrada: ").append(horaEntrada.format(formatter)).append("\n");
        info.append("Duración estimada: ").append(duracionEstimada).append(" minutos\n");

        if (fueAtendido()) {
            info.append("Hora de atención: ").append(horaAtencion.format(formatter)).append("\n");
            info.append("Tiempo de espera: ").append(calcularTiempoEspera()).append(" minutos");
        } else {
            info.append("Estado: En espera (").append(calcularTiempoEspera()).append(" min esperando)");
        }

        return info.toString();
    }

    @Override
    public String toString() {
        return getTurnoResumenLinea();
    }
}
