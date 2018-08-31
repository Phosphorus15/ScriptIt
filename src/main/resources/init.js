function plugin(id, name, version) {
 jjs_id = id
 jjs_name = name
 jjs_version = version
}

function plugin(name) {
 jjs_name = name
 jjs_id = name.toLowerCase().replaceAll(' ', '_')
 jjs_version = "undefined"
}

function subscribe(type, callback) {
 jjs_pManager.subscribe(type, callback)
}

function jimport(clazz) {
}
