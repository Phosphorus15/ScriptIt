
plugin("Test Js Plugin")

function onLoad(event) {
    print("plugin name : " + self.getName())
    print("plugin id : " + self.getId())
    print(self == event.get("plugin").get())
}

subscribe("pluginLoad", onLoad)
alert('hey!')
