package com.fcsibbul.data.notification

class NotificationMessage {
    companion object {
        const val message = """
            {
              "to": "/topics/%s",
              "data": {
                   "body":"%s"
               }
            }
            """
    }
}