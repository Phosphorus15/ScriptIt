pluginc("test", "Test Py Plugin", "1.0.0-SNAPSHOT")

jimport("java.util.Base64")


def onLoad(event):
    print event.get("pluginManager").get()
    print plugin == event.get("plugin").get()
    print System.getProperties()  # imported from lobby
    print plugin.getVersion()  # 1.0.0-SNAPSHOT


subscribe(Events.Load, onLoad)
print(Base64.getEncoder().encodeToString("hello world !"))
