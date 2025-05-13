package pt.iscte.mei.pa

import pt.iscte.mei.pa.controller.Controller
import pt.iscte.mei.pa.http.ClassRegistry
import pt.iscte.mei.pa.http.SimpleSocketServer


fun main() {
    ClassRegistry.register(Controller::class)
    val server = SimpleSocketServer()
    server.start(8080)
}

