package pe.edu.cibertec.tareas_api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    // 2 PREGUNTA

    @Test
    void listarTodos() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        var lista = usuarioService.listarTodos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_Exitoso() {
        Usuario usuario = new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        var resultado = usuarioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Julio", resultado.get().getNombre());
    }

    @Test
    void crear_Exitoso() {
        Usuario usuario = new Usuario(null, "Julio", "julio@gmail.com", "ADMIN", true);

        when(usuarioRepository.existsByEmail("julio@gmail.com")).thenReturn(false);

        when(usuarioRepository.save(usuario))
                .thenReturn(new Usuario(1L, "Julio", "julio@gmail.com", "ADMIN", true));

        var creado = usuarioService.crear(usuario);

        assertNotNull(creado);
        assertEquals(1L, creado.getId());
    }
}
