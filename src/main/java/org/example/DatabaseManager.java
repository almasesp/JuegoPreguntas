package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestión de la conexión JDBC y operaciones con la base de datos juego_test_dgt.
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost/juego_test_dbt";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Inicializa la BD y asegura que existan usuarios admin y user.
     */
    public static void initDatabase() {
        try (Connection conn = getConnection()) {
            ensureDefaultUsers(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    private static void ensureDefaultUsers(Connection conn) throws SQLException {
        Object[][] usuarios = {
                {"admin@admin.com", "Administrador"},
                {"user@user.com", "Usuario"},
                {"demo@correo.com", "Usuario Demo"},
                {"alba@correo.com", "Alba García"},
                {"carlos@correo.com", "Carlos Ruiz"},
                {"maria@correo.com", "María López"},
                {"juan@correo.com", "Juan Pérez"},
                {"laura@correo.com", "Laura Martínez"}
        };
        for (Object[] u : usuarios) {
            String email = (String) u[0];
            String nombre = (String) u[1];
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT usuario_id FROM usuarios WHERE email = ?")) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO usuarios (nombre, email, hash_contrasena) VALUES (?, ?, ?)")) {
                        ins.setString(1, nombre);
                        ins.setString(2, email);
                        ins.setString(3, "1234");
                        ins.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Crea un nuevo usuario en la base de datos. Lanza SQLException si el email ya existe.
     */
    public static void crearUsuario(String nombre, String email, String password) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, hash_contrasena) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre != null ? nombre.trim() : "");
            ps.setString(2, email != null ? email.trim().toLowerCase() : "");
            ps.setString(3, password != null ? password : "1234");
            ps.executeUpdate();
        }
    }

    /**
     * Resultado de autenticación.
     */
    public static class UsuarioInfo {
        public final int usuarioId;
        public final String nombre;
        public final boolean esAdmin;

        UsuarioInfo(int usuarioId, String nombre, boolean esAdmin) {
            this.usuarioId = usuarioId;
            this.nombre = nombre;
            this.esAdmin = esAdmin;
        }
    }

    /**
     * Autentica usuario por email o alias (admin, user, demo) y contraseña.
     */
    public static UsuarioInfo autenticarUsuario(String login, String password) {
        String email = resolveEmail(login);
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT usuario_id, nombre, hash_contrasena FROM usuarios WHERE email = ?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("hash_contrasena");
                if (hash == null || hash.equals(password)) {
                    int id = rs.getInt("usuario_id");
                    String nombre = rs.getString("nombre");
                    if (nombre == null) nombre = email;
                    boolean admin = "admin@admin.com".equalsIgnoreCase(email);
                    return new UsuarioInfo(id, nombre, admin);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar: " + e.getMessage(), e);
        }
        return null;
    }

    private static String resolveEmail(String login) {
        if (login == null) return "";
        String s = login.trim().toLowerCase();
        if (s.contains("@")) return s;
        if (s.equals("admin")) return "admin@admin.com";
        if (s.equals("user")) return "user@user.com";
        if (s.equals("demo")) return "demo@correo.com";
        return s + "@correo.com";
    }

    /**
     * Obtiene el permiso_id a partir del código (AM, A1, B, etc.).
     */
    public static int getPermisoIdByCodigo(String codigo) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT permiso_id FROM permisos WHERE codigo = ?")) {
            ps.setString(1, codigo.toUpperCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("permiso_id");
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener permiso: " + e.getMessage(), e);
        }
        throw new IllegalArgumentException("Permiso no encontrado: " + codigo);
    }

    /**
     * Pregunta con opciones desde la BD.
     */
    public static class PreguntaDB {
        public final int preguntaId;
        public final String enunciado;
        public final List<OpcionDB> opciones;
        public final String tipoPrueba;
        public final String dificultad;

        PreguntaDB(int preguntaId, String enunciado, List<OpcionDB> opciones, String tipoPrueba, String dificultad) {
            this.preguntaId = preguntaId;
            this.enunciado = enunciado;
            this.opciones = Collections.unmodifiableList(new ArrayList<>(opciones));
            this.tipoPrueba = tipoPrueba;
            this.dificultad = dificultad;
        }
    }

    public static class OpcionDB {
        public final int opcionId;
        public final String texto;
        public final boolean esCorrecta;

        OpcionDB(int opcionId, String texto, boolean esCorrecta) {
            this.opcionId = opcionId;
            this.texto = texto;
            this.esCorrecta = esCorrecta;
        }
    }

    /**
     * Carga preguntas para un permiso desde la BD, con sus opciones.
     * Usa preguntas_permisos para filtrar por permiso y limita a @limit preguntas.
     */
    public static List<PreguntaDB> getPreguntasPorPermiso(String codigoPermiso, int limit) {
        List<PreguntaDB> lista = new ArrayList<>();
        int permisoId = getPermisoIdByCodigo(codigoPermiso);
        String sql = "SELECT p.pregunta_id, p.enunciado, p.tipo_prueba, p.dificultad " +
                "FROM preguntas p " +
                "INNER JOIN preguntas_permisos pp ON p.pregunta_id = pp.pregunta_id " +
                "WHERE pp.permiso_id = ? " +
                "ORDER BY RAND() " +
                "LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, permisoId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int preguntaId = rs.getInt("pregunta_id");
                String enunciado = rs.getString("enunciado");
                String tipoPrueba = rs.getString("tipo_prueba");
                String dificultad = rs.getString("dificultad");
                List<OpcionDB> opciones = getOpciones(conn, preguntaId);
                lista.add(new PreguntaDB(preguntaId, enunciado, opciones, tipoPrueba, dificultad));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar preguntas: " + e.getMessage(), e);
        }
        return lista;
    }

    private static List<OpcionDB> getOpciones(Connection conn, int preguntaId) throws SQLException {
        List<OpcionDB> opts = new ArrayList<>();
        String sql = "SELECT opcion_id, texto, es_correcta FROM opciones WHERE pregunta_id = ? ORDER BY opcion_id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preguntaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                opts.add(new OpcionDB(
                        rs.getInt("opcion_id"),
                        rs.getString("texto"),
                        rs.getBoolean("es_correcta")
                ));
            }
        }
        return opts;
    }

    /**
     * Crea un intento y devuelve el intento_id.
     */
    public static int crearIntento(int usuarioId, int permisoId, String modalidadTeorica, int totalPreguntas) {
        String sql = "INSERT INTO intentos (usuario_id, permiso_id, modalidad_teorica, total_preguntas) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, permisoId);
            ps.setString(3, modalidadTeorica);
            ps.setInt(4, totalPreguntas);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear intento: " + e.getMessage(), e);
        }
        throw new RuntimeException("No se pudo obtener el ID del intento");
    }

    /**
     * Registra una respuesta del usuario en el intento.
     */
    public static void registrarRespuesta(int intentoId, int preguntaId, int opcionId, boolean esCorrecta, String dificultad) {
        String sql = "INSERT INTO respuestas (intento_id, pregunta_id, opcion_id, es_correcta, dificultad_en_el_momento) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, intentoId);
            ps.setInt(2, preguntaId);
            ps.setInt(3, opcionId);
            ps.setBoolean(4, esCorrecta);
            ps.setString(5, dificultad);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar respuesta: " + e.getMessage(), e);
        }
    }

    /**
     * Finaliza un intento actualizando aciertos, fallos y aprobado.
     */
    public static void finalizarIntento(int intentoId, int aciertos, int fallos, boolean aprobado) {
        String sql = "UPDATE intentos SET fecha_fin = NOW(), aciertos = ?, fallos = ?, aprobado = ? WHERE intento_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aciertos);
            ps.setInt(2, fallos);
            ps.setBoolean(3, aprobado);
            ps.setInt(4, intentoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al finalizar intento: " + e.getMessage(), e);
        }
    }

    /**
     * Devuelve [totalTests, aprobados, suspendidos] para un usuario.
     */
    public static int[] getEstadisticasUsuario(int usuarioId) {
        String sql = "SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN aprobado = 1 THEN 1 ELSE 0 END) as aprobados, " +
                "SUM(CASE WHEN aprobado = 0 THEN 1 ELSE 0 END) as suspendidos " +
                "FROM intentos WHERE usuario_id = ? AND fecha_fin IS NOT NULL";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int aprob = rs.getInt("aprobados");
                int susp = rs.getInt("suspendidos");
                return new int[]{total, aprob, susp};
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estadísticas: " + e.getMessage(), e);
        }
        return new int[]{0, 0, 0};
    }

    /**
     * Historial de un intento para mostrar en dashboard.
     */
    public static class IntentoInfo {
        public final String fecha;
        public final boolean aprobado;
        public final int aciertos;
        public final int total;

        IntentoInfo(String fecha, boolean aprobado, int aciertos, int total) {
            this.fecha = fecha;
            this.aprobado = aprobado;
            this.aciertos = aciertos;
            this.total = total;
        }
    }

    /**
     * Historial de intentos de un usuario (con fecha).
     */
    public static List<IntentoInfo> getHistorialIntentosUsuario(int usuarioId) {
        List<IntentoInfo> lista = new ArrayList<>();
        String sql = "SELECT fecha_fin, aprobado, aciertos, total_preguntas FROM intentos " +
                "WHERE usuario_id = ? AND fecha_fin IS NOT NULL ORDER BY fecha_fin DESC LIMIT 20";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha_fin");
                String fecha = ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "-";
                lista.add(new IntentoInfo(
                        fecha,
                        Boolean.TRUE.equals(rs.getObject("aprobado")),
                        rs.getInt("aciertos"),
                        rs.getInt("total_preguntas")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener historial: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Estadísticas de un usuario para listado.
     */
    public static class UsuarioEstadisticas {
        public final int usuarioId;
        public final String nombre;
        public final String email;
        public final int aprobados;
        public final int suspendidos;
        public final int total;
        public final double pctAprobados;
        public final double pctSuspendidos;

        UsuarioEstadisticas(int usuarioId, String nombre, String email, int aprobados, int suspendidos) {
            this.usuarioId = usuarioId;
            this.nombre = nombre != null ? nombre : email;
            this.email = email;
            this.aprobados = aprobados;
            this.suspendidos = suspendidos;
            this.total = aprobados + suspendidos;
            this.pctAprobados = this.total > 0 ? (aprobados * 100.0 / this.total) : 0;
            this.pctSuspendidos = this.total > 0 ? (suspendidos * 100.0 / this.total) : 0;
        }
    }

    /**
     * Lista de todos los usuarios con sus estadísticas de tests.
     */
    public static List<UsuarioEstadisticas> getListaUsuariosConEstadisticas() {
        List<UsuarioEstadisticas> lista = new ArrayList<>();
        String sql = "SELECT u.usuario_id, u.nombre, u.email, " +
                "SUM(CASE WHEN i.aprobado = 1 THEN 1 ELSE 0 END) as aprobados, " +
                "SUM(CASE WHEN i.aprobado = 0 THEN 1 ELSE 0 END) as suspendidos " +
                "FROM usuarios u " +
                "LEFT JOIN intentos i ON u.usuario_id = i.usuario_id AND i.fecha_fin IS NOT NULL " +
                "GROUP BY u.usuario_id, u.nombre, u.email";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new UsuarioEstadisticas(
                        rs.getInt("usuario_id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getInt("aprobados"),
                        rs.getInt("suspendidos")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage(), e);
        }
        return lista;
    }
}
