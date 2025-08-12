package loadAndCreateDefaults

import ProjectPaths
import ProjectPaths.toPath
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class LoadAndCreateDefault {

    fun checkForDefaultPath() {

        // if user doesn't exist
        if (!Path.of(ProjectPaths.userName.toString()).isDirectory()) {
            TODO("Exception should be thrown")
        }

        // check for parent directory
        val parentDirectory = Path.of(ProjectPaths.defaultHyprlandPaths)

        if (!parentDirectory.isDirectory()) {

            // create /.config the folder path
            if (!parentDirectory.parent.isDirectory()) {
                parentDirectory.parent.createDirectory()
            }

            // create dot.config
            parentDirectory.createDirectory()
        }

        // check for sub dir
        ProjectPaths.folder.forEach {
            val folderPath = Path.of("${ProjectPaths.defaultHyprlandPaths}/${it}")

            // create dir if it didn't exist
            if (!folderPath.isDirectory()) {
                folderPath.createDirectory()
            }
        }

        // create defaults from static
        val defaultHyprlandSettings = object {}.javaClass.getResource(ProjectPaths.STATIC_FILES)

        if (defaultHyprlandSettings == null) {
            TODO("Throw a exception")
        }

        val static = defaultHyprlandSettings.toURI()

        val fileSystemDefaults = if (static.scheme == "jar") {
            FileSystems.newFileSystem(static ,emptyMap<String , Any>())
        } else null

        val staticPaths = fileSystemDefaults?.getPath(ProjectPaths.STATIC_FILES) ?: Paths.get(static)

        Files.walk(staticPaths).forEach { paths ->
            if (!Files.isDirectory(paths)) {
                val relativePath = staticPaths.relativize(paths).toString()
                val targetFile = Path.of("${ProjectPaths.defaultHyprlandPaths}/default").resolve(relativePath)
                Files.createDirectories(targetFile.parent)
                Files.copy(paths ,targetFile , StandardCopyOption.REPLACE_EXISTING)
            }
        }

        // create backup.conf file
        val backupsFile = Path.of("${ProjectPaths.defaultHyprlandPaths}/backups/backup.conf")

        if (!backupsFile.exists()) {
            backupsFile.createFile()
        }

        // create hyprland files
        val hyprlandPath = ProjectPaths.defaultHyprlandGen.toPath()

        if (!hyprlandPath.exists()) {
            hyprlandPath.createDirectory()
        }

        ProjectPaths.hyprFolder.forEach {

            val path = "${ProjectPaths.defaultHyprlandGen}/${it}.conf"

            if (!path.toPath().exists()) {
                path.toPath().createFile()
            }
        }

        ProjectPaths.storeFolder.forEach {
            val path = "${ProjectPaths.defaultHyprlandPaths}/store/${it}.csv"

            if (!path.toPath().exists()) {
                path.toPath().createFile()
            }
        }

    }


    fun loadSingleHyprlandPath(name: String): String? {
        ProjectPaths.hyprFolder.forEach {
            val path = "${ProjectPaths.defaultHyprlandGen}/${it}.conf"

            if (it == name) {
                return path
            }
        }

        return null
    }

    fun loadSingleDotConfPath(name: String): String? {
        ProjectPaths.storeFolder.forEach {
            val path = "${ProjectPaths.defaultHyprlandPaths}/store/${it}.csv"

            if (it == name) {
                return path
            }
        }

        return null
    }
}