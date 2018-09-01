package naive.editor;

import net.steepout.scriptit.ScriptEventImpl;
import net.steepout.scriptit.ScriptPlugin;
import net.steepout.scriptit.ScriptPluginManager;
import net.steepout.scriptit.impl.IOUtils;
import net.steepout.scriptit.impl.PythonPluginManager;
import net.steepout.scriptit.misc.Events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This is not exactly a 'test', but a example showing how to use the framework
 */
public class EditorTest {

    static {// register aliases for 'events'
        Events.getAliases().put("Editor", "editorEvent");
    }

    @SuppressWarnings("WeakerAccess") // should be accessed by plugins
    public static class Components { // Lobby
        public static JFrame frame;
        public static Container container;
        public static JTextArea editor;
    }

    public static void main(String[] args) {
        manager = new PythonPluginManager(); // acquire a plugin manager
        manager.putLobby(Components.class); // put Lobby (named 'Components')
        // Initialize UI
        Components.frame = applyFrameDefaults(new JFrame("Naive Editor - ScriptIt !"));
        Components.container = Components.frame.getContentPane();
        Components.editor = applyFont(new JTextArea("**Hello!* Write it __here_ ~"));
        JScrollPane scrollPane = new JScrollPane(Components.editor);
        Components.frame.add(scrollPane);
        setMenuBar(Components.frame);
        // Register listener
        Components.editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                manager.handleEventsAsync(new EditorEvent(Components.editor, e)); // pass event
            }
        });
        // Show Window
        Components.frame.setVisible(true);
    }

    static ScriptPluginManager manager = null;

    private static JTextArea applyFont(JTextArea textArea) {
        Font font = textArea.getFont();
        textArea.setFont(new Font(font.getName(), Font.PLAIN, 16));
        return textArea;
    }

    private static JFrame applyFrameDefaults(JFrame frame) {
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private static void setMenuBar(JFrame frame) { // register menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Load plugin");
        menu.add(item);
        menuBar.add(menu);
        item.addActionListener((event) -> {
            // load the plugin script & apply it to the plugin manager
            item.setEnabled(false);
            ScriptPlugin plugin = manager.registerPlugin(IOUtils.readAllFromResource("/dtest.py"));
            // show the message of the returned plugin object
            JOptionPane.showMessageDialog(Components.frame,
                    "Plugin Load : " + plugin.getName() + "(" + plugin.getVersion() + ")");
        });
        frame.setJMenuBar(menuBar);
    }

    public static class EditorEvent extends ScriptEventImpl { // Customized events

        EditorEvent(JComponent editor, KeyEvent event) {
            super("editorEvent", createMap(new Entry<>("editor", editor), new Entry<>("keyEvent", event)));
        }

        public boolean isAWTKeyEvent() {
            return get("keyEvent").isPresent();
        }

    }

}
