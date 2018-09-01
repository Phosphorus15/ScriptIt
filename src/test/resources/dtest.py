# this is a plugin script designed for 'Naive Editor'
# see /src/test/naive/editor/EditorTest.java

pluginc("markleft", "MarkLeft", "1.0.0-SNAPSHOT")  # markleft is really(rarely) good !

# import necessary classes from java
jimport("javax.swing.JTextPane")
jimport("javax.swing.JSplitPane")
jimport("javax.swing.JScrollPane")
# html escape support
import cgi

# create the text pane for previewing 'markleft' document
markleft = JTextPane()
markleft.setText("Empty")
markleft.setEditable(False)
markleft.setContentType("text/html")  # creating special texts using HTML would be easier
scroll = JScrollPane(markleft)


# the subscription of events is at the bottom of this file (due to the limits of python)

def load(event):
    # change the title
    frame = Components.frame
    frame.setTitle(frame.getTitle().replace("Naive", "MarkLeft"))
    # fetch the original text editor
    origin = Components.container.getComponent(0)
    # create vertical split pane
    splitpane = JSplitPane(JSplitPane.VERTICAL_SPLIT)  # create split pane
    splitpane.setLeftComponent(origin)
    splitpane.setRightComponent(scroll)
    splitpane.setDividerLocation(300)
    # refresh & reset window
    frame.setVisible(False)
    Components.container = splitpane  # reset the primary container (for the sake of maintenance)
    frame.setContentPane(splitpane)  # substitute the window's root panel
    frame.setVisible(True)
    # start syncing
    sync_pane(Components.editor.getText())


def key_event(event):  # listen to key event on original editor
    if event.isAWTKeyEvent():  # this method was defined in java code within the event definition
        # see naive.editor.EditorTest$EditorEvent#isAWTKeyEvent
        sync_pane(event.get("editor").get().getText())


# refresh the 'markleft' preview panel, which was actually an HTML document container
def sync_pane(text):
    text = cgi.escape(text)  # escape for html
    text = text.replace("\n", "<br/>")  # line separator
    text = text.replace("**", "<b>").replace("*", "</b>")  # bold
    text = text.replace("__", "<i>").replace("_", "</i>")  # italic
    print text
    markleft.setText(text)


# subscribe to events
subscribe(Events.Load, load)
subscribe(Events.Editor, key_event)
