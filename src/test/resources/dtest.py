pluginc("markleft", "MarkLeft", "1.0.0-SNAPSHOT")

jimport("javax.swing.JTextPane")
jimport("javax.swing.JSplitPane")
jimport("javax.swing.JScrollPane")
import cgi

markleft = JTextPane()
markleft.setText("Empty")
markleft.setEditable(False)
markleft.setContentType("text/html")


def load(event):
    frame = Components.frame
    frame.setTitle(frame.getTitle().replace("Naive", "MarkLeft"))
    scroll = JScrollPane(markleft)
    origin = Components.container.getComponent(0)
    # container.add(scroll)
    splitpane = JSplitPane(JSplitPane.VERTICAL_SPLIT)  # create split pane
    splitpane.setLeftComponent(origin)
    splitpane.setRightComponent(scroll)
    splitpane.setDividerLocation(300)
    frame.setVisible(False)  # refresh screen
    frame.setContentPane(splitpane)
    frame.setVisible(True)
    sync_pane(Components.editor.getText())


def key_event(event):
    if event.isAWTKeyEvent():
        sync_pane(event.get("editor").get().getText())


def sync_pane(text):
    text = cgi.escape(text)  # escape for html
    text = text.replace("\n", "<br/>")  # line separator
    text = text.replace("**", "<b>").replace("*", "</b>") # bold
    text = text.replace("__", "<i>").replace("_", "</i>") # italic
    print text
    markleft.setText(text)


subscribe(Events.Load, load)
subscribe(Events.Editor, key_event)
