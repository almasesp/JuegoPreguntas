package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseManager.initDatabase();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos:\n" + e.getMessage() +
                            "\n\nAsegúrate de que MySQL está activo y la BD juego_test_dgt existe.");
            return;
        }
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }

    // ============================================================
    //                         LOGIN WINDOW
    // ============================================================
    static class LoginWindow extends JFrame {

        private JTextField userField;
        private JPasswordField passField;
        private float hue = 0f;

        public LoginWindow() {

            setTitle("Autoescuela Rápida — Acceso");
            setSize(900, 550);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel root = new JPanel(new BorderLayout());
            setContentPane(root);

            JPanel animatedBg = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    hue += 0.001f;
                    if (hue > 1) hue = 0;

                    Color c1 = Color.getHSBColor(hue, 0.4f, 1f);
                    Color c2 = Color.getHSBColor(hue + 0.1f, 0.4f, 0.9f);

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            animatedBg.setLayout(new GridBagLayout());
            root.add(animatedBg, BorderLayout.CENTER);

            new Timer(10, e -> animatedBg.repaint()).start();

            JPanel card = new JPanel(new GridBagLayout());
            card.setPreferredSize(new Dimension(420, 320));
            card.setBackground(new Color(15, 23, 42, 230));
            card.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 2));

            animatedBg.add(card);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel loginTitle = new JLabel("Panel de acceso — Autoescuela Rápida", SwingConstants.CENTER);
            loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            loginTitle.setForeground(new Color(248, 250, 252));
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 0;
            card.add(loginTitle, gbc);

            gbc.gridwidth = 1;

            gbc.gridy = 1;
            gbc.gridx = 0;
            JLabel userLabel = new JLabel("Usuario");
            userLabel.setForeground(new Color(148, 163, 184));
            card.add(userLabel, gbc);

            gbc.gridx = 1;
            userField = new JTextField(15);
            userField.setBackground(new Color(15, 23, 42));
            userField.setForeground(new Color(248, 250, 252));
            userField.setCaretColor(Color.WHITE);
            userField.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
            card.add(userField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel passLabel = new JLabel("Contraseña");
            passLabel.setForeground(new Color(148, 163, 184));
            card.add(passLabel, gbc);

            gbc.gridx = 1;
            passField = new JPasswordField(15);
            passField.setBackground(new Color(15, 23, 42));
            passField.setForeground(new Color(248, 250, 252));
            passField.setCaretColor(Color.WHITE);
            passField.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85)));
            card.add(passField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;

            JButton loginButton = new JButton("Entrar al panel");
            loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            loginButton.setBackground(new Color(56, 189, 248));
            loginButton.setForeground(new Color(15, 23, 42));
            loginButton.setFocusPainted(false);
            loginButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            card.add(loginButton, gbc);

            loginButton.addActionListener(e -> validarLogin());
        }

        private void validarLogin() {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            DatabaseManager.UsuarioInfo info = DatabaseManager.autenticarUsuario(user, pass);
            if (info != null) {
                currentUsuarioId = info.usuarioId;
                currentUserName = info.nombre;
                if (info.esAdmin) {
                    new Dashboard().setVisible(true);
                } else {
                    new LicenseSelectorWindow().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
            }
        }
    }

    // ============================================================
    //                         DASHBOARD
    // ============================================================
    static class Dashboard extends JFrame {

        public Dashboard() {
            setTitle("Autoescuela Rápida — Dashboard");
            setSize(950, 650);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel root = new JPanel(new BorderLayout(16, 16));
            root.setBackground(new Color(15, 23, 42));
            root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            setContentPane(root);

            String nombre = (currentUserName != null && !currentUserName.isEmpty())
                    ? currentUserName : "Usuario";

            // Cabecera
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(30, 64, 175));
            header.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(12, 16, 12, 16),
                    BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(56, 189, 248))
            ));

            JLabel title = new JLabel("Panel de control — " + nombre);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Segoe UI", Font.BOLD, 22));
            header.add(title, BorderLayout.WEST);

            JLabel subtitle = new JLabel("Autoescuela Rápida");
            subtitle.setForeground(new Color(191, 219, 254));
            subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            header.add(subtitle, BorderLayout.EAST);

            root.add(header, BorderLayout.NORTH);

            // Contenido principal: dos columnas
            JPanel content = new JPanel(new GridLayout(1, 2, 16, 0));
            content.setOpaque(false);

            // Columna izquierda: mis estadísticas + historial
            JPanel leftCol = new JPanel(new BorderLayout(0, 12));
            leftCol.setOpaque(false);

            int[] stats = currentUsuarioId > 0
                    ? DatabaseManager.getEstadisticasUsuario(currentUsuarioId)
                    : new int[]{0, 0, 0};
            int totalTests = stats[0];
            int totalAprobados = stats[1];
            int totalSuspendidos = stats[2];
            double aprobadosPct = totalTests > 0 ? (totalAprobados * 100.0) / totalTests : 0;
            double suspendidosPct = totalTests > 0 ? (totalSuspendidos * 100.0) / totalTests : 0;

            // Tarjeta de medias
            JPanel cardStats = crearCard("Mis estadísticas");
            JPanel statsGrid = new JPanel(new GridLayout(2, 1, 0, 12));
            statsGrid.setOpaque(false);

            JLabel lblAprob = new JLabel(String.format("Aprobados: %.0f%% (%d de %d)", aprobadosPct, totalAprobados, totalTests));
            lblAprob.setForeground(new Color(110, 231, 183));
            lblAprob.setFont(new Font("Segoe UI", Font.BOLD, 16));
            statsGrid.add(lblAprob);

            JLabel lblSusp = new JLabel(String.format("Suspendidos: %.0f%% (%d de %d)", suspendidosPct, totalSuspendidos, totalTests));
            lblSusp.setForeground(new Color(248, 113, 113));
            lblSusp.setFont(new Font("Segoe UI", Font.BOLD, 16));
            statsGrid.add(lblSusp);

            cardStats.add(statsGrid, BorderLayout.CENTER);
            leftCol.add(cardStats, BorderLayout.NORTH);

            // Historial de intentos con fechas
            JPanel cardHist = crearCard("Últimos tests realizados");
            DefaultListModel<String> histModel = new DefaultListModel<>();
            java.util.List<DatabaseManager.IntentoInfo> historial = currentUsuarioId > 0
                    ? DatabaseManager.getHistorialIntentosUsuario(currentUsuarioId)
                    : new java.util.ArrayList<>();
            for (DatabaseManager.IntentoInfo i : historial) {
                String estado = i.aprobado ? "APROBADO" : "SUSPENDIDO";
                histModel.addElement(String.format("%s — %s — %d/%d preguntas", i.fecha, estado, i.aciertos, i.total));
            }
            if (historial.isEmpty()) {
                histModel.addElement("Aún no hay tests realizados");
            }
            JList<String> histList = new JList<>(histModel);
            histList.setBackground(new Color(15, 23, 42));
            histList.setForeground(new Color(226, 232, 240));
            histList.setSelectionBackground(new Color(30, 64, 175));
            histList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cardHist.add(new JScrollPane(histList), BorderLayout.CENTER);
            leftCol.add(cardHist, BorderLayout.CENTER);

            content.add(leftCol);

            // Columna derecha: listado de usuarios
            JPanel rightCol = new JPanel(new BorderLayout(0, 12));
            rightCol.setOpaque(false);

            JPanel cardUsu = new JPanel(new BorderLayout(8, 8));
            cardUsu.setBackground(new Color(30, 41, 59));
            cardUsu.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(56, 189, 248), 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            JPanel cardUsuTop = new JPanel(new BorderLayout());
            cardUsuTop.setOpaque(false);
            JLabel lblUsu = new JLabel("Usuarios — Tests aprobados / suspendidos");
            lblUsu.setForeground(new Color(148, 163, 184));
            lblUsu.setFont(new Font("Segoe UI", Font.BOLD, 14));
            cardUsuTop.add(lblUsu, BorderLayout.WEST);
            String[] cols = {"Usuario", "Aprobados", "Suspendidos", "% Aprob", "% Susp"};
            DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
            actualizarTablaUsuarios(tableModel);
            JTable tablaUsuarios = new JTable(tableModel);
            tablaUsuarios.setBackground(new Color(15, 23, 42));
            tablaUsuarios.setForeground(new Color(226, 232, 240));
            tablaUsuarios.setSelectionBackground(new Color(30, 64, 175));
            tablaUsuarios.setSelectionForeground(Color.WHITE);
            tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tablaUsuarios.getTableHeader().setBackground(new Color(30, 64, 175));
            tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
            tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tablaUsuarios.setGridColor(new Color(51, 65, 85));
            tablaUsuarios.setRowHeight(28);
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
            btnPanel.setOpaque(false);
            JButton btnAdd = new JButton("Añadir usuario");
            btnAdd.setBackground(new Color(56, 189, 248));
            btnAdd.setForeground(new Color(15, 23, 42));
            btnAdd.setFocusPainted(false);
            btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnAdd.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            btnAdd.addActionListener(ev -> {
                String nombreUser = JOptionPane.showInputDialog(Dashboard.this, "Nombre del usuario:", "Nuevo usuario", JOptionPane.QUESTION_MESSAGE);
                if (nombreUser == null || nombreUser.trim().isEmpty()) return;
                String email = JOptionPane.showInputDialog(Dashboard.this, "Email:", "Nuevo usuario", JOptionPane.QUESTION_MESSAGE);
                if (email == null || email.trim().isEmpty()) return;
                String pass = JOptionPane.showInputDialog(Dashboard.this, "Contraseña:", "Nuevo usuario", JOptionPane.QUESTION_MESSAGE);
                if (pass == null) return;
                try {
                    DatabaseManager.crearUsuario(nombreUser.trim(), email.trim(), pass.isEmpty() ? "1234" : pass);
                    JOptionPane.showMessageDialog(Dashboard.this, "Usuario creado correctamente.");
                    actualizarTablaUsuarios(tableModel);
                } catch (java.sql.SQLException ex) {
                    JOptionPane.showMessageDialog(Dashboard.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnPanel.add(btnAdd);
            cardUsuTop.add(btnPanel, BorderLayout.EAST);
            cardUsu.add(cardUsuTop, BorderLayout.NORTH);
            cardUsu.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
            rightCol.add(cardUsu, BorderLayout.CENTER);

            content.add(rightCol);

            root.add(content, BorderLayout.CENTER);
        }

        private JPanel crearCard(String titulo) {
            JPanel card = new JPanel(new BorderLayout(8, 8));
            card.setBackground(new Color(30, 41, 59));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(56, 189, 248), 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            JLabel lbl = new JLabel(titulo);
            lbl.setForeground(new Color(148, 163, 184));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            card.add(lbl, BorderLayout.NORTH);
            return card;
        }

        private void actualizarTablaUsuarios(DefaultTableModel tableModel) {
            tableModel.setRowCount(0);
            for (DatabaseManager.UsuarioEstadisticas u : DatabaseManager.getListaUsuariosConEstadisticas()) {
                tableModel.addRow(new Object[]{
                        u.nombre != null ? u.nombre : u.email,
                        u.aprobados,
                        u.suspendidos,
                        String.format("%.0f%%", u.pctAprobados),
                        String.format("%.0f%%", u.pctSuspendidos)
                });
            }
        }

        private void styleSmallButton(JButton btn) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setBackground(new Color(30, 64, 175));
            btn.setForeground(new Color(248, 250, 252));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        }

        // ================== COMPONENTES PERSONALIZADOS ==================

        // Tarjeta base
        static class CardPanel extends JPanel {
            private final String title;

            CardPanel(String title) {
                this.title = title;
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                Color bg = new Color(15, 23, 42, 230);
                Color border = new Color(56, 189, 248);

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);

                g2.setColor(border);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);

                if (title != null && !title.isEmpty()) {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    g2.setColor(new Color(148, 163, 184));
                    g2.drawString(title, 16, 22);
                }

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        }

        // Círculo de progreso
        static class CircleStatPanel extends CardPanel {

            private final int value;
            private final String subtitle;

            CircleStatPanel(String title, int value, String subtitle) {
                super(title);
                this.value = value;
                this.subtitle = subtitle;
                setPreferredSize(new Dimension(0, 180));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                int size = Math.min(w, h) - 60;
                int x = (w - size) / 2;
                int y = 30;

                g2.setStroke(new BasicStroke(8f));
                g2.setColor(new Color(30, 64, 175));
                g2.drawOval(x, y, size, size);

                g2.setColor(new Color(56, 189, 248));
                int angle = (int) (360 * (value / 100.0));
                g2.drawArc(x, y, size, size, 90, -angle);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                g2.setColor(new Color(248, 250, 252));
                String txt = value + "%";
                FontMetrics fm = g2.getFontMetrics();
                int tx = x + size / 2 - fm.stringWidth(txt) / 2;
                int ty = y + size / 2 + fm.getAscent() / 2 - 4;
                g2.drawString(txt, tx, ty);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.setColor(new Color(148, 163, 184));
                drawMultiline(g2, subtitle, 12, h - 40, w - 24);

                g2.dispose();
            }

            private void drawMultiline(Graphics2D g2, String text, int x, int y, int maxWidth) {
                FontMetrics fm = g2.getFontMetrics();
                String[] words = text.split(" ");
                StringBuilder line = new StringBuilder();
                int yy = y;

                for (String word : words) {
                    String test = line + word + " ";
                    if (fm.stringWidth(test) > maxWidth) {
                        g2.drawString(line.toString(), x, yy);
                        yy += fm.getHeight();
                        line = new StringBuilder(word).append(" ");
                    } else {
                        line.append(word).append(" ");
                    }
                }
                if (!line.isEmpty()) {
                    g2.drawString(line.toString(), x, yy);
                }
            }
        }

        // Barra horizontal base
        static class HorizontalBar extends JPanel {
            protected String label;
            protected int value;

            HorizontalBar(String label, int value) {
                this.label = label;
                this.value = value;
                setOpaque(false);
                setPreferredSize(new Dimension(0, 26));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(148, 163, 184));
                g2.drawString(label, 4, h - 10);

                int barWidth = (int) ((w - 120) * (value / 100.0));
                int bx = 110;
                int by = h / 2 - 6;

                g2.setColor(new Color(30, 64, 175));
                g2.fillRoundRect(bx, by, w - 120, 12, 8, 8);

                g2.setColor(new Color(56, 189, 248));
                g2.fillRoundRect(bx, by, barWidth, 12, 8, 8);

                g2.setColor(new Color(248, 250, 252));
                String v = value + "%";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(v, bx + barWidth - fm.stringWidth(v) - 4, by - 2);

                g2.dispose();
            }
        }

        // Barras horizontales dependientes de licencia
        static class LicenseHorizontalBars extends CardPanel {

            private final Map<String, int[]> data = new HashMap<>();
            private final String[] labels = {
                    "Permiso B (coche)",
                    "Permiso A (moto)",
                    "Permiso C (camión)",
                    "Permiso D (autobús)",
                    "Reciclaje / Refuerzo"
            };
            private HorizontalBar[] bars;

            LicenseHorizontalBars(String title) {
                super(title);
                setLayout(new GridLayout(5, 1, 4, 4));
                initData();
                bars = new HorizontalBar[labels.length];
                int[] base = data.get("Todos");
                for (int i = 0; i < labels.length; i++) {
                    bars[i] = new HorizontalBar(labels[i], base[i]);
                    add(bars[i]);
                }
            }

            private void initData() {
                data.put("Todos", new int[]{80, 35, 20, 10, 15});
                data.put("Permiso B", new int[]{90, 10, 5, 3, 8});
                data.put("Permiso A", new int[]{20, 70, 5, 2, 10});
                data.put("Permiso C", new int[]{10, 5, 80, 15, 20});
                data.put("Permiso D", new int[]{5, 5, 10, 85, 25});
            }

            void setLicenseFilter(String filter) {
                int[] vals = data.getOrDefault(filter, data.get("Todos"));
                for (int i = 0; i < bars.length; i++) {
                    bars[i].value = vals[i];
                }
            }
        }

        // Gráfico de líneas dependiente de licencia
        static class LicenseLineChartPanel extends CardPanel {

            private final Map<String, int[]> data = new HashMap<>();
            private int[] values;
            private final String subtitle;

            LicenseLineChartPanel(String title, String subtitle) {
                super(title);
                this.subtitle = subtitle;
                setPreferredSize(new Dimension(0, 260));
                initData();
                values = data.get("Todos");
            }

            private void initData() {
                data.put("Todos", new int[]{12, 18, 25, 21, 30, 28});
                data.put("Permiso B", new int[]{20, 24, 30, 28, 35, 40});
                data.put("Permiso A", new int[]{5, 8, 10, 12, 15, 18});
                data.put("Permiso C", new int[]{2, 4, 6, 8, 10, 12});
                data.put("Permiso D", new int[]{1, 3, 4, 5, 6, 7});
            }

            void setLicenseFilter(String filter) {
                values = data.getOrDefault(filter, data.get("Todos"));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(148, 163, 184));
                g2.drawString(subtitle, 16, 40);

                int left = 40;
                int right = w - 24;
                int top = 60;
                int bottom = h - 40;

                g2.setColor(new Color(30, 64, 175));
                g2.drawLine(left, bottom, right, bottom);
                g2.drawLine(left, top, left, bottom);

                int max = 40;
                int stepX = (right - left) / (values.length - 1);

                int[] xs = new int[values.length];
                int[] ys = new int[values.length];

                for (int i = 0; i < values.length; i++) {
                    xs[i] = left + i * stepX;
                    ys[i] = bottom - (int) ((values[i] / (double) max) * (bottom - top));
                }

                g2.setColor(new Color(56, 189, 248));
                g2.setStroke(new BasicStroke(2f));
                for (int i = 0; i < values.length - 1; i++) {
                    g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                }

                g2.setColor(new Color(248, 250, 252));
                for (int i = 0; i < values.length; i++) {
                    g2.fillOval(xs[i] - 4, ys[i] - 4, 8, 8);
                }

                g2.dispose();
            }
        }

        // ============================================================
//      CALENDARIO 100% FUNCIONAL CON TAREAS POR DÍA
// ============================================================
        static class CalendarPanel extends CardPanel {

            private YearMonth month;
            private LocalDate today;

            // Mapa de tareas por día
            private final Map<LocalDate, java.util.List<String>> tareas = new HashMap<>();

            // Para detectar clics
            private Rectangle[][] dayCells = new Rectangle[6][7];

            CalendarPanel(String title) {
                super(title);
                this.today = LocalDate.now();
                this.month = YearMonth.from(today);
                setPreferredSize(new Dimension(0, 260));

                // Detectar clics en días
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleClick(e.getX(), e.getY());
                    }
                });

                // Tooltip con tareas
                setToolTipText("");
                addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        updateTooltip(e.getX(), e.getY());
                    }
                });
            }

            // ============================
            //      CLICK EN UN DÍA
            // ============================
            private void handleClick(int x, int y) {
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 7; col++) {
                        Rectangle cell = dayCells[row][col];
                        if (cell != null && cell.contains(x, y)) {

                            int day = getDayFromCell(row, col);
                            if (day == -1) return;

                            LocalDate date = month.atDay(day);

                            String tarea = JOptionPane.showInputDialog(
                                    this,
                                    "Añadir tarea para el " + date + ":",
                                    "Nueva tarea",
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (tarea != null && !tarea.trim().isEmpty()) {
                                boolean add = tareas.computeIfAbsent(date, d -> new ArrayList<>()).add(tarea.trim());
                                repaint();
                            }
                            return;
                        }
                    }
                }
            }

            // ============================
            //      TOOLTIP DINÁMICO
            // ============================
            private void updateTooltip(int x, int y) {
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < 7; col++) {
                        Rectangle cell = dayCells[row][col];
                        if (cell != null && cell.contains(x, y)) {

                            int day = getDayFromCell(row, col);
                            if (day == -1) {
                                setToolTipText(null);
                                return;
                            }

                            LocalDate date = month.atDay(day);
                            java.util.List<String> lista = tareas.get(date);

                            if (lista != null && !lista.isEmpty()) {
                                StringBuilder sb = new StringBuilder("<html><b>Tareas:</b><br>");
                                for (String t : lista) sb.append("• ").append(t).append("<br>");
                                sb.append("</html>");
                                setToolTipText(sb.toString());
                            } else {
                                setToolTipText("Sin tareas");
                            }
                            return;
                        }
                    }
                }
                setToolTipText(null);
            }

            // ============================
            //      OBTENER DÍA SEGÚN CELDA
            // ============================
            private int getDayFromCell(int row, int col) {
                int firstDay = month.atDay(1).getDayOfWeek().getValue(); // 1-7
                int index = row * 7 + col + 1;
                int day = index - firstDay + 1;

                if (day < 1 || day > month.lengthOfMonth()) return -1;
                return day;
            }

            // ============================
            //      DIBUJAR CALENDARIO
            // ============================
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Título del mes
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.setColor(new Color(248, 250, 252));
                String header = month.getMonth().toString() + " " + month.getYear();
                g2.drawString(header, 16, 40);

                // Días de la semana
                String[] days = {"L", "M", "X", "J", "V", "S", "D"};
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                int startX = 16;
                int startY = 60;
                int cellW = (w - 32) / 7;
                int cellH = (h - 80) / 6;

                for (int i = 0; i < days.length; i++) {
                    g2.setColor(new Color(148, 163, 184));
                    g2.drawString(days[i], startX + i * cellW + cellW / 2 - 4, startY);
                }

                // Días del mes
                int firstDay = month.atDay(1).getDayOfWeek().getValue(); // 1-7
                int length = month.lengthOfMonth();
                int day = 1;
                int row = 1;

                for (int r = 0; r < 6; r++) {
                    for (int col = 0; col < 7; col++) {

                        int index = r * 7 + col + 1;
                        int calcDay = index - firstDay + 1;

                        int x = startX + col * cellW;
                        int y = startY + r * cellH + 20;

                        dayCells[r][col] = new Rectangle(x, y - cellH + 8, cellW, cellH);

                        if (calcDay < 1 || calcDay > length) continue;

                        LocalDate date = month.atDay(calcDay);

                        // Fondo si tiene tareas
                        if (tareas.containsKey(date)) {
                            g2.setColor(new Color(56, 189, 248, 80));
                            g2.fillRoundRect(x + 2, y - cellH + 8, cellW - 4, cellH - 4, 8, 8);
                        }

                        // Día actual
                        if (date.equals(today)) {
                            g2.setColor(new Color(56, 189, 248));
                            g2.drawRoundRect(x + 2, y - cellH + 8, cellW - 4, cellH - 4, 8, 8);
                        }

                        // Número del día
                        g2.setColor(new Color(248, 250, 252));
                        g2.drawString(String.valueOf(calcDay), x + cellW / 2 - 6, y + 4);
                    }
                }

                g2.dispose();
            }
        }


        // Agenda de clases editable
        static class AgendaPanel extends CardPanel {

            private final DefaultListModel<String> model = new DefaultListModel<>();
            private final JList<String> list;

            AgendaPanel(String title) {
                super(title);
                setLayout(new BorderLayout(8, 8));

                list = new JList<>(model);
                list.setBackground(new Color(15, 23, 42));
                list.setForeground(new Color(248, 250, 252));
                list.setSelectionBackground(new Color(30, 64, 175));
                list.setSelectionForeground(Color.WHITE);
                list.setFont(new Font("Segoe UI", Font.PLAIN, 12));

                JScrollPane sp = new JScrollPane(list);
                sp.setBorder(null);
                add(sp, BorderLayout.CENTER);

                // Datos iniciales
                model.addElement("08:20  · Clase práctica — Juan Pérez — Vehículo B-12");
                model.addElement("09:15  · Clase teórica — Normas de circulación — Aula 2");
                model.addElement("11:00  · Examen práctico — Marta López — DGT Lleida");
                model.addElement("16:30  · Clase práctica — Carlos Ruiz — Circuito urbano");
            }

            void addClassDialog() {
                String hora = JOptionPane.showInputDialog(this, "Hora (ej: 10:30):", "Nueva clase", JOptionPane.QUESTION_MESSAGE);
                if (hora == null || hora.trim().isEmpty()) return;

                String desc = JOptionPane.showInputDialog(this, "Descripción (alumno, tipo, vehículo...):", "Nueva clase", JOptionPane.QUESTION_MESSAGE);
                if (desc == null || desc.trim().isEmpty()) return;

                model.addElement(hora.trim() + "  · " + desc.trim());
            }

            void removeSelected() {
                int idx = list.getSelectedIndex();
                if (idx >= 0) {
                    model.remove(idx);
                } else {
                    JOptionPane.showMessageDialog(this, "Selecciona una clase para eliminarla.");
                }
            }
        }

        // Tarjeta por año
        static class YearStat extends JPanel {
            private final String year;
            private final int value;

            YearStat(String year, int value) {
                this.year = year;
                this.value = value;
                setOpaque(false);
                setPreferredSize(new Dimension(0, 40));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(15, 23, 42, 220));
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.setColor(new Color(30, 64, 175));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.setColor(new Color(248, 250, 252));
                g2.drawString(year, 12, h / 2 + 5);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(new Color(148, 163, 184));
                String txt = value + " aprobados";
                int tw = g2.getFontMetrics().stringWidth(txt);
                g2.drawString(txt, w - tw - 12, h / 2 + 5);

                g2.dispose();
            }
        }

    }

    // ============================================================
//     SELECTOR DE TIPO DE TEST — ESTILO BOOTSTRAP, ANIMADO Y RESPONSIVE
// ============================================================
    static class LicenseSelectorWindow extends JFrame {

        private float hue = 0f;

        public LicenseSelectorWindow() {
            setTitle("Seleccionar tipo de test — Autoescuela Rápida");
            setSize(520, 500);
            setMinimumSize(new Dimension(380, 380));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel root = new JPanel(new BorderLayout());
            setContentPane(root);

            // Fondo animado (gradiente sutil)
            JPanel bgPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    hue += 0.003f;
                    if (hue > 1) hue = 0;
                    Color c1 = new Color(15, 23, 42);
                    Color c2 = Color.getHSBColor(hue, 0.25f, 0.5f);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            root.add(bgPanel, BorderLayout.CENTER);
            new Timer(40, e -> bgPanel.repaint()).start();

            // Cabecera Bootstrap-style
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));

            JLabel title = new JLabel("Selecciona el permiso a estudiar");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(new Color(248, 250, 252));
            header.add(title, BorderLayout.WEST);

            JLabel subtitle = new JLabel("Elige un tipo de test");
            subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitle.setForeground(new Color(148, 163, 184));
            header.add(subtitle, BorderLayout.SOUTH);

            bgPanel.add(header, BorderLayout.NORTH);

            // Grid responsive (3 columnas, scroll si ventana pequeña)
            JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 12, 12));
            cardsPanel.setOpaque(false);
            cardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 24, 20));
            JScrollPane scroll = new JScrollPane(cardsPanel);
            scroll.setBorder(null);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            String[] licencias = {"AM", "A1", "A2", "A", "B", "C1", "C", "D", "D1"};
            String[] descripciones = {"Ciclomotor", "Moto A1", "Moto A2", "Moto A", "Turismo", "C1", "Camión", "Autobús", "Autobús D1"};

            for (int i = 0; i < licencias.length; i++) {
                final String lic = licencias[i];
                final String desc = descripciones[i];
                JButton cardBtn = crearBotonPermiso(lic, desc);
                cardBtn.addActionListener(e -> abrirCurso(lic));
                cardsPanel.add(cardBtn);
            }

            bgPanel.add(scroll, BorderLayout.CENTER);
        }

        private JButton crearBotonPermiso(String codigo, String desc) {
            return new CardButton(codigo, desc);
        }

        private void abrirCurso(String licencia) {
            new CursoSimulacionWindow(licencia).setVisible(true);
            dispose();
        }

        private static class CardButton extends JButton {
            private boolean hover;
            private float glow;

            CardButton(String codigo, String desc) {
                super("<html><center><b>Permiso " + codigo + "</b><br><small>" + desc + "</small></center></html>");
                hover = false;
                glow = 0f;
                setPreferredSize(new Dimension(130, 80));
                setMinimumSize(new Dimension(100, 70));
                setMaximumSize(new Dimension(200, 120));
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                setForeground(new Color(248, 250, 252));
                setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setVerticalTextPosition(SwingConstants.CENTER);
                setHorizontalTextPosition(SwingConstants.CENTER);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (hover && glow < 1f) glow = Math.min(1f, glow + 0.15f);
                else if (!hover && glow > 0f) glow = Math.max(0f, glow - 0.15f);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                int arc = 12;
                Color border = new Color(56, 189, 248);
                Color bg = new Color(
                        (int) (30 + 26 * glow),
                        (int) (41 + 123 * glow),
                        (int) (59 + 130 * glow)
                );
                g2.setColor(bg);
                g2.fillRoundRect(2, 2, w - 4, h - 4, arc, arc);
                g2.setColor(new Color(border.getRed(), border.getGreen(), border.getBlue(), (int) (100 + 155 * glow)));
                g2.setStroke(new BasicStroke(hover ? 2f : 1f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        }
    }

// ============================================================
//     SIMULACIÓN DE CURSO SEGÚN LICENCIA
// ============================================================
    static class CursoSimulacionWindow extends JFrame {

        private final String licencia;

        public CursoSimulacionWindow(String licencia) {

            this.licencia = licencia;

            setTitle("Curso — Permiso " + licencia);
            setSize(500, 350);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel title = new JLabel("Permiso " + licencia, SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            panel.add(title, BorderLayout.NORTH);

            JTextArea info = new JTextArea(
                    "Bienvenido al curso del permiso " + licencia + ".\n\n" +
                            "Esta es una simulación temporal.\n" +
                            "Aquí podrás acceder a:\n" +
                            "• Temario oficial\n" +
                            "• Tests de examen\n" +
                            "• Estadísticas de progreso\n\n" +
                            "Próximamente se añadirá contenido real."
            );
            info.setEditable(false);
            info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            info.setBackground(panel.getBackground());
            panel.add(info, BorderLayout.CENTER);

            JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 10));

            JButton teoriaBtn = new JButton("Teoría");
            JButton testBtn = new JButton("Tests");
            JButton volverBtn = new JButton("Volver");

            // --- TEORÍA (simulación) ---
            teoriaBtn.addActionListener(e ->
                    JOptionPane.showMessageDialog(this,
                            "Simulación: abrir teoría del permiso " + licencia));

            // --- TESTS (funcional) ---
            testBtn.addActionListener(e -> {
                new TestWindow(licencia).setVisible(true);
                dispose();
            });

            // --- VOLVER ---
            volverBtn.addActionListener(e -> {
                new LicenseSelectorWindow().setVisible(true);
                dispose();
            });

            buttons.add(teoriaBtn);
            buttons.add(testBtn);
            buttons.add(volverBtn);

            panel.add(buttons, BorderLayout.SOUTH);

            add(panel);
        }
    }

    // ============================================================
//     MODELO DE PREGUNTAS Y BANCO EN MEMORIA
// ============================================================
    static class Pregunta {
        String enunciado;
        String[] opciones; // 4 opciones
        int correcta;      // índice 0-3
        String tipo;       // "comun" o "especifica"

        Pregunta(String enunciado, String[] opciones, int correcta, String tipo) {
            this.enunciado = enunciado;
            this.opciones = opciones;
            this.correcta = correcta;
            this.tipo = tipo;
        }
    }

    // Usuario actual logueado (desde BD)
    static int currentUsuarioId = -1;
    static String currentUserName = null;

    // ============================================================
    //     VENTANA DE TESTS — USA PREGUNTAS Y OPCIONES DESDE LA BD
    // ============================================================
    static class TestWindow extends JFrame {

        private java.util.List<DatabaseManager.PreguntaDB> preguntas;
        private int index = 0;
        private int aciertos = 0;
        private final String licencia;
        private int intentoId = -1;
        private int permisoId = -1;

        private JLabel lblPregunta;
        private JPanel opcionesPanel;
        private JRadioButton[] radios;
        private ButtonGroup grupo;

        public TestWindow(String licencia) {
            this.licencia = licencia;

            preguntas = DatabaseManager.getPreguntasPorPermiso(licencia, 30);
            if (preguntas.isEmpty()) {
                setTitle("Test — Permiso " + licencia);
                setSize(400, 150);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel emptyContent = new JPanel(new BorderLayout());
                emptyContent.add(new JLabel("No hay preguntas para el permiso " + licencia + "."), BorderLayout.CENTER);
                JButton volver = new JButton("Volver");
                volver.addActionListener(ev -> {
                    new LicenseSelectorWindow().setVisible(true);
                    dispose();
                });
                emptyContent.add(volver, BorderLayout.SOUTH);
                add(emptyContent);
                return;
            }

            permisoId = DatabaseManager.getPermisoIdByCodigo(licencia);
            intentoId = DatabaseManager.crearIntento(currentUsuarioId, permisoId, "comun_y_especifica", preguntas.size());

            setTitle("Test — Permiso " + licencia);
            setSize(800, 450);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(new Color(15, 23, 42));
            root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            add(root);

            // Cabecera estilo bootstrap
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(30, 64, 175));
            header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

            JLabel title = new JLabel("Test teórico — Permiso " + licencia);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            header.add(title, BorderLayout.WEST);

            JLabel progreso = new JLabel();
            progreso.setForeground(new Color(191, 219, 254));
            progreso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            header.add(progreso, BorderLayout.EAST);

            root.add(header, BorderLayout.NORTH);

            // Card central
            JPanel card = new JPanel(new BorderLayout(10, 16));
            card.setBackground(new Color(15, 23, 42));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(56, 189, 248)),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)
            ));

            lblPregunta = new JLabel("", SwingConstants.LEFT);
            lblPregunta.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblPregunta.setForeground(new Color(226, 232, 240));
            card.add(lblPregunta, BorderLayout.NORTH);

            opcionesPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            opcionesPanel.setOpaque(false);
            grupo = new ButtonGroup();
            radios = new JRadioButton[4];
            card.add(opcionesPanel, BorderLayout.CENTER);

            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
            footer.setOpaque(false);
            JButton btnCancelar = new JButton("Cancelar");
            JButton btnSiguiente = new JButton("Siguiente");

            btnCancelar.setBackground(new Color(148, 163, 184));
            btnCancelar.setForeground(new Color(15, 23, 42));
            btnCancelar.setFocusPainted(false);
            btnCancelar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            btnSiguiente.setBackground(new Color(56, 189, 248));
            btnSiguiente.setForeground(new Color(15, 23, 42));
            btnSiguiente.setFocusPainted(false);
            btnSiguiente.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

            btnCancelar.addActionListener(e -> {
                new CursoSimulacionWindow(licencia).setVisible(true);
                dispose();
            });

            btnSiguiente.addActionListener(e -> comprobar(progreso));

            footer.add(btnCancelar);
            footer.add(btnSiguiente);

            card.add(footer, BorderLayout.SOUTH);

            root.add(card, BorderLayout.CENTER);

            // Mostrar primera pregunta y progreso
            actualizarProgreso(progreso);
            mostrarPregunta();
        }

        private void mostrarPregunta() {
            DatabaseManager.PreguntaDB p = preguntas.get(index);
            lblPregunta.setText((index + 1) + ". " + p.enunciado);

            opcionesPanel.removeAll();
            grupo = new ButtonGroup();
            int n = p.opciones.size();
            radios = new JRadioButton[n];
            for (int i = 0; i < n; i++) {
                radios[i] = new JRadioButton(p.opciones.get(i).texto);
                radios[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
                radios[i].setOpaque(false);
                radios[i].setForeground(new Color(226, 232, 240));
                grupo.add(radios[i]);
                opcionesPanel.add(radios[i]);
            }
            opcionesPanel.revalidate();
            opcionesPanel.repaint();
        }

        private void actualizarProgreso(JLabel progresoLabel) {
            progresoLabel.setText("Pregunta " + (index + 1) + " de " + preguntas.size());
        }

        private void comprobar(JLabel progresoLabel) {
            DatabaseManager.PreguntaDB p = preguntas.get(index);

            int seleccion = -1;
            for (int i = 0; i < radios.length; i++) {
                if (radios[i] != null && radios[i].isSelected()) {
                    seleccion = i;
                    break;
                }
            }

            if (seleccion == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una respuesta.");
                return;
            }

            DatabaseManager.OpcionDB opcionSeleccionada = p.opciones.get(seleccion);
            boolean esCorrecta = opcionSeleccionada.esCorrecta;
            if (esCorrecta) aciertos++;

            DatabaseManager.registrarRespuesta(intentoId, p.preguntaId, opcionSeleccionada.opcionId, esCorrecta, p.dificultad);

            index++;

            if (index >= preguntas.size()) {
                int fallos = preguntas.size() - aciertos;
                boolean aprobado = (aciertos / (double) preguntas.size()) >= 0.9;
                DatabaseManager.finalizarIntento(intentoId, aciertos, fallos, aprobado);

                JOptionPane.showMessageDialog(this,
                        "Test finalizado.\nAciertos: " + aciertos + " de " + preguntas.size());

                new Dashboard().setVisible(true);
                dispose();
                return;
            }

            actualizarProgreso(progresoLabel);
            mostrarPregunta();
        }
    }


}

