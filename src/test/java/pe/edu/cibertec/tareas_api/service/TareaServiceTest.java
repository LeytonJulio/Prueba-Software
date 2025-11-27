package pe.edu.cibertec.tareas_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Tarea;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.TareaRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private TareaService tareaService;


    // 4 PREGUNTA
    @Test
    void listarTodos() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        Proyecto proyecto = new Proyecto(
                1L,
                "Sistema de Pedidos",
                "Módulo para gestionar órdenes",
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                usuario,
                true
        );

        Tarea tarea = new Tarea(
                1L,
                "Configurar base de datos",
                "Diseñar y crear tablas iniciales",
                "EN_PROGRESO",
                "MEDIA",
                proyecto,
                true
        );

        when(tareaRepository.findAll()).thenReturn(Collections.singletonList(tarea));

        var lista = tareaService.listarTodos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(tareaRepository, times(1)).findAll();
    }



    @Test
    void crear_Exitoso() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        Proyecto proyecto = new Proyecto(
                1L,
                "App Inventario",
                "Control de productos y stock",
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                usuario,
                true
        );

        Tarea nueva = new Tarea(
                null,
                "Crear endpoints REST",
                "Implementar CRUD de productos",
                "PENDIENTE",
                "ALTA",
                proyecto,
                true
        );

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> {
            Tarea t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        var creada = tareaService.crear(nueva);

        assertNotNull(creada);
        assertEquals(1L, creada.getId());
        assertEquals("Crear endpoints REST", creada.getTitulo());
    }



    @Test
    void crear_EstadoInvalido() {
        Tarea tarea = new Tarea(
                null,
                "Tarea con error",
                "El estado no es válido para la lógica del negocio",
                "NO_EXISTE",
                "MEDIA",
                null,
                true
        );

        assertThrows(RuntimeException.class, () -> tareaService.crear(tarea));
    }
}