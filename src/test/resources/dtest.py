pluginc("markleft", "MarkLeft", "1.0.0-SNAPSHOT")

jimport("javax.swing.JTextPane")
jimport("javax.swing.JSplitPane")
jimport("javax.swing.JScrollPane")

markleft = JTextPane()
markleft.setText("Empty")


def onLoad(event):
    frame = Components.frame
    frame.setTitle(frame.getTitle().replace("Naive", "MarkLeft"))
    scroll = JScrollPane(markleft)
    origin = frame.getComponent(0)
    frame.add(scroll)


subscribe(Events.Load, onLoad)
