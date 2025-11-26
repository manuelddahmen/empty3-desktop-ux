/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

// Fichier: MainMethodsLauncher.java
package launcher;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainMethodsLauncher {

    // Modifiez ceci si besoin : dossiers de sources analysés par défaut
    private static final List<String> DEFAULT_SOURCE_ROOTS = List.of(
            "src/main/java",
            "src/test/java",
            "src"
    );

    // Expression régulière pour détecter une signature main (String[] args | String... args)
    private static final Pattern MAIN_METHOD_PATTERN = Pattern.compile(
            "(?s)\\bpublic\\s+static\\s+void\\s+main\\s*\\(\\s*String(?:\\[\\]|\\.\\.\\.)\\s+\\w+\\s*\\)"
    );

    // Expression régulière pour extraire le package (si présent)
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(
            "(?m)^\\s*package\\s+([a-zA-Z_]\\w*(?:\\.[a-zA-Z_]\\w*)*)\\s*;"
    );

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lanceur de méthodes main");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            LauncherPanel panel = new LauncherPanel(DEFAULT_SOURCE_ROOTS);
            frame.setContentPane(panel);
            frame.setVisible(true);
        });
    }

    // Panneau principal avec UI Swing
    static class LauncherPanel extends JPanel {
        private final JTextField rootsField = new JTextField();
        private final JButton refreshButton = new JButton("Rafraîchir");
        private final JButton runButton = new JButton("Exécuter");
        private final JTextField searchField = new JTextField();
        private final DefaultListModel<MainEntry> listModel = new DefaultListModel<>();
        private final JList<MainEntry> list = new JList<>(listModel);
        private final JTextArea outputArea = new JTextArea();
        private final ExecutorService executor = Executors.newCachedThreadPool();

        private List<MainEntry> allEntries = new ArrayList<>();

        LauncherPanel(List<String> defaultRoots) {
            super(new BorderLayout(8, 8));
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            // Barre supérieure: racines sources + boutons
            JPanel top = new JPanel(new BorderLayout(6, 6));
            rootsField.setText(String.join(";", defaultRoots));
            top.add(new JLabel("Racines sources (séparées par ;) :"), BorderLayout.WEST);
            top.add(rootsField, BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            buttons.add(refreshButton);
            buttons.add(runButton);
            top.add(buttons, BorderLayout.EAST);

            add(top, BorderLayout.NORTH);

            // Zone centrale: filtrage + liste
            JPanel center = new JPanel(new BorderLayout(6, 6));
            JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
            searchPanel.add(new JLabel("Filtre :"), BorderLayout.WEST);
            searchPanel.add(searchField, BorderLayout.CENTER);
            center.add(searchPanel, BorderLayout.NORTH);

            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof MainEntry e) {
                        setText(e.fqcn + "    [" + e.sourcePath + "]");
                    }
                    return this;
                }
            });

            JScrollPane listScroll = new JScrollPane(list);
            center.add(listScroll, BorderLayout.CENTER);

            add(center, BorderLayout.CENTER);

            // Zone de sortie/console
            outputArea.setEditable(false);
            outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            add(new JScrollPane(outputArea), BorderLayout.SOUTH);
            ((JScrollPane) getComponent(2)).setPreferredSize(new Dimension(200, 220));

            // Listeners
            refreshButton.addActionListener(e -> refreshScan());
            runButton.addActionListener(e -> runSelectedMain());
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        runSelectedMain();
                    }
                }
            });
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) { applyFilter(); }
                @Override public void removeUpdate(DocumentEvent e) { applyFilter(); }
                @Override public void changedUpdate(DocumentEvent e) { applyFilter(); }
            });

            // Premier scan
            refreshScan();
        }

        private void refreshScan() {
            setBusy(true);
            listModel.clear();
            allEntries = List.of();
            output("Recherche des méthodes main...");

            List<Path> roots = parseRoots(rootsField.getText());
            CompletableFuture
                    .supplyAsync(() -> scanForMains(roots), executor)
                    .whenComplete((entries, err) -> SwingUtilities.invokeLater(() -> {
                        setBusy(false);
                        if (err != null) {
                            output("Erreur pendant l'analyse: " + err.getMessage());
                            err.printStackTrace();
                            return;
                        }
                        allEntries = entries;
                        output("Trouvé " + entries.size() + " classe(s) avec main().");
                        applyFilter();
                    }));
        }

        private void runSelectedMain() {
            MainEntry selected = list.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Sélectionnez une classe à exécuter.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            output("Exécution: " + selected.fqcn);
            // Exécution en thread séparé pour ne pas bloquer l'EDT
            executor.submit(() -> {
                try {
                    Class<?> cls = Class.forName(selected.fqcn);
                    Method main = cls.getMethod("main", String[].class);
                    if (!Modifier.isStatic(main.getModifiers()) || !Modifier.isPublic(main.getModifiers())) {
                        SwingUtilities.invokeLater(() ->
                                output("La méthode main de " + selected.fqcn + " n'est pas public static."));
                        return;
                    }
                    // Appel avec arguments vides
                    String[] args = new String[0];
                    main.invoke(null, (Object) args);
                    SwingUtilities.invokeLater(() -> output("Terminé: " + selected.fqcn));
                } catch (Throwable t) {
                    SwingUtilities.invokeLater(() -> {
                        output("Erreur à l'exécution de " + selected.fqcn + ": " + t);
                        StringWriter sw = new StringWriter();
                        t.printStackTrace(new java.io.PrintWriter(sw));
                        output(sw.toString());
                    });
                }
            });
        }

        private void setBusy(boolean busy) {
            refreshButton.setEnabled(!busy);
            runButton.setEnabled(!busy);
            setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        }

        private void applyFilter() {
            String q = searchField.getText().trim().toLowerCase(Locale.ROOT);
            listModel.clear();
            allEntries.stream()
                    .filter(e -> q.isEmpty()
                            || e.fqcn.toLowerCase(Locale.ROOT).contains(q)
                            || e.sourcePath.toLowerCase(Locale.ROOT).contains(q))
                    .sorted(Comparator.comparing(e -> e.fqcn))
                    .forEach(listModel::addElement);
        }

        private void output(String msg) {
            outputArea.append(msg + System.lineSeparator());
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        }

        private static List<Path> parseRoots(String text) {
            return Arrays.stream(text.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Paths::get)
                    .collect(Collectors.toList());
        }
    }

    // Entrée trouvée (FQCN + chemin source pour information)
    static class MainEntry {
        final String fqcn;
        final String sourcePath;

        MainEntry(String fqcn, String sourcePath) {
            this.fqcn = fqcn;
            this.sourcePath = sourcePath;
        }

        @Override
        public String toString() {
            return fqcn;
        }
    }

    // Scan des sources: extrait package + nom de fichier comme nom de type principal
    static List<MainEntry> scanForMains(List<Path> roots) {
        List<MainEntry> result = new ArrayList<>();
        for (Path root : roots) {
            if (!Files.exists(root)) continue;
            try {
                try (Stream<Path> stream = Files.walk(root)) {
                    List<Path> javaFiles = stream
                            .filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".java"))
                            .collect(Collectors.toList());
                    for (Path java : javaFiles) {
                        Optional<MainEntry> entry = extractMainEntry(java);
                        entry.ifPresent(result::add);
                    }
                }
            } catch (IOException e) {
                // On continue malgré tout
            }
        }
        // Dédupliquer par FQCN (si fichiers dupliqués dans plusieurs racines)
        Map<String, MainEntry> byFqcn = new LinkedHashMap<>();
        for (MainEntry e : result) {
            byFqcn.putIfAbsent(e.fqcn, e);
        }
        return new ArrayList<>(byFqcn.values());
    }

    // Extrait le FQCN si un main est trouvé
    static Optional<MainEntry> extractMainEntry(Path javaFile) {
        String content;
        try {
            // Lecture limitée pourrait suffire, mais on lit tout par simplicité
            content = Files.readString(javaFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Optional.empty();
        }

        Matcher mainMatcher = MAIN_METHOD_PATTERN.matcher(content);
        if (!mainMatcher.find()) {
            return Optional.empty();
        }

        String pkg = extractPackage(content);
        String fileName = javaFile.getFileName().toString();
        String simpleName = fileName.substring(0, fileName.length() - ".java".length());
        // Filtrer quelques cas non désirés (classes internes anonymes via fichiers séparés non pertinents)
        if (simpleName.contains("$")) {
            return Optional.empty();
        }
        String fqcn = (pkg == null || pkg.isEmpty()) ? simpleName : pkg + "." + simpleName;
        return Optional.of(new MainEntry(fqcn, javaFile.toString()));
    }

    static String extractPackage(String content) {
        Matcher m = PACKAGE_PATTERN.matcher(content);
        return m.find() ? m.group(1) : null;
    }
}