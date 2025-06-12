package Problema1;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorTurnos {
    private Queue<Turno> colaTurnos = new LinkedList<>();
    private List<Turno> historialAtendidos = new ArrayList<>();
    private final Stack<Turno> turnosCancelados = new Stack<>();

    private static final Path APP_DIR = Paths.get(System.getProperty("user.home"), "Turnos_app");
    private static final Path ARCHIVO_ESPERA = APP_DIR.resolve("turnos_espera.txt");
    private static final Path ARCHIVO_HISTORIAL = APP_DIR.resolve("historial_turnos.txt");

    static {
        try { Files.createDirectories(APP_DIR); }
        catch (Exception e) { System.err.println("Error creando directorio: " + e.getMessage()); }
    }

    public static String getArchivoEspera() { return ARCHIVO_ESPERA.toString(); }
    public static String getArchivoHistorial() { return ARCHIVO_HISTORIAL.toString(); }

    public GestorTurnos() {
        try {
            if (Files.notExists(ARCHIVO_HISTORIAL)) Files.createFile(ARCHIVO_HISTORIAL);
            if (Files.notExists(ARCHIVO_ESPERA)) Files.createFile(ARCHIVO_ESPERA);
        } catch (IOException e) { System.out.println("Error al crear archivos: " + e.getMessage()); }
        cargarTurnosDesdeArchivo();
        cargarHistorialDesdeArchivo();
    }

    public void registrarTurno(Paciente p, int d) {
        Turno t = new Turno(p, d);
        colaTurnos.offer(t);
        System.out.printf("Turno registrado: %s | Posición: %d | Espera: %d min%n",
                t.getTurnoResumenLinea(), colaTurnos.size(), calcularTiempoEsperaEstimado());
    }

    public Turno atenderSiguienteTurno() {
        if (colaTurnos.isEmpty()) { System.out.println("No hay turnos en espera."); return null; }
        Turno t = colaTurnos.poll();
        t.marcarComoAtendido();
        historialAtendidos.add(t);
        System.out.println("Atendiendo turno:\n" + t.getInformacionDetallada());
        return t;
    }

    public void mostrarTurnosEnEspera() {
        if (colaTurnos.isEmpty()) { System.out.println("No hay turnos en espera."); return; }
        int pos = 1;
        for (Turno t : colaTurnos)
            System.out.printf("%d. %s (Esperando: %d min)%n", pos++, t.getTurnoResumenLinea(), t.calcularTiempoEspera());
    }

    public void mostrarHistorialAtendidos() {
        if (historialAtendidos.isEmpty()) { System.out.println("No hay turnos atendidos."); return; }
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        int i = 1;
        for (Turno t : historialAtendidos)
            System.out.printf("%d. %s | Atendido: %s | Espera: %d min%n", i++, t.getTurnoResumenLinea(), t.getHoraAtencion().format(f), t.calcularTiempoEspera());
    }

    public void guardarTurnosEnEspera() {
        try (BufferedWriter w = Files.newBufferedWriter(ARCHIVO_ESPERA)) {
            w.write("# Turnos en espera - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))); w.newLine();
            w.write("NumeroTurno,NombrePaciente,Identificacion,Edad,Prioridad,HoraEntrada,DuracionEstimada"); w.newLine();
            for (Turno t : colaTurnos)
                w.write(String.format("%s,%s,%s,%d,%s,%s,%d%n", t.getNumeroTurno(), t.getPaciente().getNombre(), t.getPaciente().getIdentificacion(), t.getPaciente().getEdad(), t.getPaciente().getPrioridad(), t.getHoraEntrada(), t.getDuracionEstimada()));
        } catch (Exception e) { System.out.println("Error al guardar turnos: " + e.getMessage()); }
    }

    public void guardarHistorial() {
        try (BufferedWriter w = Files.newBufferedWriter(ARCHIVO_HISTORIAL)) {
            w.write("# Historial de turnos atendidos - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))); w.newLine();
            w.write("NumeroTurno,NombrePaciente,Identificacion,HoraEntrada,HoraAtencion,TiempoEspera"); w.newLine();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Turno t : historialAtendidos)
                w.write(String.format("%s,%s,%s,%s,%s,%d%n", t.getNumeroTurno(), t.getPaciente().getNombre(), t.getPaciente().getIdentificacion(), t.getHoraEntrada().format(f), t.getHoraAtencion().format(f), t.calcularTiempoEspera()));
        } catch (Exception e) { System.out.println("Error al guardar historial: " + e.getMessage()); }
    }

    private void cargarTurnosDesdeArchivo() {
        try {
            if (Files.exists(ARCHIVO_ESPERA)) {
                List<String> l = Files.readAllLines(ARCHIVO_ESPERA);
                for (int i = 2; i < l.size(); i++) {
                    String[] d = l.get(i).split(",");
                    if (d.length >= 7) { /* reconstrucción aquí */ }
                }
            }
        } catch (Exception e) { System.out.println("No se pudieron cargar turnos: " + e.getMessage()); }
    }

    private void cargarHistorialDesdeArchivo() { /* similar a cargarTurnosDesdeArchivo */ }

    private int calcularTiempoEsperaEstimado() {
        return colaTurnos.stream().mapToInt(Turno::getDuracionEstimada).sum();
    }

    public void mostrarEstadisticas() {
        System.out.printf("Turnos en espera: %d | Atendidos: %d | Cancelados: %d%n", colaTurnos.size(), historialAtendidos.size(), turnosCancelados.size());
        if (!historialAtendidos.isEmpty()) {
            double prom = historialAtendidos.stream().mapToLong(Turno::calcularTiempoEspera).average().orElse(0.0);
            System.out.printf("Tiempo promedio de espera: %.1f min%n", prom);
        }
    }

    public int getTurnosEnEspera() { return colaTurnos.size(); }
    public int getTurnosAtendidos() { return historialAtendidos.size(); }
    public boolean hayTurnosEnEspera() { return !colaTurnos.isEmpty(); }
}