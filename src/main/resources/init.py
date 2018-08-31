
def pluginc(id, name, version):
    global jython_id, jython_name, jython_version
    jython_id, jython_name, jython_version = id, name, version

def plugin(name):
    pluginc(name.lower().replace(" ", "_"), name, "undefined")

def subscribe(type, function):
    jython_pManager.subscribe(type, function)
