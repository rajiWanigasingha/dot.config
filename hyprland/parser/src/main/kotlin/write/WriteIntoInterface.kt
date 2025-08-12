package write

interface WriteIntoInterface<T> {

    fun writeIntoHyprland(hypr: T) : Result<Boolean>

    fun writeIntoDotConfig(conf: T) : Result<Boolean>

}