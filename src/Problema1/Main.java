package Problema1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class Main {

    private static GestorTurnos gestorTurnos;
    private static BufferedReader reader;

    public static void main(String[] args) {
        // Inicializar el sistema
        gestorTurnos = new GestorTurnos();
        reader = new BufferedReader(new InputStreamReader(System.in));

        // Mostrar bienvenida
        mostrarBienvenida();

        // Ejecutar el menú principal
        ejecutarMenuPrincipal();

        // Cerrar recursos y despedida
        cerrarSistema();
    }
    private static void mostrarBienvenida() {
        System.out.println("\n");

        System.out.println("          SISTEMA DE GESTIÓN DE TURNOS MÉDICOS              ");
        System.out.println("        Centro de Salud Comunitario \"San Rafael\"          ");
        System.out.println("  Bienvenido al sistema automatizado de turnos médicos      ");
        System.out.println();
    }
    private static void ejecutarMenuPrincipal() {
        int opcion = 0;

        do {
            try {
                mostrarMenu();
                opcion = leerOpcion();
                ejecutarOpcion(opcion);

                // Pausa para que el usuario pueda leer el resultado
                if (opcion != 6) {
                    pausaParaContinuar();
                }

            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                System.out.println("Por favor, intente nuevamente.");
                pausaParaContinuar();
            }
        } while (opcion != 6);
    }
    private static void mostrarMenu() {
        System.out.println("                    MENÚ PRINCIPAL                       ");
        System.out.println("  1.  Agregar nuevo paciente y registrar turno        ");
        System.out.println("  2.  Ver turnos en espera                            ");
        System.out.println("  3.  Atender siguiente turno                         ");
        System.out.println("  4.  Ver historial de atención                       ");
        System.out.println("  5.  Ver estadísticas del sistema                    ");
        System.out.println("  6.  Salir del sistema                               ");

        System.out.print("Seleccione una opción (1-6): ");
    }
    private static int leerOpcion() {
        try {
            String input = reader.readLine().trim();
            int opcion = Integer.parseInt(input);

            if (opcion < 1 || opcion > 6) {
                System.out.println(" Opción inválida. Por favor seleccione entre 1 y 6.");
                return leerOpcion(); // Recursión para volver a pedir
            }

            return opcion;

        } catch (NumberFormatException e) {
            System.out.println(" Por favor ingrese un número válido.");
            return leerOpcion();
        } catch (IOException e) {
            System.out.println(" Error al leer la entrada. Intente nuevamente.");
            return leerOpcion();
        }
    }
    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarNuevoPacienteYRegistrarTurno();
                break;
            case 2:
                verTurnosEnEspera();
                break;
            case 3:
                atenderSiguienteTurno();
                break;
            case 4:
                verHistorialAtencion();
                break;
            case 5:
                verEstadisticasSistema();
                break;
            case 6:
                System.out.println("\n Cerrando sistema...");
                break;
            default:
                System.out.println(" Opción no válida.");
        }
    }
    private static void agregarNuevoPacienteYRegistrarTurno() {
        try {
            System.out.println("\n REGISTRO DE NUEVO PACIENTE Y TURNO");
            System.out.println("");

            // Solicitar datos del paciente
            System.out.print("Nombre completo del paciente: ");
            String nombre = reader.readLine().trim();

            if (nombre.isEmpty()) {
                System.out.println(" El nombre no puede estar vacío.");
                return;
            }

            System.out.print("Número de identificación: ");
            String identificacion = reader.readLine().trim();

            if (identificacion.isEmpty()) {
                System.out.println(" La identificación no puede estar vacía.");
                return;
            }

            System.out.print("Edad: ");
            int edad = Integer.parseInt(reader.readLine().trim());

            if (edad <= 0 || edad > 150) {
                System.out.println(" Edad inválida. Debe estar entre 1 y 150 años.");
                return;
            }

            System.out.println("Prioridad del paciente:");
            System.out.println("1. Normal");
            System.out.println("2. Urgente");
            System.out.println("3. Emergencia");
            System.out.print("Seleccione (1-3): ");

            int prioridadNum = Integer.parseInt(reader.readLine().trim());
            String prioridad;

            switch (prioridadNum) {
                case 1:
                    prioridad = "Normal";
                    break;
                case 2:
                    prioridad = "Urgente";
                    break;
                case 3:
                    prioridad = "Emergencia";
                    break;
                default:
                    prioridad = "Normal";
                    System.out.println(" Opción inválida. Se asignará prioridad Normal.");
            }

            System.out.print("Duración estimada de atención (minutos): ");
            int duracion = Integer.parseInt(reader.readLine().trim());

            if (duracion <= 0) {
                duracion = 15; // Duración por defecto
                System.out.println("️ Duración inválida. Se asignará 15 minutos por defecto.");
            }

            // Crear paciente y registrar turno
            Paciente paciente = new Paciente(nombre, identificacion, edad, prioridad);
            gestorTurnos.registrarTurno(paciente, duracion);

        } catch (NumberFormatException e) {
            System.out.println(" Error: Por favor ingrese números válidos.");
        } catch (IOException e) {
            System.out.println(" Error al leer los datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Error inesperado: " + e.getMessage());
        }
    }


    private static void verTurnosEnEspera() {
        System.out.println("\n TURNOS EN ESPERA");
        System.out.println("");
        gestorTurnos.mostrarTurnosEnEspera();
    }


    private static void atenderSiguienteTurno() {
        System.out.println("\n ATENDER SIGUIENTE TURNO");
        System.out.println("");

        if (!gestorTurnos.hayTurnosEnEspera()) {
            System.out.println(" No hay turnos en espera para atender.");
            return;
        }

        try {
            System.out.print("¿Confirma que desea atender el siguiente turno? (s/n): ");
            String confirmacion = reader.readLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si") || confirmacion.equals("sí")) {
                Turno turnoAtendido = gestorTurnos.atenderSiguienteTurno();

                if (turnoAtendido != null) {
                    System.out.println("\n Turno atendido exitosamente.");
                    System.out.println(" Información de la atención:");
                    System.out.println("   Paciente: " + turnoAtendido.getPaciente().getNombre());
                    System.out.println("   Tiempo de espera: " + turnoAtendido.calcularTiempoEspera() + " minutos");
                }
            } else {
                System.out.println(" Atención cancelada.");
            }

        } catch (IOException e) {
            System.out.println(" Error al leer la confirmación: " + e.getMessage());
        }
    }


    private static void verHistorialAtencion() {
        System.out.println("\n HISTORIAL DE ATENCIÓN");
        System.out.println("");
        gestorTurnos.mostrarHistorialAtendidos();
    }


    private static void verEstadisticasSistema() {
        System.out.println("\n ESTADÍSTICAS DEL SISTEMA");
        System.out.println("");
        gestorTurnos.mostrarEstadisticas();
    }

    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     */
    private static void pausaParaContinuar() {
        try {
            System.out.println("\nPresione Enter para continuar...");
            reader.readLine();
        } catch (IOException e) {
            System.out.println(" Error en la pausa: " + e.getMessage());
        }
    }

    /**
     * Cierra el sistema guardando los datos y liberando recursos
     */
    private static void cerrarSistema() {
        try {
            System.out.println("\n Guardando datos del sistema...");

            // Guardar turnos en espera y historial
            if (gestorTurnos.hayTurnosEnEspera()) {
                gestorTurnos.guardarTurnosEnEspera();
            }

            if (gestorTurnos.getTurnosAtendidos() > 0) {
                gestorTurnos.guardarHistorial();
            }

            // Cerrar recursos
            if (reader != null) {
                reader.close();
            }

            // Mostrar mensaje de despedida
            System.out.println("\n");
            System.out.println("                    SISTEMA CERRADO                        ");
            System.out.println("");
            System.out.println("  Gracias por usar el Sistema de Gestión de Turnos Médicos ");
            System.out.println("        Centro de Salud Comunitario \"San Rafael\"           ");
            System.out.println("                                                           ");
            System.out.println("      Todos los datos han sido guardados correctamente.       ");
            System.out.println("                ¡Que tenga un excelente día!                            ");
            System.out.println("");

        } catch (IOException e) {
            System.out.println(" Error al cerrar el sistema: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Error inesperado al cerrar: " + e.getMessage());
        }
    }
}