package com.fcsibbul.data.notification

class NotificationMessage {
    companion object {
        val message = """
            {
              "to": "/topics/%s",
              "data": {
                   "body":"%s"
               }
            }
            """
    }
}