package pe.edu.cibertec.tareas_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProyectoService proyectoService;


    // 3  PREGUNTA
    @Test
    void listarTodos() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        Proyecto proyecto = new Proyecto(
                1L,
                "Sistema Contable",
                "Proyecto principal del área financiera",
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2025, 9, 15),
                usuario,
                true
        );

        when(proyectoRepository.findAll()).thenReturn(Collections.singletonList(proyecto));

        var lista = proyectoService.listarTodos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(proyectoRepository, times(1)).findAll();
    }



    @Test
    void buscarPorId_Exitoso() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        Proyecto proyecto = new Proyecto(
                1L,
                "App de Reportes",
                "Generación de reportes PDF",
                LocalDate.of(2024, 2, 10),
                LocalDate.of(2025, 7, 20),
                usuario,
                true
        );

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        var resultado = proyectoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("App de Reportes", resultado.get().getNombre());
    }



    @Test
    void crear_Exitoso() {
        Usuario usuario = new Usuario(1L, "julio", "julio@gmail.com", "ADMIN", true);

        Proyecto nuevo = new Proyecto(
                null,
                "Panel Administrativo",
                "Dashboard con métricas",
                LocalDate.of(2024, 5, 1),
                LocalDate.of(2025, 12, 31),
                usuario,
                true
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        when(proyectoRepository.save(any(Proyecto.class))).thenAnswer(i -> {
            Proyecto p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        var creado = proyectoService.crear(nuevo);

        assertNotNull(creado);
        assertEquals(1L, creado.getId());
        assertEquals("Panel Administrativo", creado.getNombre());
    }



    @Test
    void crear_FechaInvalida() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        Proyecto proyecto = new Proyecto(
                null,
                "Proyecto con Error",
                "Fechas incorrectas",
                LocalDate.of(2024, 12, 10),
                LocalDate.of(2025, 6, 5),
                usuario,
                true
        );

        assertThrows(RuntimeException.class, () -> proyectoService.crear(proyecto));
    }
}
