package com.pastodeporte.sistema.config;

import com.pastodeporte.sistema.model.OpcionMenu;
import com.pastodeporte.sistema.model.Usuario;
import com.pastodeporte.sistema.model.enums.RolUsuario;
import com.pastodeporte.sistema.repository.OpcionMenuRepository;
import com.pastodeporte.sistema.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos que se ejecuta al arrancar la aplicación.
 *
 * <p>Realiza dos tareas de inicialización en secuencia:</p>
 * <ol>
 *   <li>Crea o migra el usuario administrador del sistema.</li>
 *   <li>Inicializa el árbol de menú de navegación por defecto.</li>
 * </ol>
 *
 * <p><b>Pilar POO — ENCAPSULAMIENTO:</b> cada tarea de inicialización está
 * en su propio método privado, ocultando los detalles de implementación.</p>
 * <p><b>Pilar POO — MODULARIDAD:</b> separación clara de responsabilidades;
 * usuario y menú se inicializan de forma independiente.</p>
 * <p><b>Pilar POO — POLIMORFISMO:</b> implementa {@link CommandLineRunner}
 * sobreescribiendo el método {@code run}.</p>
 * <p><b>Pilar POO — OCULTAMIENTO:</b> la contraseña se cifra con BCrypt
 * antes de persistirse; nunca se almacena en texto plano.</p>
 *
 * @author Sistema Pasto Deporte — UCC Pasto
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository  usuarioRepository;
    private final PasswordEncoder    passwordEncoder;
    private final OpcionMenuRepository opcionMenuRepository;

    /**
     * Método de entrada invocado automáticamente por Spring Boot al arrancar.
     * Delega en los métodos privados de inicialización.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    @Override
    public void run(String... args) {
        inicializarUsuarioAdmin();
        inicializarMenuPorDefecto();
    }

    /**
     * Crea o migra el usuario administrador del sistema.
     *
     * <p>Si existe un usuario con username {@code "admin"} (versión anterior),
     * lo migra a las nuevas credenciales. Si no existe ningún usuario admin,
     * lo crea desde cero con las credenciales por defecto.</p>
     *
     * <p><b>Pilar POO — OCULTAMIENTO:</b> la contraseña se cifra con BCrypt;
     * el texto plano nunca se persiste en la base de datos.</p>
     */
    private void inicializarUsuarioAdmin() {
        usuarioRepository.findByUsernameAndEliminadoFalse("admin").ifPresent(admin -> {
            admin.setUsername("Erika1887");
            admin.setPassword(passwordEncoder.encode("Erika1887"));
            admin.setNombre("Administradora Pasto Deporte");
            usuarioRepository.save(admin);
            log.info(">>> Usuario admin migrado: username=Erika1887");
        });

        if (!usuarioRepository.existsByUsername("Erika1887") && !usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("Erika1887");
            admin.setPassword(passwordEncoder.encode("Erika1887"));
            admin.setNombre("Administradora Pasto Deporte");
            admin.setEmail("admin@pastodeporte.gov.co");
            admin.setRol(RolUsuario.SUPERADMIN);
            usuarioRepository.save(admin);
            log.info(">>> Usuario creado: username=Erika1887");
        } else {
            log.info(">>> Usuario Erika1887 ya existe en la base de datos");
        }
    }

    /**
     * Inicializa el árbol de menú de navegación por defecto si no existe ninguno.
     *
     * <p>Crea los ítems raíz del menú (Dashboard y Gestión de Clubes).
     * Estos ítems son nodos raíz (sin padre), ordenados secuencialmente.
     * La estructura puede extenderse añadiendo ítems con referencia a un padre,
     * lo que permite construir menús jerárquicos de cualquier profundidad.</p>
     *
     * <p><b>Pilar POO — ABSTRACCIÓN:</b> el menú se define como una estructura
     * de árbol independiente de la implementación del frontend; el componente
     * {@code MenuItemComponent} lo renderiza recursivamente.</p>
     */
    private void inicializarMenuPorDefecto() {
        if (opcionMenuRepository.count() > 0) {
            log.info(">>> Menú de navegación ya inicializado");
            return;
        }

        OpcionMenu dashboard = new OpcionMenu();
        dashboard.setNombre("Dashboard");
        dashboard.setRuta("/admin/dashboard");
        dashboard.setIcono("dashboard");
        dashboard.setOrden(1);
        opcionMenuRepository.save(dashboard);

        OpcionMenu clubes = new OpcionMenu();
        clubes.setNombre("Gestión de Clubes");
        clubes.setRuta("/admin/clubes");
        clubes.setIcono("groups");
        clubes.setOrden(2);
        opcionMenuRepository.save(clubes);

        log.info(">>> Menú por defecto inicializado: Dashboard, Gestión de Clubes");
    }
}
