package net.steepout.scriptit.impl;

import net.steepout.scriptit.ScriptEventImpl;
import net.steepout.scriptit.ScriptPlugin;
import net.steepout.scriptit.ScriptPluginManager;
import net.steepout.scriptit.misc.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This is not exactly a 'test', but a example showing how to use the framework
 */
public class DistributionalTest {

    static {// register aliases for 'events'
        Events.getAliases().put("Editor", "editorEvent");
    }

    @SuppressWarnings("WeakerAccess") // should be accessed by plugins
    public static class Components { // Lobby
        public static JFrame frame;
        public static Container container;
        public static JTextArea editor;
    }

    static ScriptPluginManager manager = null;

    public static void main(String[] args) {
        manager = new PythonPluginManager(); // acquire a plugin manager
        manager.putLobby(Components.class); // put Lobby (named 'Components')
        Components.frame = new JFrame("Naive Editor - ScriptIt !");
        final JFrame frame = Components.frame;
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Components.container = frame.getContentPane();
        Components.editor = new JTextArea("**Hello!* Write it __here_ ~");
        Font font = Components.editor.getFont();
        Components.editor.setFont(new Font(font.getName(), Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(Components.editor);
        Components.frame.add(scrollPane);
        setMenuBar(frame);
        Components.editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                manager.handleEventsAsync(new EditorEvent(Components.editor, e)); // pass event
            }
        });
        frame.setVisible(true);
    }

    private static void setMenuBar(JFrame frame) { // register menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Load plugin");
        menu.add(item);
        menuBar.add(menu);
        item.addActionListener((event) -> {
            item.setEnabled(false);
            ScriptPlugin plugin = manager.registerPlugin(IOUtils.readAllFromResource("/dtest.py"));
            JOptionPane.showMessageDialog(Components.frame, "Plugin Load : " + plugin.getName() + "(" + plugin.getVersion() + ")");
        });
        frame.setJMenuBar(menuBar);
    }

    public static class EditorEvent extends ScriptEventImpl {

        protected EditorEvent(JComponent editor, KeyEvent event) {
            super("editorEvent", createMap(new Entry<>("editor", editor), new Entry<>("keyEvent", event)));
        }

        public boolean isAWTKeyEvent() {
            return get("keyEvent").isPresent();
        }

    }

}
