import org.hyprconfig.parseSingleSettings.reParserVariables
import org.hyprconfig.parseSingleSettings.reParsingBind
import org.hyprconfig.parseSingleSettings.reParsingMonitors
import org.junit.platform.commons.logging.LoggerFactory
import kotlin.test.Test

class ReParse {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `Test Reparsing Variables`() {
        reParserVariables()
    }

    @Test
    fun `Test Reparsing Bind`() {
        reParsingBind()
    }

    @Test
    fun `Test Reparsing Monitors`() {
        reParsingMonitors()
    }
}