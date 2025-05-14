package pt.iscte.mei.pa

import pt.iscte.mei.pa.controller.Controller
import pt.iscte.mei.pa.GetJson


fun main() {
    val app = GetJson(Controller::class)
    app.start(8000)
}

