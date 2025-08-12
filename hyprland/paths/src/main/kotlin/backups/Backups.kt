package backups

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists

class Backups {

    fun createBackups() {

        val backupPath = "${ProjectPaths.defaultHyprlandPaths}/backups"

        if (!Path.of(backupPath).exists()) {
            Path.of(backupPath).createDirectories()
            Path.of("$backupPath/backup.conf").createFile()
        }


    }

    fun getABackup() {}

}