package com.example.jhouse_server.global.config

import java.io.IOException
import java.io.StringReader
import java.util.*
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


class RequestServletWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private var requestData: String? = null

    init {
        try {
            val s = request.inputStream?.let { Scanner(it).useDelimiter("\\A") }
            if (s != null) {
                requestData = if (s.hasNext()) s.next() else ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getInputStream(): ServletInputStream {
        val reader = StringReader(requestData)

        return object : ServletInputStream() {
            private var readListener: ReadListener? = null

            @Throws(IOException::class)
            override fun read(): Int {
                return reader.read()
            }

            override fun setReadListener(listener: ReadListener?) {
                readListener = listener
                try {
                    if (!isFinished) {
                        readListener?.onDataAvailable()
                    } else {
                        readListener?.onAllDataRead()
                    }
                } catch (io: IOException) {
                    io.printStackTrace()
                }
            }

            override fun isReady(): Boolean {
                return isFinished
            }

            override fun isFinished(): Boolean {
                try {
                    return reader.read() < 0
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return false
            }
        }
    }
}