
plugin("Test Js Plugin")
jimport("java.util.Date")

function onLoad(event) {
    print("plugin name : " + self.getName())
    print("plugin id : " + self.getId())
    print(self == event.get("plugin").get())
    print("The exact time now is " + new Date())
}

subscribe(Events.Load, onLoad) // Events belongs to event system ,to register a entry here
                               // use Events.getAliases().put(entryName, eventName)
                               // note that the 'Events' object here is a mirror of the aliases map
print(System.getProperty("java.version")) // imported from lobby
