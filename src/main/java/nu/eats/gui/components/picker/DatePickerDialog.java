package nu.eats.gui.components.picker;

import nu.eats.gui.plaf.LookAndFeel;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

/**
 * An unstyled, dynamically scaling date picker with a thin 1px single-line grid.
 */
public class DatePickerDialog extends JDialog {
    private final JButton[] buttons = new JButton[42];
    private final JLabel label = new JLabel("", SwingConstants.CENTER);
    private LocalDate cursor;
    private LocalDate selected;

    public DatePickerDialog(Window owner, LocalDate initial) {
        super(owner, "Select Date", ModalityType.APPLICATION_MODAL);
        this.cursor = (initial != null) ? initial : LocalDate.now();

        var content = new JPanel(new BorderLayout(0, 10));

        var gridColor = Theme.COLOR_BORDER;
        var gridBorder = new FramedBorder.Builder()
                .edges.bottom(edge -> edge.color(gridColor).thickness(1))
                .edges.right(edge -> edge.color(gridColor).thickness(1))
                .build();

        content.setBorder(new FramedBorder.Builder()
                .sides(side -> side.padding(10))
                .build()
        );

        // Header Navigation
        var header = new JPanel(new BorderLayout());
        var prev = new JButton("<");
        var next = new JButton(">");

        prev.addActionListener(e -> navigate(-1));
        next.addActionListener(e -> navigate(1));

        header.add(prev, BorderLayout.WEST);
        header.add(label, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);
        content.add(header, BorderLayout.NORTH);


        // 7x7 Grid Panel with Top-Left matte border
        var grid = new JPanel(new GridLayout(7, 7, 0, 0));



        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (var dayName : days) {
            var dayLabel = new JLabel(dayName, SwingConstants.CENTER) {
                @Override
                public Dimension getPreferredSize() {
                    int size = calculateDynamicSize(this);

                    return new Dimension(size, size);
                }
            };
            // Bottom-Right matte border to prevent grid line duplication
            // dayLabel.setBorder(gridBorder);
            grid.add(dayLabel);
        }

        for (int i = 0; i < 42; i++) {
            var btn = new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    int size = calculateDynamicSize(this);

                    return new Dimension(size, size);
                }
            };

            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setFocusPainted(false);
            btn.setBorder(gridBorder);

            btn.addActionListener(e -> {
                selected = (LocalDate) btn.getClientProperty("date");

                dispose();
            });

            buttons[i] = btn;

            grid.add(btn);
        }
        content.add(grid, BorderLayout.CENTER);

        setContentPane(content);
        update();
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    /**
     * Calculates a cell size matching the runtime font height.
     */
    private int calculateDynamicSize(Component c) {
        var fm = c.getFontMetrics(c.getFont());
        return (int) (fm.getHeight() * 2.0);
    }

    private void navigate(int months) {
        cursor = cursor.plusMonths(months);

        update();
    }

    private void update() {
        label.setText(cursor.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + cursor.getYear());

        var first = cursor.withDayOfMonth(1);
        int offset = first.getDayOfWeek().getValue() % 7;
        var current = first.minusDays(offset);

        for (var btn : buttons) {
            if (current.getMonth() == cursor.getMonth()) {
                btn.setText(String.valueOf(current.getDayOfMonth()));
                btn.setEnabled(true);
                btn.putClientProperty("date", current);
                btn.getAccessibleContext().setAccessibleName(current.toString());
            } else {
                btn.setText("");
                btn.setEnabled(false);
                btn.putClientProperty("date", null);
                btn.getAccessibleContext().setAccessibleName("");
            }

            current = current.plusDays(1);
        }
    }

    public Optional<LocalDate> getSelectedDate() {
        return Optional.ofNullable(selected);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new LookAndFeel());
            } catch (UnsupportedLookAndFeelException cause) {
                throw new RuntimeException(cause);
            }

            var frame = new JFrame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            var btn = new JButton("Select Date");
            btn.addActionListener(e -> {
                var picker = new DatePickerDialog(frame, LocalDate.now());
                picker.setVisible(true);
                picker.getSelectedDate().ifPresent(System.out::println);
            });

            frame.add(btn);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}