pluginc("test", "Test Py Plugin", "1.0.0-SNAPSHOT")

class Plugin:
    def onLoad(self, event):
        print event.get("pluginManager").get()
        print plugin == event.get("plugin").get()

plugin = Plugin()

subscribe("pluginLoad", plugin.onLoad)
