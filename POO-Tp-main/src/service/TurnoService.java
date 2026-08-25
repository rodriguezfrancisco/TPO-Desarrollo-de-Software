package service;

import entity.Odontologo;
import entity.OdOrtodoncia;
import entity.OdEndodoncia;
import entity.OdExtraccion;
import entity.Paciente;
import entity.PacienteParticular;
import entity.PacienteObraSocial;
import entity.Turno;
import entity.EstadoTurno;
import repository.IRepository;
import repository.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class TurnoService implements IService<Turno> {

    private IRepository<Turno> turnoRepository;
    private PacienteService pacienteService;
    private OdontologoService odontologoService;

    public TurnoService(PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoRepository = new TurnoRepository();
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }

    @Override
    public Turno registrar(Turno turno) {
        try {
            System.out.println("Iniciando validaciones para agendar nuevo turno...");

            // REGLA 1: Integridad Referencial
            if (turno.getPaciente() == null || turno.getPaciente().getId() == null) {
                throw new DatoInvalidoException("Paciente en Turno", "El turno debe tener un Paciente con un ID.");
            }
            Optional<Paciente> pacienteExistente = pacienteService.buscarPorId(turno.getPaciente().getId());
            if (pacienteExistente.isEmpty()) {
                throw new PacienteNoEncontradoException(turno.getPaciente().getId());
            }

            if (turno.getOdontologo() == null || turno.getOdontologo().getId() == null) {
                throw new DatoInvalidoException("Odontólogo en Turno", "El turno debe tener un Odontólogo con un ID.");
            }
            Optional<Odontologo> odontologoExistente = odontologoService.buscarPorId(turno.getOdontologo().getId());
            if (odontologoExistente.isEmpty()) {
                throw new OdontologoNoEncontradoException(turno.getOdontologo().getId());
            }

            // REGLA 2: Lógica Temporal
            if (turno.getFecha() == null || turno.getHora() == null) {
                throw new DatoInvalidoException("Fecha/Hora", "El turno debe tener fecha y hora asignadas.");
            }

            LocalDate hoy = LocalDate.now();
            LocalTime ahora = LocalTime.now();

            if (turno.getFecha().isBefore(hoy)) {
                throw new ClinicaException("No se puede agendar un turno para una fecha pasada (" + turno.getFecha() + ").", "ERR_TUR_004");
            }
            if (turno.getFecha().equals(hoy) && turno.getHora().isBefore(ahora)) {
                throw new ClinicaException("La hora solicitada (" + turno.getHora() + ") ya pasó hoy.", "ERR_TUR_005");
            }

            // REGLA 3: Horarios Operativos
            LocalTime horaTurno = turno.getHora();
            if (horaTurno.isBefore(LocalTime.of(8, 0)) || horaTurno.isAfter(LocalTime.of(18, 0))) {
                throw new ClinicaException("El horario (" + horaTurno + ") está fuera del rango de atención (08:00 a 18:00).", "ERR_TUR_006");
            }

            // REGLA 4: Superposición de Agenda
            List<Turno> turnosAgendados = turnoRepository.buscarTodos();
            for (Turno t : turnosAgendados) {
                if (t.getOdontologo().getId().equals(turno.getOdontologo().getId()) && t.getFecha().equals(turno.getFecha())) {
                    long diferenciaMinutos = java.time.Duration.between(t.getHora(), turno.getHora()).abs().toMinutes();
                    if (diferenciaMinutos < 30) {
                        throw new TurnoYaReservadoException("El odontólogo ya posee un turno a las " + t.getHora() + ". Reclame al menos 30 min de diferencia.");
                    }
                }
            }

            // REGLA 5: Cálculo de Facturación
            Paciente pacienteReal = pacienteExistente.get();
            Odontologo odontologoReal = odontologoExistente.get();

            if (pacienteReal instanceof PacienteParticular) {
                PacienteParticular pP = (PacienteParticular) pacienteReal;
                double costoFinal = pP.getTarifaBase();

                if (odontologoReal instanceof OdOrtodoncia) {
                    costoFinal += ((OdOrtodoncia) odontologoReal).getRecargoEspecialidad();
                } else if (odontologoReal instanceof OdEndodoncia) {
                    costoFinal += ((OdEndodoncia) odontologoReal).getRecargoEspecialidad();
                } else if (odontologoReal instanceof OdExtraccion) {
                    costoFinal += ((OdExtraccion) odontologoReal).getRecargoEspecialidad();
                }
                turno.setMontoFacturacion(costoFinal);
            } else if (pacienteReal instanceof PacienteObraSocial) {
                turno.setMontoFacturacion(0.0);
            }

            turno.setPaciente(pacienteReal);
            turno.setOdontologo(odontologoReal);
            turno.setEstado(EstadoTurno.PENDIENTE);

            System.out.println("Turno validado exitosamente.");
            return turnoRepository.guardar(turno);

        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error crítico al agendar el turno: " + e.getMessage(), "ERR_GEN_011");
        } finally {
            System.out.println("[Auditoría] Finalizó el procesamiento de asignación de turno.");
        }
    }

    @Override
    public Optional<Turno> buscarPorId(Long id) {
        try {
            return turnoRepository.buscarPorId(id);
        } catch (Exception e) {
            throw new ClinicaException("Error al buscar el turno: " + e.getMessage(), "ERR_GEN_012");
        }
    }

    @Override
    public void eliminarPorId(Long id) {
        try {
            Optional<Turno> existente = turnoRepository.buscarPorId(id);
            if (existente.isEmpty()) {
                throw new ClinicaException("No existe el turno con ID: " + id, "ERR_TUR_001");
            }
            turnoRepository.eliminar(id);
            System.out.println("Turno cancelado correctamente.");
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al cancelar el turno: " + e.getMessage(), "ERR_GEN_013");
        }
    }

    @Override
    public Turno actualizar(Turno turno) {
        try {
            if (turno.getId() == null) {
                throw new DatoInvalidoException("ID Turno", "Para actualizar, el turno debe tener un ID asignado.");
            }
            Turno modificado = turnoRepository.actualizar(turno);
            if (modificado == null) {
                throw new ClinicaException("No se encontró el turno para actualizar.", "ERR_TUR_007");
            }
            return modificado;
        } catch (ClinicaException e) {
            throw e;
        } catch (Exception e) {
            throw new ClinicaException("Error al actualizar el turno: " + e.getMessage(), "ERR_GEN_014");
        }
    }

    @Override
    public List<Turno> listarTodos() {
        try {
            return turnoRepository.buscarTodos();
        } catch (Exception e) {
            throw new ClinicaException("Error al recuperar los turnos: " + e.getMessage(), "ERR_GEN_015");
        }
    }
}