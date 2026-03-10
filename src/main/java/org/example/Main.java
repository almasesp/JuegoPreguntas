package org.example;

import javax.swing.*;
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

            if (user.equals("admin") && pass.equals("1234")) {
                new Dashboard().setVisible(true);
                dispose();
            } else if (user.equals("user") && pass.equals("1234")) {
                new LicenseSelectorWindow().setVisible(true);
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

        private float hue = 0f;

        public Dashboard() {

            setTitle("Autoescuela Rápida — Panel Principal");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel root = new JPanel(new BorderLayout());
            setContentPane(root);

            JPanel animatedBg = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    hue += 0.0008f;
                    if (hue > 1) hue = 0;

                    Color c1 = new Color(10, 12, 24);
                    Color c2 = Color.getHSBColor(hue, 0.6f, 0.4f);

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            animatedBg.setLayout(new BorderLayout());
            root.add(animatedBg, BorderLayout.CENTER);

            new Timer(16, e -> animatedBg.repaint()).start();

            // NAV SUPERIOR
            JPanel nav = new JPanel(new BorderLayout());
            nav.setBackground(new Color(15, 23, 42));
            nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 64, 175)));
            nav.setPreferredSize(new Dimension(0, 70));
            animatedBg.add(nav, BorderLayout.NORTH);

            JLabel title = new JLabel("  AUTOESCUELA RÁPIDA — Panel de control general");
            title.setFont(new Font("Segoe UI", Font.BOLD, 20));
            title.setForeground(new Color(248, 250, 252));
            nav.add(title, BorderLayout.WEST);

            JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
            navRight.setOpaque(false);
            nav.add(navRight, BorderLayout.EAST);

            JLabel userInfo = new JLabel("Administrador · Sede Mollerussa");
            userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userInfo.setForeground(new Color(148, 163, 184));
            navRight.add(userInfo);

            // FILTRO DE LICENCIA
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            filterPanel.setOpaque(false);
            JLabel filterLabel = new JLabel("Filtrar por tipo de permiso:");
            filterLabel.setForeground(new Color(148, 163, 184));
            JComboBox<String> licenseFilter = new JComboBox<>(new String[]{
                    "Todos", "Permiso B", "Permiso A", "Permiso C", "Permiso D"
            });
            filterPanel.add(filterLabel);
            filterPanel.add(licenseFilter);
            animatedBg.add(filterPanel, BorderLayout.SOUTH);

            // CONTENEDOR PRINCIPAL CON SCROLL
            JPanel contentWrapper = new JPanel(new BorderLayout());
            contentWrapper.setOpaque(false);

            JPanel mainGrid = new JPanel(new GridLayout(1, 3, 16, 0));
            mainGrid.setOpaque(false);
            mainGrid.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            JScrollPane scrollPane = new JScrollPane(mainGrid);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);

            contentWrapper.add(scrollPane, BorderLayout.CENTER);
            animatedBg.add(contentWrapper, BorderLayout.CENTER);

            // COLUMNA IZQUIERDA
            JPanel leftCol = new JPanel();
            leftCol.setOpaque(false);
            leftCol.setLayout(new BorderLayout(0, 16));
            mainGrid.add(leftCol);

            JPanel circlesPanel = new JPanel(new GridLayout(1, 3, 12, 0));
            circlesPanel.setOpaque(false);
            CircleStatPanel circleTeorico = new CircleStatPanel("Aprobados teórico", 78, "Porcentaje de alumnos que han aprobado el examen teórico este mes.");
            CircleStatPanel circlePractico = new CircleStatPanel("Aprobados práctico", 64, "Porcentaje de alumnos que han aprobado el examen práctico.");
            CircleStatPanel circleAsistencia = new CircleStatPanel("Asistencia media", 91, "Porcentaje medio de asistencia a clases prácticas.");
            circlesPanel.add(circleTeorico);
            circlesPanel.add(circlePractico);
            circlesPanel.add(circleAsistencia);
            leftCol.add(circlesPanel, BorderLayout.NORTH);

            LicenseHorizontalBars barrasHoriz = new LicenseHorizontalBars("Distribución de alumnos por tipo de permiso");
            leftCol.add(barrasHoriz, BorderLayout.CENTER);

            // COLUMNA CENTRAL
            JPanel centerCol = new JPanel();
            centerCol.setOpaque(false);
            centerCol.setLayout(new BorderLayout(0, 16));
            mainGrid.add(centerCol);

            LicenseLineChartPanel lineChart = new LicenseLineChartPanel("Evolución de alumnos inscritos", "Últimas 6 semanas");
            centerCol.add(lineChart, BorderLayout.CENTER);

            CalendarPanel calendarPanel = new CalendarPanel("Calendario de clases y exámenes");
            centerCol.add(calendarPanel, BorderLayout.SOUTH);

            // COLUMNA DERECHA
            JPanel rightCol = new JPanel();
            rightCol.setOpaque(false);
            rightCol.setLayout(new BorderLayout(0, 16));
            mainGrid.add(rightCol);

            AgendaPanel agenda = new AgendaPanel("Próximas clases prácticas");
            rightCol.add(agenda, BorderLayout.CENTER);

            JPanel yearsCard = new CardPanel("Aprobados por año");
            yearsCard.setLayout(new GridLayout(4, 1, 8, 8));
            yearsCard.add(new YearStat("2020", 120));
            yearsCard.add(new YearStat("2021", 145));
            yearsCard.add(new YearStat("2022", 168));
            yearsCard.add(new YearStat("2023", 182));
            rightCol.add(yearsCard, BorderLayout.SOUTH);

            // BOTONES PARA AGENDA
            JPanel agendaButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
            agendaButtons.setOpaque(false);
            JButton addClassBtn = new JButton("Añadir clase");
            JButton removeClassBtn = new JButton("Eliminar seleccionada");
            styleSmallButton(addClassBtn);
            styleSmallButton(removeClassBtn);
            agendaButtons.add(addClassBtn);
            agendaButtons.add(removeClassBtn);
            rightCol.add(agendaButtons, BorderLayout.NORTH);

            addClassBtn.addActionListener(e -> agenda.addClassDialog());
            removeClassBtn.addActionListener(e -> agenda.removeSelected());

            // FILTRO DE LICENCIA -> ACTUALIZA GRÁFICOS
            licenseFilter.addActionListener(e -> {
                String selected = (String) licenseFilter.getSelectedItem();
                barrasHoriz.setLicenseFilter(selected);
                lineChart.setLicenseFilter(selected);
                barrasHoriz.repaint();
                lineChart.repaint();
            });
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
//     VENTANA PARA SELECCIONAR TIPO DE LICENCIA
// ============================================================
    static class LicenseSelectorWindow extends JFrame {

        public LicenseSelectorWindow() {
            setTitle("Seleccionar tipo de licencia");
            setSize(400, 350);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(8, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel title = new JLabel("Selecciona el permiso a estudiar", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            panel.add(title);

            String[] licencias = {"AM", "A1", "A2", "A", "B", "C1", "C","D", "D1"};

            for (String lic : licencias) {
                JButton btn = new JButton("Permiso " + lic);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                btn.addActionListener(e -> abrirCurso(lic));
                panel.add(btn);
            }

            add(panel);
        }

        private void abrirCurso(String licencia) {
            new CursoSimulacionWindow(licencia).setVisible(true);
            dispose();
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

    static java.util.List<Pregunta> bancoPreguntas = new java.util.ArrayList<>();

    // ============================================================
//     CARGA DE PREGUNTAS (SIMULACIÓN BASADA EN TU SQL)
// ============================================================
    static void cargarPreguntas() {

        bancoPreguntas.clear();

        bancoPreguntas.add(new Pregunta(
                "Ante una señal de STOP, el conductor debe…",
                new String[]{
                        "Detenerse siempre por completo",
                        "Reducir la velocidad sin detenerse",
                        "Detenerse solo si viene alguien",
                        "Pasar rápido si no hay tráfico"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con pasajero en moto, la conducción debe ser…",
                new String[]{
                        "Más suave y progresiva",
                        "Más rápida para compensar el peso",
                        "Con frenadas más fuertes",
                        "Sin modificar la conducción"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un ciclomotor, la conducción defensiva es clave porque…",
                new String[]{
                        "Eres más vulnerable en caso de accidente",
                        "Tienes más potencia que un coche",
                        "Siempre tienes prioridad",
                        "Puedes frenar mejor que un turismo"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "¿Quién tiene prioridad en una intersección sin señalización?",
                new String[]{
                        "El vehículo que llega por la derecha",
                        "El vehículo más grande",
                        "El que vaya más rápido",
                        "El que circule por una vía más ancha"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con niebla densa, lo más recomendable es usar…",
                new String[]{
                        "Luces antiniebla y velocidad moderada",
                        "Largas siempre",
                        "Solo posición",
                        "Intermitentes de emergencia"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con lluvia, el agarre suele disminuir especialmente sobre…",
                new String[]{
                        "Marcas viales y tapas metálicas",
                        "Asfalto nuevo",
                        "Zonas sin pintar",
                        "Arcenes de tierra"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Un neumático con poca presión puede provocar…",
                new String[]{
                        "Mayor distancia de frenado y peor estabilidad",
                        "Menor consumo de combustible",
                        "Mejor agarre en todas las condiciones",
                        "Mayor velocidad punta"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Manipular el móvil mientras conduces aumenta el riesgo porque…",
                new String[]{
                        "Distrae la atención y aumenta el tiempo de reacción",
                        "Mejora la orientación",
                        "Permite avisar a otros conductores",
                        "Reduce el estrés"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una frenada de emergencia con moto en asfalto seco, normalmente conviene…",
                new String[]{
                        "Frenar fuerte con ambos frenos, sin bloquear ruedas",
                        "Usar solo el freno trasero",
                        "Usar solo el freno delantero",
                        "Soltar el manillar"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una curva, una técnica segura es…",
                new String[]{
                        "Entrar más despacio y acelerar suavemente a la salida",
                        "Entrar rápido y frenar dentro",
                        "Mantener gas constante y mirar solo delante",
                        "Frenar fuerte en mitad de la curva"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La distancia de seguridad sirve para…",
                new String[]{
                        "Evitar colisiones en caso de frenado brusco",
                        "Adelantar con más facilidad",
                        "Reducir el consumo de combustible",
                        "Circular más rápido"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Si un semáforo está en amarillo fijo, debes…",
                new String[]{
                        "Detenerte si puedes hacerlo con seguridad",
                        "Acelerar para pasar antes de rojo",
                        "Ignorarlo",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso del casco en moto es obligatorio…",
                new String[]{
                        "Siempre, tanto conductor como pasajero",
                        "Solo en carretera",
                        "Solo para el conductor",
                        "Solo en ciudad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Antes de iniciar un adelantamiento debes…",
                new String[]{
                        "Asegurarte de que la vía está libre y señalizar",
                        "Tocar el claxon y acelerar",
                        "Pegarte al vehículo delantero",
                        "Adelantar sin mirar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una glorieta, tiene prioridad…",
                new String[]{
                        "El vehículo que ya circula por ella",
                        "El que entra más rápido",
                        "El más grande",
                        "El que viene por la derecha siempre"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El alcohol al volante provoca…",
                new String[]{
                        "Mayor tiempo de reacción y menor capacidad de atención",
                        "Mejor percepción del entorno",
                        "Conducción más segura",
                        "Mayor coordinación"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una pendiente descendente prolongada es recomendable…",
                new String[]{
                        "Usar el freno motor",
                        "Circular en punto muerto",
                        "Mantener el embrague pisado",
                        "Acelerar constantemente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una línea continua en la calzada indica que…",
                new String[]{
                        "No se puede adelantar ni cambiar de carril",
                        "Se puede adelantar libremente",
                        "Es solo informativa",
                        "Es exclusiva para motos"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Al aproximarte a un paso de peatones debes…",
                new String[]{
                        "Reducir la velocidad y ceder el paso si es necesario",
                        "Acelerar para cruzarlo rápido",
                        "Ignorarlo si no ves peatones",
                        "Tocar el claxon siempre"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de aquaplaning debes…",
                new String[]{
                        "Levantar suavemente el pie del acelerador",
                        "Frenar bruscamente",
                        "Girar el volante rápidamente",
                        "Acelerar más"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El intermitente debe utilizarse para…",
                new String[]{
                        "Indicar cambios de dirección o carril",
                        "Advertir enfado",
                        "Saludar a otros conductores",
                        "Sustituir al claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con viento lateral fuerte en moto debes…",
                new String[]{
                        "Reducir velocidad y sujetar firme el manillar",
                        "Aumentar la velocidad",
                        "Circular en punto muerto",
                        "Inclinarte hacia el lado contrario exageradamente"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Los espejos retrovisores deben ajustarse…",
                new String[]{
                        "Antes de iniciar la marcha",
                        "Durante la conducción",
                        "Solo si llueve",
                        "Nunca es necesario ajustarlos"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El ABS en una motocicleta sirve para…",
                new String[]{
                        "Evitar el bloqueo de ruedas al frenar",
                        "Aumentar la velocidad",
                        "Reducir el consumo",
                        "Mejorar la iluminación"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con fatiga al volante lo mejor es…",
                new String[]{
                        "Parar y descansar",
                        "Subir la música",
                        "Abrir la ventanilla",
                        "Acelerar para llegar antes"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un túnel es obligatorio…",
                new String[]{
                        "Encender las luces de cruce",
                        "Apagar las luces",
                        "Circular en punto muerto",
                        "Tocar el claxon continuamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Para transportar pasajero en ciclomotor es necesario…",
                new String[]{
                        "Que el vehículo esté homologado para dos personas",
                        "Que el pasajero sea menor de edad",
                        "No llevar casco",
                        "Circular solo en ciudad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal triangular con borde rojo indica…",
                new String[]{
                        "Peligro",
                        "Obligación",
                        "Prohibición absoluta",
                        "Fin de prohibición"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El mantenimiento periódico del vehículo ayuda a…",
                new String[]{
                        "Mejorar la seguridad y prevenir averías",
                        "Aumentar multas",
                        "Reducir visibilidad",
                        "Incrementar desgaste innecesario"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una curva con visibilidad reducida está prohibido…",
                new String[]{
                        "Adelantar",
                        "Reducir velocidad",
                        "Encender luces",
                        "Mirar lejos"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En autopista, la velocidad mínima permitida es…",
                new String[]{
                        "La mitad de la velocidad máxima salvo señalización distinta",
                        "Siempre 60 km/h",
                        "Siempre 90 km/h",
                        "No existe velocidad mínima"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El carril izquierdo en autopista debe usarse principalmente para…",
                new String[]{
                        "Adelantar",
                        "Circular siempre",
                        "Reducir velocidad",
                        "Estacionar en caso de tráfico"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Si un agente de tráfico da una orden, esta prevalece sobre…",
                new String[]{
                        "Las señales y normas generales",
                        "Solo los semáforos",
                        "Nada, no tiene prioridad",
                        "Las marcas viales únicamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Circular con neumáticos desgastados aumenta…",
                new String[]{
                        "El riesgo de accidente",
                        "La estabilidad",
                        "El ahorro de combustible",
                        "La potencia del vehículo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de accidente, lo primero que debes hacer es…",
                new String[]{
                        "Proteger la zona y señalizar",
                        "Abandonar el lugar",
                        "Mover a los heridos inmediatamente",
                        "Quitar el vehículo sin señalizar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La presión correcta de los neumáticos debe comprobarse…",
                new String[]{
                        "En frío",
                        "Después de un viaje largo",
                        "Con el motor caliente",
                        "Solo cuando llueve"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una motocicleta, la mayor potencia de frenado la aporta normalmente…",
                new String[]{
                        "El freno delantero",
                        "El freno trasero",
                        "Ambos por igual siempre",
                        "El embrague"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso del cinturón de seguridad es obligatorio…",
                new String[]{
                        "En todas las vías y asientos que lo tengan instalado",
                        "Solo en autopista",
                        "Solo en ciudad",
                        "Solo para el conductor"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal circular con fondo azul indica generalmente…",
                new String[]{
                        "Obligación",
                        "Peligro",
                        "Prohibición",
                        "Recomendación opcional"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con lluvia intensa debes aumentar…",
                new String[]{
                        "La distancia de seguridad",
                        "La velocidad",
                        "El uso del claxon",
                        "La presión del acelerador"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un cruce sin visibilidad, debes…",
                new String[]{
                        "Reducir velocidad y extremar precaución",
                        "Acelerar para cruzar rápido",
                        "Ignorar la situación",
                        "Circular en punto muerto"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El casco debe estar correctamente…",
                new String[]{
                        "Abrochado y ajustado",
                        "Suelto para mayor comodidad",
                        "Solo apoyado en la cabeza",
                        "Sin visera siempre"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El tiempo de reacción aumenta cuando…",
                new String[]{
                        "Estás cansado o distraído",
                        "Estás concentrado",
                        "Conduces despacio",
                        "Hay buena visibilidad"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una vía con varios carriles, debes circular normalmente por…",
                new String[]{
                        "El carril derecho",
                        "El izquierdo siempre",
                        "El central únicamente",
                        "Cualquiera indistintamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Para evitar el ángulo muerto en moto es recomendable…",
                new String[]{
                        "Mover ligeramente la cabeza y comprobar espejos",
                        "No mirar atrás nunca",
                        "Circular pegado a otros vehículos",
                        "Acelerar constantemente"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una zona escolar debes…",
                new String[]{
                        "Reducir velocidad y extremar precaución",
                        "Mantener velocidad alta",
                        "Tocar el claxon continuamente",
                        "Adelantar siempre que puedas"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El claxon debe utilizarse…",
                new String[]{
                        "Para evitar un posible peligro",
                        "Para saludar",
                        "Para mostrar enfado",
                        "En cualquier situación"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de hielo en la calzada es recomendable…",
                new String[]{
                        "Circular con suavidad y sin movimientos bruscos",
                        "Frenar fuerte",
                        "Acelerar más",
                        "Girar bruscamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La documentación obligatoria al conducir incluye…",
                new String[]{
                        "Permiso de conducir y documentación del vehículo",
                        "Solo el DNI",
                        "Solo el seguro",
                        "Ninguna documentación"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una pendiente ascendente con moto debes…",
                new String[]{
                        "Dosificar acelerador y embrague correctamente",
                        "Circular en punto muerto",
                        "Frenar constantemente",
                        "Inclinarte hacia atrás exageradamente"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "La tasa máxima de alcohol permitida para conductores noveles es…",
                new String[]{
                        "Inferior a la de conductores experimentados",
                        "La misma que cualquier conductor",
                        "No existe límite",
                        "El doble que la general"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Antes de cambiar de carril debes…",
                new String[]{
                        "Señalizar y comprobar espejos y ángulo muerto",
                        "Girar directamente",
                        "Frenar bruscamente",
                        "Acelerar sin mirar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una motocicleta, la postura correcta implica…",
                new String[]{
                        "Espalda recta y brazos relajados",
                        "Ir completamente rígido",
                        "Conducir con una mano",
                        "Inclinarse hacia atrás siempre"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal octogonal roja indica…",
                new String[]{
                        "STOP",
                        "Ceda el paso",
                        "Prohibido adelantar",
                        "Fin de prohibición"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La distancia de frenado aumenta cuando…",
                new String[]{
                        "Aumenta la velocidad",
                        "El vehículo pesa menos",
                        "Hay buen tiempo",
                        "Circulas despacio"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de pinchazo en moto debes…",
                new String[]{
                        "Sujetar firme el manillar y reducir velocidad progresivamente",
                        "Frenar bruscamente",
                        "Soltar el manillar",
                        "Acelerar para estabilizar"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso de luces de cruce es obligatorio…",
                new String[]{
                        "Entre la puesta y la salida del sol",
                        "Solo en ciudad",
                        "Solo si llueve",
                        "Nunca es obligatorio"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una línea discontinua en la calzada permite…",
                new String[]{
                        "Adelantar si es seguro",
                        "Prohibido cualquier cambio",
                        "Circular marcha atrás",
                        "Estacionar en medio"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una retención prolongada debes…",
                new String[]{
                        "Mantener distancia y estar atento",
                        "Pegarme al vehículo delantero",
                        "Circular por el arcén",
                        "Adelantar por la derecha"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, mirar lejos en una curva ayuda a…",
                new String[]{
                        "Trazar mejor y anticipar riesgos",
                        "Perder el equilibrio",
                        "Aumentar el consumo",
                        "Reducir visibilidad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso del móvil con manos libres puede ser peligroso porque…",
                new String[]{
                        "También distrae la atención",
                        "Mejora la concentración",
                        "Reduce el tiempo de reacción",
                        "No influye en la conducción"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de lluvia ligera tras periodo seco hay mayor riesgo porque…",
                new String[]{
                        "Se forma una mezcla deslizante con polvo y aceite",
                        "El asfalto mejora agarre",
                        "No hay ningún riesgo",
                        "La visibilidad aumenta"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Para arrancar en pendiente con moto debes…",
                new String[]{
                        "Coordinar freno trasero, embrague y acelerador",
                        "Soltar todo de golpe",
                        "Usar solo acelerador",
                        "Circular en punto muerto"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El control periódico del nivel de aceite es importante para…",
                new String[]{
                        "Evitar averías graves",
                        "Aumentar velocidad",
                        "Reducir visibilidad",
                        "Gastar más combustible"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un paso a nivel sin barreras debes…",
                new String[]{
                        "Detenerte si hay señal de STOP o peligro",
                        "Cruzar sin mirar",
                        "Acelerar siempre",
                        "Tocar el claxon y pasar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El exceso de velocidad influye en…",
                new String[]{
                        "Mayor gravedad en caso de accidente",
                        "Menor distancia de frenado",
                        "Mayor seguridad",
                        "Mejor control del vehículo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, los guantes protegen principalmente…",
                new String[]{
                        "Las manos en caso de caída",
                        "La vista",
                        "Los pies",
                        "El oído"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una incorporación a autopista debes…",
                new String[]{
                        "Adaptar velocidad al tráfico y usar carril de aceleración",
                        "Pararte al final del carril",
                        "Entrar sin mirar",
                        "Circular marcha atrás"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El descanso recomendado en viajes largos es cada…",
                new String[]{
                        "2 horas aproximadamente",
                        "5 horas",
                        "30 minutos exactos",
                        "Solo cuando tengas sueño extremo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una motocicleta, el chaleco reflectante mejora…",
                new String[]{
                        "La visibilidad ante otros conductores",
                        "La velocidad",
                        "La potencia",
                        "El consumo"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una línea continua en la calzada indica que…",
                new String[]{
                        "No se puede adelantar ni cambiar de carril",
                        "Se puede adelantar libremente",
                        "Es solo decorativa",
                        "Permite estacionar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un túnel es obligatorio…",
                new String[]{
                        "Encender las luces de cruce",
                        "Circular sin luces",
                        "Usar solo luces largas",
                        "Tocar el claxon constantemente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El aquaplaning se produce cuando…",
                new String[]{
                        "El neumático pierde contacto con el asfalto por agua",
                        "El motor se apaga",
                        "Se frena demasiado",
                        "Hay viento fuerte"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, en ciudad debes prestar especial atención a…",
                new String[]{
                        "Puertas de vehículos estacionados",
                        "Solo semáforos",
                        "Las nubes",
                        "El ruido del motor"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal triangular con borde rojo indica…",
                new String[]{
                        "Peligro",
                        "Obligación",
                        "Prohibición absoluta",
                        "Información turística"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un adelantamiento debes asegurarte de que…",
                new String[]{
                        "Hay visibilidad suficiente y espacio",
                        "El vehículo de delante acelera",
                        "El arcén está libre",
                        "No viene nadie detrás"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, la presión incorrecta de neumáticos afecta…",
                new String[]{
                        "A la estabilidad y agarre",
                        "Solo al color",
                        "A la matrícula",
                        "Al claxon"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Ante un semáforo en ámbar debes…",
                new String[]{
                        "Detenerte si puedes hacerlo con seguridad",
                        "Acelerar siempre",
                        "Ignorarlo",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La distancia de seguridad debe aumentarse cuando…",
                new String[]{
                        "Las condiciones son adversas",
                        "Hay buena visibilidad",
                        "Circulas solo",
                        "Vas despacio"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el uso de botas adecuadas protege…",
                new String[]{
                        "Pies y tobillos en caso de caída",
                        "La cabeza",
                        "Las manos",
                        "La vista"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Un conductor fatigado puede experimentar…",
                new String[]{
                        "Disminución de reflejos",
                        "Mayor concentración",
                        "Mejor visión nocturna",
                        "Mayor estabilidad"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una glorieta, tiene prioridad…",
                new String[]{
                        "El que ya circula por ella",
                        "El que se incorpora",
                        "El más rápido",
                        "El más grande"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, debes usar intermitentes para…",
                new String[]{
                        "Señalizar cualquier cambio de dirección",
                        "Saludar",
                        "Aumentar velocidad",
                        "Frenar"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal rectangular azul suele indicar…",
                new String[]{
                        "Información o servicio",
                        "Prohibición",
                        "Peligro inmediato",
                        "Multa automática"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Con viento lateral fuerte debes…",
                new String[]{
                        "Sujetar firmemente el volante o manillar",
                        "Soltar el control",
                        "Acelerar más",
                        "Cerrar los ojos"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El ABS en motocicleta sirve para…",
                new String[]{
                        "Evitar el bloqueo de ruedas al frenar",
                        "Aumentar potencia",
                        "Reducir consumo",
                        "Mejorar sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso de casco en ciclomotor es…",
                new String[]{
                        "Obligatorio para conductor y pasajero",
                        "Opcional en ciudad",
                        "Solo obligatorio en carretera",
                        "No necesario"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Al aproximarte a un paso de peatones debes…",
                new String[]{
                        "Reducir velocidad y ceder si es necesario",
                        "Acelerar",
                        "Ignorar peatones",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Circular en punto muerto reduce…",
                new String[]{
                        "El control del vehículo",
                        "El riesgo",
                        "El consumo siempre",
                        "La distancia de frenado"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una motocicleta, la visera del casco debe estar…",
                new String[]{
                        "Limpia y en buen estado",
                        "Rayada",
                        "Siempre levantada",
                        "Opcional"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una vía urbana, la velocidad debe adaptarse especialmente a…",
                new String[]{
                        "La presencia de peatones y tráfico",
                        "El color de los edificios",
                        "El tamaño del vehículo",
                        "La música que escuches"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso de auriculares mientras conduces está…",
                new String[]{
                        "Prohibido",
                        "Permitido siempre",
                        "Permitido en autopista",
                        "Recomendado"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, una correcta inclinación en curva permite…",
                new String[]{
                        "Tomar la curva con mayor estabilidad",
                        "Perder adherencia",
                        "Reducir visibilidad",
                        "Frenar más tarde"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal de prohibición se caracteriza por…",
                new String[]{
                        "Forma circular con borde rojo",
                        "Forma triangular azul",
                        "Fondo verde",
                        "Forma cuadrada amarilla"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El consumo de drogas afecta a la conducción porque…",
                new String[]{
                        "Altera percepción y reflejos",
                        "Mejora concentración",
                        "Reduce riesgos",
                        "Aumenta la estabilidad"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de lluvia intensa en moto es recomendable…",
                new String[]{
                        "Reducir velocidad y evitar maniobras bruscas",
                        "Acelerar",
                        "Frenar fuerte en curvas",
                        "Circular en punto muerto"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El espejo retrovisor debe ajustarse para…",
                new String[]{
                        "Minimizar ángulos muertos",
                        "Ver solo el interior",
                        "Reflejar el techo",
                        "No usarlo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una carretera secundaria sin arcén debes…",
                new String[]{
                        "Extremar precaución",
                        "Aumentar velocidad",
                        "Circular por el centro",
                        "Ignorar señales"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El chaleco reflectante es obligatorio usarlo cuando…",
                new String[]{
                        "Sales del vehículo en vía interurbana",
                        "Siempre dentro del coche",
                        "Solo en ciudad",
                        "Nunca"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el pasajero debe…",
                new String[]{
                        "Mantenerse alineado con el conductor",
                        "Inclinarse al lado contrario",
                        "Moverse constantemente",
                        "Soltar las manos"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El límite de velocidad existe para…",
                new String[]{
                        "Garantizar la seguridad vial",
                        "Aumentar multas",
                        "Reducir consumo siempre",
                        "Obligar a frenar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Al estacionar en pendiente descendente debes…",
                new String[]{
                        "Dejar una marcha engranada y girar ruedas hacia bordillo",
                        "Dejar en punto muerto",
                        "No usar freno",
                        "Acelerar antes de bajar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, la cadena debe mantenerse…",
                new String[]{
                        "Lubricada y tensada correctamente",
                        "Suelta",
                        "Seca siempre",
                        "Oxidada"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Un cruce con señal de CEDA EL PASO obliga a…",
                new String[]{
                        "Ceder prioridad si es necesario",
                        "Detenerse siempre",
                        "Acelerar",
                        "Ignorar vehículos"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La conducción eficiente contribuye a…",
                new String[]{
                        "Reducir consumo y emisiones",
                        "Aumentar velocidad",
                        "Mayor desgaste",
                        "Menor seguridad"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el uso de chaqueta con protecciones sirve para…",
                new String[]{
                        "Reducir lesiones en caso de caída",
                        "Aumentar velocidad",
                        "Reducir consumo",
                        "Mejorar sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Si un peatón inicia el cruce indebidamente debes…",
                new String[]{
                        "Evitar el atropello adaptando velocidad",
                        "Acelerar",
                        "Ignorarlo",
                        "Tocar el claxon sin frenar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una vía de doble sentido debes circular…",
                new String[]{
                        "Por la derecha de la calzada",
                        "Por la izquierda",
                        "Por el centro",
                        "Por el arcén"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, el freno motor ayuda a…",
                new String[]{
                        "Reducir velocidad de forma progresiva",
                        "Aumentar potencia",
                        "Gastar más gasolina",
                        "Perder estabilidad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "La señalización horizontal incluye…",
                new String[]{
                        "Líneas y marcas pintadas en la calzada",
                        "Solo semáforos",
                        "Solo señales verticales",
                        "Solo señales luminosas"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de deslumbramiento nocturno debes…",
                new String[]{
                        "Mirar hacia el borde derecho de la calzada",
                        "Cerrar los ojos",
                        "Encender luces largas",
                        "Acelerar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El intermitente debe activarse…",
                new String[]{
                        "Con suficiente antelación",
                        "Durante el giro únicamente",
                        "Después de girar",
                        "Solo en autopista"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el uso del casco integral ofrece…",
                new String[]{
                        "Mayor protección facial",
                        "Menor seguridad",
                        "Más ruido",
                        "Menos visibilidad obligatoria"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal amarilla intermitente en semáforo indica…",
                new String[]{
                        "Precaución",
                        "Paso libre",
                        "Prohibido continuar",
                        "Detención obligatoria siempre"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La distancia de reacción depende principalmente de…",
                new String[]{
                        "El estado del conductor",
                        "El color del coche",
                        "La marca del neumático",
                        "El tamaño del motor"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, circular entre vehículos detenidos requiere…",
                new String[]{
                        "Extrema precaución",
                        "Acelerar constantemente",
                        "Ignorar retrovisores",
                        "Tocar el claxon sin parar"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un paso estrecho sin señalización tiene prioridad…",
                new String[]{
                        "El que haya entrado primero",
                        "El más grande",
                        "El más rápido",
                        "El que toque el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La luz antiniebla trasera debe usarse…",
                new String[]{
                        "Con niebla densa o visibilidad muy reducida",
                        "Siempre de noche",
                        "Con lluvia ligera",
                        "En ciudad siempre"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, frenar en curva bruscamente puede…",
                new String[]{
                        "Provocar pérdida de adherencia",
                        "Mejorar estabilidad",
                        "Reducir consumo",
                        "Aumentar visibilidad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El conductor debe mantener atención constante para…",
                new String[]{
                        "Anticipar posibles riesgos",
                        "Reducir consumo",
                        "Aumentar velocidad",
                        "Ignorar señales"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal de fondo verde suele indicar…",
                new String[]{
                        "Direcciones en autopistas o autovías",
                        "Prohibición",
                        "Peligro inmediato",
                        "Obligación"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, llevar mochila pesada puede afectar…",
                new String[]{
                        "Al equilibrio",
                        "Al color del casco",
                        "A la matrícula",
                        "Al sonido del motor"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso de luces largas está prohibido cuando…",
                new String[]{
                        "Deslumbran a otros usuarios",
                        "Circulas solo",
                        "Es de día",
                        "Hay autopista"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un control policial debes…",
                new String[]{
                        "Seguir las instrucciones del agente",
                        "Acelerar",
                        "Ignorar señales",
                        "Cambiar de sentido"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el mantenimiento de frenos es importante para…",
                new String[]{
                        "Garantizar una frenada eficaz",
                        "Reducir consumo",
                        "Aumentar velocidad",
                        "Mejorar sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El cinturón debe colocarse…",
                new String[]{
                        "Ajustado y sin holguras",
                        "Suelto",
                        "Por debajo del brazo",
                        "Solo en carretera"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La señal de STOP obliga a…",
                new String[]{
                        "Detenerse completamente",
                        "Reducir velocidad",
                        "Ceder sin detenerse",
                        "Acelerar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, la ropa reflectante ayuda a…",
                new String[]{
                        "Ser visto con mayor facilidad",
                        "Aumentar velocidad",
                        "Reducir consumo",
                        "Mejorar frenado"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una curva cerrada debes…",
                new String[]{
                        "Reducir velocidad antes de entrar",
                        "Frenar fuerte dentro",
                        "Acelerar al máximo",
                        "Mirar solo al suelo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La conducción preventiva consiste en…",
                new String[]{
                        "Anticiparse a posibles peligros",
                        "Circular más rápido",
                        "Ignorar señales",
                        "Frenar siempre bruscamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una vía con hielo es recomendable…",
                new String[]{
                        "Conducir con suavidad y evitar frenazos",
                        "Acelerar fuerte",
                        "Frenar bruscamente",
                        "Girar de forma rápida"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso del casco homologado es obligatorio porque…",
                new String[]{
                        "Reduce el riesgo de lesiones graves",
                        "Aumenta velocidad",
                        "Reduce consumo",
                        "Mejora sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un cruce regulado por semáforo en rojo debes…",
                new String[]{
                        "Detenerte antes de la línea",
                        "Acelerar",
                        "Ignorar si no viene nadie",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La distancia de seguridad adecuada permite…",
                new String[]{
                        "Evitar colisiones por alcance",
                        "Aumentar velocidad",
                        "Reducir visibilidad",
                        "Circular más cerca"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el uso correcto del embrague ayuda a…",
                new String[]{
                        "Cambiar de marcha suavemente",
                        "Aumentar consumo",
                        "Reducir estabilidad",
                        "Bloquear ruedas"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal de prohibido adelantar implica…",
                new String[]{
                        "No superar a otro vehículo",
                        "Adelantar por la derecha",
                        "Aumentar velocidad",
                        "Reducir distancia"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En condiciones de baja visibilidad debes…",
                new String[]{
                        "Reducir velocidad y aumentar distancia",
                        "Acelerar",
                        "Apagar luces",
                        "Circular en punto muerto"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, una mala distribución del peso puede…",
                new String[]{
                        "Afectar al equilibrio",
                        "Mejorar estabilidad",
                        "Reducir consumo",
                        "No tener efecto"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Al aproximarte a un ciclista debes…",
                new String[]{
                        "Mantener distancia lateral suficiente",
                        "Pasar muy cerca",
                        "Tocar el claxon fuerte",
                        "Acelerar"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso indebido del claxon puede…",
                new String[]{
                        "Molestar y distraer",
                        "Mejorar seguridad siempre",
                        "Reducir consumo",
                        "Aumentar potencia"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el neumático trasero influye en…",
                new String[]{
                        "La tracción",
                        "La visibilidad",
                        "El sonido",
                        "El color"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Un paso de peatones sin semáforo obliga a…",
                new String[]{
                        "Ceder el paso al peatón",
                        "Acelerar",
                        "Ignorar peatones",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En carretera mojada debes evitar…",
                new String[]{
                        "Maniobras bruscas",
                        "Reducir velocidad",
                        "Aumentar distancia",
                        "Encender luces"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, el mantenimiento de la suspensión es importante para…",
                new String[]{
                        "La estabilidad",
                        "El sonido",
                        "El color",
                        "El consumo"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una intersección sin prioridad señalizada debes…",
                new String[]{
                        "Ceder al vehículo que viene por la derecha",
                        "Acelerar",
                        "Ignorar normas",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso de alcohol al volante provoca…",
                new String[]{
                        "Disminución de reflejos",
                        "Mayor concentración",
                        "Mejor coordinación",
                        "Reducción del riesgo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, mirar a la salida de la curva ayuda a…",
                new String[]{
                        "Mejorar trazada",
                        "Perder equilibrio",
                        "Reducir agarre",
                        "Aumentar consumo"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal de fin de prohibición indica…",
                new String[]{
                        "Que termina una restricción anterior",
                        "Inicio de peligro",
                        "Obligación nueva",
                        "Multa inmediata"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En autopista está prohibido…",
                new String[]{
                        "Circular marcha atrás",
                        "Adelantar",
                        "Usar intermitente",
                        "Circular por la derecha"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, mantener ambas manos en el manillar es importante para…",
                new String[]{
                        "Conservar control y estabilidad",
                        "Aumentar velocidad",
                        "Reducir consumo",
                        "Mejorar sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una vía con obras debes…",
                new String[]{
                        "Respetar la señalización provisional",
                        "Ignorar señales temporales",
                        "Acelerar para salir rápido",
                        "Circular por cualquier carril"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, una presión demasiado alta en neumáticos puede…",
                new String[]{
                        "Reducir la superficie de contacto",
                        "Mejorar agarre siempre",
                        "Aumentar estabilidad",
                        "No afectar en nada"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El arcén debe utilizarse normalmente para…",
                new String[]{
                        "Emergencias o averías",
                        "Adelantar",
                        "Circular habitualmente",
                        "Aumentar velocidad"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un túnel con retención es importante…",
                new String[]{
                        "Mantener distancia y no apagar motor innecesariamente",
                        "Salir del vehículo",
                        "Circular marcha atrás",
                        "Acelerar bruscamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el uso correcto del freno delantero requiere…",
                new String[]{
                        "Aplicarlo progresivamente",
                        "Bloquear rueda",
                        "Usarlo de golpe siempre",
                        "No utilizarlo"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Ante una señal de peligro debes…",
                new String[]{
                        "Extremar precaución",
                        "Ignorarla",
                        "Acelerar",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La velocidad excesiva influye en…",
                new String[]{
                        "Mayor distancia de frenado",
                        "Menor riesgo",
                        "Mejor estabilidad",
                        "Reducción de consumo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, el pasajero debe apoyar los pies en…",
                new String[]{
                        "Reposapiés específicos",
                        "Escape",
                        "Rueda trasera",
                        "Asiento delantero"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una vía interurbana, el chaleco reflectante debe estar…",
                new String[]{
                        "Al alcance del conductor",
                        "En el maletero bajo equipaje",
                        "En casa",
                        "No es obligatorio"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El consumo de alcohol afecta especialmente a…",
                new String[]{
                        "Capacidad de reacción",
                        "Color del vehículo",
                        "Marca del casco",
                        "Sonido del motor"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, frenar solo con el freno trasero provoca…",
                new String[]{
                        "Mayor distancia de frenado",
                        "Mejor estabilidad siempre",
                        "Reducción inmediata de velocidad óptima",
                        "Aumento de potencia"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Una señal luminosa roja fija en un paso a nivel indica…",
                new String[]{
                        "Prohibido el paso",
                        "Paso libre",
                        "Precaución leve",
                        "Fin de peligro"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La fatiga al volante puede causar…",
                new String[]{
                        "Pérdida de concentración",
                        "Mayor atención",
                        "Reflejos más rápidos",
                        "Menor distancia de frenado"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, el mantenimiento del sistema de iluminación es importante para…",
                new String[]{
                        "Ser visto correctamente",
                        "Aumentar velocidad",
                        "Reducir consumo",
                        "Mejorar sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una curva con visibilidad reducida debes…",
                new String[]{
                        "Reducir velocidad antes de entrar",
                        "Adelantar",
                        "Acelerar dentro",
                        "Circular por el centro"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso del cinturón reduce…",
                new String[]{
                        "La gravedad de lesiones",
                        "La velocidad",
                        "El consumo",
                        "El ruido"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, circular con lluvia requiere…",
                new String[]{
                        "Mayor suavidad en maniobras",
                        "Aceleraciones bruscas",
                        "Frenadas fuertes",
                        "Menor distancia"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Ante un vehículo de emergencia con señales activadas debes…",
                new String[]{
                        "Facilitar el paso",
                        "Competir en velocidad",
                        "Ignorarlo",
                        "Bloquear su carril"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En autopista, el carril derecho debe usarse como norma general para…",
                new String[]{
                        "Circular habitualmente",
                        "Adelantar",
                        "Estacionar",
                        "Reducir velocidad bruscamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, el control visual constante permite…",
                new String[]{
                        "Anticipar riesgos",
                        "Reducir agarre",
                        "Aumentar consumo",
                        "Perder estabilidad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una incorporación con señal de CEDA EL PASO debes…",
                new String[]{
                        "Adaptar velocidad y ceder si es necesario",
                        "Entrar sin mirar",
                        "Acelerar siempre",
                        "Detenerte en medio del carril"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, una correcta posición de los pies ayuda a…",
                new String[]{
                        "Mejorar control y equilibrio",
                        "Reducir visibilidad",
                        "Aumentar consumo",
                        "Disminuir estabilidad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un paso de peatones con personas esperando debes…",
                new String[]{
                        "Reducir velocidad y prever posible cruce",
                        "Acelerar",
                        "Ignorar si no han cruzado",
                        "Tocar el claxon"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La señal de prioridad respecto al sentido contrario indica…",
                new String[]{
                        "Que tienes preferencia de paso",
                        "Que debes detenerte siempre",
                        "Prohibido continuar",
                        "Fin de autopista"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, el uso del freno motor es útil para…",
                new String[]{
                        "Controlar la velocidad en descensos",
                        "Aumentar potencia",
                        "Reducir agarre",
                        "Bloquear ruedas"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Ante una señal de obras debes…",
                new String[]{
                        "Seguir indicaciones y reducir velocidad",
                        "Ignorarla",
                        "Acelerar",
                        "Adelantar siempre"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La conducción agresiva provoca…",
                new String[]{
                        "Mayor riesgo de accidente",
                        "Mayor seguridad",
                        "Menor consumo",
                        "Mejor estabilidad"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, circular con casco mal ajustado puede…",
                new String[]{
                        "Reducir protección en caso de caída",
                        "Mejorar comodidad siempre",
                        "Aumentar estabilidad",
                        "No afectar en nada"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una glorieta debes señalizar…",
                new String[]{
                        "La salida que vas a tomar",
                        "Al entrar obligatoriamente siempre",
                        "Nunca",
                        "Solo si hay policía"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La distancia lateral al adelantar a un ciclista debe ser…",
                new String[]{
                        "Suficiente para garantizar seguridad",
                        "Mínima posible",
                        "Inexistente",
                        "No es necesaria"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, la suspensión trasera influye en…",
                new String[]{
                        "Estabilidad y confort",
                        "Color del vehículo",
                        "Sonido del motor",
                        "Consumo directo"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "Si pierdes adherencia en una curva debes…",
                new String[]{
                        "Evitar movimientos bruscos",
                        "Frenar de golpe",
                        "Acelerar al máximo",
                        "Cerrar los ojos"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El uso de luces de posición solas es insuficiente cuando…",
                new String[]{
                        "Circulas de noche",
                        "Estás estacionado",
                        "Hay buena visibilidad",
                        "Es de día soleado"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, la visibilidad del conductor mejora si…",
                new String[]{
                        "Mantiene limpia la pantalla del casco",
                        "Circula sin casco",
                        "No usa espejos",
                        "Cierra los ojos"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En un cambio de rasante sin visibilidad está prohibido…",
                new String[]{
                        "Adelantar",
                        "Reducir velocidad",
                        "Encender luces",
                        "Mantener distancia"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La señal de fin de autopista indica…",
                new String[]{
                        "Que terminan las normas específicas de autopista",
                        "Inicio de autopista",
                        "Prohibición absoluta",
                        "Obligación de detenerse"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, mantener distancia con el vehículo delantero permite…",
                new String[]{
                        "Reaccionar ante imprevistos",
                        "Reducir agarre",
                        "Aumentar consumo",
                        "Perder estabilidad"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una zona residencial debes…",
                new String[]{
                        "Extremar precaución y reducir velocidad",
                        "Acelerar",
                        "Adelantar siempre",
                        "Ignorar peatones"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "La conducción bajo efectos del sueño puede provocar…",
                new String[]{
                        "Microsueños peligrosos",
                        "Mayor concentración",
                        "Reflejos rápidos",
                        "Mejor coordinación"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, una conducción suave contribuye a…",
                new String[]{
                        "Mayor seguridad y estabilidad",
                        "Mayor desgaste",
                        "Menor control",
                        "Pérdida de adherencia"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En caso de avería en autopista debes…",
                new String[]{
                        "Señalizar, colocarte en lugar seguro y usar chaleco",
                        "Quedarte dentro sin señalizar",
                        "Caminar por el carril",
                        "Ignorar la situación"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En moto, la anticipación en la conducción permite…",
                new String[]{
                        "Evitar situaciones de riesgo",
                        "Aumentar consumo",
                        "Reducir visibilidad",
                        "Perder control"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "El respeto a las normas de tráfico garantiza…",
                new String[]{
                        "Mayor seguridad para todos",
                        "Más velocidad",
                        "Menos responsabilidad",
                        "Mayor consumo"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una bajada prolongada es recomendable…",
                new String[]{
                        "Utilizar freno motor y marchas adecuadas",
                        "Circular en punto muerto",
                        "Frenar continuamente fuerte",
                        "Apagar motor"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, revisar periódicamente los frenos ayuda a…",
                new String[]{
                        "Mantener eficacia de frenado",
                        "Aumentar velocidad",
                        "Reducir estabilidad",
                        "Mejorar sonido"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "En una intersección con señalización contradictoria prevalece…",
                new String[]{
                        "La indicación del agente",
                        "La señal vertical",
                        "La marca vial",
                        "La costumbre"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "El conductor debe adaptar su conducción a…",
                new String[]{
                        "Las condiciones del tráfico y la vía",
                        "Solo su prisa",
                        "La música",
                        "El clima únicamente"
                },
                0, "comun"
        ));

        bancoPreguntas.add(new Pregunta(
                "En motocicleta, una correcta planificación del recorrido permite…",
                new String[]{
                        "Reducir riesgos innecesarios",
                        "Aumentar velocidad",
                        "Reducir agarre",
                        "Perder concentración"
                },
                0, "especifica"
        ));

        bancoPreguntas.add(new Pregunta(
                "La conducción responsable implica…",
                new String[]{
                        "Respetar normas y usuarios",
                        "Ignorar señales",
                        "Aumentar velocidad",
                        "Competir con otros"
                },
                0, "comun"
        ));

    }

    // ============================================================
//     VENTANA DE TESTS COMPLETA Y FUNCIONAL
// ============================================================
    static class TestWindow extends JFrame {

        private java.util.List<Pregunta> preguntas;
        private int index = 0;
        private int aciertos = 0;

        private JLabel lblPregunta;
        private JRadioButton[] radios;
        private ButtonGroup grupo;

        public TestWindow(String licencia) {

            cargarPreguntas();

            // De momento no filtramos por licencia, pero puedes hacerlo por tipo
            preguntas = new java.util.ArrayList<>(bancoPreguntas);

            setTitle("Test — Permiso " + licencia);
            setSize(700, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            lblPregunta = new JLabel("", SwingConstants.LEFT);
            lblPregunta.setFont(new Font("Segoe UI", Font.BOLD, 18));
            panel.add(lblPregunta, BorderLayout.NORTH);

            JPanel opcionesPanel = new JPanel(new GridLayout(4, 1, 10, 10));
            radios = new JRadioButton[4];
            grupo = new ButtonGroup();

            for (int i = 0; i < 4; i++) {
                radios[i] = new JRadioButton();
                radios[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
                grupo.add(radios[i]);
                opcionesPanel.add(radios[i]);
            }

            panel.add(opcionesPanel, BorderLayout.CENTER);

            JButton btnSiguiente = new JButton("Siguiente");
            btnSiguiente.addActionListener(e -> comprobar());
            panel.add(btnSiguiente, BorderLayout.SOUTH);

            add(panel);

            mostrarPregunta();
        }

        private void mostrarPregunta() {
            Pregunta p = preguntas.get(index);
            lblPregunta.setText((index + 1) + ". " + p.enunciado);

            for (int i = 0; i < 4; i++) {
                radios[i].setText(p.opciones[i]);
            }

            grupo.clearSelection();
        }

        private void comprobar() {
            Pregunta p = preguntas.get(index);

            int seleccion = -1;
            for (int i = 0; i < 4; i++) {
                if (radios[i].isSelected()) {
                    seleccion = i;
                    break;
                }
            }

            if (seleccion == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una respuesta.");
                return;
            }

            if (seleccion == p.correcta) {
                aciertos++;
            }

            index++;

            if (index >= preguntas.size()) {
                JOptionPane.showMessageDialog(this,
                        "Test finalizado.\nAciertos: " + aciertos + " de " + preguntas.size());
                dispose();
                return;
            }

            mostrarPregunta();
        }
    }


}

