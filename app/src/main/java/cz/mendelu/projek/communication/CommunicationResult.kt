package cz.mendelu.projek.communication

sealed class CommunicationResult<out T: Any> {
    class Success<out T: Any>(val data: T) : CommunicationResult<T>()
    class Error(val error: CommunicationError) : CommunicationResult<Nothing>()
    class Exception(val exception: Throwable) : CommunicationResult<Nothing>()
    class ConnectionError() : CommunicationResult<Nothing>()

}