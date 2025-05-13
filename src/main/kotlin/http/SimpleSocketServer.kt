package pt.iscte.mei.pa.http

import KsonLib
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class SimpleSocketServer {

    private var serverSocket: ServerSocket? = null
    private val threadPool = Executors.newCachedThreadPool()

    fun start(port: Int = 8080) {
        serverSocket = ServerSocket(port)
        threadPool.submit {
            println("Server running on http://localhost:$port")
            while (!serverSocket!!.isClosed) {
                try {
                    val request = serverSocket!!.accept()
                    threadPool.submit { handleRequest(request) }
                } catch (e: IOException) {
                }
            }
        }
    }

    fun stop() {
        println("Shutting down server...")
        serverSocket?.close()
        threadPool.shutdownNow()
    }

    fun handleRequest(request: Socket) {
        request.use {
            val reader = BufferedReader(InputStreamReader(request.getInputStream()))
            val writer = BufferedWriter(OutputStreamWriter(request.getOutputStream()))
            val requestLine = reader.readLine() ?: return
            val path = requestLine.split(" ")[1]
            val r = Dispatcher(ClassRegistry).send(path)

            val response = KsonLib(r).asJson()

            if (response == "404") {
                notFound(writer, response)
            } else {
               ok(writer, response)
            }
        }
    }

    private fun notFound(writer: Writer, response: String) {
        writer.write("HTTP/1.1 404 Not Found\r\n")
        writer.write("Content-Type: text/plain\r\n")
        writer.write("Content-Length: 0\r\n")
        writer.write("\r\n")
        writer.flush()
    }

    private fun ok(writer: BufferedWriter, response: String) {
        writer.write("HTTP/1.1 200 OK\r\n")
        writer.write("Content-Type: application/json;charset=UTF-8\r\n")
        writer.write("Content-Length: ${response.length}\r\n")
        writer.write("\r\n")
        writer.write(response)
        writer.flush()
    }
}